package database;

import logging.LoggerWrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager
{
    private static DatabaseManager instance;

    private static final LoggerWrapper logger = new LoggerWrapper(DatabaseManager.class);

    private String dbUrl;

    private Connection conn;

    private DatabaseManager(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }

    public static synchronized DatabaseManager getInstance(String dbUrl)
    {
        if(instance == null)
        {
            instance = new DatabaseManager(dbUrl);
        }
        return instance;
    }

    public Connection getConn() throws SQLException {
        if(conn == null || conn.isClosed())
        {
            conn = DriverManager.getConnection(dbUrl);
        }
        return conn;
    }

    public void closeConnecting()
    {
        if(conn != null)
        {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createTables()
    {
        // creating database tables

        //--------------------------------------------------------------------------------------------------------------
        // 1.) MailSettings
        // here the mail settings will be stored

        String createMailConfigTableSQL = "CREATE TABLE IF NOT EXISTS MailSettings (" +
                "ID INTEGER PRIMARY KEY," +
                "Email TEXT NOT NULL," +
                "User TEXT," +
                "Password TEXT," +
                "Server TEXT, " +
                "Port TEXT" +
                ")";

        //--------------------------------------------------------------------------------------------------------------
        // 2.) employees
        // here the employee settings will be stored

        String createEmployeeTableSQL = "CREATE TABLE IF NOT EXISTS Employee (" +
                "ID INTEGER," +
                "name TEXT NOT NULL," +
                "surname TEXT NOT NULL," +
                "email TEXT NOT NULL," +
                "department TEXT NOT NULL," +
                "category TEXT," +
                "state STRING NOT NULL," +
                "PRIMARY KEY (ID, category))";

        //--------------------------------------------------------------------------------------------------------------
        // 3.) Import/export
        // here the import/export settings will be stored

        String createImportExportTableSQL = "CREATE TABLE IF NOT EXISTS ImportExport (" +
                "ID INTEGER PRIMARY KEY," +
                "importpath TEXT NOT NULL," +
                "filename TEXT NOT NULL," +
                "Sachbearbeiter TEXT NOT NULL," +
                "Vermittler TEXT NOT NULL" +
                ")";

        //--------------------------------------------------------------------------------------------------------------
        // 4.) Trigger
        // here the trigger settings will be stored

        String createTriggerTableSQL = "CREATE TABLE IF NOT EXISTS Trigger (" +
                "ID INTEGEGER PRIMARY KEY," +
                "scheduleName TEXT NOT NULL," +
                "scheduleInterval TEXT NOT NULL," +
                "scheduleIntervalOption TEXT," +
                "scheduleTime TEXT NOT NULL" +
                ")";

        //--------------------------------------------------------------------------------------------------------------

        try (Connection conn = getConn(); Statement stmt = conn.createStatement())
        {
         stmt.executeUpdate(createMailConfigTableSQL);
         stmt.executeUpdate(createEmployeeTableSQL);
         stmt.executeUpdate(createImportExportTableSQL);
         stmt.executeUpdate(createTriggerTableSQL);

         // creation was successfully

         logger.info("Table - MailSettings - successfully created");
        }
        catch (SQLException e)
        {
            // creation was not successfully

            logger.info("Table - MailSettings - could not be created! " + e.getMessage());

            throw new RuntimeException(e);
        }

    }
}
