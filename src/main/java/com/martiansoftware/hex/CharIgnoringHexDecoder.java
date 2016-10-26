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
import java.text.ParseException;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A HexDecoder that is parameterized on which characters should be ignored
 * between bytes.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class CharIgnoringHexDecoder extends HexDecoderAdapter {

    private final Predicate<Character> _shouldIgnore;
    
    /**
     * Creates a new CharIgnoringHexDecoder that will ignore (between bytes)
     * any characters in the specified String
     * 
     * @param charsToIgnore the characters to ignore between bytes
     */
    public CharIgnoringHexDecoder(String charsToIgnore) {
        this(charsToIgnore.toCharArray());
    }
    
    /**
     * Creates a new CharIgnoringHexDecoder that will ignore (between bytes)
     * any characters in the specified array
     * 
     * @param charsToIgnore the characters to ignore between bytes
     */
    public CharIgnoringHexDecoder(char[] charsToIgnore) {
        Set<Character> toIgnore = new java.util.HashSet<>();
        for (char c : charsToIgnore) toIgnore.add(c);
        _shouldIgnore = c -> toIgnore.contains(c);
    }

    /**
     * Creates a new CharIgnoringHexDecoder that will ignore (between bytes)
     * any characters for which the specified Predicate returns true
     * 
     * @param shouldIgnore a `Predicate` that determine which characters should
     * be ignored between bytes
     */
    public CharIgnoringHexDecoder(Predicate<Character> shouldIgnore) {
        _shouldIgnore = shouldIgnore;
    }
    
    @Override
    protected Strategy newStrategy() {
        return new CharIgnoringStrategy();
    }

    private class CharIgnoringStrategy implements Strategy {
        @Override public void start(OutputStream out) throws IOException {}

        @Override
        public boolean shouldIgnore(char c, long charIndexInStream, long charIndexInLine, OutputStream out) throws ParseException, IOException {
            return _shouldIgnore.test(c);
        }

        @Override public void finish(long totalChars, long totalBytes, OutputStream out) throws ParseException, IOException {}        
    }
}
