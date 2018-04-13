package pl.regzand.nbpdata.commands;

import pl.regzand.nbpdata.commands.currency.CurrencyCommand;
import pl.regzand.nbpdata.commands.gold.GoldCommand;

/**
 * Class for base command of project.
 */
public class NBPDataCommand extends NBPCommand {

    /**
     * Creates base command and all its sub commands
     */
    public NBPDataCommand() {
        super(
                null,
                "nbpdata",
                "[command]",
                "Information about gold and currency based on NBP data.",
                ""
        );

        // create sub commands
        new GoldCommand(this);
        new CurrencyCommand(this);
    }

    @Override
    protected void handleCommand(String[] args) {
        this.displayUsage();
    }
}
