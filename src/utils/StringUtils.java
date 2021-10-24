package utils;

import java.util.Formatter;

/**
 * @author Rafael Barbieru
 */
public class StringUtils {

    /**
     * Returns a formatted string
     * @param format
     * @param args
     * @return
     */
    public static final String f(String format, Object... args) {
        return new Formatter().format(format, args).toString();
    }

    /**
     * Returns a decorated string that looks like a title
     * @param title
     * @return
     */
    public static final String title(String title) {
        String localTitle = drawLineOfChar('*', title.length() + 3) + "*";
        localTitle += "\n* " + title + " *";
        localTitle += "\n*" + drawLineOfChar('*', title.length() + 2) + "*";
        return localTitle;
    }

    /**
     * Draws a line of {@code length} units of the {@code character} specified
     * @param character
     * @param length
     * @return
     */
    private static String drawLineOfChar(char character, int length) {
        return new String(new char[length]).replace('\0', character);
    }
}
