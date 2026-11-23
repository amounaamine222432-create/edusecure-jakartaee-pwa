package tn.supcom.edusecure.crypto;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JcsCanonicalizationTest {

    private final JcsCanonicalization jcs = new JcsCanonicalization();

    @Test
    void shouldCanonicalizeDeterministically() {
        String first = "{\"z\":3,\"a\":{\"y\":2,\"x\":1}}";
        String second = " { \"a\" : { \"x\" : 1, \"y\" : 2 }, \"z\" : 3 } ";

        assertArrayEquals(jcs.canonicalize(first), jcs.canonicalize(second));
        assertEquals("{\"a\":{\"x\":1,\"y\":2},\"z\":3}",
                new String(jcs.canonicalize(first), StandardCharsets.UTF_8));
    }

    @Test
    void shouldMatchRfc8785Section322And323Vector() {
        String input = "{"
                + "\"numbers\":[333333333.33333329,1E30,4.50,2e-3,"
                + "0.000000000000000000000000001],"
                + "\"string\":\"" + "\\u20ac" + "$" + "\\u000F" + "\\u000a"
                + "A'" + "\\u0042" + "\\u0022" + "\\u005c"
                + "\\\\" + "\\\"" + "\\/" + "\","
                + "\"literals\":[null,true,false]}";
        String expected = "{\"literals\":[null,true,false],"
                + "\"numbers\":[333333333.3333333,1e+30,4.5,0.002,1e-27],"
                + "\"string\":\"€$\\u000f\\nA'B\\\"\\\\\\\\\\\"/\"}";

        assertEquals(expected,
                new String(jcs.canonicalize(input), StandardCharsets.UTF_8));
    }

    @Test
    void shouldMatchRfc8785Utf16PropertyOrderingVector() {
        String input = "{"
                + "\"€\":\"Euro Sign\","
                + "\"\\r\":\"Carriage Return\","
                + "\"דּ\":\"Hebrew Letter Dalet With Dagesh\","
                + "\"1\":\"One\","
                + "\"😀\":\"Emoji: Grinning Face\","
                + "\"\":\"Control\","
                + "\"ö\":\"Latin Small Letter O With Diaeresis\"}";
        String canonical = new String(jcs.canonicalize(input), StandardCharsets.UTF_8);

        assertTrue(canonical.indexOf("Carriage Return") < canonical.indexOf("One"));
        assertTrue(canonical.indexOf("One") < canonical.indexOf("Control"));
        assertTrue(canonical.indexOf("Control") < canonical.indexOf("Latin Small"));
        assertTrue(canonical.indexOf("Latin Small") < canonical.indexOf("Euro Sign"));
        assertTrue(canonical.indexOf("Euro Sign") < canonical.indexOf("Emoji"));
        assertTrue(canonical.indexOf("Emoji") < canonical.indexOf("Hebrew"));
    }

    @Test
    void shouldRejectDuplicateProperties() {
        assertThrows(IllegalArgumentException.class,
                () -> jcs.canonicalize("{\"a\":1,\"a\":2}"));
    }
}
