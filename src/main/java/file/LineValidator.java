package file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The LineValidator class provides methods to validate lines based on specific criteria.
 * This class uses a predefined set of valid first column values and an expected column count,
 * but it also allows users to add custom validation rules.
 */
public class LineValidator {

    // Singleton instance
    private static LineValidator instance;

    // Set of valid first column values
    private final Set<String> validFirstColumns;

    // Expected column count
    private int expectedColumnCount;

    /**
     * Private constructor to initialize the valid first columns and expected column count.
     */
    private LineValidator()
    {
        validFirstColumns = new HashSet<>();
        initializeDefaultValidFirstColumns();
        expectedColumnCount = 71;
    }

    /**
     * Get the singleton instance of the LineValidator.
     *
     * @return the singleton instance
     */
    public static LineValidator getInstance()
    {
        if (instance == null) {
            instance = new LineValidator();
        }
        return instance;
    }

    /**
     * Initialize the default valid first columns.
     */
    private void initializeDefaultValidFirstColumns()
    {
        validFirstColumns.add("KV Genehmigt");
        validFirstColumns.add("Vorgang");
        validFirstColumns.add("Kostenvoranschlag");
        validFirstColumns.add("Auftrag");
        validFirstColumns.add("Lieferschein");
    }

    /**
     * Add a custom valid first column value.
     *
     * @param firstColumn the custom valid first column value
     */
    public void addValidFirstColumn(String firstColumn)
    {
        validFirstColumns.add(firstColumn);
    }

    /**
     * Set the expected column count for validation.
     *
     * @param count the expected column count
     */
    public void setExpectedColumnCount(int count)
    {
        this.expectedColumnCount = count;
    }

    /**
     * Check if the line is valid based on the first column and column count.
     *
     * @param line the line to validate
     * @return true if the line is valid, false otherwise
     */
    public boolean isValid(String line)
    {
        if (line == null || line.trim().isEmpty())
        {
            return false;
        }
        String[] data = StringSplitter.splitString(line);

        return data.length == expectedColumnCount && isValidFirstColumn(data[0]);
    }

    /**
     * Check if the first column value is one of the valid cases.
     *
     * @param firstColumn the first column value
     * @return true if the first column value is valid, false otherwise
     */
    private boolean isValidFirstColumn(String firstColumn)
    {
        return validFirstColumns.contains(firstColumn);
    }

    /**
     * Method to filter valid lines and return a new list of valid lines.
     *
     * @param lines the list of lines to validate
     * @return a list of valid lines
     */
    public List<String> validateLines(List<String> lines)
    {
        List<String> validLines = new ArrayList<>();

        for (String line : lines)
        {
            if (isValid(line))
            {
                validLines.add(line);
            }
        }

        return validLines;
    }
}
