package file;

import java.util.ArrayList;
import java.util.List;

public class StringSplitter {

    /**
     * Splits the input string into an array of substrings based on commas,
     * considering quoted sections to avoid splitting within quotes.
     *
     * @param line the input string to split
     * @return an array of substrings
     */
    public static String[] splitString(String line)
    {
        List<String> result = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean insideQuotes = false;

        for (char ch : line.toCharArray())
        {
            if (ch == '"')
            {
                insideQuotes = !insideQuotes; // Toggle the insideQuotes flag
                currentPart.append(ch);
            }
            else if (ch == ',' && !insideQuotes)
            {
                result.add(currentPart.toString().trim()); // Add the part to the result list
                currentPart.setLength(0); // Clear the StringBuilder for the next part
            }
            else
            {
                currentPart.append(ch); // Append the character to the current part
            }
        }

        // Add the last part to the result list
        if (!currentPart.isEmpty())
        {
            result.add(currentPart.toString().trim());
        }

        return result.toArray(new String[0]);
    }
}


