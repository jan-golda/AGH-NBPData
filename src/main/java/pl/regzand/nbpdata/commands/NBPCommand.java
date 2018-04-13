package pl.regzand.nbpdata.commands;

import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Class that implements exception handling for all bnp commands, and adds some functionality
 */
public abstract class NBPCommand extends Command {

    /**
     * Creates new nbp command.
     *
     * @param superCommand command that this one wil be dependent to
     * @param name         name of this command
     * @param syntax       syntax of this command, generally arguments placeholders
     * @param description  one line description what this command does
     * @param details      more details about this command, some additional info
     */
    public NBPCommand(Command superCommand, String name, String syntax, String description, String details) {
        super(superCommand, name, syntax, description, details);
    }

    /**
     * Handles exception thrown during command execution.
     *
     * @param e exception thrown during execution
     */
    @Override
    protected void handleException(Exception e) {

        if(e instanceof CommandException){
            System.out.println(e.getMessage());
            if(((CommandException) e).isDisplayUsage())
                this.displayUsage();
            return;
        }

        if(e instanceof UnexpectedHttpStatusCodeException){
            if(((UnexpectedHttpStatusCodeException) e).getCode() == 404){
                System.out.println("There is no requested data in NBP database.");
                return;
            }

            System.out.println("NBP API returned unexpected http status code "+((UnexpectedHttpStatusCodeException) e).getCode()+ " while requesting: "+((UnexpectedHttpStatusCodeException) e).getUrl());
            return;
        }

        System.out.println("Unexpected exception: "+e.getMessage());
        e.printStackTrace();

    }


    // ==============================================================================
    // === STATIC UTILITY FUNCTION
    // ==============================================================================

    /**
     * Returns Date created based on given String.
     *
     * @param text date in format ISO 8601 (YYYY-MM-DD)
     *
     * @return Date created based on given String
     * @throws CommandException if given text was in wrong format
     */
    protected static Date parseStringToDate(String text) throws CommandException {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(text);
        } catch (ParseException e) {
            throw new CommandException("Wrong date format, expected: ISO 8601 (YYYY-MM-DD)", false);
        }
    }

    /**
     * Returns date from weird date format.
     * @param text date in weird format
     * @param dayOfTheWeek day of week
     * @return date from weird date format.
     * @throws CommandException if given text was in wrong format
     */
    protected static Date parseWeirdDateFormat(String text, int dayOfTheWeek) throws CommandException {
        try {
            String[] data = text.split(",");

            if(data.length != 3)
                throw new Exception();

            int year  = Integer.parseInt(data[0]);
            int month = Integer.parseInt(data[1]);
            int week  = Integer.parseInt(data[2]);

            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month-1);
            cal.set(Calendar.WEEK_OF_MONTH, week);
            cal.set(Calendar.DAY_OF_WEEK, dayOfTheWeek+1);

            return cal.getTime();

        } catch (Exception e) {
            throw new CommandException("Wrong date format, expected: <year>,<month>,<week in month>", false);
        }
    }

    /**
     * Returns currency code created from given string.
     *
     * @param text currency code in format ISO 4217 (XXX)
     *
     * @return currency code created from given string.
     * @throws CommandException if given text was in wrong format
     */
    protected static String parseCurrencyCode(String text) throws CommandException {
        text = text.trim().toUpperCase();

        if (!text.matches("[A-Z]{3}"))
            throw new CommandException("Wrong currency format, expected: ISO 4217 (XXX)", false);

        return text;
    }

    /**
     * Checks if given date isn't from the future.
     *
     * @param date to check
     *
     * @throws CommandException if date is wrong
     */
    protected static void checkDate(Date date) throws CommandException {
        if (date.after(new Date()))
            throw new CommandException("I don't know how to look in to the future :(", false);
    }

    /**
     * Checks if first date is before second.
     *
     * @param first  first date to check
     * @param second second date to check
     *
     * @throws CommandException if first date is after second
     */
    protected static void checkDateOrder(Date first, Date second) throws CommandException {
        if (first.after(second))
            throw new CommandException("First date has to be before second", false);
    }

}
