package Tasks;

import Schedule.Jobs.DailyJob;
import database.DatabaseManager;
import database.DatabaseQuery;
import file.FileManager;
import logging.LoggerWrapper;

import java.io.IOException;
import java.util.List;

public class Klaerungsliste
{
    private static final LoggerWrapper logger = new LoggerWrapper(DailyJob.class);

    public void readIn() throws IOException {
        DatabaseManager dbManager = DatabaseManager.getInstance(DatabaseManager.getDatabaseFolderPath());
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);
        FileManager fileManager = new FileManager();

        logger.info("Reading in the new Data");
        List<String> newLines = fileManager.readInKVData(dbQuery.getImportExport());




    }
}
