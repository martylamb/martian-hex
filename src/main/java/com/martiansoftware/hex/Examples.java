/*
 * Copyright 2016 mlamb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.martiansoftware.hex;

import java.util.Random;

/**
 *
 * @author mlamb
 */
public class Examples {
    
    public static void main(String[] args) throws Exception {
        
        byte[] b = new byte[67];
        Random r = new Random();
        r.nextBytes(b);
        StandardHexCodecs.HEXDUMP.encode(b, System.out); // can decode this as well
    }
}
