package pl.regzand.dataparser;

import pl.regzand.dataparser.exceptions.UnexpectedHttpStatusCodeException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class responsible for making requests to JSON HTTP API
 */
public class ApiConnector {

    private final String baseUrl;

    /**
     * Create new api connector with specified base api url
     *
     * @param baseUrl
     */
    public ApiConnector(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * Returns InputStream containing response form API
     *
     * @param apiUrl url that will be added to baseUrl to create request url
     *
     * @return InputStream containing response form API
     * @throws IOException                       if there was some sort of I/O error with connection
     * @throws UnexpectedHttpStatusCodeException if response had different response code then 200
     */
    public InputStream makeRequest(String apiUrl) throws IOException, UnexpectedHttpStatusCodeException {

        // create connection
        URL url = new URL(baseUrl + apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // set connection properties
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        // making request
        connection.connect();

        // check response code
        if (connection.getResponseCode() != 200)
            throw new UnexpectedHttpStatusCodeException(connection.getResponseCode(), url);

        // returning response
        return connection.getInputStream();
    }

}
