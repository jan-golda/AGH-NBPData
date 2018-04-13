package pl.regzand.dataparser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DataProvidingTest {

    @Test
    void dataProvidingTest(){

        // ==============================================================================
        // === PREPARATION
        // ==============================================================================

        // create fetcher
        DataFetcher<String> fetcher = new DataFetcher<String>() {

            private String[] data = new String[]{"A", "B", "C", "D", "E", "F", "G"};

            private int pointer = 0;

            @Override
            public Collection<String> fetch() throws IOException {
                pointer += 3;
                if(pointer-3 >= data.length)
                    return new ArrayList<>();
                return Arrays.asList(Arrays.copyOfRange(data, pointer-3, Math.min(pointer, data.length)));
            }
        };

        // crate provider
        DataProvider<String> provider = new DataProvider<>(fetcher);

        // ==============================================================================
        // === TESTS
        // ==============================================================================

        try{

            assertTrue(provider.hasNext());
            assertEquals("A", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("B", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("C", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("D", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("E", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("F", provider.next());

            assertTrue(provider.hasNext());
            assertEquals("G", provider.next());

            assertFalse(provider.hasNext());

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

}