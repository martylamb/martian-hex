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

import java.io.PrintWriter;

/**
 * A HexEncoder that provides a formatted "hex dump" of the bytes provided,
 * including addresses within the stream and the bytes' ASCII representation.
 * 
 * An example of the format this HexEncoder can encode:
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
public class HexDumpEncoder extends HexEncoderAdapter {

    @Override
    protected Strategy newStrategy() {
        return new DumpStrategy();
    }
    
    //           1         2         3         4         5         6         7     
    //  12345678901234567890123456789012345678901234567890123456789012345678901234567890
    //  12340000: 57 69 6B 69 70 65 64 69   61 2C 20 74 68 65 20 66   Wikipedia, the f
    
    private class DumpStrategy implements Strategy {

        private long _address = 0;
        private final StringBuilder _buf = new StringBuilder();
        private final StringBuilder _asciiBuf = new StringBuilder();
        
        @Override
        public void start(PrintWriter out) {
        }

        private void flush(PrintWriter out) {
            if (_address > 0) out.println();
            while (_buf.length() < 62) _buf.append(' ');
            while (_asciiBuf.length() < 16) _asciiBuf.append(' ');
            out.print(_buf.toString());
            out.print(_asciiBuf.toString());
            out.flush();
            _address += 16;
            _buf.setLength(0);
            _asciiBuf.setLength(0);
        }

        private char ascii(int b) {
            if (b < 0x20 || b > 0x7e) return '.';
            return (char) b;
        }
        
        @Override
        public void next(int b, long byteIndexInStream, PrintWriter out) {
            long indexInLine = byteIndexInStream % 16;
            if (indexInLine == 0) {
                if (byteIndexInStream > 0) flush(out);
                _buf.append(String.format("%08x: ", _address));
            }
            if (indexInLine == 8) _buf.append("  ");
            _buf.append(Hex.of(b));
            _buf.append(' ');
            _asciiBuf.append(ascii(b));
        }

        @Override
        public void finish(long totalBytes, PrintWriter out) {
            if (_asciiBuf.length() > 0) flush(out);
        }
        
    }
}
