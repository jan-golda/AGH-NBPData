package pl.regzand.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that prints (horizontal) column graph to standard output.
 */
public class ConsoleGraph {

    private static final char CHAR = '■';

    private final List<GraphEntry> data = new ArrayList<>();

    private double max = Double.NEGATIVE_INFINITY;
    private double min = Double.POSITIVE_INFINITY;

    /**
     * Adds column to graph.
     *
     * @param label name of column
     * @param value value of column
     */
    public void addEntry(String label, double value) {
        data.add(new GraphEntry(label, value));

        max = Math.max(max, value);
        min = Math.min(min, value);
    }

    /**
     * Prints this graph to standard output.
     *
     * @param labelWidth width, in chars, of labels
     * @param valueWidth width, in chars, of columns
     */
    public void draw(int labelWidth, int valueWidth) {

        // print
        for (GraphEntry entry : data) {
            StringBuilder builder = new StringBuilder();

            // name
            builder.append(String.format("%-" + labelWidth + "s ", entry.label));

            // bar
            double c = valueWidth * 0.1 + (entry.value - min) / (max - min) * (valueWidth * 0.9);
            for (int i = 1; i < c; i++)
                builder.append(CHAR);

            // value
            builder.append(String.format(" %.4f", entry.value));

            System.out.println(builder.toString());
        }

    }

    /**
     * Private graph class used for storing graph data.
     */
    private class GraphEntry {

        final String label;
        final double value;

        /**
         * Creates graph entry.
         *
         * @param label name of column
         * @param value value of column
         */
        GraphEntry(String label, double value) {
            this.label = label;
            this.value = value;
        }
    }
}
