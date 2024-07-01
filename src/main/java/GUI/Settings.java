package GUI;

import com.sun.source.tree.TryTree;
import database.DatabaseManager;
import database.DatabaseQuery;
import jakarta.mail.MessagingException;
import logging.LoggerWrapper;
import mail.EmailService;
import model.Employee;
import model.ImportExport;
import model.MailSetting;
import model.Trigger;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

public class Settings extends JFrame
{
    private static DatabaseManager dbManager;
    private static DatabaseQuery dbQuery;
    public String databaseFolderPath;

    private MailSetting mailSetting;

    private ImportExport importExport;

    private EmployeeTableModel ETM;
    private TriggerTableModel TTM;

    private static final LoggerWrapper logger = new LoggerWrapper(Settings.class);

    private String[] scheduleOptionsWeekly;
    private String[] scheduleOptionsMonthly;

    private JPanel pnSettings;
    private JTabbedPane tpSettings;
    private JPanel tpSettingsEmail;
    private JPanel tpSettingsImportExport;
    private JPanel tpSettingsMitarbeiter;
    private JLabel lbUser;
    private JLabel lbPassword;
    private JLabel lbServer;
    private JLabel lbPort;
    private JTextField tfUser;
    private JTextField tfPassword;
    private JTextField tfServer;
    private JLabel lbSettings;
    private JTextField tfPort;
    private JButton btMailConnectionTest;
    private JTextField tfEmail;
    private JLabel lbEmail;
    private JButton btSave;
    private JTable tbEmployees;
    private JTextField tfName;
    private JTextField tfSurname;
    private JTextField tfEmailEmployee;
    private JTextField tfDepartment;
    private JTextField tfID;
    private JLabel lbName;
    private JLabel lbSurname;
    private JLabel lbmail;
    private JLabel lbDepartment;
    private JLabel lbCategory;
    private JLabel lbID;
    private JLabel lbState;
    private JComboBox cbCategory;
    private JComboBox cbState;
    private JButton btSaveEmployee;
    private JScrollPane spEmployee;
    private JScrollPane spTrigger;
    private JTable tbEmployee;
    private JTextField tfImportPath;
    private JButton btImportPath;
    private JTextField tfImportFileName;
    private JComboBox cbSachbearbeiter;
    private JComboBox cbVermittler;
    private JButton btSaveImportExport;
    private JPanel tpSettingsTrigger;
    private JTable tbTrigger;
    private JButton btTriggerSave;
    private JButton btTriggerAdmin;
    private JTextField tfTriggerID;
    private JTextField tfTriggerName;
    private JButton btTriggerDelete;
    private JComboBox cbSchedule;
    private JComboBox cbScheduleOption;
    private JComboBox cbScheduleTime;
    private JButton btEmployeeDelete;

