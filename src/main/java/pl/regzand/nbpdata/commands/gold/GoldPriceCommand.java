package pl.regzand.nbpdata.commands.gold;

import org.json.JSONArray;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;
import pl.regzand.nbpdata.commands.NBPCommand;
import pl.regzand.nbpdata.data.NBPConnector;
import pl.regzand.utils.ANSIColors;

import java.io.IOException;
import java.util.Date;

/**
 * Class that handles gold price command.
 */
public class GoldPriceCommand extends NBPCommand {

    /**
     * Creates gold price command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.gold.GoldCommand}
     */
    GoldPriceCommand(Command superCommand) {
        super(
                superCommand,
                "price",
                "<date>",
                "Displays price of gold from given date.",
                "\tDate format - ISO 8601 (YYYY-MM-DD)"
        );
    }

    @Override
    protected void handleCommand(String[] args) throws CommandException, IOException, UnexpectedHttpStatusCodeException {

        // display usage if there is no arguments provided
        if (args.length == 0) {
            this.displayUsage();
            return;
        }

        // verify arguments count
        checkArgumentsCount(args, 1);

        // parse arguments
        Date date = parseStringToDate(args[0]);

        // verify arguments
        checkDate(date);

        // get data
        JSONArray data = new NBPConnector().makeJSONArrayRequest(String.format("cenyzlota/%tF", date));

        // get value
        Number price = data.getJSONObject(0).getNumber("cena");

        // display
        System.out.format("Price of %s according to NBP data form %tF was %s%n",
                ANSIColors.highlite("1g GOLD"),
                date,
                ANSIColors.highlite(price + " PLN")
        );

    }
}
