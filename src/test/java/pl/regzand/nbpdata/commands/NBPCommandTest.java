package pl.regzand.nbpdata.commands;

import org.junit.jupiter.api.Test;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.jupiter.api.Assertions.*;

class NBPCommandTest {

    @Test
    void parseStringToDate_OK() throws Exception {

        assertEquals(new GregorianCalendar(2001, 3 - 1, 14).getTime(), NBPCommand.parseStringToDate("2001-03-14"));

        assertEquals(new GregorianCalendar(2001, 3 - 1, 1).getTime(), NBPCommand.parseStringToDate("2001-03-1"));

        assertEquals(new GregorianCalendar(2042, 6 - 1, 30).getTime(), NBPCommand.parseStringToDate("2042-06-30"));

        assertEquals(new GregorianCalendar(2001, 3 - 1, 14).getTime(), NBPCommand.parseStringToDate("2001-03-14"));

        assertEquals(new GregorianCalendar(2001, 3 - 1, 14).getTime(), NBPCommand.parseStringToDate("2001-3-14"));

    }

    @Test
    void parseStringToDate_WRONG() {

        assertThrows(CommandException.class, () -> NBPCommand.parseStringToDate("41234"));

        assertThrows(CommandException.class, () -> NBPCommand.parseStringToDate("2014-ma-23"));

        assertThrows(CommandException.class, () -> NBPCommand.parseStringToDate("test"));

        assertThrows(CommandException.class, () -> NBPCommand.parseStringToDate("06.12.2003"));

        assertThrows(CommandException.class, () -> NBPCommand.parseStringToDate("1234 1234 1234"));

    }

    @Test
    void parseCurrencyCode_OK() throws Exception {

        assertEquals("QWE", NBPCommand.parseCurrencyCode("qwe"));

        assertEquals("QWE", NBPCommand.parseCurrencyCode("QwE"));

        assertEquals("RET", NBPCommand.parseCurrencyCode("RET"));

        assertEquals("ZET", NBPCommand.parseCurrencyCode("Zet"));

        assertEquals("POQ", NBPCommand.parseCurrencyCode("Poq"));

    }

    @Test
    void parseCurrencyCode_WRONG() {

        assertThrows(CommandException.class, () -> NBPCommand.parseCurrencyCode("qw"));

        assertThrows(CommandException.class, () -> NBPCommand.parseCurrencyCode("rewq"));

        assertThrows(CommandException.class, () -> NBPCommand.parseCurrencyCode("1ew"));

        assertThrows(CommandException.class, () -> NBPCommand.parseCurrencyCode("-am"));

        assertThrows(CommandException.class, () -> NBPCommand.parseCurrencyCode("23#"));

    }

    @Test
    void checkDate_OK() {

        try {

            NBPCommand.checkDate(new GregorianCalendar(2013, 1, 1).getTime());
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDate(new GregorianCalendar(1979, 1, 1).getTime());
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDate(new GregorianCalendar(1997, 4, 13).getTime());
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDate(new GregorianCalendar(2018, 0, 1).getTime());
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDate(new GregorianCalendar(2017, 11, 25).getTime());
        } catch (CommandException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void checkDate_WRONG() {
        Calendar cal = new GregorianCalendar();

        cal.add(Calendar.SECOND, 10);
        assertThrows(CommandException.class, () -> NBPCommand.checkDate(cal.getTime()));

        cal.add(Calendar.MONTH, 3);
        assertThrows(CommandException.class, () -> NBPCommand.checkDate(cal.getTime()));

        cal.add(Calendar.YEAR, 1);
        assertThrows(CommandException.class, () -> NBPCommand.checkDate(cal.getTime()));

    }

    @Test
    void checkDateOrder_OK() {

        try {
            NBPCommand.checkDateOrder(
                    new GregorianCalendar(2010, 1, 1).getTime(),
                    new GregorianCalendar(2011, 1, 1).getTime()
            );
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDateOrder(
                    new GregorianCalendar(2000, 0, 30).getTime(),
                    new GregorianCalendar(2000, 1, 30).getTime()
            );
        } catch (CommandException e) {
            fail(e.getMessage());
        }

        try {
            NBPCommand.checkDateOrder(
                    new GregorianCalendar(2000, 3, 12).getTime(),
                    new GregorianCalendar(2000, 3, 15).getTime()
            );
        } catch (CommandException e) {
            fail(e.getMessage());
        }

    }

    @Test
    void checkDateOrder_WRONG() {

        assertThrows(CommandException.class, () -> NBPCommand.checkDateOrder(
                new GregorianCalendar(2004, 3, 12).getTime(),
                new GregorianCalendar(2000, 3, 12).getTime()
        ));

        assertThrows(CommandException.class, () -> NBPCommand.checkDateOrder(
                new GregorianCalendar(2000, 9, 12).getTime(),
                new GregorianCalendar(2000, 7, 12).getTime()
        ));

        assertThrows(CommandException.class, () -> NBPCommand.checkDateOrder(
                new GregorianCalendar(2000, 3, 15).getTime(),
                new GregorianCalendar(2000, 3, 12).getTime()
        ));

    }

}