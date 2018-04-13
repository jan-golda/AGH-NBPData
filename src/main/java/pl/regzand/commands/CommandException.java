package pl.regzand.commands;

/**
 * Exception thrown by {@link pl.regzand.commands.Command commands} to indicate problem with command execution caused by user.
 */
public class CommandException extends Exception {

    private final boolean displayUsage;

    /**
     * Creates new command exception.
     *
     * @param message      exception message
     * @param displayUsage if usage should be displayed alongside exception message
     */
    public CommandException(String message, boolean displayUsage) {
        super(message);
        this.displayUsage = displayUsage;
    }

    /**
     * Returns if usage should be displayed alongside exception message.
     *
     * @return if usage should be displayed alongside exception message
     */
    public boolean isDisplayUsage() {
        return displayUsage;
    }

}
