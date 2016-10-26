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
 * The simplest encoder possible.  Straight hex, single line, non-delimited.
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class DefaultHexEncoder implements HexEncoder {

    @Override
    public void encode(InputStream in, PrintWriter out) throws IOException {
        int b;
        while ((b = in.read()) >= 0) {
            out.print(Hex.of(b));
        }
        out.flush();
    }
    
}
