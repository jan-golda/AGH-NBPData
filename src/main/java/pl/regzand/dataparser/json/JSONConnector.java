package pl.regzand.dataparser.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import pl.regzand.dataparser.ApiConnector;
import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;

import java.io.IOException;

/**
 * Class extending ApiConnector with functionality to parse response to JSON
 */
public class JSONConnector extends ApiConnector {

    public JSONConnector(String baseUrl) {
        super(baseUrl);
    }

    /**
     * Returns response from api parsed to JSONObject
     *
     * @param apiUrl url that will be added to baseUrl to create request url
     *
     * @return json object created based on data returned from api
     * @throws IOException                       if there was some sort of I/O error with connection
     * @throws UnexpectedHttpStatusCodeException if response had different response code then 200
     * @throws JSONException                     if there was an problem with parsing data as json
     */
    public JSONObject makeJSONObjectRequest(String apiUrl) throws IOException, UnexpectedHttpStatusCodeException, JSONException {
        return new JSONObject(new JSONTokener(this.makeRequest(apiUrl)));
    }

    /**
     * Returns response from api parsed to JSONArray
     *
     * @param apiUrl url that will be added to baseUrl to create request url
     *
     * @return json array created based on data returned from api
     * @throws IOException                       if there was some sort of I/O error with connection
     * @throws UnexpectedHttpStatusCodeException if response had different response code then 200
     * @throws JSONException                     if there was an problem with parsing data as json
     */
    public JSONArray makeJSONArrayRequest(String apiUrl) throws IOException, UnexpectedHttpStatusCodeException, JSONException {
        return new JSONArray(new JSONTokener(this.makeRequest(apiUrl)));
    }

}
