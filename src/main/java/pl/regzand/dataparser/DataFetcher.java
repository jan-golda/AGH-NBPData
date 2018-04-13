package pl.regzand.dataparser;

import java.io.IOException;
import java.util.Collection;

/**
 * Interface providing method that fetches data in blocks
 *
 * @param <T>
 */
public interface DataFetcher<T> {

    /**
     * Returns next data block
     *
     * @return next data block
     */
    public Collection<T> fetch() throws IOException;

}
