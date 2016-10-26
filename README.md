# hex

Simple tools for encoding binary to hexadecimal and hexadecimal to binary.

Separate HexEncoder (for binary to hex) and HexDecoder (hex to binary) interfaces are provided, as well as HexCodec (both directions). 

Several implementations are provided supporting several different hex formats, plus adapters for easily defining your own.  Predefined `HexCodecs` can be found in [StandardHexCodecs.java](src/main/java/com/martiansoftware/hex/StandardHexCodecs.java).

Static methods provided by [Hex.java](src/main/java/com/martiansoftware/hex/Hex.java) provide quick access to the `StandardHexCodecs.DEFAULT` `HexCodec`.

## Encoding and Decoding

Encoding methods accept binary via `byte` arrays and `InputStreams` and can return `Strings` or encode directly to `PrintStreams` or `PrintWriters`.  See [HexEncoder.java](src/main/java/com/martiansoftware/hex/HexEncoder.java) for all encoding options.

Decoding methods accept hexadecimal in `Strings` or `Readers` and can return `byte` arrays or decode directly to `OutputStreams`.   See [HexDecoder.java](src/main/java/com/martiansoftware/hex/HexDecoder.java) for all decoding options.


## Usage Examples

###Bytes to Simple Hex
```java
// byte array to string
byte[] b = "Here is an example.".getBytes();
System.out.println(Hex.encode(b)); // uses static method; could also call StandardHexCodecs.DEFAULT.encode or create a new DefaultHexEncoder
```

Outputs:
<code>
4865726520697320616e206578616d706c652e
</code>

###Simple Hex to Bytes
```java
byte[] b = Hex.decode("4865726520697320616e206578616d706c652e");
System.out.println(new String(b)); // uses static method; could also call StandardHexCodecs.DEFAULT.decode or create a new DefaultHexDecoder
```

Outputs:
<code>
Here is an example.
</code>

###Delimited by Colons, 16 bytes per line
```java
String s = "Here is an example.";
StandardHexCodecs.SIMPLE.encode(s.getBytes(), System.out); // the same HexCodec can decode this back to binary as well
```
Outputs:
<code>
48:65:72:65:20:69:73:20:61:6e:20:65:78:61:6d:70
6c:65:2e
</code>

###Hex Dump
```java
byte[] b = new byte[67];
Random r = new Random();
r.nextBytes(b);
StandardHexCodecs.HEXDUMP.encode(b, System.out); // the same HexCodec can decode this back to binary as well
```
Outputs:
```none
00000000: c9 85 90 e5 3c ca 13 72   82 52 5b c9 39 92 4b 41   ....<..r.R[.9.KA
00000010: b9 a8 f8 9a e9 53 0d a3   18 84 2e c0 8a 80 b5 aa   .....S..........
00000020: af b5 cf b5 25 2a 36 c3   52 ff b1 f2 b4 ae 13 33   ....%*6.R......3
00000030: bf 33 8d ed 17 9c 62 a0   91 80 fc 67 d0 8e 5d 6f   .3....b....g..]o
00000040: 8f af 85    
```