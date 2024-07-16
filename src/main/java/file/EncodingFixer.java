package file;

import java.util.HashMap;
import java.util.Map;

/**
 * The EncodingFixer class provides methods to fix common encoding issues in text.
 * This class uses a predefined set of encoding fixes, but it also allows users to add custom fixes.
 */
public class EncodingFixer {

    // Singleton instance
    private static EncodingFixer instance;

    // Dictionary mapping incorrectly encoded characters to their correct counterparts
    private final Map<String, String> encodingFixes;

    /**
     * Private constructor to initialize the encoding fixes map.
     */
    private EncodingFixer()
    {
        encodingFixes = new HashMap<>();
        initializeDefaultFixes();
    }

    /**
     * Get the singleton instance of the EncodingFixer.
     *
     * @return the singleton instance
     */
    public static EncodingFixer getInstance()
    {
        if (instance == null)
        {
            instance = new EncodingFixer();
        }
        return instance;
    }

    /**
     * Initialize the default encoding fixes.
     */
    private void initializeDefaultFixes()
    {
        encodingFixes.put("Ã¤", "ä");
        encodingFixes.put("Ã¶", "ö");
        encodingFixes.put("Ã¼", "ü");
        encodingFixes.put("ÃŸ", "ß");
        encodingFixes.put("Ã¡", "á");
        encodingFixes.put("Ã©", "é");
        encodingFixes.put("Ã­", "í");
        encodingFixes.put("Ã³", "ó");
        encodingFixes.put("Ãº", "ú");
        encodingFixes.put("Ã€", "À");
        encodingFixes.put("Ãˆ", "È");
        encodingFixes.put("ÃŒ", "Ì");
        encodingFixes.put("Ã’", "Ò");
        encodingFixes.put("Ã™", "Ù");
        encodingFixes.put("Ã¢", "â");
        encodingFixes.put("Ãª", "ê");
        encodingFixes.put("Ã®", "î");
        encodingFixes.put("Ã´", "ô");
        encodingFixes.put("Ã»", "û");
        encodingFixes.put("Ã¢â‚¬", "’");
        encodingFixes.put("Ã‚", "Â");
        encodingFixes.put("Ã„", "Ä");
        encodingFixes.put("Ã–", "Ö");
        encodingFixes.put("Ãœ", "Ü");
        encodingFixes.put("Ãƒ", "Ã");
        encodingFixes.put("Ã‘", "Ñ");
        encodingFixes.put("Ã±", "ñ");
    }

    /**
     * Adds a custom encoding fix.
     *
     * @param incorrect the incorrect encoding
     * @param correct   the correct encoding
     */
    public void addEncodingFix(String incorrect, String correct)
    {
        encodingFixes.put(incorrect, correct);
    }

    /**
     * Fix encoding issues in the provided string.
     *
     * @param line the string with potential encoding issues
     * @return the string with encoding issues fixed
     */
    public String encodeFix(String line)
    {
        for (Map.Entry<String, String> entry : encodingFixes.entrySet())
        {
            line = line.replace(entry.getKey(), entry.getValue());
        }

        return line;
    }
}
