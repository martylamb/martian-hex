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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;

/**
 * A utility class providing static accessors to the StandardHexCodecs.DEFAULT
 * HexCodec.
 * 
 * @see HexEncoder
 * @see HexDecoder
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class Hex {

    private Hex() {}
    
    /**
     * @see HexEncoder#encode(byte[])
     */
    public static String encode(byte[] b) {
        return StandardHexCodecs.DEFAULT.encode(b);
    }
    
    /**
     * @see HexEncoder#encode(byte[],int,int)
     */
    public static String encode(byte[] b, int offset, int len) {
        return StandardHexCodecs.DEFAULT.encode(b, offset, len);
    }

    /**
     * @see HexEncoder#encode(InputStream)
     */
    public static String encode(InputStream in) throws IOException {
        return StandardHexCodecs.DEFAULT.encode(in);
    }

    /**
     * @see HexEncoder#encode(byte[],PrintWriter)
     */
    public static void encode(byte[] b, PrintWriter out) {
        StandardHexCodecs.DEFAULT.encode(b, out);
    }
    
    /**
     * @see HexEncoder#encode(byte[],int,int,PrintWriter)
     */
    public static void encode(byte[] b, int offset, int len, PrintWriter out) {
        StandardHexCodecs.DEFAULT.encode(b, offset, len, out);
    }

    /**
     * @see HexEncoder#encode(InputStream,PrintWriter)
     */
    public static void encode(InputStream in, PrintWriter out) throws IOException {
        StandardHexCodecs.DEFAULT.encode(in, out);
    }
    
    /**
     * @see HexEncoder#encode(byte[],PrintStream)
     */
    public static void encode(byte[] b, PrintStream out) {
        StandardHexCodecs.DEFAULT.encode(b, out);
    }
    
    /**
     * @see HexEncoder#encode(byte[],int,int,PrintStream)
     */
    public static void encode(byte[] b, int offset, int len, PrintStream out) {
        StandardHexCodecs.DEFAULT.encode(b, offset, len, out);
    }

    /**
     * @see HexEncoder#encode(InputStream,PrintStream)
     */
    public static void encode(InputStream in, PrintStream out) throws IOException {
        StandardHexCodecs.DEFAULT.encode(in, out);
    }
    
    /**
     * @see HexDecoder#decode(String)
     */
    public static byte[] decode(String in) throws ParseException {
        return StandardHexCodecs.DEFAULT.decode(in);
    }
    
    /**
     * @see HexDecoder#decode(Reader)
     */
    public static byte[] decode(Reader in) throws ParseException {
        return StandardHexCodecs.DEFAULT.decode(in);
    }
    
    /**
     * @see HexDecoder#decode(String,OutputStream)
     */
    public static void decode(String in, OutputStream out) throws ParseException, IOException {
        StandardHexCodecs.DEFAULT.decode(in, out);
    }    
    
    /**
     * @see HexDecoder#decode(Reader,OutputStream)
     */
    public static void decode(Reader in, OutputStream out) throws ParseException, IOException {
        StandardHexCodecs.DEFAULT.decode(in, out);
    }
        
    static String of(int i) {
        return String.format("%02x", i & 0xff);
    }
}
