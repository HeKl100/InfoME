package database;

import logging.LoggerWrapper;
import model.Employee;
import model.ImportExport;
import model.MailSetting;
import model.Trigger;

import java.sql.*;

public class DatabaseQuery
{
    private DatabaseManager dbManager;

    private static final LoggerWrapper logger = new LoggerWrapper(DatabaseQuery.class);

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


}
