package pl.regzand.nbpdata.commands.currency;

import org.json.JSONException;
import org.json.JSONObject;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.DataProvider;
import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;
import pl.regzand.dataparser.json.JSONAnalyser;
import pl.regzand.dataparser.json.JSONSingleFetcher;
import pl.regzand.nbpdata.commands.NBPCommand;
import pl.regzand.nbpdata.data.NBPConnector;
import pl.regzand.utils.ANSIColors;

import java.io.IOException;
import java.util.Date;

/**
 * Class that handles lowest-price command.
 */
public class CurrencyLowestPriceCommand extends NBPCommand {

    /**
     * Creates lowest-price command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyLowestPriceCommand(Command superCommand) {
        super(
                superCommand,
                "lowest-price",
                "<date>",
                "Displays currency with the lowest exchange rate at given date",
                "\tDate format - ISO 8601 (YYYY-MM-DD)"
        );
    }

    @Override
    protected void handleCommand(String[] args) throws CommandException, IOException, JSONException, UnexpectedHttpStatusCodeException {

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

        // create analyser
        JSONAnalyser analyser = new JSONAnalyser(
                new DataProvider<JSONObject>(
                        new JSONSingleFetcher(
                                new NBPConnector(),
                                String.format("exchangerates/tables/c/%tF", date),
                                "/0/rates"
                        )
                )
        );

        // get value
        JSONObject lowest = analyser.findTheSmallestNumber(jsonObject -> jsonObject.getNumber("bid"));

        // display
        System.out.format("The currency with the lowest buy price on day %tF was %s worth %s%n",
                date,
                ANSIColors.highlite(lowest.getString("code").toUpperCase()),
                ANSIColors.highlite(lowest.getNumber("bid") + " PLN")
        );

    }
}
