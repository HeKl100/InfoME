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
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReadWriteKV {
    private static final LoggerWrapper logger = new LoggerWrapper(ReadWriteKV.class);
    private static final EmailService emailService = EmailService.getInstance();


    public void readAndWrite() throws IOException {
        String databaseFolderPath = "C:\\InfoME\\Datenbank";
        DatabaseManager dbManager = DatabaseManager.getInstance(databaseFolderPath);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);
        FileManager fileManager = new FileManager();

        //  ------- Read in new File -------
        logger.info("Reading the new KV_Data");
        List<String> newLines = fileManager.readInKVData(dbQuery.getImportExport());

        //  ------- Compare new Lines to old Database -------
        logger.info("Looking for Changes in KV_Data");

        List<String> newAcceptedKVs = dbQuery.compareToDatabase(newLines,"KV_Data");
        //newAcceptedKVs.addAll(dbQuery.getNewLines(newLines, "KV_Data"));
        for (String line : newAcceptedKVs) {
            String[] data = StringSplitter.splitString(line);
            logger.info(data[44] + " " + line);
        }
        // write new Data to the Database
        // dbQuery.importNewKVData(newLines);

        getMailsSent(newAcceptedKVs);
    }
    public void getMailsSent (List<String> newAcceptedKVs) throws IOException {

        // DatabaseManager and DatabaseQuery Initialization
        String databaseFolderPath = "C:\\InfoME\\Datenbank";
        DatabaseManager dbManager = DatabaseManager.getInstance(databaseFolderPath);
        DatabaseQuery dbQuery = new DatabaseQuery(dbManager);

        // List of Employees from Employee Database
        List<Employee> employees = dbQuery.getKVEmployees();

        // List of recently approved KV Requests
        List<KVRequest> approvedRequests = dbQuery.getKVRequests(newAcceptedKVs);

        // Map for the approved Requests and their Vermittler
        Map<String, List<KVRequest>> emailKVMap = new HashMap<>();



        String mail = employees.getFirst().getEmail();
        List<KVRequest> KvPerEmail = new ArrayList<>();


        // Iterate through the list of employees
        for (Employee employee : employees)
        {
            String mailNow = employee.getEmail();

            // Check if the current employee's email matches the previous one
            if (mailNow.equals(mail))
            {
                for (KVRequest request : approvedRequests)
                {
                    // Add the approved KV requests for the current employee
                    if (request.getVermittlerNr() == employee.getId())
                    {
                        KvPerEmail.add(request);
                    }

                }
            }
            else
            {
                // If the email has changed, store the current list of KV requests and reset the list
                emailKVMap.put(mail, new ArrayList<>(KvPerEmail));
                KvPerEmail.clear();
                mail = mailNow;
                // Add the approved KV requests for the new employee
                for (KVRequest request : approvedRequests)
                {
                    if (request.getVermittlerNr() == employee.getId())
                    {
                        KvPerEmail.add(request);
                    }

                }
            }
        }

        // Add the remaining KV requests for the last email
        if (!KvPerEmail.isEmpty())
        {
            emailKVMap.put(mail, new ArrayList<>(KvPerEmail));
        }


        for (Map.Entry<String, List<KVRequest>> entry : emailKVMap.entrySet()) {
            String email = entry.getKey();
            List<KVRequest> kvRequestList = entry.getValue();
            StringBuilder emailContent = new StringBuilder();


            String subject = String.format("Genehmigte KVs vom %s", getCurrentDate());
            emailContent.append("<html>");
            emailContent.append("<body>");
            emailContent.append("Sehr geehrte/r Mitarbeiter/in,<br><br>folgende KVs wurden genehmigt:<br><br>");

            // Add each approved KV request to the email content
            for (KVRequest request : kvRequestList)
            {

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

            try
            {
                // Send the email ONLY if there are approved KV requests
                if(!kvRequestList.isEmpty())
                {
                    emailService.sendEmail(email,subject,emailContent.toString());
                }

            }
            catch (MessagingException me)
            {
                logger.error("Fehler beim Senden der E-Mail: " + me.getMessage());
            }
        }

    }
    public static String removeQuotes(String input) {
        if (input != null && input.length() > 1 && input.startsWith("\"") && input.endsWith("\"")) {
            return input.substring(1, input.length() - 1);
        }
        return input;
    }
    public static String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}