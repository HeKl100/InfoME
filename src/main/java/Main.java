//
//
// InfoME
// programming start 13.06.2024
// Version 1.0
// © Heimo Klöckl
//
//

import GUI.MainWindow;
import database.DatabaseManager;
import file.FileManager;
import logging.LoggerWrapper;

import javax.swing.*;
import java.io.IOException;

public class Main
{
    private static LoggerWrapper logger;
    public static final String dbUrl = "jdbc:sqlite:C:\\InfoME\\Datenbank\\InfoME.db";

    public static void main(String[] args)
    {
        // program entry

        // initialize logger
        logger = new LoggerWrapper(Main.class);
        logger.info("   + + +   program start   + + +   ");

        FileManager fManager;
        DatabaseManager dbManager;

        // create Folders where the data will be stored
        // create database folders

        try
        {
            fManager = new FileManager();

            dbManager = DatabaseManager.getInstance(dbUrl);
            dbManager.createTables();

        } catch (IOException e)
        {
            // Ups - something went wrong - inform user and shutdown the program!

            logger.info("Error during file and/or table creation! " + e.getMessage());
            JOptionPane.showMessageDialog(null, e.getMessage());

            // close program
            System.exit(1);
        }

        // everything is fine
        // so open MainWindow

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });

    }
}
