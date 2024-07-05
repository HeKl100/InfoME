package file;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {

    /**
     * Splits the input string by commas that are not within quotation marks.
     *
     * @param line the input string to split
     * @return a list of strings split by commas outside of quotation marks
     * @implNote Example: The string 'Max, 15, Houston, "I like cake, if it's chocolate"'
     * will be splitted into: [Max], [15], [Houston], ["I like cake, if it's chocolate"]
     * and not into: [Max], [15], [Houston], ["I like cake], [if it's chocolate"]
     */
    public static String[] splitString(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean insideQuotes = false;

        for (char ch : line.toCharArray()) {
            if (ch == '"') {
                insideQuotes = !insideQuotes; // Toggle the insideQuotes flag
                currentPart.append(ch);
            } else if (ch == ',' && !insideQuotes) {
                result.add(currentPart.toString().trim()); // Add the part to the result list
                currentPart.setLength(0); // Clear the StringBuilder for the next part
            } else {
                currentPart.append(ch); // Append the character to the current part
            }
        }

        // Add the last part to the result list
        if (currentPart.length() > 0) {
            result.add(currentPart.toString().trim());
        }

        return result.toArray(new String[0]);
    }
}


