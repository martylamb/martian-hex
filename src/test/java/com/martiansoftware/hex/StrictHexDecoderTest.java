package com.martiansoftware.hex;

import java.text.ParseException;
import java.util.Arrays;
import junit.framework.TestCase;

public class StrictHexDecoderTest extends TestCase {
    
    private byte[] b = { 1, 10, 100 };
    private String s = "010a64";
    
    public void testStrictDecoder() throws Exception {
        StrictHexDecoder d = new StrictHexDecoder();
        assertTrue(Arrays.equals(b, d.decode(s)));
        
        try {
            d.decode("01-0a-64");
            fail("strict decoder accepted non-hex characters");
        } catch (ParseException expected) {}
    }
    
}
