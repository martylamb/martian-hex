package com.martiansoftware.hex;

import java.util.Arrays;
import java.util.Set;
import junit.framework.TestCase;

public class CharIgnoringHexDecoderTest extends TestCase {
    
    private static final String s = "43x68X61=====7249676e6f72696e674865784465636f646572";
    private static final byte[] b = "CharIgnoringHexDecoder".getBytes();
    
    public void testStringConstructor() throws Exception {
        CharIgnoringHexDecoder d = new CharIgnoringHexDecoder("xX=");
        assertTrue(Arrays.equals(b, d.decode(s)));
    }

    public void testCharArrayConstructor() throws Exception {
        char[] c = {'x', 'X', '='};
        CharIgnoringHexDecoder d = new CharIgnoringHexDecoder(c);
        assertTrue(Arrays.equals(b, d.decode(s)));
    }

    public void testPredicateConstructor() throws Exception {
        Set<Character> i = new java.util.HashSet<>();
        i.add('x'); i.add('X'); i.add('=');
        CharIgnoringHexDecoder d = new CharIgnoringHexDecoder(c -> i.contains(c));
        assertTrue(Arrays.equals(b, d.decode(s)));
    }

}
