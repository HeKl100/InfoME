package Schedule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The TaskUtils class provides utility methods for tasks including time extraction,
 * day of week conversion, and current date-time formatting.
 */
public class TaskUtils
{

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DAY_OF_WEEK_FORMATTER = DateTimeFormatter.ofPattern("EEEE", Locale.GERMANY);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Extracts the hour and minute from a time string formatted as "HH:mm Uhr".
     *
     * @param time the time string (e.g., "14:30 Uhr")
     * @return an array with two integers: [hour, minute]
     * @throws NumberFormatException if the time string is incorrectly formatted
     */
    public static int[] timeValues(String time)
    {
        if (time == null || time.isEmpty())
        {
            throw new IllegalArgumentException("Time string must not be null or empty.");
        }

        String[] times = time.replace(" Uhr", "").split(":");

        if (times.length != 2)
        {
            throw new IllegalArgumentException("Invalid time format. Expected format is 'HH:mm Uhr'.");
        }

        try
        {
            int hour = Integer.parseInt(times[0]);
            int minute = Integer.parseInt(times[1]);
            if (hour < 0 || hour > 23 || minute < 0 || minute > 59)
            {
                throw new IllegalArgumentException("Hour must be between 0 and 23, minute must be between 0 and 59.");
            }
            return new int[]{hour, minute};
        }
        catch (NumberFormatException e)
        {
            throw new NumberFormatException("Invalid time format. Please ensure the time is correctly formatted.");
        }
    }

    /**
     * Converts a German day of the week string to a corresponding day of the week number.
     *
     * @param dayOfWeek the day of the week string (e.g., "Sonntags")
     * @return the day of the week as an integer (1 = Sunday, 7 = Saturday)
     * @throws IllegalArgumentException if the dayOfWeek is unsupported
     */
    public static int getDayOfWeek(String dayOfWeek)
    {
        if (dayOfWeek == null || dayOfWeek.isEmpty())
        {
            throw new IllegalArgumentException("Day of week must not be null or empty.");
        }

        return switch (dayOfWeek.toUpperCase(Locale.GERMANY))
        {
            case "SONNTAGS" -> 1;
            case "MONTAGS" -> 2;
            case "DIENSTAGS" -> 3;
            case "MITTWOCHS" -> 4;
            case "DONNERSTAGS" -> 5;
            case "FREITAGS" -> 6;
            case "SAMSTAGS" -> 7;
            default -> throw new IllegalArgumentException("Unsupported day of week: " + dayOfWeek);
        };
    }

    /**
     * Gets the current date and time formatted as "HH:mm Uhr - DayOfWeek, dd.MM.yyyy".
     *
     * @return the current date and time as a formatted string
     */
    public static String currentDateTime()
    {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());

        String time = localDateTime.format(TIME_FORMATTER);
        String dayOfWeek = localDateTime.format(DAY_OF_WEEK_FORMATTER);
        String date = localDateTime.format(DATE_FORMATTER);

        return String.format("%s Uhr - %s, %s", time, dayOfWeek, date);
    }
}

