package Schedule;

import Schedule.Jobs.DailyJob;
import Schedule.Jobs.MonthlyJob;
import Schedule.Jobs.WeeklyJob;
import logging.LoggerWrapper;
import model.Trigger;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.List;

/**
 * The TaskScheduler class is responsible for scheduling and managing jobs using Quartz Scheduler.
 */
public class TaskScheduler
{
    private final LoggerWrapper logger = new LoggerWrapper(TaskScheduler.class);
    private final Scheduler scheduler;

    /**
     * Constructs a TaskScheduler instance and initializes the Quartz Scheduler.
     *
     * @throws SchedulerException if there is an error initializing the scheduler
     */
    public TaskScheduler() throws SchedulerException
    {
        this.scheduler = StdSchedulerFactory.getDefaultScheduler();
    }

    /**
     * Starts the Quartz Scheduler.
     *
     * @throws SchedulerException if there is an error starting the scheduler
     */
    public void start() throws SchedulerException
    {
        scheduler.start();
    }

    /**
     * Schedules jobs based on the provided list of triggers.
     *
     * @param triggers the list of triggers
     * @throws SchedulerException if there is an error scheduling the jobs
     */
    public void scheduleJobs(List<Trigger> triggers) throws SchedulerException
    {
        for (Trigger trigger : triggers)
        {
            JobDetail jobDetail = createJobDetail(trigger);
            if (jobDetail != null)
            {
                org.quartz.Trigger quartzTrigger = createTrigger(trigger);
                scheduler.scheduleJob(jobDetail, quartzTrigger);
                logger.info("Scheduled job: " + jobDetail.getKey() + " with trigger: " + quartzTrigger.getKey());
            }
            else
            {
                logger.error("No job detail created for trigger with id " + trigger.getId());
            }
        }
    }

    /**
     * Creates a JobDetail instance based on the provided trigger.
     *
     * @param trigger the trigger
     * @return the JobDetail instance, or null if the schedule interval is unsupported
     */
    private JobDetail createJobDetail(Trigger trigger)
    {
        return switch (trigger.getScheduleInterval())
        {
            case "täglich" -> JobBuilder.newJob(DailyJob.class)
                    .withIdentity(trigger.getScheduleName() + trigger.getId(), "DailyGroup")
                    .build();
            case "wöchentlich" -> JobBuilder.newJob(WeeklyJob.class)
                    .withIdentity(trigger.getScheduleName() + trigger.getId(), "WeeklyGroup")
                    .build();
            case "monatlich" -> JobBuilder.newJob(MonthlyJob.class)
                    .withIdentity(trigger.getScheduleName() + trigger.getId(), "MonthlyGroup")
                    .build();
            default -> {
                logger.error("Unsupported schedule interval: " + trigger.getScheduleInterval());
                yield null;
            }
        };
    }

    /**
     * Creates a Quartz Trigger instance based on the provided trigger.
     *
     * @param trigger the trigger
     * @return the Quartz Trigger instance
     */
    private org.quartz.Trigger createTrigger(Trigger trigger)
    {
        String cronExpression = getCronExpression(trigger);
        return TriggerBuilder.newTrigger()
                .withIdentity(String.valueOf(trigger.getId()), trigger.getScheduleName())
                .startNow()
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();
    }

    /**
     * Generates a Cron expression based on the provided trigger.
     *
     * @param trigger the trigger
     * @return the Cron expression
     */
    private String getCronExpression(Trigger trigger)
    {
        int[] time = TaskUtils.timeValues(trigger.getGetScheduleTime());
        int hour = time[0];
        int minute = time[1];

        return switch (trigger.getScheduleInterval())
        {
            case "täglich" -> String.format("0 %d %d * * ?", minute, hour);
            case "wöchentlich" ->
            {
                int dayOfWeek = TaskUtils.getDayOfWeek(trigger.getScheduleIntervalOption());
                yield String.format("0 %d %d ? * %d", minute, hour, dayOfWeek);
            }
            case "monatlich" ->
            {
                String dayOfMonth = trigger.getScheduleIntervalOption().replace(". des Monats", "");
                yield String.format("0 %d %d %s * ?", minute, hour, dayOfMonth);
            }
            default ->
                    throw new IllegalArgumentException("Unsupported schedule interval: " + trigger.getScheduleInterval());
        };
    }

    /**
     * Shuts down the Quartz Scheduler.
     *
     * @throws SchedulerException if there is an error shutting down the scheduler
     */
    public void shutdown() throws SchedulerException
    {
        logger.info("Shutting down scheduler...");
        scheduler.shutdown();
    }
}