    public Settings()
    {
        // fullscreen
        // not that what it should be

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setContentPane(pnSettings);
        setTitle("InfoME - Einstellungen");
        setSize(screenSize.width, screenSize.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        databaseFolderPath = "C:\\InfoME\\Datenbank";
        dbManager = DatabaseManager.getInstance(databaseFolderPath);
        dbQuery = new DatabaseQuery(dbManager);

        try
        {
            // try to store data from MailSetting table

            mailSetting = dbQuery.getMailSetting();
        }
        catch (RuntimeException e)
        {
            // something went wrong!

            logger.error(e.getMessage());
        }

        // set the loaded data

        if(mailSetting != null)
        {
            tfEmail.setText(mailSetting.getEmail());
            tfUser.setText(mailSetting.getUser());
            tfPassword.setText(mailSetting.getPassword());
            tfServer.setText(mailSetting.getServer());
            tfPort.setText(mailSetting.getPort());
        }

        // -------------------------------------------------------------------------------------------------------------
        // now get and set the employee data

        Employee[] employees = dbQuery.getAllEmployees();

        tbEmployees = new JTable();

        ETM = new EmployeeTableModel(employees);
        tbEmployees.setModel(ETM);

        tbEmployees.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        spEmployee.setViewportView(tbEmployees);

        JTableHeader headerEmployee = tbEmployees.getTableHeader();
        spEmployee.setColumnHeaderView(headerEmployee);

        ETM.adjustColumnWidths(tbEmployees);
        tbEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {
                int selectedRow = tbEmployees.getSelectedRow();
                if(selectedRow != -1)
                {
                    loadEmployeeDataIntoFields(selectedRow);
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // now get and set the ImportExport settings

        try
        {
            importExport = dbQuery.getImportExport();
        }
        catch (RuntimeException e)
        {
            logger.error(e.getMessage());
        }

        if(importExport != null)
        {
            tfImportPath.setText(importExport.getImportPath());
            tfImportFileName.setText(importExport.getFilename());
            cbSachbearbeiter.setSelectedItem(importExport.getSachbearbeiter());
            cbVermittler.setSelectedItem(importExport.getVermittler());
        }

        //--------------------------------------------------------------------------------------------------------------
        // load Trigger settings

        scheduleOptionsWeekly = new String[]{"Montags", "Dienstags", "Mittwochs", "Donnerstags", "Freitags", "Samstags", "Sonntags"};
        scheduleOptionsMonthly = new String[]{"1. des Monats","2. des Monats","3. des Monats","4. des Monats","5.des Monats",
                                              "6. des Monats","7. des Monats","8. des Monats","9. des Monats","10. des Monats",
                                              "11. des Monats","12. des Monats","13. des Monats","14. des Monats","15. des Monats",
                                              "16. des Monats","17. des Monats","18. des Monats","19. des Monats","20. des Monats",
                                              "21. des Monats","22. des Monats","23. des Monats","24. des Monats","25. des Monats",
                                              "26. des Monats","27. des Monats","28. des Monats","29. des Monats","30. des Monats",
                                              "31. des Monats"};

        Trigger[] triggers = dbQuery.getTriggerSettings();

        TTM = new TriggerTableModel(triggers);
        tbTrigger.setModel(TTM);

        tbTrigger.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        spTrigger.setViewportView(tbTrigger);

        JTableHeader headerTrigger = tbTrigger.getTableHeader();

        spTrigger.setColumnHeaderView(headerTrigger);

        TTM.adjustColumnWidths(tbTrigger);
        tbTrigger.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {
                int selectedRow = tbTrigger.getSelectedRow();
                if(selectedRow != -1)
                {
                    loadTriggerDataIntoFields(selectedRow);
                }
            }
        });


        //--------------------------------------------------------------------------------------------------------------

        // send Test-Mail

        btMailConnectionTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String email = tfEmail.getText();
                String user = tfUser.getText();
                String password = tfPassword.getText();
                String server = tfServer.getText();
                String port = tfPort.getText();

                if(email.equals("") || user.equals("") || password.equals("") || server.equals("") || port.equals(""))
                {
                    JOptionPane.showMessageDialog(null, "Bitte alle Daten angeben!");
                }
                else
                {
                    // let the user insert the receiver
                    String receiver = JOptionPane.showInputDialog(null, "Bitte die Empfänger-Mail-Adresse eingeben!");

                    EmailService ES = EmailService.getInstance(email, user, password, server, port);

                    try
                    {
                        // try to send the testmail
                        ES.senEmail(receiver, "Test-Mail // InfoME", "Daten wurden korrekt eingegeben! :)");

                        logger.info("test-e-mail was send successfully to " + receiver);
                    }
                    catch (MessagingException ex)
                    {
                        logger.error("test-e-mail failed: " + ex.getMessage());
                        JOptionPane.showMessageDialog(null, "Folgendes Problem ist aufgetreten: " + ex.getMessage());
                        throw new RuntimeException(ex);
                    }
                }

            }
        });

        // save button
        btSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String email = tfEmail.getText();
                String user = tfUser.getText();
                String password = tfPassword.getText();
                String server = tfServer.getText();
                String port = tfPort.getText();

                try
                {
                    // insert data or update the existing data
                    dbQuery.upsertMailSettings(email, user, password, server, port);
                    logger.info("mail settings were saved to the database!");
                    JOptionPane.showMessageDialog(null, "Die Daten wurden erfolgreich gespeichert!");
                }
                catch (RuntimeException ex)
                {
                    logger.error("saving mail settings failed! " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, "Die Daten konnten nicht gespeichert werden, weil -> " + ex.getMessage());
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // save the employee data

        btSaveEmployee.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    int id = Integer.parseInt(tfID.getText());
                    String name = tfName.getText();
                    String surname = tfSurname.getText();
                    String email = tfEmailEmployee.getText();
                    String department = tfDepartment.getText();
                    String category = cbCategory.getSelectedItem().toString();
                    String state = cbState.getSelectedItem().toString();

                    Employee employee = new Employee(id, name, surname, email, department, category, state);
                    dbQuery.saveEmployee(employee);

                    ETM.updateEmployeesTable(dbQuery.getAllEmployees());
                    clearEmployeeFields();
                    ETM.adjustColumnWidths(tbEmployees);

                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(pnSettings, "Bitte gültige Werte eingeben!", "Fehler", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // FileChooser for import-path

        btImportPath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser chooser = new JFileChooser();

                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int result = chooser.showOpenDialog(btImportPath);

                if(result == JFileChooser.APPROVE_OPTION)
                {
                    File file = chooser.getSelectedFile();
                    tfImportPath.setText(file.getAbsolutePath());
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // save import and export data to the database

        btSaveImportExport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    int id = 1;
                    String importPath = tfImportPath.getText();
                    String fileName = tfImportFileName.getText();
                    String Sachbearbeiter = cbSachbearbeiter.getSelectedItem().toString();
                    String Vermittler = cbVermittler.getSelectedItem().toString();

                    ImportExport ie = new ImportExport(importPath, fileName, Sachbearbeiter, Vermittler);

                    dbQuery.upsertImportExport(ie);

                    JOptionPane.showMessageDialog(pnSettings, "ImportExport Daten wurden erfolgreich gespeichert!");
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(pnSettings,"Bitte gültige Werte eingeben! " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // save trigger data to the database

        btTriggerSave.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    btTriggerDelete.setEnabled(false);
                    tfTriggerID.setEnabled(false);
                    tfTriggerName.setEnabled(false);
                    tfTriggerID.setEditable(false);
                    tfTriggerName.setEditable(false);

                    int id = Integer.parseInt(tfTriggerID.getText());
                    String triggerName = tfTriggerName.getText();
                    String triggerInterval = cbSchedule.getSelectedItem().toString();
                    String triggerIntervalOption = " ";
                    if(cbScheduleOption.getSelectedItem() != null)
                    {
                        triggerIntervalOption = cbScheduleOption.getSelectedItem().toString();
                    }
                    String triggerTime = cbScheduleTime.getSelectedItem().toString();

                    Trigger trigger = new Trigger(id, triggerName, triggerInterval, triggerIntervalOption, triggerTime);

                    dbQuery.upsertTrigger(trigger);

                    TTM.updateTriggersTable(dbQuery.getTriggerSettings());
                    clearTriggerFields();
                    TTM.adjustColumnWidths(tbTrigger);

                    JOptionPane.showMessageDialog(pnSettings,"Trigger-Daten wurden erfolgreich gespeichert!");
                }
                catch (NumberFormatException ex)
                {
                    JOptionPane.showMessageDialog(pnSettings, "Trigger-Daten konnten nicht gespeichert werden! " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // set Trigger options dynamically
        cbSchedule.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selectedSchedule = cbSchedule.getSelectedItem().toString();
                cbScheduleOption.removeAllItems();

                switch (selectedSchedule)
                {
                    case "täglich":
                        cbScheduleOption.addItem("");
                        break;
                    case "wöchentlich":
                        for(String item : scheduleOptionsWeekly)
                        {
                            cbScheduleOption.addItem(item);
                        }
                        break;
                    case "monatlich":
                        for(String item : scheduleOptionsMonthly)
                        {
                            cbScheduleOption.addItem(item);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // Admin Button

        btTriggerAdmin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //int password = Integer.parseInt(JOptionPane.showInputDialog("Bitte das Passwort eingeben: "));

                JPasswordField passwordField = new JPasswordField();
                String passwordString = "";

                int option = JOptionPane.showConfirmDialog(pnSettings,passwordField, "Passwort eingeben: ", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if(option == JOptionPane.OK_OPTION)
                {
                    char[] password = passwordField.getPassword();

                    passwordString = new String(password);
                    Arrays.fill(password, '*');
                }

                if (passwordString.equals("170294"))
                {
                    btTriggerDelete.setEnabled(true);
                    tfTriggerID.setEnabled(true);
                    tfTriggerName.setEnabled(true);
                    tfTriggerID.setEditable(true);
                    tfTriggerName.setEditable(true);
                }
                else
                {
                    JOptionPane.showMessageDialog(pnSettings,"Falsches Passwort!");
                }


            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // delete selected trigger form the database

        btTriggerDelete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int triggerToDeleteID = Integer.parseInt(tfTriggerID.getText());

                btTriggerDelete.setEnabled(false);
                tfTriggerID.setEnabled(false);
                tfTriggerName.setEnabled(false);
                tfTriggerID.setEditable(false);
                tfTriggerName.setEditable(false);
                
                try
                {
                    dbQuery.deleteTrigger(triggerToDeleteID);

                    TTM.updateTriggersTable(dbQuery.getTriggerSettings());
                    clearTriggerFields();
                    TTM.adjustColumnWidths(tbTrigger);
                    JOptionPane.showMessageDialog(pnSettings, "Eintrag wurde erfolgreich gelöscht!");
                }
                catch (RuntimeException ex)
                {
                    logger.error("Error while deleting trigger: " + triggerToDeleteID + " !" + ex.getMessage());
                }
            }
        });

        //--------------------------------------------------------------------------------------------------------------
        // delete selected employee from database

        btEmployeeDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int employeeToDeleteID = Integer.parseInt(tfID.getText());
                String employeeToDeleteCategory = cbCategory.getSelectedItem().toString();

                try
                {
                    dbQuery.deleteEmployee(employeeToDeleteID, employeeToDeleteCategory);
                    ETM.updateEmployeesTable(dbQuery.getAllEmployees());
                    clearEmployeeFields();
                    ETM.adjustColumnWidths(tbEmployee);
                    JOptionPane.showMessageDialog(pnSettings,"Eintrag wurde erfolgreich gelöscht!");
                }
                catch (RuntimeException ex)
                {
                    logger.error("Fehler während des löschens von Mitarbeiter: " + employeeToDeleteID + " mit dem Trigger " + employeeToDeleteCategory + "!" + ex.getMessage());
                }
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    // clear employee input fields

    private void clearEmployeeFields()
    {
        tfID.setText("");
        tfName.setText("");
        tfSurname.setText("");
        tfEmailEmployee.setText("");
        tfDepartment.setText("");
        cbCategory.setSelectedIndex(0);
        cbState.setSelectedIndex(0);
    }

    //------------------------------------------------------------------------------------------------------------------
    // load selected employee in input fields

    private void loadEmployeeDataIntoFields(int selectedRow)
    {
        Employee employee = ETM.getEmployeeAt(selectedRow);

        tfID.setText(String.valueOf(employee.getId()));
        tfName.setText(employee.getName());
        tfSurname.setText(employee.getSurname());
        tfEmailEmployee.setText(employee.getEmail());
        tfDepartment.setText(employee.getDepartment());
        cbCategory.setSelectedItem(employee.getCategory());
        cbState.setSelectedItem(employee.getState());
    }

    //------------------------------------------------------------------------------------------------------------------
    // load selected trigger in input fields

    private void loadTriggerDataIntoFields(int selectedRow)
    {
        Trigger trigger = TTM.getTriggerAt(selectedRow);

        tfTriggerID.setText(String.valueOf(trigger.getId()));
        tfTriggerName.setText(String.valueOf(trigger.getScheduleName()));
        cbSchedule.setSelectedItem(trigger.getScheduleInterval());
        cbScheduleOption.setSelectedItem(trigger.getScheduleIntervalOption());
        cbScheduleTime.setSelectedItem(trigger.getGetScheduleTime());

    }

    //------------------------------------------------------------------------------------------------------------------
    // clear employee input fields

    private void clearTriggerFields()
    {
        tfTriggerID.setText("");
        tfTriggerName.setText("");
        cbSchedule.setSelectedIndex(0);
        cbScheduleOption.setSelectedIndex(0);
        cbScheduleTime.setSelectedIndex(0);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
