package file;

import jcifs.CIFSContext;
import jcifs.context.BaseContext;
import jcifs.config.PropertyConfiguration;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbException;

import java.io.*;
import java.util.Comparator;
import java.util.Optional;
import java.util.Properties;
import java.util.Arrays;

/**
 * The SMBFileShare class provides methods to interact with SMB file shares.
 * This class allows users to connect to an SMB share and retrieve the newest file based on specific criteria.
 */
public class SMBFileShare {

    private CIFSContext context;
    private final String url;

    /**
     * Constructs an SMBFileShare instance with the given URL, username, and password.
     *
     * @param url      the URL of the SMB share
     * @param username the username for authentication
     * @param password the password for authentication
     * @throws SmbException if there is an error with the SMB connection
     * @throws IOException  if there is an I/O error
     */
    public SMBFileShare(String url, String username, String password) throws SmbException, IOException
    {
        this.url = url;
        Properties props = new Properties();
        props.setProperty("jcifs.smb.client.username", username);
        props.setProperty("jcifs.smb.client.password", password);

        this.context = new BaseContext(new PropertyConfiguration(props));
    }

    /**
     * Retrieves the newest file in the specified folder that includes the specified fileName in its name.
     *
     * @param folderPath the path of the folder to search
     * @param fileName   the name to match in the files
     * @return the newest matching file as a local temporary file, or null if no matching files are found
     * @throws SmbException if there is an error with the SMB connection
     * @throws IOException  if there is an I/O error
     */
    public File getNewestFile(String folderPath, String fileName) throws SmbException, IOException
    {
        SmbFile directory = new SmbFile(folderPath, context);
        SmbFile[] files = directory.listFiles();

        if (files == null || files.length == 0)
        {
            return null;  // No files in the directory
        }

        // Find the most recent file that includes the specified fileName in its name
        Optional<SmbFile> newestFileOpt = Arrays.stream(files)
                .filter(file ->
                {
                    try
                    {
                        return file.isFile() && file.getName().contains(fileName);
                    }
                    catch (SmbException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .max(Comparator.comparingLong(SmbFile::getLastModified));

        return newestFileOpt.map(this::downloadFile).orElse(null);
    }

    /**
     * Downloads the specified SMB file to a local temporary file.
     *
     * @param smbFile the SMB file to download
     * @return the downloaded local temporary file
     * @throws RuntimeException if there is an error during file download
     */
    private File downloadFile(SmbFile smbFile)
    {
        try
        {
            File localFile = File.createTempFile("smb_", "_" + smbFile.getName());

            try (InputStream in = smbFile.getInputStream(); FileOutputStream out = new FileOutputStream(localFile))
            {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1)
                {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return localFile;
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error downloading file: " + smbFile.getName(), e);
        }
    }

}
