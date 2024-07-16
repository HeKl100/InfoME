package database;

import logging.LoggerWrapper;
import model.*;
import file.StringSplitter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DatabaseQuery
{
    private DatabaseManager dbManager;
    private static final LoggerWrapper logger = new LoggerWrapper(DatabaseQuery.class);


    //------------------------------------------------------------------------------------------------------------------
    // Constructor of DatabaseQuery

    public DatabaseQuery(DatabaseManager dbManager)
    {
        this.dbManager = dbManager;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method to make a change on the mail settings

    public void upsertMailSettings(String email, String user, String password, String server, String port)
    {
        String sql = "INSERT OR REPLACE INTO MailSettings (ID, Email, User, Password, Server, Port) VALUES(?,?,?,?,?,?)";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, 1);
            pstmt.setString(2, email);
            pstmt.setString(3, user);
            pstmt.setString(4, password);
            pstmt.setString(5, server);
            pstmt.setString(6, port);
            pstmt.executeUpdate();
        } catch (SQLException e)
        {
            logger.error("Fehler beim Speichern der MailSetting Daten! " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method to get the mail settings

    public MailSetting getMailSetting()
    {
        String sql = "SELECT * FROM MailSettings WHERE ID = ?";
        MailSetting setting = null;

        try(Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, 1);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    String email = rs.getString("Email");
                    String user = rs.getString("User");
                    String password = rs.getString("Password");
                    String server = rs.getString("Server");
                    String port = rs.getString("Port");

                    setting = new MailSetting(email, user, password, server, port);
                    logger.info("Mail settings successfully loaded! User: " + user + ", Password: " + password + ", Server: " + server + ", Port: " + port);
                }
                else
                {
                    logger.info("Mail settings not found!");
                }
            }
        } catch (SQLException e)
        {
            logger.error("there was an error while loading the mail settings! " + e.getMessage());
            throw new RuntimeException(e);
        }
        return setting;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method to get all employees

    public Employee[] getAllEmployees()
    {
        String countSql = "SELECT COUNT(*) FROM Employee";
        String dataSql = "SELECT * FROM Employee";
        int rowCount = 0;

        try (Connection conn = dbManager.getConn();
             PreparedStatement countPstmt = conn.prepareStatement(countSql);
             ResultSet countRs = countPstmt.executeQuery())
        {
            if (countRs.next())
            {
                rowCount = countRs.getInt(1);
            }
        } catch (SQLException e)
        {
            logger.error("Fehler beim Zählen der Mitarbeiter! " + e.getMessage());
            throw new RuntimeException("error while counting the employees! " + e);
        }

        Employee[] employees = new Employee[rowCount];
        int index = 0;

        try (Connection conn = dbManager.getConn();
             PreparedStatement dataPstmt = conn.prepareStatement(dataSql);
             ResultSet dataRs = dataPstmt.executeQuery())
        {
            while(dataRs.next())
            {
                int id = dataRs.getInt("ID");
                String name = dataRs.getString("name");
                String surname = dataRs.getString("surname");
                String email = dataRs.getString("email");
                String department = dataRs.getString("department");
                String category = dataRs.getString("category");
                String state = dataRs.getString("state");

                employees[index++] = new Employee(id, name, surname, email, department, category, state);
            }
        } catch (SQLException e)
        {
            logger.error("Fehler beim Laden der Mitarbeiter! " + e.getMessage());
            throw new RuntimeException("error while loading the employees! " + e);
        }

        return employees;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method to add an employee to the database

    public void addEmployee(Employee employee)
    {
        String sql = "INSERT INTO Employee VALUES(?,?,?,?,?,?)";

        try (Connection conn = dbManager.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getSurname());
            pstmt.setString(4, employee.getEmail());
            pstmt.setString(5, employee.getDepartment());
            pstmt.setString(6, employee.getCategory());
            pstmt.setString(7, employee.getState());

            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            logger.error("Fehler beim Einfügen eines neuen Mitarbeiters! " + e.getMessage());
            throw new RuntimeException("error while adding the employee to the database! " + e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method to update an existing employee

    public void updateEmployee(Employee employee)
    {
        String sql = "UPDATE Employee SET name = ?, surname = ?, email = ?, department = ?, state = ? WHERE ID = ? AND category = ?";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getSurname());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getDepartment());
            pstmt.setString(5,employee.getState());
            pstmt.setInt(6, employee.getId());
            pstmt.setString(7, employee.getCategory());

            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            logger.error("Fehler beim Ändern eines Mitarbeiters! " + e.getMessage());
            throw new RuntimeException("error during updating an employee" + e);
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // method for inserting or updating a user

    public void saveEmployee(Employee employee)
    {
        String sql = "INSERT OR REPLACE INTO Employee (ID, name, surname, email, department, category, state) " +
                     "VALUES (?,?,?,?,?,?,?)";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setString(3, employee.getSurname());
            pstmt.setString(4, employee.getEmail());
            pstmt.setString(5, employee.getDepartment());
            pstmt.setString(6, employee.getCategory());
            pstmt.setString(7, employee.getState());

            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            logger.error("Fehler beim aktualisieren oder einfügen eines Mitarbeiters! " + e.getMessage());
            throw new RuntimeException("error during inserting or updating a user! " + e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // save ImportExport data to the database

    public void upsertImportExport(ImportExport importExport)
    {
        String sql = "INSERT OR REPLACE INTO ImportExport (ID, importPath, filename, sachbearbeiter, vermittler) VALUES(?,?,?,?,?)";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, 1);
            pstmt.setString(2, importExport.getImportPath());
            pstmt.setString(3, importExport.getFilename());
            pstmt.setString(4, importExport.getSachbearbeiter());
            pstmt.setString(5, importExport.getVermittler());

            pstmt.executeUpdate();

        } catch (SQLException e)
        {
            logger.error("Fehler beim Speichern der ImportExport Daten! " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // get the ImportExport data from database

    public ImportExport getImportExport()
    {
        String sql = "SELECT * FROM ImportExport WHERE ID = ?";
        ImportExport importExport = null;


        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, 1);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if(rs.next())
                {
                    String importPath = rs.getString("importPath");
                    String filename = rs.getString("filename");
                    String sachbearbeiter = rs.getString("sachbearbeiter");
                    String vermittler = rs.getString("vermittler");

                    importExport = new ImportExport(importPath, filename, sachbearbeiter, vermittler);
                    logger.info("ImportExport settings erfolgreich geladen! Pfad: " + importExport + "Filename: " + filename + " Sachbearbeiter: " + sachbearbeiter + " Vermittler: " + vermittler);
                }
            }
            catch (SQLException e)
            {
                logger.info("Keine ImportExport settings gefunden! " + e.getMessage());
            }

        }
        catch (SQLException e)
        {
            logger.error("Fehler beim Laden der ImportExport settings! " + e.getMessage());
        }

        return importExport;
    }

    //------------------------------------------------------------------------------------------------------------------
    // save trigger data to the database

    public void upsertTrigger(Trigger t)
    {
        String sql = "INSERT OR REPLACE INTO Trigger (ID, scheduleName, scheduleInterval, scheduleIntervalOption, scheduleTime) VALUES(?,?,?,?,?)";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {

            pstmt.setInt(1, t.getId());
            pstmt.setString(2, t.getScheduleName());
            pstmt.setString(3, t.getScheduleInterval());
            pstmt.setString(4, t.getScheduleIntervalOption());
            pstmt.setString(5,t.getGetScheduleTime());

            pstmt.executeUpdate();

            logger.info("Triggers erfolgreich gespeichert!");
        }
        catch (SQLException e)
        {
            logger.error("Fehler beim Speichern der Trigger! " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // get all triggers

    public Trigger[] getTriggerSettings()
    {
        String countSql = "SELECT COUNT(*) FROM Trigger";
        String dataSql = "SELECT * FROM Trigger";
        int rowCount = 0;

        try(Connection conn = dbManager.getConn();
            PreparedStatement pstmt = conn.prepareStatement(countSql);
            ResultSet countRs = pstmt.executeQuery())
        {
            if(countRs.next())
            {
                rowCount = countRs.getInt(1);
            }
        }
        catch (SQLException e)
        {
            logger.error("Fehler beim Zählen der Trigger! " + e.getMessage());
            throw new RuntimeException("error while counting triggers! " + e);
        }

        Trigger[] triggers = new Trigger[rowCount];
        int index = 0;

        try (Connection conn = dbManager.getConn();
             PreparedStatement dataPstmt = conn.prepareStatement(dataSql);
             ResultSet dataRs = dataPstmt.executeQuery())
        {
            while (dataRs.next())
            {
                int id = dataRs.getInt(1);
                String scheduleName = dataRs.getString(2);
                String scheduleInterval = dataRs.getString(3);
                String scheduleIntervalOption = dataRs.getString(4);
                String scheduleTime = dataRs.getString(5);

                triggers[index++] = new Trigger(id, scheduleName, scheduleInterval, scheduleIntervalOption, scheduleTime);
            }
        }
        catch (SQLException e)
        {
            logger.error("Fehler beim Laden der Trigger! " + e.getMessage());
            throw new RuntimeException("error while counting triggers! " + e);
        }

        return triggers;
    }

    //------------------------------------------------------------------------------------------------------------------
    // delete a Trigger from the database

    public void deleteTrigger(int id)
    {
        String sql = "DELETE FROM Trigger WHERE ID = ?";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            logger.info("Trigger " + id + " wurde erfolgreich gelöscht!");
        }
        catch (SQLException e)
        {
            logger.error("Fehler beim Löschen des Triggers " + id + "! " + e.getMessage());
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // delete Employee from database

    public void deleteEmployee(int id, String category)
    {
        String sql = "DELETE FROM Employee WHERE ID = ? AND category = ?";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, id);
            pstmt.setString(2, category);

            pstmt.executeUpdate();
            logger.info("Mitarbeiter " + id + " mit dem Trigger " + category + " wurde erfolgreich gelöscht!");
        }
        catch (SQLException e)
        {
            logger.error("Mitarbeiter " + id + " mit dem Trigger " + category + " konnte nicht gelöscht werden!");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // clear Database Table if needed

    private void clearTable(String tableName) {
        String sql = "DELETE FROM " + tableName;
        try (Connection conn = dbManager.getConn();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            logger.info("Table '" + tableName + "' has been cleared.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // write new Data to Database Table ( KV_Data)

    public void importNewKVData(List<String> lines) {
        clearTable("KV_Data");

        String sql = "INSERT INTO KV_Data (Status, Vorgang_Nr, VO_Datum, Datum, Kunden_Nr, Kunde, Sachbearbeiter_Nr, Sachbearbeiter, ERF_Mitarbeiter_Nr, ERF_Mitarbeiter, Filiale_Nr, Filiale, Vermittler_Nr, Vermittler, Betreff, KV_Nr, KV_Datum, KV_Genehmigung, KV_Genehm_Datum, Letzte_Aenderung_am, Letzte_Aenderung_Tage) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            int counter = 1;

            for (String line : lines) {

                String[] data = StringSplitter.splitString(line);
                if (data.length == 71)  // Ensure data has exactly 71 columns
                {
                    try
                    {
                        pstmt.setString(1, getValue(data, 0)); // Status
                        pstmt.setString(2, getValue(data, 2)); // Vorgang Nr
                        pstmt.setString(3, getValue(data, 3)); // VO Datum
                        pstmt.setString(4, getValue(data, 5)); // Datum
                        pstmt.setString(5, getValue(data, 8)); // Kunden Nr
                        pstmt.setString(6, getValue(data, 9)); // Kunde
                        pstmt.setString(7, getValue(data, 21)); // Sachbearbeiter Nr
                        pstmt.setString(8, getValue(data, 22)); // Sachbearbeiter
                        pstmt.setString(9, getValue(data, 23)); // Erf Mitarbeiter Nr
                        pstmt.setString(10, getValue(data, 24)); // ERF Mitarbeiter
                        pstmt.setString(11, getValue(data, 25)); // Filiale Nr
                        pstmt.setString(12, getValue(data, 26)); // Filiale
                        pstmt.setString(13, getValue(data, 29)); // Vermittler Nr
                        pstmt.setString(14, getValue(data, 30)); // Vermittler
                        pstmt.setString(15, getValue(data, 31)); // Betreff
                        pstmt.setString(16, getValue(data, 44)); // KV Nr
                        pstmt.setString(17, getValue(data, 45)); // KV Datum
                        pstmt.setString(18, getValue(data, 46)); // KV Genehmigung
                        pstmt.setString(19, getValue(data, 47)); // KV Genehmigung Datum
                        pstmt.setString(20, getValue(data, 66)); // Letzte Aenderung am
                        pstmt.setString(21, getValue(data, 67)); // Letzte Aenderung vor X Tagen

                        pstmt.executeUpdate();
                    }
                    catch (SQLException sqle)
                    {
                        logger.info("Could'nt process following line: \n" + line);
                        sqle.printStackTrace();
                    }
                }
                else
                {
                    logger.info("Line skipped, it does not have 71 columns: " + line + "\nActual Length:" + data.length);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        logger.info("Successfully imported " + lines.size() + " lines.");
    }

    /**
     *
     * @param data Array of Strings
     * @param index preferred index to return
     * @return returns the specific value of the array specified by the given index.
     *         If index > array size returns ""
     */
    private String getValue(String[] data, int index) {
        return index < data.length ? data[index] : "";
    }

    //------------------------------------------------------------------------------------------------------------------
    // Compares a List of Lines to a Database Table and returns only those lines where the value KV Genehmigung
    // changed from "offen" or from "zur Klärung to "genehmigt". OR THOSE Which are new and "genehmigt"

    public List<String> compareToDatabase(List<String> lines, String tableName) {

        List<String> acceptedKVs = new ArrayList<>();

        // Iterate through the new Data Lines
        for (String line : lines) {

            String[] data = StringSplitter.splitString(line); // Split
            if (data.length < 47) {
                System.out.println("Line does not have enough data: " + line);
                continue;
            }
            String KVNr = data[44]; // Isolate the KVNr as String


            if(isNumeric(KVNr)) // if KVNr is a Number ( and not "Ohne Zuordnung")
            {
                int KVNrInt = Integer.parseInt(KVNr); // turn KVNr into an actual number
                // fetch the old KV Status from the old Data
                String oldKVStatus = getKVGenehmigungByKVNr(KVNrInt, "KV_Data");
                // fetch the new KV Status from the new Line
                String newKVStatus = data[46];

                if("genehmigt".equals(newKVStatus))
                {
                    if("offen".equals(oldKVStatus) || "zur Klärung".equals(oldKVStatus))
                    {
                        acceptedKVs.add(line);
                    }

                }

            }

        }
        return acceptedKVs;
    }

    //------------------------------------------------------------------------------------------------------------------
    // get KV Genehmigung Value by the given KV Number in our Database

    public String getKVGenehmigungByKVNr(int kvNr, String tableName) {
    String sql = "SELECT KV_Genehmigung FROM KV_Data WHERE KV_Nr = ?";
    String kvGenehmigung = null;

    try (Connection conn = dbManager.getConn(); PreparedStatement pstmt = conn.prepareStatement(sql))
    {
        pstmt.setInt(1, kvNr);  // Set the KV_Nr parameter

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                kvGenehmigung = rs.getString("KV_Genehmigung");  // Get the KV_Genehmigung value
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return kvGenehmigung;
}

    //------------------------------------------------------------------------------------------------------------------
    // Returns true if the given String is a Number

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<Employee> getEmployeesOfCategory(String categoryStr) throws IOException {
        List<Employee> employees = new ArrayList<>();


        String sql = "SELECT * FROM Employee WHERE state = 'aktiv' AND category = "+ categoryStr +" ORDER BY email ASC ";
        try (Connection conn = dbManager.getConn();
             PreparedStatement dataPstmt = conn.prepareStatement(sql);
             ResultSet data = dataPstmt.executeQuery()) {
            while (data.next()) {
                int id = data.getInt(1);
                String firstname = data.getString(2);
                String lastname = data.getString(3);
                String email = data.getString(4);
                String department = data.getString(5);
                String category = data.getString(6);
                String state = data.getString(7);

                Employee temp = new Employee(id, firstname, lastname, email, department, category, state);

                employees.add(temp);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    public List<KVRequest> getKVRequests(List<String> newAcceptedKVs) {
            List<KVRequest> kvRequests = new ArrayList<>();
        for (String line : newAcceptedKVs) {
            String[] data = StringSplitter.splitString(line);

            String kundenNr = getValue(data, 8);  // Kunden Nr
            String kunde = getValue(data, 9);  // Kunde
            String sachbearbeiterNr = getValue(data, 21);  // Sachbearbeiter Nr
            String sachbearbeiter = getValue(data, 22);  // Sachbearbeiter
            String vermittlerNr = getValue(data, 29);  // Vermittler Nr
            String vermittler = getValue(data, 30);  // Vermittler
            String betreff = getValue(data, 31);  // Betreff
            String kvNr = getValue(data, 44);  // KV Nr

            // If there is a Vermittler then add the KV Request to the List
            if(isNumeric(vermittlerNr)) {
                KVRequest kvRequest = new KVRequest(kundenNr, kunde, Integer.parseInt(sachbearbeiterNr), sachbearbeiter, Integer.parseInt(vermittlerNr), vermittler, betreff, Integer.parseInt(kvNr));
                kvRequests.add(kvRequest);
            }

        }
        return kvRequests;
    }

    /*
     *
     * @param lines
     * @param tableName
     * @return List<String> lines

    public List<String> getNewLines(List<String> lines, String tableName) {
        List<String> newLines = new ArrayList<>();
        Set<String> existingIds = new HashSet<>();

        try (Connection connection = dbManager.getConn()) {
            // Retrieve existing IDs from the database
            String selectQuery = "SELECT Vorgang_Nr FROM " + tableName;
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
                 ResultSet resultSet = selectStmt.executeQuery()) {
                while (resultSet.next()) {
                    existingIds.add(resultSet.getString("Vorgang_Nr"));
                }
            }

            // Compare new data against the existing IDs
            for (String line : lines) {
                String[] data = StringSplitter.splitString(line);// Assuming CSV format

                // Check if there are at least 47 columns
                if (data.length >= 47)
                {
                    String vorgangsNr = data[2].trim();  // Extract the "VorgangsNr" from the third column and trim whitespace
                    String status = data[46].trim();
                    if (!existingIds.contains(vorgangsNr) && "genehmigt".equalsIgnoreCase(status)) {
                        newLines.add(line);
                    }
                } else {
                    System.err.println("Line does not have enough columns: " + line);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return newLines;
    }*/
}
