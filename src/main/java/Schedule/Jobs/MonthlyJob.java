package Schedule.Jobs;

import Schedule.TaskUtils;
import Tasks.ApprovedKV;
import logging.LoggerWrapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;

/**
 * The MonthlyJob class represents a job that is executed monthly.
 * It reads and writes key-value data and logs the execution time.
 */
public class MonthlyJob implements Job
{
    private static final LoggerWrapper logger = new LoggerWrapper(MonthlyJob.class);

    /**
     * Executes the monthly job, which involves reading and writing key-value data.
     *
     * @param context the job execution context
     */
    @Override
    public void execute(JobExecutionContext context)
    {
        switch (context.getJobDetail().getKey().getName())
        {
            case "Bewilligte-KV Liste":
                ApprovedKV approvedKV = new ApprovedKV();

                try
                {
                    approvedKV.readAndWrite();
                    logger.info("Monthly task executed successfully at " + TaskUtils.currentDateTime());
                }
                catch (IOException e)
                {
                    logger.error("Failed to execute monthly task at " + TaskUtils.currentDateTime() + "\n" + e.getMessage());
                    throw new RuntimeException("Error during monthly job execution", e);
                }
                break;
            case "Klärungsliste":
                break;
            case "Krankenkasse-Bewilligungs-Urgenzliste":
                break;
            case "Abrechnungs-Kontrollliste":
                break;
            default:
                break;
        }



    }
}
