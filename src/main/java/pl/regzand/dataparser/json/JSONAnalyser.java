package pl.regzand.dataparser.json;

import org.json.JSONObject;
import pl.regzand.dataparser.DataProvider;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Class with functionality to perform analysis on JSON data from provider.
 */
public class JSONAnalyser {

    private final DataProvider<JSONObject> provider;

    private boolean used;

    /**
     * Creates analyser that used data from given provider.
     *
     * @param provider provider of JSON data to analyse
     */
    public JSONAnalyser(DataProvider<JSONObject> provider) {
        this.provider = provider;
        this.used = false;
    }

    /**
     * Calculates average numeric value from all remaining data.
     *
     * @param getter function that takes json object and returns numeric value from it
     *
     * @return average numeric value from all remaining data
     * @throws IOException           if provider throw an IOException
     * @throws IllegalStateException if all data from this analyser was already used
     */
    public double average(Function<JSONObject, Number> getter) throws IOException {
        if (used)
            throw new IllegalStateException("Data from this analyser was already used");

        used = true;

        double sum = 0;
        int n = 0;

        while (provider.hasNext()) {
            sum += getter.apply(provider.next()).doubleValue();
            n++;
        }

        if (n == 0)
            throw new IllegalStateException("Analyser found no data to perform average calculation");

        return sum / n;
    }

    /**
     * Returns json object that has the smallest number value pointed by json pointer.
     *
     * @param getter function that takes json object and returns numeric value from it
     *
     * @return json object that has the smallest number value pointed by json pointer
     * @throws IOException if provider throw an IOException
     */
    public JSONObject findTheSmallestNumber(Function<JSONObject, Number> getter) throws IOException {
        if (used)
            throw new IllegalStateException("Data from this analyser was already used");

        used = true;

        if (!provider.hasNext())
            return null;

        JSONObject smallest = provider.next();
        double num = getter.apply(smallest).doubleValue();

        while (provider.hasNext()) {
            JSONObject next = provider.next();
            double nextNum = getter.apply(next).doubleValue();

            if (nextNum < num) {
                smallest = next;
                num = nextNum;
            }
        }

        return smallest;
    }

    /**
     * Returns json object that has the biggest number value pointed by json pointer.
     *
     * @param getter function that takes json object and returns numeric value from it
     *
     * @return json object that has the biggest number value pointed by json pointer
     * @throws IOException if provider throw an IOException
     */
    public JSONObject findTheBiggestNumber(Function<JSONObject, Number> getter) throws IOException {
        if (used)
            throw new IllegalStateException("Data from this analyser was already used");

        used = true;

        if (!provider.hasNext())
            return null;

        JSONObject biggest = provider.next();
        double num = getter.apply(biggest).doubleValue();

        while (provider.hasNext()) {
            JSONObject next = provider.next();
            double nextNum = getter.apply(next).doubleValue();

            if (nextNum > num) {
                biggest = next;
                num = nextNum;
            }
        }

        return biggest;
    }

    /**
     * Returns json object containing two json objects, the one with the biggest value number and the one with the smallest value number pointer by json pointer.
     * <p>
     * Returned object is in format:
     * <code>{min: minObj, max: maxObj}</code>
     * </p>
     *
     * @param getter function that takes json object and returns numeric value from it
     *
     * @return json object in format <code>{min: minObj, max: maxObj}</code>
     * @throws IOException if provider throw an IOException
     */
    public JSONObject findRangeNumber(Function<JSONObject, Number> getter) throws IOException {
        if (used)
            throw new IllegalStateException("Data from this analyser was already used");

        used = true;

        if (!provider.hasNext())
            return null;

        JSONObject biggest = provider.next();
        JSONObject smallest = provider.next();
        double bNum = getter.apply(biggest).doubleValue();
        double sNum = getter.apply(smallest).doubleValue();

        while (provider.hasNext()) {
            JSONObject next = provider.next();
            double nextNum = getter.apply(next).doubleValue();

            if (nextNum > bNum) {
                biggest = next;
                bNum = nextNum;
            }

            if (nextNum < sNum) {
                smallest = next;
                sNum = nextNum;
            }
        }

        JSONObject res = new JSONObject();
        res.put("max", biggest);
        res.put("min", smallest);

        return res;
    }

    /**
     * Returns data sorted using given comparator.
     *
     * @param comparator used to sort data
     *
     * @return data sorted using given comparator
     * @throws IOException if provider throw an IOException
     */
    public List<JSONObject> sorted(Comparator<JSONObject> comparator) throws IOException {
        if (used)
            throw new IllegalStateException("Data from this analyser was already used");

        used = true;

        List<JSONObject> data = provider.getAll();

        data.sort(comparator);

        return data;
    }

}
