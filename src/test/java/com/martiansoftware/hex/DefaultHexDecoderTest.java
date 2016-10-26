package com.martiansoftware.hex;

import java.text.ParseException;
import java.util.Arrays;
import junit.framework.TestCase;

public class DefaultHexDecoderTest extends TestCase {
    
    private static final byte[] b = "The quick brown fox jumps over the lazy dog.".getBytes();
    private static final String s = "54 6865207175    69:63-6B2062.726F  - ::.776e\n\n20666f78206a756d7073206f76657220746865206c617a7920646f672e";
    
    public void testDecode() throws Exception {
        HexDecoder d = StandardHexCodecs.DEFAULT;
        assertTrue(Arrays.equals(b, d.decode(s)));
        
        try {
            d.decode("a");
            fail("decoded a single-char hex string");
        } catch (ParseException expected) {}

        try {
            d.decode("ab+12");
            fail("decoded a hex string with an illegal character");
        } catch (ParseException expected) {}

        assertEquals(0, d.decode("").length);
    }
    
}
