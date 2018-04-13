package pl.regzand.dataparser.json;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
import pl.regzand.dataparser.DataFetcher;

import java.io.IOException;
import java.util.*;

/**
 * DataFetcher that fetches data from JSON API in portions specified by date/time
 */
public class JSONDateRangeFetcher implements DataFetcher<JSONObject> {

    private final JSONConnector connector;

    private final Calendar start;
    private final Calendar end;

    private final int stepType;
    private final int step;

    private final String format;

    private final boolean isArray;
    private final String jsonPointer;


    /**
     * Creates new data fetcher that fetches data from date range in some portions.
     *
     * @param connector   connector providing connection to json api
     * @param start       begin of range
     * @param end         end of range
     * @param stepType    unit type of step (int code from {@link java.util.Calendar Calendar})
     * @param step        how many of stepType take in each fetch
     * @param format      format of api url, will be parsed using String.format() with begin date as first argument and end date as second
     * @param isArray     true if response is expected to be and json array, false if json object
     * @param jsonPointer path in json returned by api to json array containing data
     */
    public JSONDateRangeFetcher(@NotNull JSONConnector connector, @NotNull Date start, @NotNull Date end, int stepType, int step, @NotNull String format, boolean isArray, @Nullable String jsonPointer) {
        this.connector = connector;
        this.stepType = stepType;
        this.step = step;
        this.format = format;
        this.isArray = isArray;
        this.jsonPointer = jsonPointer;

        this.start = new GregorianCalendar();
        this.end = new GregorianCalendar();
        this.start.setTime(start);
        this.end.setTime(end);
    }

    /**
     * Creates new data fetcher that fetches data from date range in some portions.
     *
     * @param connector   connector providing connection to json api
     * @param start       begin of range
     * @param end         end of range
     * @param stepType    unit type of step (int code from {@link java.util.Calendar Calendar})
     * @param step        how many of stepType take in each fetch
     * @param format      format of api url, will be parsed using String.format() with begin date as first argument and end date as second
     * @param isArray     true if response is expected to be and json array, false if json object
     * @param jsonPointer path in json returned by api to json array containing data
     */
    public JSONDateRangeFetcher(@NotNull JSONConnector connector, @NotNull Calendar start, @NotNull Calendar end, int stepType, int step, @NotNull String format, boolean isArray, @Nullable String jsonPointer) {
        this.connector = connector;
        this.start = start;
        this.end = end;
        this.stepType = stepType;
        this.step = step;
        this.format = format;
        this.isArray = isArray;
        this.jsonPointer = jsonPointer;
    }

    /**
     * Returns next data block
     *
     * @return next data block
     */
    @Override
    public Collection<JSONObject> fetch() throws IOException {

        // if there is nothing left return empty
        if (start.after(end))
            return new ArrayList<>();

        // get range
        Date from = start.getTime();
        start.add(stepType, step);
        Date to = start.getTime();
        start.add(stepType, 1);

        // if range expands after end trim it
        if (to.after(end.getTime()))
            to = end.getTime();

        JSONArray array;

        // make request
        if (isArray) {
            array = connector.makeJSONArrayRequest(String.format(format, from, to));

            // get data from request
            if (jsonPointer != null)
                array = (JSONArray) array.optQuery(jsonPointer);
        } else {
            array = (JSONArray) connector.makeJSONObjectRequest(String.format(format, from, to)).optQuery(jsonPointer);
        }

        // copy data
        List<JSONObject> result = new ArrayList<>();
        for (Object obj : array)
            result.add((JSONObject) obj);

        return result;
    }
}
