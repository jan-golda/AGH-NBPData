package pl.regzand.commands;

import com.sun.istack.internal.Nullable;
import pl.regzand.utils.ANSIColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class responsible for handling end executing command line commands.
 */
public abstract class Command {

    private final String name;
    private final String syntax;
    private final String description;
    private final String details;

    @Nullable
    private final Command superCommand;
    private final List<Command> subCommands;

    /**
     * Creates new command.
     *
     * @param superCommand command that this one wil be dependent to
     * @param name         name of this command
     * @param syntax       syntax of this command, generally arguments placeholders
     * @param description  one line description what this command does
     * @param details      more details about this command, some additional info
     */
    public Command(@Nullable Command superCommand, String name, String syntax, String description, String details) {
        this.name = name;
        this.syntax = syntax;
        this.description = description;
        this.details = details;

        this.superCommand = superCommand;
        this.subCommands = new ArrayList<>();

        if (this.superCommand != null)
            this.superCommand.subCommands.add(this);
    }

    // ==============================================================================
    // === ABSTRACT METHODS
    // ==============================================================================

    /**
     * Handles execution of this command.
     *
     * @param args command arguments
     *
     * @throws Exception if there was thrown any exception during execution of this command
     */
    protected abstract void handleCommand(String[] args) throws Exception;

    /**
     * Handles exception thrown during command execution.
     *
     * @param e exception thrown during execution
     */
    protected abstract void handleException(Exception e);


    // ==============================================================================
    // === EXECUTION
    // ==============================================================================

    /**
     * Executes command with given arguments, firstly checking if first argument doesn't match any sub command and if not executing this command itself.
     *
     * @param args command arguments
     */
    public void execute(String[] args) {

        // check if there is matching sub command to execute
        if (args.length > 0) {
            for (Command cmd : this.subCommands) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {

                    // prepare args
                    if (args.length == 1)
                        args = new String[0];
                    else
                        args = Arrays.copyOfRange(args, 1, args.length);

                    // execute sub command
                    cmd.execute(args);
                    return;
                }
            }
        }

        // execute this command and handle exceptions
        try {
            handleCommand(args);
        } catch (Exception e) {
            handleException(e);
        }
    }


    // ==============================================================================
    // === UTILITY
    // ==============================================================================

    /**
     * Writes usage of this command to standard output.
     * Usage contains: usage, description, all sum commands with their descriptions, details.
     * Sections that are {@code null} will not be rendered.
     */
    public void displayUsage() {

        // syntax
        System.out.format("%n%s%n\t%s %s %s%n",
                ANSIColors.highlite("Usage"),
                this.getCommandPath(),
                this.getName(),
                this.getSyntax()
        );

        // description
        if (!this.getDescription().isEmpty()) {
            System.out.format("%n%s%n\t%s%n",
                    ANSIColors.highlite("Description"),
                    this.getDescription()
            );
        }

        // sub commands
        if (!this.getSubCommands().isEmpty()) {
            System.out.format("%n%s%n", ANSIColors.highlite("Available commands"));
            for (Command c : this.getSubCommands())
                System.out.format("\t%-20s %s%n", c.getName(), c.getDescription());
        }

        // details
        if (!this.getDetails().isEmpty()) {
            System.out.format("%n%s%n%s%n",
                    ANSIColors.highlite("Details"),
                    this.getDetails()
            );
        }

    }

    protected void checkArgumentsCount(String[] args, int count) throws CommandException {
        if (args.length != count)
            throw new CommandException("Wrong command syntax!", true);
    }


    // ==============================================================================
    // === GETTERS
    // ==============================================================================

    /**
     * Returns name of this command
     *
     * @return name of this command
     */
    public String getName() {
        return name;
    }

    /**
     * Returns syntax of this command, generally in form of placeholders for arguments
     *
     * @return syntax of this command
     */
    public String getSyntax() {
        return syntax;
    }

    /**
     * Returns general description of this command
     *
     * @return general description of this command
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns additional information about this command
     *
     * @return additional information about this command
     */
    public String getDetails() {
        return details;
    }

    /**
     * Returns super command of this command, can be null if this command is first in command path
     *
     * @return super command of this command, can be null if this command is first in command path
     */
    @Nullable
    public Command getSuperCommand() {
        return superCommand;
    }

    /**
     * Returns list of all commands that this command is a direct super command to
     *
     * @return list of all commands that this command is a direct super command to
     */
    public List<Command> getSubCommands() {
        return subCommands;
    }

    /**
     * Returns list of all its ancestors in command path
     *
     * @return list of all its ancestors in command path
     */
    public List<Command> getSuperCommands() {
        if (this.getSuperCommand() == null)
            return new ArrayList<Command>();

        List<Command> list = this.getSuperCommand().getSuperCommands();
        list.add(this.getSuperCommand());

        return list;
    }

    /**
     * Returns command path created by joining super commands names
     *
     * @return command path created by joining super commands names
     */
    public String getCommandPath() {
        return this.getSuperCommands().stream().map(Command::getName).collect(Collectors.joining(" "));
    }

}
