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
        String folderPath = importExport.getImportPath();
        String fileName = importExport.getFilename();
        File newestFile = getNewestFile(folderPath, fileName);


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
                line = EncodingFixer.encodeFix(line);
                encodedLines.add(line);
            }
            allLines = encodedLines;

            validLines = LineValidator.validateLines(allLines);
        }
        return validLines;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Returns newest modified File in a Folder given the folderPath
    public static File getNewestFile(String folderPath, String fileName) {
        File folder = new File(folderPath);

        // Check if the folder exists and is indeed a directory
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("The path must be a valid directory.");
        }

        // Get all files from the directory
        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            return null;  // No files in the directory
        }

        // Find the most recent file that includes the specified fileName in its name
        return Arrays.stream(files)
                .filter(File::isFile)  // Make sure to only include files, not directories
                .filter(file -> file.getName().contains(fileName))  // Include only files that contain the fileName substring
                .max(Comparator.comparingLong(File::lastModified))  // Get the file with the most recent last modified date
                .orElse(null);  // Return null if no matching files are found
    }
}
