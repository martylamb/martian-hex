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

/**
 * Constant definitions for standard `HexCodecs`
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class StandardHexCodecs {
    
    /**
     * A strict, plain `HexEncoder` and a `HexDecoder` that ignores whitespace and some common hex delimiters
     * @see DefaultHexEncoder
     * @see DefaultHexDecoder
     */
    public static final HexCodec DEFAULT = new CompositeHexCodec(new DefaultHexEncoder(), new DefaultHexDecoder());

    /**
     * A strict, plain `HexEncoder` and a strict `HexDecoder` that ignores no characters
     * @see DefaultHexEncoder
     * @see StrictHexDecoder
     */
    public static final HexCodec STRICT = new CompositeHexCodec(new DefaultHexEncoder(), new StrictHexDecoder());

    /**
     * A `HexCodec` capable of encoding and decoding hex dumps including byte addresses and ASCII representation
     * @see HexDumpEncoder
     * @see HexDumpDecoder
     */
    public static final HexCodec HEXDUMP = new CompositeHexCodec(new HexDumpEncoder(), new HexDumpDecoder());

    /**
     * A `HexEncoder` that outputs 16 bytes per line, delimited by colons, and a `HexDecoder` that ignores whitespace
     * and some common hex delimiters
     * @see DelimitedMultilineHexEncoder
     * @see DefaultHexEncoder
     */
    public static final HexCodec SIMPLE = new CompositeHexCodec(new DelimitedMultilineHexEncoder(16, ":"), new DefaultHexDecoder());
    
}
