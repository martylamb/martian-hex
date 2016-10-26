package com.martiansoftware.hex;

//   Copyright 2016 Martian Software, Inc.
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * An interface providing binary-to-hexadecimal encoding.  This interface
 * provides multiple methods of providing binary data and an output destination,
 * but it does not prescribe the specific hexadecimal format.  Different
 * implementations of this interface will provide different encodings.  See the
 * README accompanying this source code for examples.
 * 
 * Implementations only need to implement the
 * `public void encode(InputStream in, PrintWriter out)` method.
 * 
 * Alternatively, a SimpleBaseHexEncoder class is provided that may allow
 * for simpler implementations of more complex hex encodings such as the
 * one provided by HexDumpEncoder.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public interface HexEncoder {
    
    /**
     * Encodes the specified byte array to a String
     * @param b the byte array to encode
     * @return the encoded byte array as a String
     */
    public default String encode(byte[] b) {
        return encode(b, 0, b.length);
    }
    
    /**
     * Encodes a portion of the specified byte array to a String
     * @param b the byte array to encode
     * @param offset the beginning index in the byte array to encode
     * @param len the number of bytes to encode
     * @return the encoded portion of the byte array as a String
     */
    public default String encode(byte[] b, int offset, int len) {
        try {
            return encode(new ByteArrayInputStream(b, offset, len));
        } catch (IOException notExpected) {
            throw new RuntimeException(notExpected);
        }
    }

    /**
     * Encodes the contents of the specified InputStream to a String
     * @param in the InputStream to read and encode
     * @return the encoded contents of the InputStream as a String
     * @throws IOException 
     */
    public default String encode(InputStream in) throws IOException {
        StringWriter s = new StringWriter();
        try (PrintWriter p = new PrintWriter(s)) {
            encode(in, p);
        }
        return s.toString();
    }

    /**
     * Encodes the specified byte array to a PrintWriter
     * @param b the byte array to encode
     * @param out the destination to which the encoded byte array will be written
     */
    public default void encode(byte[] b, PrintWriter out) {
        encode(b, 0, b.length, out);
    }
    
    /**
     * Encodes the specified byte array to a PrintStream
     * @param b the byte array to encode
     * @param out the destination to which the encoded byte array will be written
     */
    public default void encode(byte[] b, PrintStream out) {
        try (PrintWriter p = new PrintWriter(out)) {
            encode(b, p);
        }
    }
    
    /**
     * Encodes a portion of the specified byte array to a PrintWriter
     * @param b the byte array to encode
     * @param offset the beginning index in the byte array to encode
     * @param len the number of bytes to encode
     * @param out the destination to which the encoded portion of the byte array
     * will be written
     */
    public default void encode(byte[] b, int offset, int len, PrintWriter out) {
        try {
            encode(new ByteArrayInputStream(b, offset, len), out);
        } catch (IOException notExpected) {
            throw new RuntimeException(notExpected);
        }
    }
    
    /**
     * Encodes a portion of the specified byte array to a PrintStream
     * @param b the byte array to encode
     * @param offset the beginning index in the byte array to encode
     * @param len the number of bytes to encode
     * @param out the destination to which the encoded portion of the byte array
     * will be written
     */
    public default void encode(byte[] b, int offset, int len, PrintStream out) {
        try (PrintWriter p = new PrintWriter(out)) {
            encode(b, offset, len, p);
        }
    }
    
    /**
     * Encodes the contents of the specified InputStream to a PrintStream
     * @param in the InputStream to read and encode
     * @param out the destination to which the encoded contents of the InputStream
     * will be written
     * @throws IOException 
     */
    public default void encode(InputStream in, PrintStream out) throws IOException {
        try (PrintWriter p = new PrintWriter(out)) {
            encode(in, p);
        }
    }

    /**
     * Encodes the contents of the specified InputStream to a PrintWriter.
     * Concrete implementations of this interface must implement this method.
     * @param in the InputStream to read and encode
     * @param out the destination to which the encoded contents of the InputStream
     * will be written
     * @throws IOException 
     */
    public void encode(InputStream in, PrintWriter out) throws IOException;
    
}
