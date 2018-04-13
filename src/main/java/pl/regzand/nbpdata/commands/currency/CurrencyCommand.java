package pl.regzand.nbpdata.commands.currency;

import pl.regzand.commands.Command;
import pl.regzand.nbpdata.commands.NBPCommand;

/**
 * Class representing super command responsible for all currency commands.
 */
public class CurrencyCommand extends NBPCommand {

    /**
     * Creates currency command and all its sub commands
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.NBPDataCommand NBPDataCommand}
     */
    public CurrencyCommand(Command superCommand) {
        super(
                superCommand,
                "currency",
                "[command]",
                "Information about currency exchange rates based od NBP data",
                ""
        );

        // create sub commands
        new CurrencyPriceCommand(this);
        new CurrencyFluctuationsCommand(this);
        new CurrencyLowestPriceCommand(this);
        new CurrencyDifferenceCommand(this);
        new CurrencyRecordsCommand(this);
        new CurrencyGraphCommand(this);
    }

    /**
     * Handles this command by displaying usage
     *
     * @param args command arguments
     */
    @Override
    protected void handleCommand(String[] args) {
        this.displayUsage();
    }
}
