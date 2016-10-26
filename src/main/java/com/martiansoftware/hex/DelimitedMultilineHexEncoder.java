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

import com.martiansoftware.validation.Hope;
import java.io.PrintWriter;

/**
 * A HexEncoder that can output multiple lines and add separators between bytes
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class DelimitedMultilineHexEncoder extends HexEncoderAdapter {

    private final int _bytesPerLine;
    private final String _separator;
    
    /**
     * Creates a new DelimitedHexEncoder with no delimiters that outputs all
     * encoded hex on a single line (equivalent to DefaultHexEncoder)
     */
    public DelimitedMultilineHexEncoder() {
        this(Integer.MAX_VALUE, null);
    }
    
    /**
     * Creates a new DelimitedHexEcoder with no delimiters that outputs the
     * specified maximum number of bytes per line out of output
     * 
     * @param bytesPerLine the maximum number of bytes to encode to a single
     * line of output
     */
    public DelimitedMultilineHexEncoder(int bytesPerLine) {
        this(bytesPerLine, null);
    }
    
    /**
     * Creates a new DelimitedHexEncoder that separates consecutive bytes with
     * the specified separator, and outputs all encoded hex to a single line
     * @param separator the separator to output between bytes (may be null)
     */
    public DelimitedMultilineHexEncoder(String separator) {
        this(Integer.MAX_VALUE, separator);
    }
    
    /**
     * Creates a new DelimitedHexEcoder with no delimiters that outputs the specified
     * maximum number of bytes per line out of output and separates consecutive
     * bytes with the specified separator
     * 
     * @param bytesPerLine the maximum number of bytes to encode to a single
     * line of output
     * @param separator the separator to output between bytes (may be null)
     */
    public DelimitedMultilineHexEncoder(int bytesPerLine, String separator) {
        _bytesPerLine = Hope.that(bytesPerLine)
                            .named("bytesPerLine")
                            .isTrue(b -> b > 0, "bytesPerLine must be greater than zero")
                            .value();
        _separator = separator;
    }   
    
    @Override
    protected Strategy newStrategy() {
        return new SimpleByteHandler();
    }
    
    private class SimpleByteHandler implements Strategy {
        @Override
        public void start(PrintWriter p) {}

        private void writeSeparator(PrintWriter w) {
            if (_separator != null) w.print(_separator);
        }
        
        @Override
        public void next(int b, long byteIndexInStream, PrintWriter p) {
            if (byteIndexInStream % _bytesPerLine == 0) {
                if (byteIndexInStream > 0) p.println();
            } else {
                writeSeparator(p);
            }
            p.print(Hex.of(b));
        }

        @Override
        public void finish(long totalBytes, PrintWriter p) {}
    }
}
