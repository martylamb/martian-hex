package com.martiansoftware.hex;

import java.util.Arrays;
import junit.framework.TestCase;

public class HexDumpTest extends TestCase {

    private static final byte[] b = "It was the best of times, it was the worst of times...".getBytes();
    
    private static final String s = "00000000: 49 74 20 77 61 73 20 74   68 65 20 62 65 73 74 20   It was the best \n"
                                  + "00000010: 6f 66 20 74 69 6d 65 73   2c 20 69 74 20 77 61 73   of times, it was\n"
                                  + "00000020: 20 74 68 65 20 77 6f 72   73 74 20 6f 66 20 74 69    the worst of ti\n"
                                  + "00000030: 6d 65 73 2e 2e 2e                                   mes...          ";
    
    public void testHexDump() throws Exception {
        HexDumpEncoder e = new HexDumpEncoder();
        assertEquals(s, e.encode(b));
        
        HexDumpDecoder d = new HexDumpDecoder();
        assertTrue(Arrays.equals(b, d.decode(s)));
    }
}
