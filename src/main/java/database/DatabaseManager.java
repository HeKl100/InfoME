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
    private static final String DATABASE_FOLDER_PATH = "C:\\InfoME\\Datenbank";
    private String dbUrl;
    private Connection conn;

    private DatabaseManager(String dbUrl)
    {
        this.dbUrl = dbUrl;
    }

    public static String getDatabaseFolderPath() {
        return DATABASE_FOLDER_PATH;
    }

    public static synchronized DatabaseManager getInstance(String dbUrl)
    {
        if(instance == null)
        {
            instance = new DatabaseManager(dbUrl);
        }
        return instance;
    }

    public Connection getConn() throws SQLException
    {
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
        // 5.) KVData
        // here the recent KV Data will be saved

        String createKVDataTableSQL = "CREATE TABLE IF NOT EXISTS KV_Data (" +
                "Status TEXT NOT NULL," +
                "Vorgang_Nr TEXT NOT NULL," +
                "VO_Datum TEXT NOT NULL," +
                "Datum TEXT NOT NULL," +
                "Kunden_Nr TEXT NOT NULL," +
                "Kunde TEXT NOT NULL," +
                "Sachbearbeiter_Nr TEXT NOT NULL," +
                "Sachbearbeiter TEXT NOT NULL," +
                "ERF_Mitarbeiter_Nr TEXT NOT NULL," +
                "ERF_Mitarbeiter TEXT NOT NULL," +
                "Filiale_Nr TEXT NOT NULL," +
                "Filiale TEXT NOT NULL," +
                "Vermittler_Nr TEXT NOT NULL," +
                "Vermittler TEXT NOT NULL," +
                "Betreff TEXT NOT NULL," +
                "KV_Nr TEXT NOT NULL," +
                "KV_Datum TEXT NOT NULL," +
                "KV_Genehmigung TEXT NOT NULL," +
                "KV_Genehm_Datum TEXT NOT NULL," +
                "Letzte_Aenderung_am TEXT NOT NULL," +
                "Letzte_Aenderung_Tage TEXT NOT NULL" +
                ");";


        //--------------------------------------------------------------------------------------------------------------
        // 6.) Controlling Data List Creation


        String createControllingDataSQL = "CREATE TABLE IF NOT EXISTS ControllingData (" +
                "Status TEXT NOT NULL," +  // 0
                "Uebernahme_Datum TEXT NOT NULL," + // 1
                "Vorgang_Nr TEXT NOT NULL," + // 2
                "VO_Datum TEXT NOT NULL," + // 3
                "Datum TEXT NOT NULL," + // 4
                "Kunden_Nr TEXT NOT NULL," + // 5
                "Kunde TEXT NOT NULL," + // 6
                "KT_Typ TEXT NOT NULL," + // 7
                "KT_Nr TEXT NOT NULL," + // 8
                "Kostentraeger TEXT NOT NULL," + // 9
                "Mitarbeiter1_Nr TEXT NOT NULL," + // 10
                "Mitarbeiter1 TEXT NOT NULL," + // 11
                "Mitarbeiter2_Nr TEXT NOT NULL," + // 12
                "Mitarbeiter2 TEXT NOT NULL," + // 13
                "Mitarbeiter3_Nr TEXT NOT NULL," + // 14
                "Mitarbeiter3 TEXT NOT NULL," + // 15
                "Sachbearbeiter_Nr TEXT NOT NULL," + // 16
                "Sachbearbeiter TEXT NOT NULL," + // 17
                "ERF_Mitarbeiter_Nr TEXT NOT NULL," + // 18
                "ERF_Mitarbeiter TEXT NOT NULL," + // 19
                "Filiale_Nr TEXT NOT NULL," + // 20
                "Filiale TEXT NOT NULL," + // 21
                "Verordner_Nr TEXT NOT NULL," + // 22
                "Verordner TEXT NOT NULL," + // 23
                "Vermittler_Nr TEXT NOT NULL," + // 24
                "Vermittler TEXT NOT NULL," + // 25
                "Betreff TEXT NOT NULL," + // 26
                "Bemerkung TEXT NOT NULL," + // 27
                "Auftrag_Nr TEXT NOT NULL," + // 28
                "AU_Datum TEXT NOT NULL," + // 29
                "Lieferschein_Nr TEXT NOT NULL," + // 30
                "Lieferschein_Datum TEXT NOT NULL," + // 31
                "Lieferdatum TEXT NOT NULL," + // 32
                "KV_Nr TEXT NOT NULL," + // 33
                "KV_Datum TEXT NOT NULL," + // 34
                "KV_Genehmigung TEXT NOT NULL," + // 35
                "KV_Genehm_Datum TEXT NOT NULL," + // 36
                "Lieferstatus TEXT NOT NULL," + // 37
                "Abrechnungsstatus TEXT NOT NULL," + // 38
                "Letzte_Aenderung_am TEXT NOT NULL," + // 39
                "Letzte_Aenderung_Tage TEXT NOT NULL," + // 40
                "Letzte_Aenderung TEXT NOT NULL" + // 41
                ");";

        //--------------------------------------------------------------------------------------------------------------
        // Executing and creating all Tables.

        try (Connection conn = getConn(); Statement stmt = conn.createStatement())
        {
         stmt.executeUpdate(createMailConfigTableSQL);
         stmt.executeUpdate(createEmployeeTableSQL);
         stmt.executeUpdate(createImportExportTableSQL);
         stmt.executeUpdate(createTriggerTableSQL);
         stmt.executeUpdate(createKVDataTableSQL);
         stmt.executeUpdate(createControllingDataSQL);

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
