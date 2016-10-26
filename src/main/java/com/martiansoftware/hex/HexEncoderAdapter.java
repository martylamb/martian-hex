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
import java.io.PrintWriter;

/**
 * A base class for easily creating custom HexEncoders.  Subclasses must
 * provide a Strategy used to encode individual bytes and handle encoding
 * lifecycle events.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public abstract class HexEncoderAdapter implements HexEncoder {
    
    @Override
    public void encode(InputStream in, PrintWriter out) throws IOException {
        Strategy h = newStrategy();
        h.start(out);
        int b, count = 0;
        while ((b = in.read()) >= 0) {
            h.next((byte) b, count++, out);
        }
        h.finish(count, out);        
    }

    /**
     * Subclasses should implement this method to provide a reactive receiver
     * for data.
     * 
     * @return a reactive receiver for data
     */
    protected abstract Strategy newStrategy();
    
    /**
     * A simple handler for reactively encoding data.  Lifecycle is:
     * 
     * <ul>
     * <li>start() is always called first, exactly once</li>
     * <li>next() is then called zero or more times</li>
     * <li>finish() is then called if no errors have occurred</li>
     * </ul>
     */
    public static interface Strategy {
        
        /**
         * Called before handling the first byte
         * @param out the encoding destination
         */
        public void start(PrintWriter out);
        
        /**
         * Called for each byte to encode until the end of data is reached
         * or an error occurs
         * @param b the byte to encode
         * @param byteIndexInStream the index of the byte in the stream (0 for first byte,
         * 1 for second, etc.)
         * @param out the encoding destination
         */
        public void next(int b, long byteIndexInStream, PrintWriter out);
        
        /**
         * Called after successfully encoding the stream.  Not called
         * if an error is encountered.
         * @param totalBytes the number of bytes encoded
         * @param out the encoding destination
         */
        public void finish(long totalBytes, PrintWriter out);
    }
    
}
