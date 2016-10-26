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
import java.io.PrintWriter;
import java.io.Reader;
import java.text.ParseException;

/**
 * A HexCodec that delegates all encoding and decoding calls to a HexEncoder
 * and HexDecoder provided during construction.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class CompositeHexCodec implements HexCodec {

    private final HexEncoder _encoder;
    private final HexDecoder _decoder;
    
    /**
     * Creates a new CompositHexCodec that will delegate all encoding and
     * decoding calls to the specified HexEncoder and HexDecoder.
     * @param encoder the HexEncoder to which all encoding calls will be delegated
     * @param decoder the HexDecoder to which all decoding calls will be delegated
     */
    public CompositeHexCodec(HexEncoder encoder, HexDecoder decoder) {
        _encoder = encoder;
        _decoder = decoder;
    }

    @Override
    public void encode(InputStream in, PrintWriter out) throws IOException {
        _encoder.encode(in, out);
    }

    @Override
    public void decode(Reader in, OutputStream out) throws ParseException, IOException {
        _decoder.decode(in, out);
    }
}
