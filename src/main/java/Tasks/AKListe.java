package Tasks;

import database.DatabaseManager;
import database.DatabaseQuery;
import file.FileManager;
import logging.LoggerWrapper;
import mail.EmailService;
import model.Employee;

import java.io.IOException;
import java.util.List;

public class AKListe
{
    private static final LoggerWrapper logger = new LoggerWrapper(AKListe.class);
    private static final EmailService emailService = EmailService.getInstance();

    private static final String DATABASE_FOLDER_PATH = "C:\\InfoME\\Datenbank";

    public void doTask() throws IOException
    {
        DatabaseManager dbManager = DatabaseManager.getInstance(DATABASE_FOLDER_PATH);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);
        FileManager fileManager = new FileManager();


        logger.info("Reading the recent File");
        List<String> newLines = fileManager.readInKVData(dbQuery.getImportExport());

        logger.info("Writing to the Database");
        dbQuery.importControllingData(newLines);

        logger.info("Reading the Triggers(Employees)");
        List<Employee> employees = dbQuery.getEmployeesOfCategory("Klaerungsliste");

        logger.info("Reading the Database (ControllingData Table)");
        List<String> urgenzData = dbQuery.getUrgenzData();

        getMailsSent(urgenzData, employees);

    }

    private void getMailsSent(List<String> akliste, List<Employee> employees)
    {

    }
}
