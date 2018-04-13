package pl.regzand.dataparser.exceptions;

import java.io.IOException;
import java.net.URL;

/**
 * Exception thrown when http request ends with unexpected status code
 */
public class UnexpectedHttpStatusCodeException extends IOException {

    private final int code;
    private final URL url;

    /**
     * Creates new unexpected status code exception
     *
     * @param code http status code
     * @param url  to which http request was made
     */
    public UnexpectedHttpStatusCodeException(int code, URL url) {
        super("Request to '" + url.toString() + "' returned unexpected status code: " + code);
        this.code = code;
        this.url = url;
    }

    /**
     * Returns http status code returned form request
     *
     * @return http status code returned form request
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns url to which http request was made
     *
     * @return url to which http request was made
     */
    public URL getUrl() {
        return url;
    }

}
