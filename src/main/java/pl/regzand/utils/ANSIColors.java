package pl.regzand.utils;

/**
 * Utility class with ANSI color codes.
 */
public class ANSIColors {

    // utility
    public static final String RESET = "\u001b[0m";

    // special
    public static final String BOLD = "\u001b[1m";
    public static final String UNDERLINE = "\u001b[4m";
    public static final String REVERSED = "\u001b[7m";

    // colors
    public static final String BLACK = "\u001b[30m";
    public static final String RED = "\u001b[31m";
    public static final String GREEN = "\u001b[32m";
    public static final String YELLOW = "\u001b[33m";
    public static final String BLUE = "\u001b[34m";
    public static final String MAGENTA = "\u001b[35m";
    public static final String CYAN = "\u001b[36m";
    public static final String WHITE = "\u001b[37m";
    public static final String BLACK_BRIGHT = "\u001b[30;1m";
    public static final String RED_BRIGHT = "\u001b[31;1m";
    public static final String GREEN_BRIGHT = "\u001b[32;1m";
    public static final String YELLOW_BRIGHT = "\u001b[33;1m";
    public static final String BLUE_BRIGHT = "\u001b[34;1m";
    public static final String MAGENTA_BRIGHT = "\u001b[35;1m";
    public static final String CYAN_BRIGHT = "\u001b[36;1m";
    public static final String WHITE_BRIGHT = "\u001b[37;1m";

    // background colors
    public static final String BG_BLACK = "\u001b[40m";
    public static final String BG_RED = "\u001b[41m";
    public static final String BG_GREEN = "\u001b[42m";
    public static final String BG_YELLOW = "\u001b[43m";
    public static final String BG_BLUE = "\u001b[44m";
    public static final String BG_MAGENTA = "\u001b[45m";
    public static final String BG_CYAN = "\u001b[46m";
    public static final String BG_WHITE = "\u001b[47m";
    public static final String BG_BLACK_BRIGHT = "\u001b[40;1m";
    public static final String BG_RED_BRIGHT = "\u001b[41;1m";
    public static final String BG_GREEN_BRIGHT = "\u001b[42;1m";
    public static final String BG_YELLOW_BRIGHT = "\u001b[43;1m";
    public static final String BG_BLUE_BRIGHT = "\u001b[44;1m";
    public static final String BG_MAGENTA_BRIGHT = "\u001b[45;1m";
    public static final String BG_CYAN_BRIGHT = "\u001b[46;1m";
    public static final String BG_WHITE_BRIGHT = "\u001b[47;1m";

    /**
     * Returns given text formatted as: <code>BOLD+REVERSE+" "+text+" "+RESET</code>
     *
     * @param text to be formatted
     *
     * @return formatted text
     */
    public static String highlite(String text) {
        return BOLD + REVERSED + " " + text + " " + RESET;
    }
}
