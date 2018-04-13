package pl.regzand.dataparser;

import java.io.IOException;
import java.util.*;

/**
 * Class responsible for providing data in form of an iterator
 *
 * @param <T> type of provided data
 */
public class DataProvider<T> {

    // available data storage
    private final Queue<T> queue = new LinkedList<T>();

    // data fetcher
    private final DataFetcher<T> fetcher;

    /**
     * Creates new DataProvider that provides data from given DataFetcher.
     *
     * @param fetcher that fetches data for this provider
     */
    public DataProvider(DataFetcher<T> fetcher) {
        this.fetcher = fetcher;
    }

    /**
     * Returns {@code true} if there is another data segment available.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if there is another data segment available
     * @throws IOException when used data fetcher throw IOException
     */
    public boolean hasNext() throws IOException {
        if (queue.isEmpty())
            queue.addAll(fetcher.fetch());

        return !queue.isEmpty();
    }

    /**
     * Returns the next data segment.
     *
     * @return the next data segment
     * @throws IOException            when used data fetcher throw IOException
     * @throws NoSuchElementException if there is not more data segments available
     */
    public T next() throws IOException {
        if (!hasNext())
            throw new NoSuchElementException();

        return queue.remove();
    }

    /**
     * Returns everything that this provider has to offer.
     *
     * @return collection containing all data
     * @throws IOException when used data fetcher throw IOException
     */
    public List<T> getAll() throws IOException {
        List<T> result = new ArrayList<T>();

        while (this.hasNext())
            result.add(this.next());

        return result;
    }
}
