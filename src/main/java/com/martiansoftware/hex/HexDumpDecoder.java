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

/**
 * A HexDecoder capable of decoding the output of a HexDumpEncoder (which
 * contains addresses and ASCII representations that HexDumpDecoder ignores).
 * 
 * An example of the format this HexDecoder can decode:
 * 
 * ```
 *   00000000: bc 1d ef d7 4c 92 37 1a   fd 21 a7 ca 4a fa 75 b6   ....L.7..!..J.u.
 *   00000010: c6 d4 22 e6 71 85 bf a3   00 53 61 f0 92 92 e2 08   ..".q....Sa.....
 *   00000020: f1 b3 dc 59 53 ad ea a3   88 ed 2a ed c7 8d 98 17   ...YS.....*.....
 *   00000030: 46 7b 52 f8 71 cf 91 22   34 a6 53 22 a7 4f 75 1e   F{R.q.."4.S".Ou.
 * ```
 * 
 * @author <a href="http://martylamb.com">Marty Lamb</a>
 */
public class HexDumpDecoder extends HexDecoderAdapter {

    @Override
    protected Strategy newStrategy() {
        return new DumpStrategy();
    }
    
    private class DumpStrategy implements Strategy {
        @Override
        public void start(OutputStream out) throws IOException {
        }

        @Override
        public boolean shouldIgnore(char c, long charIndexInStream, long charIndexInLine, OutputStream out) throws IOException {
            return (charIndexInLine < 10
                    || charIndexInLine > 58
                    || Character.isWhitespace(c));
        }

        @Override
        public void finish(long totalChars, long totalBytes, OutputStream out) throws IOException {
        }
        
    }
}
