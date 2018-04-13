package pl.regzand.nbpdata.data;

import pl.regzand.dataparser.json.JSONConnector;

/**
 * Connector used to connect to NBP API in JSON format.
 */
public class NBPConnector extends JSONConnector {

    /**
     * Creates connector to NBP API in JSON format.
     */
    public NBPConnector() {
        super("http://api.nbp.pl/api/");
    }

}
