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
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Class that handles difference command
 */
public class CurrencyDifferenceCommand extends NBPCommand {

    /**
     * Creates difference command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyDifferenceCommand(Command superCommand) {
        super(
                superCommand,
                "difference",
                "<date> <N>",
                "Displays N currencies sorted by difference in buy and sell prices",
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
        checkArgumentsCount(args, 2);

        // parse arguments
        Date date = parseStringToDate(args[0]);

        // parse N
        int N;
        try {
            N = Integer.parseInt(args[1]);
        } catch (Exception e) {
            throw new CommandException("Wrong syntax: N is expected to be an integer", true);
        }

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

        // get data
        List<JSONObject> data = analyser.sorted((o1, o2) -> {
            double diff1 = o1.getDouble("ask") - o1.getDouble("bid");
            double diff2 = o2.getDouble("ask") - o2.getDouble("bid");
            return (diff1 > diff2 ? 1 : -1);
        });

        // display
        System.out.format("Currencies sorted according to difference between buy and sell price on %tF%n%n", date);
        System.out.println("\t" + ANSIColors.highlite("        BUY       SELL      DIFF   "));
        for (JSONObject obj : data) {
            if(N-- == 0) break;

            double bid = obj.getDouble("bid");
            double ask = obj.getDouble("ask");
            System.out.format("\t%s  %.6f  %.6f  %.6f%n",
                    ANSIColors.highlite(obj.getString("code")),
                    bid,
                    ask,
                    ask - bid
            );
        }


    }
}
