package Schedule.Jobs;

import Schedule.TaskUtils;
import Tasks.ApprovedKV;
import database.DatabaseManager;
import database.DatabaseQuery;
import logging.LoggerWrapper;
import model.Trigger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The DailyJob class represents a job that is executed daily.
 * It reads and writes key-value data and logs the execution time.
 */
public class DailyJob implements Job
{
    private static final LoggerWrapper logger = new LoggerWrapper(DailyJob.class);
    private static final String DATABASE_FOLDER_PATH = "C:\\InfoME\\Datenbank";

    /**
     * Executes the daily job, which involves reading and writing key-value data.
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
                    logger.info("Daily task executed successfully at " + TaskUtils.currentDateTime());
                }
                catch (IOException e)
                {
                    logger.error("Failed to execute daily task at " + TaskUtils.currentDateTime() + "\n" + e.getMessage());
                    throw new RuntimeException("Error during daily job execution", e);
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
