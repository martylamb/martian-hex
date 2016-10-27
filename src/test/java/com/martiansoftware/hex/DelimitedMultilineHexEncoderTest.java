package com.martiansoftware.hex;

import junit.framework.TestCase;

public class DelimitedMultilineHexEncoderTest extends TestCase {
    
    byte[] b = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    
    public void testDelimited() {
        DelimitedMultilineHexEncoder e = new DelimitedMultilineHexEncoder("*");
        assertEquals("00*01*02*03*04*05*06*07*08*09*0a*0b*0c*0d*0e*0f", e.encode(b));
    }
    
    public void testMultiline() {
        DelimitedMultilineHexEncoder e = new DelimitedMultilineHexEncoder(4);
        assertEquals(String.format("00010203%n04050607%n08090a0b%n0c0d0e0f"), e.encode(b));
    }
    
    public void testDelimitedMultiline() {
        DelimitedMultilineHexEncoder e = new DelimitedMultilineHexEncoder(4, "_");
        assertEquals(String.format("00_01_02_03%n04_05_06_07%n08_09_0a_0b%n0c_0d_0e_0f"), e.encode(b));
    }

    public void testNoArgs() {
        DelimitedMultilineHexEncoder e1 = new DelimitedMultilineHexEncoder();
        DefaultHexEncoder e2 = new DefaultHexEncoder();
        assertEquals(e1.encode(b), e2.encode(b));        
    }
    
    public void testBadConstruction() {
        try {
            DelimitedMultilineHexEncoder e = new DelimitedMultilineHexEncoder(0);
            fail("created a DelimitedMultilineHexEncoder with zero bytes per line");
        } catch (Exception expected) {}
    }
}
