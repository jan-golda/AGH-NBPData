package pl.regzand.nbpdata.commands.currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.DataProvider;
import pl.regzand.dataparser.json.JSONDateRangeFetcher;
import pl.regzand.nbpdata.commands.NBPCommand;
import pl.regzand.nbpdata.data.NBPConnector;
import pl.regzand.utils.ANSIColors;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles difference command
 */
public class CurrencyFluctuationsCommand extends NBPCommand {

    /**
     * Creates fluctuations command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyFluctuationsCommand(Command superCommand) {
        super(
                superCommand,
                "fluctuations",
                "<date>",
                "Displays currency that according to NBP data had the biggest fluctuation starting from given date",
                "\tDate format - ISO 8601 (YYYY-MM-DD)"
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
        Date date = parseStringToDate(args[0]);

        // verify arguments
        checkDate(date);

        // get data provider
        DataProvider<JSONObject> provider = new DataProvider<JSONObject>(
                new JSONDateRangeFetcher(
                        new NBPConnector(),
                        date,
                        new Date(),
                        Calendar.DATE,
                        90,
                        "exchangerates/tables/a/%tF/%tF",
                        true,
                        null
                )
        );

        Map<String, Double> max = new HashMap<>();
        Map<String, Double> min = new HashMap<>();

        // for each table
        while (provider.hasNext()) {
            JSONArray array = provider.next().getJSONArray("rates");

            // for each entry
            for (int i = 0; i < array.length(); i++) {
                JSONObject entry = array.getJSONObject(i);
                String code = entry.getString("code");
                Double mid = entry.getDouble("mid");

                if (!max.containsKey(code))
                    max.put(code, Double.NEGATIVE_INFINITY);
                if (!min.containsKey(code))
                    min.put(code, Double.POSITIVE_INFINITY);

                if (mid > max.get(code))
                    max.put(code, mid);
                if (mid < min.get(code))
                    min.put(code, mid);
            }
        }

        // find biggest difference
        String code = "";
        double diff = 0;
        for (String key : max.keySet()) {
            double value = max.get(key) - min.get(key);
            if (value > diff) {
                code = key;
                diff = value;
            }
        }

        // display
        System.out.format("Currency with the biggest change is price between %tF and %tF is %s%n\tMax price  %s%n\tMin price  %s%n\tDifference %s%n",
                date,
                new Date(),
                ANSIColors.highlite(code),
                ANSIColors.highlite(String.format("%.4f PLN", max.get(code))),
                ANSIColors.highlite(String.format("%.4f PLN", min.get(code))),
                ANSIColors.highlite(String.format("%.4f PLN", diff))
        );


    }
}
