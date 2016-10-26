package com.martiansoftware.hex;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Arrays;
import junit.framework.TestCase;

public class HexTest extends TestCase {
    
    private static final byte[] b1 = { 8, 10, 15, 16 };
    private static final String s1 = "080a0f10";
    
    public void testEncodeString() {
        assertEquals(s1, Hex.encode(b1));       
    }
    
    public void testDecodeString() throws Exception {
        assertTrue(Arrays.equals(b1, Hex.decode(s1)));
    }
    
    public void testDecodeReader() throws Exception {
        assertTrue(Arrays.equals(b1, Hex.decode(new StringReader(s1))));
    }
    
    public void testDecodeStringToOutputStream() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.decode(s1, b);
        assertTrue(Arrays.equals(b1, b.toByteArray()));
    }

    public void testDecodeReaderToOutputStream() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.decode(new StringReader(s1), b);
        assertTrue(Arrays.equals(b1, b.toByteArray()));
    }

    public void testEncodeInputStream() throws Exception {
        assertEquals(s1, Hex.encode(new ByteArrayInputStream(b1)));
    }
    
    public void testEncodeInputStreamToPrintStream() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(new ByteArrayInputStream(b1), new PrintStream(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));
    }
    
    public void testEncodeByteArrayToPrintStream() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(b1, new PrintStream(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));        
    }
    
    public void testEncodeByteArrayToPrintStream2() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(b1, 0, b1.length, new PrintStream(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));        
    }

    public void testEncodeInputStreamToPrintWriter() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(new ByteArrayInputStream(b1), new PrintWriter(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));
    }

    public void testEncodeByteArrayToPrintWriter() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(b1, new PrintWriter(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));        
    }

    public void testEncodeByteArrayToPrintWriter2() throws Exception {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        Hex.encode(b1, 0, b1.length, new PrintWriter(b));
        assertTrue(Arrays.equals(s1.getBytes(), b.toByteArray()));        
    }
    
    public void testEncodeToString2() throws Exception {
        assertEquals("0a0f", Hex.encode(b1, 1, 2));
    }
}
