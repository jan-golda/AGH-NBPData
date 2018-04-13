package pl.regzand.nbpdata.commands.currency;

import org.json.JSONException;
import org.json.JSONObject;
import pl.regzand.commands.Command;
import pl.regzand.commands.CommandException;
import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;
import pl.regzand.nbpdata.commands.NBPCommand;
import pl.regzand.nbpdata.data.NBPConnector;
import pl.regzand.utils.ANSIColors;

import java.io.IOException;
import java.util.Date;

/**
 * Class that handles currency price command.
 */
public class CurrencyPriceCommand extends NBPCommand {

    /**
     * Creates price command.
     *
     * @param superCommand super command for this command, expected {@link pl.regzand.nbpdata.commands.currency.CurrencyCommand}
     */
    CurrencyPriceCommand(Command superCommand) {
        super(
                superCommand,
                "price",
                "<currency> <date>",
                "Displays price of specified currency from given date, or today's currency price if date is not provided.",
                "\tCurrency format - ISO 4217 (XXX)\n\tDate format - ISO 8601 (YYYY-MM-DD)"
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
        String currency = parseCurrencyCode(args[0]);
        Date date = parseStringToDate(args[1]);

        // verify arguments
        checkDate(date);

        // get data
        JSONObject data = new NBPConnector().makeJSONObjectRequest(String.format("exchangerates/rates/a/%s/%tF", currency, date));

        // get value
        Number price = data.getJSONArray("rates").getJSONObject(0).getNumber("mid");

        // display
        System.out.format("Price of %s according to NBP data from %tF was %s%n",
                ANSIColors.highlite("1.00 " + currency),
                date,
                ANSIColors.highlite(price + " PLN")
        );

    }
}
