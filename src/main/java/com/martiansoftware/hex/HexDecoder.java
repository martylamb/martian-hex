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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

/**
 * An interface providing hexadecimal-to-binary decoding.  This interface
 * provides multiple methods of providing hexadecimal data and an output destination,
 * but it does not prescribe the specific hexadecimal format.  Different
 * implementations of this interface will provide different decodings.  See the
 * README accompanying this source code for examples.
 * 
 * Implementations only need to implement the
 * `public void decode(Reader in, OutputStream out)` method.
 * 
 * Alternatively, a SimpleBaseHexDecoder class is provided that may allow
 * for simpler implementations of more complex hex decodings such as the
 * one provided by HexDumpDecoder.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */

public interface HexDecoder {

    /**
     * Decodes the specified String to a byte array
     * @param in the String to decode
     * @return the decoded String as a byte array
     * @throws ParseException
     */
    public default byte[] decode(String in) throws ParseException {
        return decode(new StringReader(in));
    }    

    /**
     * Decodes the contents of the specified Reader to a byte array
     * @param in the Reader to read and decode
     * @return the decoded contents of the Reader as a byte array
     * @throws ParseException 
     */
    public default byte[] decode(Reader in) throws ParseException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        try {
            decode(in, b);
        } catch (IOException notExpected) {
            throw new RuntimeException(notExpected);
        }
        return b.toByteArray();
    }
    
    /**
     * Decodes the contents of the specified String to an OutputStream
     * @param in the String to read and decode
     * @param out the destination to which the decoded String will be written
     * @throws ParseException
     * @throws IOException
     */
    public default void decode(String in, OutputStream out) throws ParseException, IOException {
        decode(new StringReader(in), out);
    }
    
    /**
     * Decodes the contents of the specified Reader to an OutputStream.
     * Concrete implementations of this interface must implement this method.
     * @param in the Reader to read and decode
     * @param out the destination to which the decoded Reader contents will be written
     * @throws ParseException
     * @throws IOException
     */    
    public void decode(Reader in, OutputStream out) throws ParseException, IOException;
    
}
