package pl.regzand.nbpdata.commands.currency;

import org.json.JSONException;
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
 * Class that handles currency records command.
 */
public class CurrencyRecordsCommand extends NBPCommand {

    /**
     * Creates currency records command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyRecordsCommand(Command superCommand) {
        super(
                superCommand,
                "records",
                "<currency>",
                "Displays the lowest and the highest price for this currency according to NBP data",
                "\tCurrency format - ISO 4217 (XXX)"
        );
    }

    @Override
    protected void handleCommand(String[] args) throws CommandException, IOException, JSONException {

        // display usage if there is no arguments provided
        if (args.length == 0) {
            this.displayUsage();
            return;
        }

        // verify arguments count
        checkArgumentsCount(args, 1);

        // parse arguments
        String code = parseCurrencyCode(args[0]);

        // get max span dates
        Date from = parseStringToDate("2002-01-02");
        Date to = new Date();

        // create analyser
        JSONAnalyser analyser = new JSONAnalyser(
                new DataProvider<JSONObject>(
                        new JSONDateRangeFetcher(
                                new NBPConnector(),
                                from,
                                to,
                                Calendar.DATE,
                                90,
                                "exchangerates/rates/a/" + code + "/%tF/%tF",
                                false,
                                "/rates"
                        )
                )
        );

        JSONObject range = analyser.findRangeNumber(jsonObject -> jsonObject.getNumber("mid"));

        // display
        System.out.format("Currency %s had the highest value of %s at %s%n",
                ANSIColors.highlite(code),
                ANSIColors.highlite(String.format("%.4f PLN", range.getJSONObject("max").getDouble("mid"))),
                range.getJSONObject("max").getString("effectiveDate")
        );
        System.out.format("Currency %s had the lowest  value of %s at %s%n",
                ANSIColors.highlite(code),
                ANSIColors.highlite(String.format("%.4f PLN", range.getJSONObject("min").getDouble("mid"))),
                range.getJSONObject("min").getString("effectiveDate")
        );

    }
}
