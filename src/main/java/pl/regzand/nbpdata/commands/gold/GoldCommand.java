package pl.regzand.nbpdata.commands.gold;

import pl.regzand.commands.Command;
import pl.regzand.nbpdata.commands.NBPCommand;

/**
 * Class representing super command responsible for all gold commands.
 */
public class GoldCommand extends NBPCommand {

    /**
     * Creates gold command and all its sub commands
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.NBPDataCommand NBPDataCommand}
     */
    public GoldCommand(Command superCommand) {
        super(
                superCommand,
                "gold",
                "[command]",
                "Information about gold prices based on NBP data.",
                ""
        );

        // create sub commands
        new GoldPriceCommand(this);
        new GoldAverageCommand(this);
    }

    @Override
    protected void handleCommand(String[] args) {
        this.displayUsage();
    }
}
