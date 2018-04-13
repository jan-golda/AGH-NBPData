package pl.regzand.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ANSIColorsTest {

    @Test
    void highlite() {

        assertEquals(
                "\u001b[1m\u001b[7m test \u001b[0m",
                ANSIColors.highlite("test")
        );

        assertEquals(
                "\u001b[1m\u001b[7m AAB \u001b[0m",
                ANSIColors.highlite("AAB")
        );

        assertEquals(
                "\u001b[1m\u001b[7m with space \u001b[0m",
                ANSIColors.highlite("with space")
        );

        assertEquals(
                "\u001b[1m\u001b[7m text \u001b[31m with code \u001b[0m",
                ANSIColors.highlite("text \u001b[31m with code")
        );

        assertEquals(
                "\u001b[1m\u001b[7m 129.43 PLN \u001b[0m",
                ANSIColors.highlite("129.43 PLN")
        );

    }

}