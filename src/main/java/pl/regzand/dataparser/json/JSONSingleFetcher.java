package pl.regzand.dataparser.json;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.regzand.dataparser.DataFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Fetcher that fetches data just once as single JSON API request.
 */
public class JSONSingleFetcher implements DataFetcher<JSONObject> {

    private final JSONConnector connector;

    private final String apiUrl;

    private final String jsonPointer;

    private boolean used;

    /**
     * Creates fetcher that just once fetches complete block of data form connector.
     *
     * @param connector   connector providing connection to JSON API
     * @param apiUrl      api url, will be added to base url of connector
     * @param jsonPointer path in json returned by api to json array containing data
     */
    public JSONSingleFetcher(JSONConnector connector, String apiUrl, String jsonPointer) {
        this.connector = connector;
        this.apiUrl = apiUrl;
        this.jsonPointer = jsonPointer;

        this.used = false;
    }

    /**
     * Returns data block. This method is supposed to be called only once
     *
     * @return data block
     */
    @Override
    public Collection<JSONObject> fetch() throws IOException {

        if (used)
            return new ArrayList<>();

        used = true;

        // make request
        JSONArray array = connector.makeJSONArrayRequest(apiUrl);

        // get data from request
        if (jsonPointer != null)
            array = (JSONArray) array.optQuery(jsonPointer);

        // copy data
        List<JSONObject> result = new ArrayList<>();
        for (Object obj : array)
            result.add((JSONObject) obj);

        return result;
    }
}
