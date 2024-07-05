package file;
import java.util.HashMap;
import java.util.Map;

public class EncodingFixer {


    public static String encodeFix(String line) {
        return repairEncodingIssues(line);
    }
    // Method to repair encoding issues based on predefined character mappings
    private static String repairEncodingIssues(String input) {
        // Dictionary mapping incorrectly encoded characters to their correct counterparts
        Map<String, String> encodingFixes = new HashMap<>();
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

        for (Map.Entry<String, String> entry : encodingFixes.entrySet()) {
            input = input.replace(entry.getKey(), entry.getValue());
        }

        return input;
    }

}
