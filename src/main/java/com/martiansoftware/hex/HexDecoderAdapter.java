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
import java.io.OutputStream;
import java.io.Reader;
import java.text.ParseException;

/**
 * A base class for easily creating custom HexDecoders.  Subclasses must
 * provide a Strategy used to determine if characters between bytes should
 * be ignored by the hex parser and to handle decoding lifecycle events.
 * 
 * The hex parser does not support bytes with characters between the first and 
 * second nybble of their hex representation.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public abstract class HexDecoderAdapter implements HexDecoder {

    private static final int CR = '\r';
    private static final int LF = '\n';
    
    private enum State { NEEDS_DIGIT_1, NEEDS_DIGIT_2 }

    /**
     * Parses a character sequence into a byte array with behavior determined by
     * a specified ParserHints.
     * 
     * Parsing follows a simple state machine:
     * 
     * 
     *                 +---------------------------------------------------+
     *                 v                                                   |
     *          +------+------+        +-------------+        +---------+  |
     *      +-->|Needs Digit 1|+------>|Needs Digit 2|+------>|Emit Byte|+-+
     *      |   +-------------+  hex?  +-------------+  hex?  +---------+
     *      |       +  +  +                    +
     *      |       |  |  |                    |eof || !hex?
     *      +-------+  |  |                    v
     *       ignore?   |  |                 +-------------+
     *                 |  +---------------->|ParseExeption|
     *             eof?|   !hex && !ignore? +-------------+
     *                 v
     *               +---+
     *               |End|
     *               +---+ 
     * 
     * 
     */
    @Override
    public void decode(Reader in, OutputStream out) throws IOException, ParseException {
        Strategy o = newStrategy();
        o.start(out);
        
        State state = State.NEEDS_DIGIT_1;
        
        long charIndexInStream = -1, charIndexInLine = 0, totalBytes = 0;
        int b = 0;
        outer:
        while(true) {
            int c = in.read();
            ++charIndexInStream;

            if (c == CR || c == LF) {
                charIndexInLine = 0;
            } else {
                ++charIndexInLine;
            }
            
            switch(state) {
                case NEEDS_DIGIT_1: if (c == -1) break outer;
                                    if (o.shouldIgnore((char) c, charIndexInStream, charIndexInLine, out)) {
                                        continue;                
                                    }
                                    b = 16 * valueOf((char) c, charIndexInStream);
                                    state = State.NEEDS_DIGIT_2;
                                    break;
                                    
                case NEEDS_DIGIT_2: if (c == -1) {
                                        throw new ParseException(String.format("unexpected eof at position %d", charIndexInStream), (int) charIndexInStream);
                                    }
                                    b += valueOf((char) c, charIndexInStream);
                                    out.write(b);
                                    state = State.NEEDS_DIGIT_1;
                                    break;
            }            
        }
        o.finish(charIndexInStream, totalBytes, out);
    }
    
    protected int valueOf(char c, long index) throws ParseException {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        throw new ParseException(String.format("invalid character '%c' at position %d", c, index), (int) index);
    }    
    
    /**
     * Subclasses should implement this method to provide a reactive receiver
     * for data.
     * 
     * @return a reactive receiver for data
     */
    protected abstract Strategy newStrategy();
    
    /**
     * A simple strategy for reactively encoding data.  Lifecycle is:
     * 
     * <ul>
     * <li>start() is always called first, exactly once</li>
     * <li>shouldIgnore() is then called zero or more times</li>
     * <li>finish() is then called once if no errors have occurred</li>
     * </ul>
     */
    public static interface Strategy {
        
        /**
         * Called before any bytes are processed
         * @param out the decoding destination
         * @throws IOException 
         */
        public void start(OutputStream out) throws IOException;
        
        /**
         * Called for each character between bytes to determine if that
         * character should be interpreted as the beginning of the next byte.
         * 
         * @param c the character to examine
         * @param charIndexInStream the index of this character in the overall
         * stream being processed, starting at zero.
         * @param charIndexInLine the index of this character in the current
         * line of output being processed, reset to zero after each CR or LF.
         * @param out the decoding destination
         * @return true if this character should be ignored by the hex parser
         * @throws ParseException
         * @throws IOException 
         */
        public boolean shouldIgnore(char c, long charIndexInStream, long charIndexInLine, OutputStream out) throws ParseException, IOException;
        
        /**
         * Called at the end of a successful decoding of a stream of characters
         * @param totalChars the total number of characters read
         * @param totalBytes the total number of bytes decoded
         * @param out the decoding destination
         * @throws ParseException
         * @throws IOException 
         */
        public void finish(long totalChars, long totalBytes, OutputStream out) throws ParseException, IOException;
    }
    
}
