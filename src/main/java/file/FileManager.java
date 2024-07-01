package file;

import logging.LoggerWrapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class FileManager
{
    public String rootFolderPath;
    public String databaseFolderPath;
    public String logFolderPath;

    private static final LoggerWrapper logger = new LoggerWrapper(FileManager.class);


    public FileManager() throws IOException {
        createRootFolder();
        createDatabaseFolder();
        createLogFolder();
    }

    public void createRootFolder() throws IOException
    {
        // set path for the root folder, where all the data is stored
        // check if folder already exists - if not -> create
        // path = C:\InfoME

        rootFolderPath = "C:\\InfoME";

        File rootFolder = new File(rootFolderPath);

        if (!rootFolder.exists())
        {
            if (rootFolder.mkdir())
            {
                // successfully created -> inform happy user

                logger.info("Programm-Ordner wurde erfolgreich hier erstellt: " + rootFolderPath);
                JOptionPane.showMessageDialog(null, "Programm-Ordner wurde hier erstellt: " + rootFolderPath, "InfoME", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                // there is a problem with creating -> inform user
                logger.info("Fehler beim erstellen des Programm-Ordners unter " + rootFolderPath);
                JOptionPane.showMessageDialog(null, "Fehler beim erstellen des Programm-Ordners! ");
                throw new IOException("Fehler bei erstellen des Programm-Ordners! " + rootFolderPath);
            }
        }

    }

    public void createDatabaseFolder()
    {
        // set the path for the database folder, where the database stuff is stored
        // check if folder already exists - if not -> create
        // path = C:\InfoME\Datenbank

        databaseFolderPath = "C:\\InfoME\\Datenbank";

        File databaseFolder = new File(databaseFolderPath);

        if(!databaseFolder.exists())
        {
            if(databaseFolder.mkdir())
            {
                // everything is fine
            }
        }
    }

    public void createLogFolder()
    {
        // set the path for the logfiles folder, where the logfiles are stored
        // check if folder already exists - if not -> crete
        // path = C:\InfoME\Log

        logFolderPath = "C:\\InfoME\\Log";

        File logFolder = new File(logFolderPath);

        if (!logFolder.exists())
        {
            if (logFolder.mkdir())
            {
                // everything is fine
            }
        }
    }
}
