package Tasks;

import database.DatabaseQuery;
import database.DatabaseManager;
import file.FileManager;
import file.StringSplitter;
import jakarta.mail.MessagingException;
import logging.LoggerWrapper;
import mail.EmailService;
import model.Employee;
import model.KVRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ApprovedKV
{
    private static final LoggerWrapper logger = new LoggerWrapper(ApprovedKV.class);
    private static final EmailService emailService = EmailService.getInstance();

    private static final String DATABASE_FOLDER_PATH = "C:\\InfoME\\Datenbank";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Reads new KV data, compares it to the existing database, sends approval emails, and updates the database.
     *
     * @throws IOException if there is an error reading the KV data or updating the database
     */
    public void readAndWrite() throws IOException
    {
        DatabaseManager dbManager = DatabaseManager.getInstance(DATABASE_FOLDER_PATH);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);
        FileManager fileManager = new FileManager();

        logger.info("Reading the new KV_Data");
        List<String> newLines = fileManager.readInKVData(dbQuery.getImportExport());

        logger.info("Looking for changes in KV_Data");
        List<String> newAcceptedKVs = dbQuery.compareToDatabase(newLines, "KV_Data");

        // Log the accepted KV lines
        /*
        for (String line : newAcceptedKVs)
        {
            String[] data = StringSplitter.splitString(line);
            logger.info(data[44] + " " + line);
        }
        */

        // Send emails for newly accepted KVs
        getMailsSent(newAcceptedKVs);

        // Write new lines to the database
        dbQuery.importNewKVData(newLines);
    }

    /**
     * Sends emails for the newly accepted KV requests.
     *
     * @param newAcceptedKVs the list of new accepted KV requests
     * @throws IOException if there is an error retrieving employee data or sending emails
     */
    public void getMailsSent(List<String> newAcceptedKVs) throws IOException
    {
        DatabaseManager dbManager = DatabaseManager.getInstance(DATABASE_FOLDER_PATH);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);

        List<Employee> employees = dbQuery.getEmployeesOfCategory("genehmigte KVs");
        List<KVRequest> approvedRequests = dbQuery.getKVRequests(newAcceptedKVs);

        // Map for the approved Requests and their Vermittler
        Map<String, List<KVRequest>> emailKVMap = new HashMap<>();

        // Iterate through the list of employees
        for (Employee employee : employees)
        {
            String email = employee.getEmail();
            List<KVRequest> kvRequestsForEmployee = new ArrayList<>();

            for (KVRequest request : approvedRequests)
            {
                if (request.getVermittlerNr() == employee.getId())
                {
                    kvRequestsForEmployee.add(request);
                }
            }

            if (!kvRequestsForEmployee.isEmpty())
            {
                emailKVMap.put(email, kvRequestsForEmployee);
            }
        }

        // Send emails for each employee
        for (Map.Entry<String, List<KVRequest>> entry : emailKVMap.entrySet()) {
            String email = entry.getKey();
            List<KVRequest> kvRequestList = entry.getValue();
            StringBuilder emailContent = new StringBuilder();

            String subject = String.format("Genehmigte KVs vom %s", getCurrentDate());
            emailContent.append("<html>");
            emailContent.append("<body>");
            emailContent.append("Sehr geehrte/r Mitarbeiter/in,<br><br>folgende KVs wurden genehmigt:<br><br>");

            // Add each approved KV request to the email content
            for (KVRequest request : kvRequestList) {
                emailContent.append("&rarr; ");
                emailContent.append(removeQuotes(request.getKunde()));
                emailContent.append(" - ");
                emailContent.append(request.getKvNr());
                emailContent.append(" - ");
                emailContent.append(request.getBetreff());
                emailContent.append(" - ");
                emailContent.append(request.getVermittler());
                emailContent.append("<br>");
            }
            emailContent.append("<br><b>Sanitätshaus Ortho-Aktiv </b><br>Gradnerstraße 108<br>8055 Graz<br>");
            emailContent.append("</body>");
            emailContent.append("</html>");

            try {
                emailService.sendEmail(email, subject, emailContent.toString());
            } catch (MessagingException me) {
                logger.error("Fehler beim Senden der E-Mail an " + email + ": " + me.getMessage());
            }
        }
    }

    /**
     * Removes surrounding quotes from a string.
     *
     * @param input the string from which to remove quotes
     * @return the string without surrounding quotes
     */
    public static String removeQuotes(String input)
    {
        if (input != null && input.startsWith("\"") && input.endsWith("\""))
        {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }

    /**
     * Gets the current date formatted as "dd-MM-yyyy".
     *
     * @return the current date as a formatted string
     */
    public static String getCurrentDate()
    {
        StringBuilder Date = new StringBuilder();

        int day = LocalDate.now().getDayOfMonth();
        int month = LocalDate.now().getMonthValue();
        int year = LocalDate.now().getYear();

        Date.append(day);
        Date.append("-");
        Date.append(month);
        Date.append("-");
        Date.append(year);

        return Date.toString();
    }


}
