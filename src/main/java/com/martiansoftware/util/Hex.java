package com.martiansoftware.util;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 * A collection of utilities for working with binary data in hexadecimal format.
 * 
 * @author mlamb
 */
public class Hex {

    /**
     * ParserHints that will only allow parsing of a continuous stream of hex characters
     */
    public static final ParserHints PARSE_STRICT = new ParserHints() {
        public boolean isByteSeparator(char c) { return false; }
    };
    
    /**
     * ParserHints that will allow hex bytes to be separated by any amount of whitespace (including none)
     */
    public static final ParserHints PARSE_WITH_WHITESPACE_SEPARATORS = new SimpleParserHints().withWhitespace();
    
    /**
     * ParserHints that will allow hex bytes to be separated by any amount or combination of whitespace and the characters ":-_.[](){}xX" (including none)
     */
    public static final ParserHints PARSE_WITH_COMMON_SEPARATORS = new SimpleParserHints().withWhitespace().withSeparatorChars(":-_.[](){}xX");
    
    /**
     * ParserHints that will allow hex bytes to be separated by any number of non-hex characters
     */
    public static final ParserHints PARSE_PERMISSIVE = new ParserHints() {
        public boolean isByteSeparator(char c) {
            return valueOf(c) < 0;
        }
    };

    /**
     * ParserHints allow tuning of hex parsing
     */
    public abstract static class ParserHints {
        
        /**
         * Indicates whether the specified character may appear between bytes
         * in the hexadecimal string being parsed.  Note that the parser will
         * allow ANY NUMBER OF CONSECUTIVE BYTE SEPARATORS, including none.
         * 
         * Note that NO non-hex characters may appear between the two hex
         * characters making up a byte.
         * 
         * @param c the character to test
         * @return true iff c is a valid byte separator character.
         */
        public abstract boolean isByteSeparator(char c);
    }
    
