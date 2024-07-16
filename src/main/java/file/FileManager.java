package file;

import database.DatabaseManager;
import database.DatabaseQuery;
import logging.LoggerWrapper;
import model.ImportExport;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileManager
{
    public String rootFolderPath = "C:\\InfoME";
    public String databaseFolderPath = "C:\\InfoME\\Datenbank";
    public String logFolderPath = "C:\\InfoME\\Log";

    private static final LoggerWrapper logger = new LoggerWrapper(FileManager.class);

    //------------------------------------------------------------------------------------------------------------------
    // FileManager Constructor

    public FileManager() throws IOException
    {
        createRootFolder();
        createDatabaseFolder();
        createLogFolder();
    }
    //------------------------------------------------------------------------------------------------------------------
    // If Root Folder is missing, it will be created here.

    public void createRootFolder() throws IOException
    {
        // set path for the root folder, where all the data is stored
        // check if folder already exists - if not -> create
        // path = C:\InfoME

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
    //------------------------------------------------------------------------------------------------------------------
    // If Database Folder is missing, it will be created here.

    public void createDatabaseFolder()
    {
        // set the path for the database folder, where the database stuff is stored
        // check if folder already exists - if not -> create
        // path = C:\InfoME\Datenbank

        File databaseFolder = new File(databaseFolderPath);

        if(!databaseFolder.exists())
        {
            if(databaseFolder.mkdir())
            {
                // everything is fine
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // If Log Folder is missing, it will be created here.

    public void createLogFolder()
    {
        // set the path for the logfiles folder, where the logfiles are stored
        // check if folder already exists - if not -> crete
        // path = C:\InfoME\Log

        File logFolder = new File(logFolderPath);

        if (!logFolder.exists())
        {
            if (logFolder.mkdir())
            {
                // everything is fine
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    // Reads In the new KV Data from the newest File in the imported File.
    public List<String> readInKVData(ImportExport importExport) throws IOException
    {
        EncodingFixer eF = EncodingFixer.getInstance();
        LineValidator lV = LineValidator.getInstance();


        String url = "smb://172.22.161.239/Controlling_Daten/";
        String username = "qlik\\controlling";
        String password = "Hd7h3jn-d3nund3";
        ;
        SMBFileShare smb = new SMBFileShare(url, username, password);

        // String folderPath = importExport.getImportPath();
        String fileName = importExport.getFilename();
        File newestFile = smb.getNewestFile(url, fileName);


            // Reads in all Lines
        assert newestFile != null;
        List<String> allLines = Files.readAllLines(newestFile.toPath(), StandardCharsets.UTF_8);


        // Temporary Lists
        List<String> validLines = new ArrayList<>();
        List<String> encodedLines = new ArrayList<>();

        if (allLines.isEmpty())
        {
            System.out.println("File to read is empty");
        }
        else
        {
            // Remove the Header Line
            allLines.remove(0);
            // Fix encoding issues and then validate the Lines
            for (String line : allLines) {
                line = eF.encodeFix(line);
                encodedLines.add(line);
            }
            allLines = encodedLines;

            validLines = lV.validateLines(allLines);
        }
        return validLines;
    }

    public List<String> getKlaerung()
    {
        System.out.println("g");
        return null;
    }
}
