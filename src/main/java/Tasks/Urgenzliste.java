package Tasks;

import database.DatabaseManager;
import database.DatabaseQuery;
import file.FileManager;
import file.StringSplitter;
import jakarta.mail.MessagingException;
import logging.LoggerWrapper;
import mail.EmailService;
import model.Employee;
import model.KVRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Urgenzliste
{

    private static final LoggerWrapper logger = new LoggerWrapper(Urgenzliste.class);
    private static final EmailService emailService = EmailService.getInstance();

    private static final String DATABASE_FOLDER_PATH = "C:\\InfoME\\Datenbank";

    public static void doTask() throws IOException
    {
        DatabaseManager dbManager = DatabaseManager.getInstance(DATABASE_FOLDER_PATH);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);
        FileManager fileManager = new FileManager();


        logger.info("Reading the recent File");
        List<String> newLines = fileManager.readInKVData(dbQuery.getImportExport());

        logger.info("Writing to the Database");
        dbQuery.importControllingData(newLines);

        logger.info("Reading the Triggers(Employees)");
        List<Employee> employees = dbQuery.getEmployeesOfCategory("KK-Urgenzliste");

        logger.info("Reading the Database (ControllingData Table)");
        List<String> urgenzData = dbQuery.getUrgenzData();

        getMailsSent(urgenzData, employees);

    }

    private static void getMailsSent(List<String> urgenzData, List<Employee> employees) {
        for (Employee employee : employees)
        {
            String email = employee.getEmail();
            List<String> urgenzDataForEmployee = new ArrayList<>();

            for (String line : urgenzData)
            {
                String parts[] = StringSplitter.splitString(line);
                String SachbearbeiterNr = parts[4];

                if (Integer.parseInt(SachbearbeiterNr.replaceAll(" ", "")) == employee.getId())
                {
                    urgenzDataForEmployee.add(line);
                }
            }

            StringBuilder emailContent = new StringBuilder();

            String subject = String.format("Krankenkassen Urgenzliste");
            emailContent.append("<html>");
            emailContent.append("<body>");
            emailContent.append("Sehr geehrte/r Mitarbeiter/in,<br><br>folgende KVs sind älter als 2 Wochen:<br><br>");

            // Add each approved KV request to the email content
            for (String line : urgenzDataForEmployee)
            {
                // split the line.
                String[] parts = StringSplitter.splitString(line);
                String[] KundeParts = parts[3].split("\\(");
                String Kunde = KundeParts[0];

                emailContent.append("&rarr; ");
                emailContent.append(parts[0]); // KV-Nr
                emailContent.append("&nbsp; | &nbsp;");
                emailContent.append(parts[1]); // KV Datum
                emailContent.append("&nbsp; | &nbsp;");
                emailContent.append(Kunde.replaceAll("\"", "")); // Kunde
                emailContent.append("&nbsp; | &nbsp;");
                emailContent.append(parts[5]); // Betreff
                emailContent.append("<br>");
            }
            emailContent.append("<br><b>Sanitätshaus Ortho-Aktiv </b><br>Gradnerstraße 108<br>8055 Graz<br>");
            emailContent.append("</body>");
            emailContent.append("</html>");


            if(!urgenzDataForEmployee.isEmpty()) {
                try {
                    emailService.sendEmail(email, subject, emailContent.toString());
                    logger.info("Sent E-Mail to " + email);
                } catch (MessagingException me) {
                    logger.error("Fehler beim Senden der E-Mail an " + email + ": " + me.getMessage());
                }
            }
        }
    }


}