    /**
     * Returns the decimal value of the specified single hex character, or -1 
     * if the character is not hex.
     * 
     * @param c
     */
    private static int valueOf(char c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'D') return c - 'A' + 10;
        return -1; // not a hex character
    }
    
    /**
     * Parses a character sequence into a byte array using the PARSE_STRICT ParserHints
     * @throws ParseException
     */
    public static byte[] fromChars(CharSequence cs) throws ParseException {
        return fromChars(cs, 0, cs.length(), PARSE_STRICT);
    }
    
    /**
     * Parses a character sequence into a byte array using the specified ParserHints
     * @throws ParseException 
     */
    public static byte[] fromChars(CharSequence cs, ParserHints cfg) throws ParseException {
        return fromChars(cs, 0, cs.length(), cfg);
    }

    /**
     * Parses a subsequence of a character sequence into a byte array using the PARSE_STRICT ParserHints
     * @throws ParseException 
     */
    public static byte[] fromChars(CharSequence cs, int offset, int len) throws ParseException {
        return fromChars(cs, offset, len, PARSE_STRICT);
    }
            
    /**
     * Parses a character sequence into a byte array with behavior determined by
     * a specified ParserHints.
     * 
     * Parsing follows a simple state machine:
     * <pre>
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
     * </pre>
     * @param cs the character sequence to parse into a byte array
     * @param offset the index of the first character in the sequence to parse
     * @param len the number of characters in the sequence to parse
     * @param cfg the ParserHints used to tune behavior.  If null, PARSE_STRICT will be used.
     * @throws ParseException
     * @return the resulting byte array
     */
    public static byte[] fromChars(CharSequence cs, int offset, int len, ParserHints cfg) throws ParseException {
        if (cfg == null) cfg = PARSE_STRICT;
        ByteBuffer buf = ByteBuffer.wrap(new byte[len / 2]); // max number of bytes resulting is len/2 if no chars are ignored
        
        int i = offset;         // index of char we're looking at
        char c;                 // char we're looking at
        int eof = offset + len; // index considered end of char stream
        int digitValue;         // hex value of most-recently-examined character (-1 if not hex)
        int thisByte;           // the current byte value we are parsing (possibly in-progress if only one digit read so far)

        // the three states in the diagram above "Needs Digit 1", "Needs Digit 2" and "Emit Byte" are the "happy path"
        // through the code.  The other two states ("End" and "ParseException") are both terminal, either returning
        // from the method or throwing an exception.
        while(true) {
            // state = Needs Digit 1
            if (i >= eof) break; // all done!
            c = cs.charAt(i++);
            if (cfg.isByteSeparator(c)) continue;
            thisByte = valueOf(c) * 16; // TODO: what if they allow a hex digit as a separator character?  THAT WOULD BE STUPID
            if (thisByte < 0) throw new ParseException("Invalid character '" + c + "' at position " + (i - 1), i - 1);

            // state = Needs Digit 2
            if (i >= eof) throw new ParseException("Unexpected EOF at position " + i, i);
            c = cs.charAt(i++);
            digitValue = valueOf(c);
            if (digitValue < 0) throw new ParseException("Invalid character '" + c + "' at position " + (i - 1), i - 1);
            thisByte += digitValue;

            // state = Emit Byte
            buf.put((byte) thisByte);
        }

        // state = End
        buf.flip();
        byte[] result = new byte[buf.remaining()];
        buf.get(result);
        return result;
    }

    public static String hex(byte[] b, int offset, int len, String separator) {
        int seplen = (separator == null) ? 0 : separator.length();
        int slen = b.length * 2; // account for hex digits
        if (b.length > 1) slen += (b.length - 1) * seplen; // account for separator
        StringBuilder s = new StringBuilder(slen);
        for (int i = offset; i < offset + len; ++i) {
            if (i > offset && seplen > 0) s.append(separator);
            s.append(String.format("%02x", b[i]));
        }
        return s.toString();
    }
    
    
    /**
     * Returns an Iterator of Strings, each of which contains one line of a formatted
     * hex dump of the specified data.  len bytes of b, beginning at index offset, are
     * formatter for display. 
     * 
     * @param b the array containing data to dump
     * @param offset the index of the first byte to dump
     * @param len the number of bytes to dump
     * @param startAddress the address to associated with the first byte (for display purposes only)
     * @return an Iterator of Strings, each of which contains one line of a formatted
     * hex dump of the specified data.
     */
    public static Iterable<String> dump(byte[] b, int offset, int len, long startAddress) {
        return new Dumper(b, offset, len, startAddress);
        
    }
    
    public static Iterable<String> dump(byte[] b) {
        return dump(b, 0, b.length, 0);
    }
    
    public static Iterable<String> dump(byte[] b, int offset, int len) {
        return dump(b, offset, len, offset);
    }
    
    /**
     * A helper class for implementing ParserHints
     */
    public static class SimpleParserHints extends ParserHints {
        
        /**
         * Should whitespace characters be allowed as byte separators?  
         */
        protected final boolean _withWS;
        protected final Set<Character> _separators;
        
        /**
         * Create a new SimpleParserHints that doesn't allow any byte separators
         */
        public SimpleParserHints() {
            this(false, null);
        }
        
        /**
         * Create a new SimpleParserHints
         * @param withWS if true, whitespace characters (as determined by Character.isWhitespace() may be used as byte separators
         * @param separators other permissible byte separator characters (may be null)
         */
        private SimpleParserHints(boolean withWS, Set<Character> separators) {
            _withWS = withWS;
            _separators = separators == null ? null : Collections.unmodifiableSet(separators);
        }
        
        public boolean isByteSeparator(char c) {
            if (_withWS && Character.isWhitespace(c)) return true;
            if (_separators != null && _separators.contains(c)) return true;
            return false;
        }
        
        /**
         * Creates ane returns a new SimpleParserHints that allows whitespace as byte separators and retains any previously set separator chars.
         * @return
         */
        public SimpleParserHints withWhitespace() {
            return _withWS ? this : new SimpleParserHints(true, _separators);
        }
        
        /**
         * Creates and returns a new SimpleParserHints that adds the specified separator chars to any previously set, and retains the whitespace setting.
         * @return
         */
        public SimpleParserHints withSeparatorChars(String s) {
            if (s == null || s.length() == 0) return this;
            Set<Character> tmpIgnoreChars = new java.util.HashSet<Character>();
            if (_separators != null) tmpIgnoreChars.addAll(_separators);
            for (char c : s.toCharArray()) tmpIgnoreChars.add(c);
            return new SimpleParserHints(_withWS, tmpIgnoreChars);
        }
    }
    
    private static class Dumper implements Iterable<String>, Iterator<String> {

        private final byte[] _b;
        private final int _offset, _len;
        private final long _startAddress;
        
        private int _end; // one more than last byte index to output
        private int _i; // next array index to consider for display
        private long _nextAddress;
        
        public Dumper(byte[] b, int offset, int len, long startAddress) {
            _b = b;
            _offset = offset;
            _len = len;
            _startAddress = startAddress;
            
            _end = _offset + _len;
            _i = offset;
            
            int skip = (int) (startAddress % 16); // how many bytes to skip at beginning of display because address lines are 16-byte aligned?
            _nextAddress = _startAddress - skip; // address lines are 16-byte aligned
            _i = _offset - skip;            
        }
        
        public Iterator<String> iterator() {
            return this;
        }

        public boolean hasNext() {
            return _i < _end;
        }

        public String next() {
            if (_i >= _end) throw new NoSuchElementException();
            String result = String.format("_i=%d, _nextAddress=%d\n", _i, _nextAddress);
            _nextAddress += 16;
            _i += 16;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException("remove() is not supported.");
        }        
    }
    
    public static void main(String[] args) throws Exception {
            byte[] b = new byte[127];
            Random r = new Random();
            r.nextBytes(b);
            
            for (String s : Hex.dump(b, 3, 17, 5)) System.out.println(s);
            
    }
}
