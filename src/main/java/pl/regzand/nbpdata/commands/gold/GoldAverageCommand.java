package pl.regzand.nbpdata.commands.gold;

import org.json.JSONObject;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.DataProvider;
import pl.regzand.dataparser.json.JSONAnalyser;
import pl.regzand.dataparser.json.JSONDateRangeFetcher;
import pl.regzand.nbpdata.commands.NBPCommand;
import pl.regzand.nbpdata.data.NBPConnector;
import pl.regzand.utils.ANSIColors;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Class that handles gold average command.
 */
public class GoldAverageCommand extends NBPCommand {

    /**
     * Creates gold average command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.gold.GoldCommand}
     */
    GoldAverageCommand(Command superCommand) {
        super(
                superCommand,
                "average",
                "<start> <end>",
                "Displays average price of gold in given time range.",
                "\tDate format - ISO 8601 (YYYY-MM-DD)"
        );
    }

    @Override
    protected void handleCommand(String[] args) throws CommandException, IOException {

        // display usage if there is no arguments provided
        if (args.length == 0) {
            this.displayUsage();
            return;
        }

        // verify arguments count
        checkArgumentsCount(args, 2);

        // parse arguments
        Date start = parseStringToDate(args[0]);
        Date end = parseStringToDate(args[1]);

        // verify arguments
        checkDate(start);
        checkDate(end);
        checkDateOrder(start, end);

        // create analyser
        JSONAnalyser analyser = new JSONAnalyser(
                new DataProvider<JSONObject>(
                        new JSONDateRangeFetcher(
                                new NBPConnector(),
                                start,
                                end,
                                Calendar.DATE,
                                90,
                                "cenyzlota/%tF/%tF",
                                true,
                                null
                        )
                )
        );

        // calculate
        double price = analyser.average(jsonObject -> jsonObject.getNumber("cena"));

        // display
        System.out.format("Average price of %s between %tF and %tF was %s%n",
                ANSIColors.highlite("1g GOLD"),
                start,
                end,
                ANSIColors.highlite(String.format("%.4f PLN", price))
        );

    }

}
