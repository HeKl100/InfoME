package GUI;

import Schedule.TaskScheduler;
import database.DatabaseManager;
import database.DatabaseQuery;
import org.quartz.SchedulerException;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class MainWindow extends JFrame
{
    private JPanel pnMain;
    private JButton btSettings;
    private JButton btStart;
    private String dbUrl;




    public MainWindow()
    {


        setContentPane(pnMain);
        setTitle("InfoME");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);

        // button "Einstellungen"

        btSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // open the "Einstellungen" settings-UI

                SwingUtilities.invokeLater(() -> new Settings());
            }
        });


        btStart.addActionListener(new ActionListener() {


            @Override
            public void actionPerformed(ActionEvent e)
            {
                String databaseFolderPath = "C:\\InfoME\\Datenbank";
                DatabaseManager dbManager = DatabaseManager.getInstance(databaseFolderPath);
                DatabaseQuery dbQuery = new DatabaseQuery(dbManager);


                TaskScheduler taskScheduler = null;

                // Create new Instance from TaskScheduler
                try
                {
                    taskScheduler = new TaskScheduler();
                }
                catch (SchedulerException ex)
                {
                    throw new RuntimeException(ex);
                }

                // Start the TaskScheduler
                try
                {
                    taskScheduler.start();
                }
                catch (SchedulerException ex)
                {
                    throw new RuntimeException(ex);
                }

                //
                try
                {
                    taskScheduler.scheduleJobs(Arrays.stream(dbQuery.getTriggerSettings()).toList());
                }
                catch (SchedulerException ex)
                {
                    throw new RuntimeException(ex);
                }



            }
        });
    }}
