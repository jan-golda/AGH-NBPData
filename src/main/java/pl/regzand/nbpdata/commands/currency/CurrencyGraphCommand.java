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
import pl.regzand.utils.ConsoleGraph;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Class that handles graph command.
 */
public class CurrencyGraphCommand extends NBPCommand {

    private static String[] DAYS = new String[]{"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

    /**
     * Creates new graph command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyGraphCommand(Command superCommand) {
        super(
                superCommand,
                "graph",
                "<currency> <start> <end>",
                "Displays graph with price of specified currency in given time frame",
                "\tCurrency format - ISO 4217 (XXX)\n\tDate format - <year>,<month>,<week in month>"
        );
    }

    @Override
    protected void handleCommand(String[] args) throws CommandException, IOException, JSONException, ParseException {

        // display usage if there is no arguments provided
        if (args.length == 0) {
            this.displayUsage();
            return;
        }

        // verify arguments count
        checkArgumentsCount(args, 3);

        // parse arguments
        String code = parseCurrencyCode(args[0]);
        Date start  = parseWeirdDateFormat(args[1], 1);
        Date end    = parseWeirdDateFormat(args[2], 5);

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
                                "exchangerates/rates/a/" + code + "/%tF/%tF",
                                false,
                                "/rates"
                        )
                )
        );

        // date format
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // get data
        List<JSONObject> data = analyser.sorted((o1, o2) -> {
            try {
                Date d1 = dateFormat.parse(o1.getString("effectiveDate"));
                Date d2 = dateFormat.parse(o2.getString("effectiveDate"));

                if (d1.getDay() == d2.getDay())
                    return (d1.after(d2) ? 1 : -1);

                return d1.getDay() - d2.getDay();
            } catch (Exception e) {
                return 0;
            }
        });

        // create grapher
        ConsoleGraph graph = new ConsoleGraph();

        // prepare data
        for (JSONObject obj : data) {
            Date d = dateFormat.parse(obj.getString("effectiveDate"));
            graph.addEntry(obj.getString("effectiveDate") + " " + ANSIColors.highlite(DAYS[d.getDay()]), obj.getDouble("mid"));
        }

        // draw graph
        graph.draw(16, 50);
    }
}
