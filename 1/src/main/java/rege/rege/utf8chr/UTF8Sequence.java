package rege.rege.utf8chr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.Collections.sort;

/**
 * @author REGE
 * @since 0.0.1a1
 */
public class UTF8Sequence
implements Comparable<UTF8Sequence>, Iterable<UTF8Char> {
    /**
     * Where to store the UTF8 characters.
     */
    private final UTF8Char[] chars;

    /**
     * Line separators from Python, contains {@code \v}, {@code \n},
     * {@code \r}, {@code \r\n}.
     */
    private static final UTF8Sequence[] LINESEPS;
    /**
     * Whitespace characters from Python.
     */
    public static final UTF8Sequence WHITESPACES;
    /**
     * Decimal characters from Python.
     */
    public static final UTF8Sequence DECIMALS;
    /**
     * Digit characters from Python.
     */
    public static final UTF8Sequence DIGITS;
    /**
     * Numeric characters from Python.
     */
    public static final UTF8Sequence NUMERICS;
    private static final Map<UTF8Char, Iterable<UTF8Char>> TOLOWERS;
    private static final Map<UTF8Char, Iterable<UTF8Char>> TOUPPERS;

    /**
     * @param chars Create a new UTF8Sequence with the characters in the array.
     * @throws NullPointerException If the array or one of its element is null.
     */
    public UTF8Sequence(UTF8Char[] chars) {
        this.chars = new UTF8Char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            this.chars[i] = chars[i];
        }
    }

    /**
     * @param chars Create a new UTF8Sequence with the characters in the
     * iterable.
     * @throws NullPointerException If the iterable or one of its next is null.
     */
    public UTF8Sequence(Iterable<UTF8Char> chars) {
        final List<UTF8Char> LIST = new ArrayList<UTF8Char>();
        for (UTF8Char i : chars) {
            LIST.add(i);
        }
        this.chars = new UTF8Char[LIST.size()];
        int j = 0;
        while (!(LIST.isEmpty())) {
            this.chars[j] = LIST.remove(0);
            j++;
        }
    }

    /**
     * @param codepoints Create a new UTF8Sequence with the character
     * codepoints in the array.
     * @throws NullPointerException If the array is null.
     */
    public UTF8Sequence(long[] codepoints) {
        this.chars = new UTF8Char[codepoints.length];
        for (int i = 0; i < codepoints.length; i++) {
            this.chars[i] = new UTF8Char(codepoints[i]);
        }
    }

    /**
     * @param codepoints Create a new UTF8Sequence with the character
     * codepoints in the array.
     * @throws NullPointerException If the array or one of its element is null.
     */
    public UTF8Sequence(Long[] codepoints) {
        this.chars = new UTF8Char[codepoints.length];
        for (int i = 0; i < codepoints.length; i++) {
            this.chars[i] = new UTF8Char(codepoints[i].longValue());
        }
    }

    /**
     * @param string Create a new UTF8Sequence converts from
     * {@link java.lang.String}.
     * @throws NullPointerException If the string is null.
     */
    public UTF8Sequence(String string) {
        final List<UTF8Char> LIST = new ArrayList<UTF8Char>();
        for (int i = 0; true; i++) {
            try {
                LIST.add(new UTF8Char(string.codePointAt(i)));
                if (string.codePointAt(i) > 0xffff) {
                    i++;
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }
        this.chars = new UTF8Char[LIST.size()];
        int j = 0;
        while (!(LIST.isEmpty())) {
            this.chars[j] = LIST.remove(0);
            j++;
        }
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Long.toString(i))}. @see
     * {@link java.lang.Long}, {@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(long i) {
        this(Long.toString(i));
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Integer.toString(i))}.
     * @see {@link java.lang.Integer},{@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(int i) {
        this(Integer.toString(i));
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Short.toString(i))}. @see
     * {@link java.lang.Short}, {@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(short i) {
        this(Short.toString(i));
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Byte.toString(i))}. @see
     * {@link java.lang.Byte}, {@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(byte i) {
        this(Byte.toString(i));
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Float.toString(i))}. @see
     * {@link java.lang.Float}, {@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(float i) {
        this(Float.toString(i));
    }

    /**
     * @param i Equivalent to {@code new UTF8Sequence(Double.toString(i))}.
     * @see {@link java.lang.Double}, {@link #UTF8Sequence(String)}
     */
    public UTF8Sequence(double i) {
        this(Double.toString(i));
    }

    /**
     * @param i Equivalent to
     * {@code new UTF8Sequence(new UTF8Char[]&#123;i&#125;)}. @see
     * {@link #UTF8Sequence(UTF8Char[])}
     * @throws NullPointerException If the character is null.
     * @see UTF8Char
     */
    public UTF8Sequence(UTF8Char i) {
        this(new UTF8Char[]{i});
    }

    /**
     * @param i Equivalent to
     * {@code new UTF8Sequence(new UTF8Char[]&#123;new UTF8Char(i)&#125;)}.
     * @see {@link #UTF8Sequence(UTF8Char[])}
     */
    public UTF8Sequence(char i) {
        this(new UTF8Char[]{new UTF8Char(i)});
    }

    /**
     * Create a new empty UTF8Sequence.
     */
    public UTF8Sequence() {
        this(new long[0]);
    }

    //@Override
    public Iterator<UTF8Char> iterator() {
        List<UTF8Char> LIST = new ArrayList<UTF8Char>();
        for (int i = 0; i < this.chars.length; i++) {
            LIST.add(this.chars[i]);
        }
        return LIST.iterator();
    }

    /**
     * @return An array contains 1-length {@link UTF8Sequence}s.
     * e.g. {@code new UTF8Sequence("test").singles()} will return
     * {@code new UTF8Sequence[]&#123;new UTF8Sequence('t'),
     * new UTF8Sequence('e'), new UTF8Sequence('s'),
     * new UTF8Sequence('t')&#125;}.
     */
    public UTF8Sequence[] singles() {
        UTF8Sequence[] R = new UTF8Sequence[this.chars.length];
        for (int i = 0; i < R.length; i++) {
            R[i] = new UTF8Sequence(this.chars[i]);
        }
        return R;
    }

    /**
     * @return The length of this sequence. e.g.
     * {@code new UTF8Sequence("test").length()} will return {@code 4}.
     */
    public int length() {
        return this.chars.length;
    }

    /**
     * @return The length of this sequence encoded to bytes. e.g.
     * {@code new UTF8Sequence("\u6d4b\u8bd5").length()} will return {@code 6}.
     */
    public long byteLength() {
        long res = 0L;
        for (UTF8Char i : this) {
            res += i.getByteLength();
        }
        return res;
    }

    public List<Byte> flattenBytes() {
        final List<Byte> RES = new ArrayList<Byte>();
        byte[] sl;
        for (UTF8Char i : this) {
            sl = i.toByteArray();
            for (byte j = 0; j < sl.length; j++) {
                RES.add(Byte.valueOf(sl[j]));
            }
        }
        return RES;
    }

    /**
     * @return a byte array of encoded sequence. e.g.
     * {@code new UTF8Sequence("\u6d4b\u8bd5").getBytes()} will return
     * {@code new byte[]&#123;-26, -75, -117, -24, -81, -107&#125;}.
     * @throws IndexOutOfBoundsException if
     * {@code this.byteLength() > 0x7fffffffL}. @see
     * {@link UTF8Sequence#byteLength()}.
     */
    public byte[] getBytes() throws IndexOutOfBoundsException {
        final long LENGTH = this.byteLength();
        if (LENGTH > 0x7fffffffL) {
            throw new IndexOutOfBoundsException();
        }
        final byte[] RES = new byte[(int)LENGTH];
        byte[] sl;
        int index = 0;
        for (UTF8Char i : this) {
            sl = i.toByteArray();
            for (byte j = 0; j < sl.length; j++) {
                RES[index] = sl[j];
                index++;
            }
        }
        return RES;
    }

    /**
     * @param i The index to get, count from 0.
     * @return The character at index {@code i}. If {@code i} is negative, it
     * is equivalent to {@code this.charAt(this.length() - i)}. e.g.
     * {@code new UTF8Sequence("test").charAt(-3)} will return
     * {@code new UTF8Char('e')}.
     * @throws IndexOutOfBoundsException If
     * {@code i >= this.length() || i < -this.length()}. @see
     * {@link UTF8Sequence#byteLength()}.
     */
    public UTF8Char charAt(int i) throws IndexOutOfBoundsException {
        return this.chars[(i < 0) ? this.chars.length + i : i];
    }

    //@Override
    public int compareTo(UTF8Sequence o) {
        final int MINLEN = Math.min(this.length(), o.length());
        int cmp;
        for (int i = 0; i < MINLEN; i++) {
            if ((cmp = this.charAt(i).compareTo(o.charAt(i))) != 0) {
                return cmp;
            }
        }
        return Integer.compare(this.length(), o.length());
    }

    /**
     * @param o Another object.
     * @return A boolean reflects whether two sequences are equal, the
     * regulars are ignored.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof UTF8Sequence) {
            final UTF8Sequence CVT = (UTF8Sequence)o;
            if (CVT.length() != this.length()) {
                return false;
            }
            return this.compareTo(CVT) == 0;
        }
        return false;
    }

    /**
     * @return A boolean reflect whether this sequence is empty.
     */
    public boolean isEmpty() {
        return this.chars.length == 0;
    }

    /**
     * @return A boolean reflect whether this sequence is going to write
     * nothing.
     */
    public boolean isWriteEmpty() {
        for (UTF8Char i : this) {
            if (!(i.equals(UTF8Char.EOF))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.chars);
    }

    @Override
    public String toString() {
        final StringBuilder SB = new StringBuilder();
        boolean fallback = false;
        for (UTF8Char i : this) {
            if (i.ord() == -1L || i.ord() > 0xffffffffL) {
                fallback = true;
                break;
            }
            SB.append(i.toString());
        }
        if (fallback) {
            return this.toEvalString();
        }
        return SB.toString();
    }

    public String toEvalString() {
        final StringBuilder SB = new StringBuilder();
        SB.append("new " + UTF8Sequence.class.getName() + "(new " +
                   UTF8Char.class.getName() + "[]{");
        for (UTF8Char i : this) {
            SB.append(i.toEvalString());
            SB.append(',');
        }
        SB.append("})");
        return SB.toString();
    }

    /**
     * @param seqs The sequences to concatenate to this.
     * @return A UTF8Sequence with combined content, from less index to
     * greater index.
     */
    public UTF8Sequence concat(UTF8Sequence... seqs) {
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            R.add(i);
        }
        for (int i = 0; i < seqs.length; i++) {
            for (UTF8Char j : seqs[i]) {
                R.add(j);
            }
        }
        return new UTF8Sequence(R);
    }

    /**
     * @param value
     * @return A Python-like value of {@code this*value}. e.g.
     * {@code new UTF8Sequence("test").times(3)} will return
     * {@code new UTF8Sequence("testtesttest")}, and
     * {@code new UTF8Sequence("test").times(-3)} will return
     * {@code new UTF8Sequence()}. Unlike {@link #repeat(int)}, it will not
     * throw an exception when {@code value} is negative.
     */
    public UTF8Sequence times(int value) {
        if (value <= 0) {
            return new UTF8Sequence();
        }
        value--;
        final UTF8Sequence[] R = new UTF8Sequence[value];
        for (int i = 0; i < value; i++) {
            R[i] = this;
        }
        return this.concat(R);
    }

    public UTF8Sequence repeat(int count) throws IllegalArgumentException {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " +
                                               Integer.toString(count));
        }
        return this.times(count);
    }

    /**
     * @param start
     * @param stop
     * @param step
     * @return A Python-like value of {@code this[start:stop:step]}, with
     * {@code null = None}. e.g.
     * {@code new UTF8Sequence("test").slice(null, null, -1)} will return
     * {@code new UTF8Sequence("tset")}, and
     * {@code new UTF8Sequence("test").slice(1, -1, null)} will return
     * {@code new UTF8Sequence("es")}.
     * @throws IllegalArgumentException When {@code step.intValue() == 0}.
     */
    public UTF8Sequence slice(Integer start, Integer stop, Integer step)
    throws IllegalArgumentException {
        final int STEP = (step != null) ? step.intValue() : 1;
        if (STEP == 0) {
            throw new IllegalArgumentException("slice step cannot be zero");
        }
        int ts;
        final int NEWLEN;
        final UTF8Char[] R;
        if (STEP < 0) {
            ts = (start == null) ? this.chars.length - 1 : start.intValue();
            if (ts < 0) {
                ts += this.chars.length;
                if (ts < 0) {
                    return new UTF8Sequence();
                }
            }
            if (ts >= this.chars.length) {
                ts = this.chars.length - 1;
            }
            if (stop == null) {
                NEWLEN = (ts + 1) / -STEP + (((ts + 1) % STEP != 0) ? 1 : 0);
            } else {
                if (stop.intValue() < 0) {
                    stop = Integer.valueOf(this.chars.length +stop.intValue());
                    if (stop.intValue() < 0) {
                        return this.slice(start, null, step);
                    }
                }
                if (stop.intValue() >= ts) {
                    return new UTF8Sequence();
                }
                NEWLEN = (ts - stop.intValue()) / -STEP +
                         (((ts - stop.intValue()) % -STEP != 0) ? 1 : 0);
            }
        } else {
            ts = (start == null) ? 0 : start.intValue();
            if (ts < 0) {
                ts += this.chars.length;
                if (ts < 0) {
                    ts = 0;
                }
            }
            if (stop == null) {
                NEWLEN = (this.chars.length - ts) / STEP +
                         (((this.chars.length - ts) % STEP != 0) ? 1 : 0);
            } else {
                if (stop.intValue() < 0) {
                    stop = Integer.valueOf(this.chars.length +stop.intValue());
                    if (stop.intValue() <= ts) {
                        return new UTF8Sequence();
                    }
                }
                if (stop.intValue() >= this.chars.length) {
                    return this.slice(start, null, step);
                }
                NEWLEN = (stop.intValue() - ts) / STEP +
                         (((stop.intValue() - ts) % STEP != 0) ? 1 : 0);
            }
        }
        if (NEWLEN <= 0) {
            return new UTF8Sequence();
        }
        R = new UTF8Char[NEWLEN];
        int p = ts;
        for (int i = 0; i < NEWLEN; i++) {
            R[i] = this.chars[p];
            p += STEP;
        }
        return new UTF8Sequence(R);
    }

    /**
     * @param start
     * @param stop
     * @return @see {@link #slice(Integer, Integer, Integer)}
     */
    public UTF8Sequence slice(Integer start, Integer stop) {
        return this.slice(start, stop, Integer.valueOf(1));
    }

    /**
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return
     * @throws IndexOutOfBoundsException When
     * {@code start > end || start < 0 || end > this.}{@link #length()}.
     */
    public UTF8Sequence subSequence(int start, int end)
    throws IndexOutOfBoundsException {
        if (start > end) {
            throw new IndexOutOfBoundsException();
        }
        final UTF8Char[] NEWS = new UTF8Char[end - start];
        for (int i = start; i < end; i++) {
            NEWS[i - start] = this.chars[i];
        }
        return new UTF8Sequence(NEWS);
    }

    /**
     * @param start @see {@link #subSequence(int, int)}
     * @return Equivalent to {@code this.subSequence(start, this.length())}.
     * @throws IndexOutOfBoundsException When
     * {@code start < 0 || start > this.}{@link #length()}.
     */
    public UTF8Sequence subSequence(int start)throws IndexOutOfBoundsException{
        return this.subSequence(start, this.length());
    }

    public UTF8Sequence join(UTF8Sequence[] seqs) {
        if (seqs.length == 0) {
            return new UTF8Sequence();
        }
        if (seqs.length == 1) {
            return seqs[0];
        }
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        final List<UTF8Char> P = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            P.add(i);
        }
        for (UTF8Char i : seqs[0]) {
            R.add(i);
        }
        for (int i = 1; i < seqs.length; i++) {
            R.addAll(P);
            for (UTF8Char j : seqs[i]) {
                R.add(j);
            }
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence join(Iterable<UTF8Sequence> seqs) {
        final Iterator<UTF8Sequence> ITT = seqs.iterator();
        if (!(ITT.hasNext())) {
            return new UTF8Sequence();
        }
        final UTF8Sequence SINGLE = ITT.next();
        if (!(ITT.hasNext())) {
            return SINGLE;
        }
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        final List<UTF8Char> P = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            P.add(i);
        }
        for (UTF8Char i : SINGLE) {
            R.add(i);
        }
        while (ITT.hasNext()) {
            R.addAll(P);
            for (UTF8Char i : ITT.next()) {
                R.add(i);
            }
        }
        return new UTF8Sequence(R);
    }

    public static UTF8Sequence join(UTF8Sequence self, UTF8Sequence... seqs) {
        return self.join(seqs);
    }

    public static UTF8Sequence
    join(UTF8Sequence self, Iterable<UTF8Sequence> seqs) {
        return self.join(seqs);
    }

    public static UTF8Sequence join(UTF8Char self, UTF8Sequence... seqs) {
        return new UTF8Sequence(self).join(seqs);
    }

    public static UTF8Sequence
    join(UTF8Char self, Iterable<UTF8Sequence> seqs) {
        return new UTF8Sequence(self).join(seqs);
    }

    public static UTF8Sequence decodeFrom(byte[] from, String errors)
    throws IllegalArgumentException {
        byte[] bytes = null;
        int pos = 0;
        boolean malformed;
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        while (true) {
            if (pos >= from.length) {
                return new UTF8Sequence(R);
            }
            malformed = false;
            if (from[pos] >= (byte)0) {
                R.add(new UTF8Char(from[pos]));
                pos++;
            } else if (((byte)-64) <= from[pos] && from[pos] < (byte)-32) {
                if (pos + 1 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1]};
                }
                if (malformed || bytes[1] >= (byte)-64) {
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((0x1fL & (long)(bytes[0])) << 6L) |
                        (0x3fL & (long)(bytes[1])), (byte)2
                    ));
                    pos += 2;
                }
            } else if (((byte)-32) <= from[pos] && from[pos] < (byte)-16) {
                if (pos + 2 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1],from[pos + 2]};
                }
                if (malformed||bytes[1] >= ((byte)-64)||bytes[2] >= (byte)-64){
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((0xfL & (long)(bytes[0])) << 12L) |
                        ((0x3fL & (long)(bytes[1])) << 6L) |
                        (0x3fL & (long)(bytes[2])), (byte)3
                    ));
                    pos += 3;
                }
            } else if (((byte)-16) <= from[pos] && from[pos] < (byte)-8) {
                if (pos + 3 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1], from[pos + 2],
                                       from[pos + 3]};
                }
                if (malformed || bytes[1] >= ((byte)-64) ||
                    bytes[2] >= ((byte)-64) || bytes[3] >= (byte)-64) {
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((0x7L & (long)(bytes[0])) << 18L) |
                        ((0x3fL & (long)(bytes[1])) << 12L) |
                        ((0x3fL & (long)(bytes[2])) << 6L) |
                        (0x3fL & (long)(bytes[3])), (byte)4
                    ));
                    pos += 4;
                }
            } else if (((byte)-8) <= from[pos] && from[pos] < (byte)-4) {
                if (pos + 4 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1], from[pos + 2],
                                       from[pos + 3], from[pos + 4]};
                }
                if (malformed || bytes[1] >= ((byte)-64) ||
                    bytes[2] >= ((byte)-64) || bytes[3] >= ((byte)-64) ||
                    bytes[4] >= (byte)-64) {
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((0x3L & (long)(bytes[0])) << 24L) |
                        ((0x3fL & (long)(bytes[1])) << 18L) |
                        ((0x3fL & (long)(bytes[2])) << 12L) |
                        ((0x3fL & (long)(bytes[3])) << 6L) |
                        (0x3fL & (long)(bytes[4])), (byte)5
                    ));
                    pos += 5;
                }
            } else if (((byte)-4) == from[pos] || from[pos] == (byte)-3) {
                if (pos + 5 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1], from[pos + 2],
                                       from[pos + 3], from[pos + 4],
                                       from[pos + 5]};
                }
                if (malformed || bytes[1] >= ((byte)-64) ||
                    bytes[2] >= ((byte)-64) || bytes[3] >= ((byte)-64) ||
                    bytes[4] >= ((byte)-64) || bytes[5] >= (byte)-64) {
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((1L & (long)(bytes[0])) << 30L) |
                        ((0x3fL & (long)(bytes[1])) << 24L) |
                        ((0x3fL & (long)(bytes[2])) << 18L) |
                        ((0x3fL & (long)(bytes[3])) << 12L) |
                        ((0x3fL & (long)(bytes[4])) << 6L) |
                        (0x3fL & (long)(bytes[5])), (byte)6
                    ));
                    pos += 6;
                }
            } else if (from[pos] == (byte)-2) {
                if (pos + 6 >= from.length) {
                    malformed = true;
                } else {
                    bytes = new byte[]{from[pos], from[pos + 1], from[pos + 2],
                                       from[pos + 3], from[pos + 4],
                                       from[pos + 5], from[pos + 6]};
                }
                if (malformed || bytes[1] >= ((byte)-64) ||
                    bytes[2] >= ((byte)-64) || bytes[3] >= ((byte)-64) ||
                    bytes[4] >= ((byte)-64) || bytes[5] >= ((byte)-64) ||
                    bytes[6] >= (byte)-64) {
                    if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))){
                        throw new IllegalArgumentException(
                            "unknown error handler name '" + errors + "'"
                        );
                    }
                    if (malformed) {
                        bytes = new byte[from.length - pos];
                        for (int i = 0; i < bytes.length; i++) {
                            bytes[i] = from[pos + i];
                        }
                    }
                    final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                    UTF8DecodeErrorHandler
                    .HANDLER.get(errors).provide(pos, bytes);
                    for (UTF8Char i : HANDLED.getValue()) {
                        R.add(i);
                    }
                    pos = HANDLED.getKey().intValue();
                } else {
                    R.add(new UTF8Char(
                        ((0x3fL & (long)(bytes[1])) << 30L) |
                        ((0x3fL & (long)(bytes[2])) << 24L) |
                        ((0x3fL & (long)(bytes[3])) << 18L) |
                        ((0x3fL & (long)(bytes[4])) << 12L) |
                        ((0x3fL & (long)(bytes[5])) << 6L) |
                        (0x3fL & (long)(bytes[6])), (byte)7
                    ));
                    pos += 7;
                }
            } else {
                if (!(UTF8DecodeErrorHandler.HANDLER.containsKey(errors))) {
                    throw new IllegalArgumentException(
                        "unknown error handler name '" + errors + "'"
                    );
                }
                final Entry<Integer, Iterable<UTF8Char>> HANDLED =
                UTF8DecodeErrorHandler.HANDLER.get(errors)
                .provide(pos, new byte[]{from[pos]});
                for (UTF8Char i : HANDLED.getValue()) {
                    R.add(i);
                }
                pos = HANDLED.getKey().intValue();
            }
        }
    }

    public static UTF8Sequence decodeFrom(byte[] from)
    throws IllegalArgumentException {
        return decodeFrom(from, "strict");
    }

    public UTF8Sequence unescape() {
        /* 0: Not escaped
         * 1: Met '\'
         * 2: 3-digit octal, digit 1
         * 3: 3-digit octal, digit 2
         * 4: 2-digit hex, digit 0
         * 5: 2-digit hex, digit 1
         * 6: 4-digit hex, digit 0
         * 7: 4-digit hex, digit 1
         * 8: 4-digit hex, digit 2
         * 9: 4-digit hex, digit 3
         * 10: 8-digit hex, digit 0
         * 11: 8-digit hex, digit 1
         * 12: 8-digit hex, digit 2
         * 13: 8-digit hex, digit 3
         * 14: 8-digit hex, digit 4
         * 15: 8-digit hex, digit 5
         * 16: 8-digit hex, digit 6
         * 17: 8-digit hex, digit 7
         */
        byte escapeMode = 0;
        long escapeCodepoint = 0L;
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            if (i.ord() == 92L) { // '\'
                switch (escapeMode) {
                    case 0: {
                        escapeMode = 1;
                        break;
                    }
                    case 1: {
                        escapeMode = 0;
                        R.add(i);
                        break;
                    }
                    case 4: {
                        escapeMode = 1;
                        R.add(i);
                        R.add(new UTF8Char('x'));
                        break;
                    }
                    case 6: {
                        escapeMode = 1;
                        R.add(i);
                        R.add(new UTF8Char('u'));
                        break;
                    }
                    case 10: {
                        escapeMode = 1;
                        R.add(i);
                        R.add(new UTF8Char('U'));
                        break;
                    }
                    case 2:
                    case 3:
                    case 5:
                    case 7:
                    case 8:
                    case 9:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17: {
                        escapeMode = 1;
                        R.add(new UTF8Char(escapeCodepoint));
                        break;
                    }
                    default: assert false : "Unexpected escapeMode " +
                                            Byte.toString(escapeMode);
                }
            } else {
                switch (escapeMode) {
                    case 0: {
                        R.add(i);
                        break;
                    }
                    case 1: {
                        final long ORD = i.ord();
                        if (ORD < 0xffffffffL) {
                            final int ORDINT = (int)ORD;
                            switch (ORDINT) {
                                case 10: { // '\n'
                                    escapeMode = 0;
                                    break;
                                }
                                case 34: { // '"'
                                    escapeMode = 0;
                                    R.add(new UTF8Char('"'));
                                    break;
                                }
                                case 39: { // '\''
                                    escapeMode = 0;
                                    R.add(new UTF8Char('\''));
                                    break;
                                }
                                case 48: // '0'
                                case 49: // '1'
                                case 50: // '2'
                                case 51: // '3'
                                case 52: // '4'
                                case 53: // '5'
                                case 54: // '6'
                                case 55: { // '7'
                                    escapeCodepoint = ORDINT - 48L;
                                    escapeMode = 2;
                                    break;
                                }
                                case 85: { // 'U'
                                    escapeMode = 10;
                                    escapeCodepoint = 0L;
                                    break;
                                }
                                case 97: { // 'a'
                                    escapeMode = 0;
                                    R.add(UTF8Char.BEL);
                                    break;
                                }
                                case 98: { // 'b'
                                    escapeMode = 0;
                                    R.add(UTF8Char.BS);
                                    break;
                                }
                                case 101: { // 'e'
                                    escapeMode = 0;
                                    R.add(UTF8Char.ESC);
                                    break;
                                }
                                case 102: { // 'f'
                                    escapeMode = 0;
                                    R.add(UTF8Char.FF);
                                    break;
                                }
                                case 110: { // 'n'
                                    escapeMode = 0;
                                    R.add(UTF8Char.LF);
                                    break;
                                }
                                case 114: { // 'r'
                                    escapeMode = 0;
                                    R.add(UTF8Char.CR);
                                    break;
                                }
                                case 116: { // 't'
                                    escapeMode = 0;
                                    R.add(UTF8Char.HT);
                                    break;
                                }
                                case 117: { // 'u'
                                    escapeMode = 6;
                                    escapeCodepoint = 0L;
                                    break;
                                }
                                case 118: { // 'v'
                                    escapeMode = 0;
                                    R.add(UTF8Char.VT);
                                    break;
                                }
                                case 120: { // 'x'
                                    escapeMode = 4;
                                    escapeCodepoint = 0L;
                                    break;
                                }
                                default: {
                                    escapeMode = 0;
                                    R.add(new UTF8Char('\\'));
                                    R.add(i);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                    case 2:
                    case 3: {
                        final long ORD = i.ord();
                        if (48L <= ORD && ORD <= 55L) {
                            escapeCodepoint *= 8L;
                            escapeCodepoint += ORD - 48L;
                            if (escapeMode == (byte)3) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            } else {
                                escapeMode = 3;
                            }
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char(escapeCodepoint));
                        }
                        break;
                    }
                    case 4: {
                        final long ORD = i.ord();
                        escapeMode = 5;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint = ORD - 48L;
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint = ORD - 55L;
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint = ORD - 87L;
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char('\\'));
                            R.add(new UTF8Char('x'));
                            R.add(i);
                        }
                        break;
                    }
                    case 5: {
                        final long ORD = i.ord();
                        escapeMode = 0;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint *= 8L;
                            escapeCodepoint += ORD - 48L;
                            R.add(new UTF8Char(escapeCodepoint));
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint *= 8L;
                            escapeCodepoint += ORD - 55L;
                            R.add(new UTF8Char(escapeCodepoint));
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint *= 8L;
                            escapeCodepoint += ORD - 87L;
                            R.add(new UTF8Char(escapeCodepoint));
                        } else {
                            R.add(new UTF8Char(escapeCodepoint));
                            R.add(i);
                        }
                        break;
                    }
                    case 6: {
                        final long ORD = i.ord();
                        escapeMode = 7;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint = ORD - 48L;
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint = ORD - 55L;
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint = ORD - 87L;
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char('\\'));
                            R.add(new UTF8Char('u'));
                            R.add(i);
                        }
                        break;
                    }
                    case 7:
                    case 8:
                    case 9: {
                        final long ORD = i.ord();
                        escapeMode++;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 48L;
                            if (escapeMode == (byte)10) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 55L;
                            if (escapeMode == (byte)10) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 87L;
                            if (escapeMode == (byte)10) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char(escapeCodepoint));
                            R.add(i);
                        }
                        break;
                    }
                    case 10: {
                        final long ORD = i.ord();
                        escapeMode = 11;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint = ORD - 48L;
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint = ORD - 55L;
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint = ORD - 87L;
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char('\\'));
                            R.add(new UTF8Char('U'));
                            R.add(i);
                        }
                        break;
                    }
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17: {
                        final long ORD = i.ord();
                        escapeMode++;
                        if (48L <= ORD && ORD <= 57L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 48L;
                            if (escapeMode == (byte)18) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else if (65L <= ORD && ORD <= 70L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 55L;
                            if (escapeMode == (byte)18) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else if (97L <= ORD && ORD <= 102L) {
                            escapeCodepoint *= 16L;
                            escapeCodepoint += ORD - 87L;
                            if (escapeMode == (byte)18) {
                                escapeMode = 0;
                                R.add(new UTF8Char(escapeCodepoint));
                            }
                        } else {
                            escapeMode = 0;
                            R.add(new UTF8Char(escapeCodepoint));
                            R.add(i);
                        }
                        break;
                    }
                    default: assert false : "Unexpected escapeMode " +
                                            Byte.toString(escapeMode);
                }
            }
        }
        switch (escapeMode) {
            case 0: break;
            case 1: {
                R.add(new UTF8Char('\\'));
                break;
            }
            case 4: {
                R.add(new UTF8Char('\\'));
                R.add(new UTF8Char('x'));
                break;
            }
            case 6: {
                R.add(new UTF8Char('\\'));
                R.add(new UTF8Char('u'));
                break;
            }
            case 10: {
                R.add(new UTF8Char('\\'));
                R.add(new UTF8Char('U'));
                break;
            }
            case 2:
            case 3:
            case 5:
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17: {
                R.add(new UTF8Char(escapeCodepoint));
                break;
            }
            default: assert false : "Unexpected escapeMode " +
                                    Byte.toString(escapeMode);
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence upper() {
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            if (TOUPPERS.containsKey(i)) {
                for (UTF8Char j : TOUPPERS.get(i)) {
                    R.add(j);
                }
            } else {
                R.add(i);
            }
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence lower() {
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            if (TOLOWERS.containsKey(i)) {
                for (UTF8Char j : TOLOWERS.get(i)) {
                    R.add(j);
                }
            } else {
                R.add(i);
            }
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence ljust(int width, UTF8Char fillchar) {
        if (this.chars.length >= width) {
            return this;
        }
        final UTF8Char[] R = new UTF8Char[width];
        for (int i = 0; i < this.chars.length; i++) {
            R[i] = this.charAt(i);
        }
        for (int i = this.chars.length; i < width; i++) {
            R[i] = fillchar;
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence ljust(int width, UTF8Sequence fillchar)
    throws IllegalArgumentException {
        if (fillchar.chars.length != 1) {
            throw new IllegalArgumentException(
                "The fill character must be exactly one character long"
            );
        }
        return this.ljust(width, fillchar.charAt(0));
    }

    public UTF8Sequence ljust(int width) {
        return this.ljust(width, new UTF8Char(' '));
    }

    public UTF8Sequence rjust(int width, UTF8Char fillchar) {
        if (this.chars.length >= width) {
            return this;
        }
        final UTF8Char[] R = new UTF8Char[width];
        final int FW = width - this.chars.length;
        for (int i = 0; i < FW; i++) {
            R[i] = fillchar;
        }
        for (int i = FW; i < width; i++) {
            R[i] = this.charAt(i - FW);
        }
        return new UTF8Sequence(R);
    }

    public UTF8Sequence rjust(int width, UTF8Sequence fillchar)
    throws IllegalArgumentException {
        if (fillchar.chars.length != 1) {
            throw new IllegalArgumentException(
                "The fill character must be exactly one character long"
            );
        }
        return this.rjust(width, fillchar.charAt(0));
    }

    public UTF8Sequence rjust(int width) {
        return this.rjust(width, new UTF8Char(' '));
    }

    public UTF8Sequence format(Object... objects)
    throws IllegalArgumentException {
        // TODO
        return new UTF8Sequence();
    }

    public UTF8Sequence format(Iterable<Object> objects)
    throws IllegalArgumentException {
        // TODO
        return new UTF8Sequence();
    }

    public UTF8Sequence
    format(Iterable<Object> args, Map<UTF8Sequence, Object> kw)
    throws IllegalArgumentException {
        // TODO
        return new UTF8Sequence();
    }

    public static UTF8Sequence format(UTF8Sequence self, Object... objects)
    throws IllegalArgumentException {
        return self.format(objects);
    }

    public int indexOf(UTF8Char sub, int fromIndex) {
        for (int i = fromIndex; i < this.chars.length; i++) {
            if (sub.equals(this.chars[i])) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(UTF8Char sub) {
        return this.indexOf(sub, 0);
    }

    public int indexOf(long sub, int fromIndex) {
        return this.indexOf(new UTF8Char(sub), fromIndex);
    }

    public int indexOf(long sub) {
        return this.indexOf(new UTF8Char(sub), 0);
    }

    public int indexOf(UTF8Sequence sub, int fromIndex) {
        if (sub.isEmpty()) {
            if (this.chars.length - fromIndex >= 0) {
                return fromIndex;
            }
            return -1;
        }
        if (this.isEmpty()) {
            return -1;
        }
        final int CW = this.chars.length - sub.chars.length;
        for (int i = fromIndex; i <= CW; i++) {
            if (sub.equals(this.subSequence(i, i + sub.chars.length))) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(UTF8Sequence sub) {
        return this.indexOf(sub, 0);
    }

    public boolean contains(UTF8Char sub) {
        return this.indexOf(sub, 0) != -1;
    }

    public boolean contains(UTF8Sequence sub) {
        return this.indexOf(sub, 0) != -1;
    }

    public int lastIndexOf(UTF8Char sub, int fromIndex) {
        // TODO
        return -1;
    }

    public int lastIndexOf(UTF8Char sub) {
        return this.lastIndexOf(sub, 0);
    }

    public int lastIndexOf(long sub, int fromIndex) {
        return this.lastIndexOf(new UTF8Char(sub), fromIndex);
    }

    public int lastIndexOf(long sub) {
        return this.lastIndexOf(new UTF8Char(sub), 0);
    }

    public int lastIndexOf(UTF8Sequence sub, int fromIndex) {
        // TODO
        return -1;
    }

    public int lastIndexOf(UTF8Sequence sub) {
        return this.lastIndexOf(sub, 0);
    }

    public int count(UTF8Char sub) {
        int res = 0;
        for (int i = 0; i < this.chars.length; i++) {
            if (sub.equals(this.chars[i])) {
                res++;
            }
        }
        return res;
    }

    public int count(long sub) {
        return this.count(new UTF8Char(sub));
    }

    public int count(UTF8Sequence sub, boolean allowOverlap) {
        int res = 0;
        final int CW = this.chars.length - sub.chars.length;
        for (int i = 0; i <= CW;) {
            if (sub.equals(this.subSequence(i, i + sub.chars.length))) {
                res++;
            }
            if (allowOverlap || sub.chars.length == 0) {
                i++;
            } else {
                i += sub.chars.length;
            }
        }
        return res;
    }

    public int[] indicesOf(UTF8Char sub, int fromIndex) {
        final List<Integer> R = new ArrayList<Integer>();
        for (int i = fromIndex; i < this.chars.length; i++) {
            if (sub.equals(this.chars[i])) {
                R.add(Integer.valueOf(i));
            }
        }
        final int[] RES = new int[R.size()];
        for (int i = 0; i < RES.length; i++) {
            RES[i] = R.get(i).intValue();
        }
        return RES;
    }

    public int[] indicesOf(UTF8Char sub) {
        return this.indicesOf(sub, 0);
    }

    public int[] indicesOf(long sub, int fromIndex) {
        return this.indicesOf(new UTF8Char(sub), fromIndex);
    }

    public int[] indicesOf(long sub) {
        return this.indicesOf(new UTF8Char(sub), 0);
    }

    public int[]
    indicesOf(UTF8Sequence sub, boolean allowOverlap, int fromIndex) {
        if (sub.isEmpty()) {
            if (this.chars.length - fromIndex >= 0) {
                final int[] RES = new int[this.chars.length - fromIndex + 1];
                for (int i = fromIndex; i <= this.chars.length; i++) {
                    RES[i - fromIndex] = i;
                }
            }
            return new int[0];
        }
        if (this.isEmpty()) {
            return new int[0];
        }
        final int CW = this.chars.length - sub.chars.length;
        final List<Integer> R = new ArrayList<Integer>();
        for (int i = fromIndex; i <= CW;) {
            if (sub.equals(this.subSequence(i, i + sub.chars.length))) {
                R.add(Integer.valueOf(i));
            }
            if (allowOverlap || sub.chars.length == 0) {
                i++;
            } else {
                i += sub.chars.length;
            }
        }
        final int[] RES = new int[R.size()];
        for (int i = 0; i < RES.length; i++) {
            RES[i] = R.get(i).intValue();
        }
        return RES;
    }

    public int[]
    indicesOf(UTF8Sequence sub, int fromIndex, boolean allowOverlap) {
        return this.indicesOf(sub, allowOverlap, fromIndex);
    }

    public int[] indicesOf(UTF8Sequence sub, boolean allowOverlap) {
        return this.indicesOf(sub, allowOverlap, 0);
    }

    public UTF8Sequence[] split(UTF8Sequence sep, int maxsplit)
    throws IllegalArgumentException {
        if (sep.isEmpty()) {
            throw new IllegalArgumentException("empty separator");
        }
        final int[] INDICES = this.indicesOf(sep, false);
        if (maxsplit >= 0 && INDICES.length > maxsplit) {
            final UTF8Sequence[] RES = new UTF8Sequence[maxsplit + 1];
            int startIndex = 0;
            for (int i = 0; i < maxsplit; i++) {
                RES[i] = this.subSequence(startIndex, INDICES[i]);
                startIndex = INDICES[i] + sep.chars.length;
            }
            RES[maxsplit] = this.subSequence(startIndex);
            return RES;
        }
        final UTF8Sequence[] RES = new UTF8Sequence[INDICES.length + 1];
        int startIndex = 0;
        for (int i = 0; i < INDICES.length; i++) {
            RES[i] = this.subSequence(startIndex, INDICES[i]);
            startIndex = INDICES[i] + sep.chars.length;
        }
        RES[maxsplit] = this.subSequence(startIndex);
        return RES;
    }

    public UTF8Sequence[] split(UTF8Sequence sep)
    throws IllegalArgumentException {
        return this.split(sep, -1);
    }

    public UTF8Sequence[] split(UTF8Char sep, int maxsplit) {
        final int[] INDICES = this.indicesOf(sep);
        if (maxsplit >= 0 && INDICES.length > maxsplit) {
            final UTF8Sequence[] RES = new UTF8Sequence[maxsplit + 1];
            int startIndex = 0;
            for (int i = 0; i < maxsplit; i++) {
                RES[i] = this.subSequence(startIndex, INDICES[i]);
                startIndex = INDICES[i] + 1;
            }
            RES[maxsplit] = this.subSequence(startIndex);
            return RES;
        }
        final UTF8Sequence[] RES = new UTF8Sequence[INDICES.length + 1];
        int startIndex = 0;
        for (int i = 0; i < INDICES.length; i++) {
            RES[i] = this.subSequence(startIndex, INDICES[i]);
            startIndex = INDICES[i] + 1;
        }
        RES[INDICES.length] = this.subSequence(startIndex);
        return RES;
    }

    public UTF8Sequence[] split(UTF8Char sep) {
        return this.split(sep, -1);
    }

    public UTF8Sequence[] split(int maxsplit) {
        final UTF8Sequence STRIPPED = this.strip();
        final List<Integer> ALL_INDICES = new ArrayList<Integer>();
        for (UTF8Char i : WHITESPACES) {
            final int[] INDICES = STRIPPED.indicesOf(i);
            for (int j = 0; j < INDICES.length; j++) {
                ALL_INDICES.add(Integer.valueOf(INDICES[j]));
            }
        }
        sort(ALL_INDICES);
        if (maxsplit >= 0 && ALL_INDICES.size() > maxsplit) {
            final UTF8Sequence[] RES = new UTF8Sequence[maxsplit + 1];
            int startIndex = 0;
            for (int i = 0; i < maxsplit; i++) {
                RES[i] = STRIPPED.subSequence(startIndex, ALL_INDICES.get(i)
                                                          .intValue());
                startIndex = ALL_INDICES.get(i).intValue() + 1;
            }
            RES[maxsplit] = STRIPPED.subSequence(startIndex);
            return RES;
        }
        final int SIZE = ALL_INDICES.size();
        final UTF8Sequence[] RES = new UTF8Sequence[SIZE + 1];
        int startIndex = 0;
        for (int i = 0; i < SIZE; i++) {
            RES[i] = STRIPPED
                     .subSequence(startIndex, ALL_INDICES.get(i).intValue());
            startIndex = ALL_INDICES.get(i).intValue() + 1;
        }
        RES[SIZE] = STRIPPED.subSequence(startIndex);
        return RES;
    }

    public UTF8Sequence[] split() {
        return this.split(-1);
    }

    public UTF8Sequence[] splitlines(boolean keepends) {
        final Map<Integer, UTF8Sequence> ALL_INDICES =
        new HashMap<Integer, UTF8Sequence>();
        for (int i = 0; i < LINESEPS.length; i++) {
            final int[] INDICES = this.indicesOf(LINESEPS[i], false);
            for (int j = 0; i < INDICES.length; j++) {
                ALL_INDICES.put(Integer.valueOf(INDICES[j]), LINESEPS[i]);
            }
        }
        final int SIZE = ALL_INDICES.size();
        final List<Integer> INDICES = new ArrayList<Integer>();
        INDICES.addAll(ALL_INDICES.keySet());
        sort(INDICES);
        final boolean ENDSEP = INDICES.get(SIZE - 1).intValue() +
                               ALL_INDICES.get(INDICES.get(SIZE - 1))
                               .chars.length == this.chars.length;
        final UTF8Sequence[] RES = new UTF8Sequence[SIZE + (ENDSEP ? 0 : 1)];
        int startIndex = 0;
        for (int i = 0; i < SIZE; i++) {
            RES[i] = this.subSequence(
                startIndex, INDICES.get(i).intValue() +
                            (keepends ?
                             ALL_INDICES.get(INDICES.get(i)).chars.length : 0)
            );
            startIndex = INDICES.get(i).intValue() +
                         ALL_INDICES.get(INDICES.get(i)).chars.length;
        }
        if (!ENDSEP) {
            RES[SIZE] = this.subSequence(startIndex);
        }
        return RES;
    }

    public UTF8Sequence[] splitlines() {
        return this.splitlines(false);
    }

    public UTF8SequencePartition partition(UTF8Sequence sep) {
        final UTF8Sequence[] E = this.split(sep, 1);
        return (E.length == 2) ? new UTF8SequencePartition(E[0], sep, E[1]) :
               new UTF8SequencePartition(this, new UTF8Sequence(),
                                         new UTF8Sequence());
    }

    public UTF8SequencePartition partition(UTF8Char sep) {
        final UTF8Sequence[] E = this.split(sep, 1);
        return (E.length == 2) ?
               new UTF8SequencePartition(E[0], new UTF8Sequence(sep), E[1]) :
               new UTF8SequencePartition(this, new UTF8Sequence(),
                                         new UTF8Sequence());
    }

    public UTF8Sequence lstrip(UTF8Sequence chars) {
        if (chars == null) {
            chars = WHITESPACES;
        }
        int stripIndex = 0;
        while (stripIndex < this.chars.length) {
            if (chars.indexOf(this.chars[stripIndex]) == -1) {
                break;
            }
            stripIndex++;
        }
        return (stripIndex != 0) ? this.subSequence(stripIndex) : this;
    }

    public UTF8Sequence lstrip(UTF8Char chars) {
        if (chars == null) {
            return this.lstrip(WHITESPACES);
        }
        int stripIndex = 0;
        while (stripIndex < this.chars.length) {
            if (!(chars.equals(this.chars[stripIndex]))) {
                break;
            }
            stripIndex++;
        }
        return (stripIndex != 0) ? this.subSequence(stripIndex) : this;
    }

    public UTF8Sequence lstrip() {
        return this.lstrip(WHITESPACES);
    }

    public UTF8Sequence rstrip(UTF8Sequence chars) {
        if (chars == null) {
            chars = WHITESPACES;
        }
        int stripIndex = this.chars.length - 1;
        while (stripIndex >= 0) {
            if (chars.indexOf(this.chars[stripIndex]) == -1) {
                break;
            }
            stripIndex--;
        }
        return (stripIndex != this.chars.length - 1) ?
               this.subSequence(0, stripIndex + 1) : this;
    }

    public UTF8Sequence rstrip(UTF8Char chars) {
        if (chars == null) {
            return this.rstrip(WHITESPACES);
        }
        int stripIndex = this.chars.length - 1;
        while (stripIndex >= 0) {
            if (!(chars.equals(this.chars[stripIndex]))) {
                break;
            }
            stripIndex--;
        }
        return (stripIndex != this.chars.length - 1) ?
               this.subSequence(0, stripIndex + 1) : this;
    }

    public UTF8Sequence rstrip() {
        return this.rstrip(WHITESPACES);
    }

    public UTF8Sequence strip(UTF8Sequence chars) {
        if (chars == null) {
            chars = WHITESPACES;
        }
        int stripIndexStart = 0;
        while (stripIndexStart < this.chars.length) {
            if (chars.indexOf(this.chars[stripIndexStart]) == -1) {
                break;
            }
            stripIndexStart++;
        }
        int stripIndexEnd = this.chars.length - 1;
        while (stripIndexEnd >= 0) {
            if (chars.indexOf(this.chars[stripIndexEnd]) == -1) {
                break;
            }
            stripIndexEnd--;
            if (stripIndexStart >= stripIndexEnd) {
                return new UTF8Sequence();
            }
        }
        return (stripIndexStart != 0 || stripIndexEnd != this.chars.length - 1)
               ? this.subSequence(stripIndexStart, stripIndexEnd + 1) : this;
    }

    public UTF8Sequence strip(UTF8Char chars) {
        if (chars == null) {
            return this.strip(WHITESPACES);
        }
        int stripIndexStart = 0;
        while (stripIndexStart < this.chars.length) {
            if ((chars.equals(this.chars[stripIndexStart]))) {
                break;
            }
            stripIndexStart++;
        }
        int stripIndexEnd = this.chars.length - 1;
        while (stripIndexEnd >= 0) {
            if ((chars.equals(this.chars[stripIndexEnd]))) {
                break;
            }
            stripIndexEnd--;
            if (stripIndexStart >= stripIndexEnd) {
                return new UTF8Sequence();
            }
        }
        return (stripIndexStart != 0 || stripIndexEnd != this.chars.length - 1)
               ? this.subSequence(stripIndexStart, stripIndexEnd + 1) : this;
    }

    public UTF8Sequence strip() {
        return this.strip(WHITESPACES);
    }

    public UTF8Sequence trim() {
        return this.strip(new UTF8Sequence(new long[]{
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
            19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32
        }));
    }

    public boolean isupper() {
        return this.equals(this.upper());
    }

    public boolean islower() {
        return this.equals(this.lower());
    }

    public boolean isdecimal() {
        return this.chars.length != 0 && this.strip(DECIMALS).chars.length ==0;
    }

    public boolean isdigit() {
        return this.chars.length != 0 && this.strip(DIGITS).chars.length == 0;
    }

    public boolean isnumeric() {
        return this.chars.length != 0 && this.strip(NUMERICS).chars.length ==0;
    }

    public UTF8Sequence
    replace(UTF8Sequence old, UTF8Sequence new_, int count) {
        if (count == 0 || old.equals(new_)) {
            return this;
        }
        if (!(old.isEmpty())) {
            return new_.join(this.split(old, count));
        }
        final List<UTF8Char> P = new ArrayList<UTF8Char>();
        for (UTF8Char i : new_) {
            P.add(i);
        }
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            R.addAll(P);
            R.add(i);
            count--;
            if (count <= 0) {
                return new UTF8Sequence(R);
            }
        }
        R.addAll(P);
        return new UTF8Sequence(R);
    }

    public UTF8Sequence replace(UTF8Sequence old, UTF8Sequence new_) {
        return this.replace(old, new_, -1);
    }

    public UTF8Sequence replace(UTF8Sequence old, UTF8Char new_, int count) {
        if (count == 0 || old.equals(new UTF8Sequence(new_))) {
            return this;
        }
        if (!(old.isEmpty())) {
            return join(new_, this.split(old, count));
        }
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            R.add(new_);
            R.add(i);
            count--;
            if (count <= 0) {
                return new UTF8Sequence(R);
            }
        }
        R.add(new_);
        return new UTF8Sequence(R);
    }

    public UTF8Sequence replace(UTF8Sequence old, UTF8Char new_) {
        return this.replace(old, new_, -1);
    }

    public UTF8Sequence replace(UTF8Char old, UTF8Sequence new_, int count) {
        if (count == 0 || new UTF8Sequence(old).equals(new_)) {
            return this;
        }
        return new_.join(this.split(old, count));
    }

    public UTF8Sequence replace(UTF8Char old, UTF8Sequence new_) {
        return this.replace(old, new_, -1);
    }

    public UTF8Sequence replace(UTF8Char old, UTF8Char new_, int count) {
        if (count == 0 || old.equals(new_)) {
            return this;
        }
        return join(new_, this.split(old, count));
    }

    public UTF8Sequence replace(UTF8Char old, UTF8Char new_) {
        return this.replace(old, new_, -1);
    }

    public boolean startswith(UTF8Sequence prefix, int start, int end) {
        if (prefix.isEmpty()) {
            return start <= -this.chars.length ||
                   (start <= this.chars.length && start >= 0 && end >= 0 &&
                    start <= end) ||
                   (start <= this.chars.length && start >= 0 && end < 0 &&
                    start <= end + this.chars.length) ||
                   (start <= this.chars.length && start < 0 && end >= 0 &&
                    start + this.chars.length <= end) ||
                   (start <= this.chars.length && start < 0 && end < 0 &&
                    start <= end);
        }
        if (start < 0) {
            start += this.chars.length;
            if (start < 0) {
                start = 0;
            }
        } else if (start > this.chars.length) {
            start = this.chars.length;
        }
        if (end < 0) {
            end += this.chars.length;
            if (end < 0) {
                end = 0;
            }
        } else if (end > this.chars.length) {
            end = this.chars.length;
        }
        return end - start < prefix.chars.length &&
               this.subSequence(start, start + prefix.chars.length)
               .equals(prefix);
    }

    public boolean startswith(UTF8Sequence prefix, int start) {
        return this.startswith(prefix, start, this.chars.length);
    }

    public boolean startswith(UTF8Sequence prefix) {
        return this.startswith(prefix, 0, this.chars.length);
    }

    public boolean startsWith(UTF8Sequence prefix, int toffset) {
        if (toffset < 0) {
            return false;
        }
        if (toffset > 0) {
            return this.subSequence(toffset).startsWith(prefix, 0);
        }
        if (prefix.chars.length > this.chars.length) {
            return false;
        }
        return this.subSequence(0, prefix.chars.length).equals(prefix);
    }

    public boolean startsWith(UTF8Sequence prefix) {
        return this.startsWith(prefix, 0);
    }

    public UTF8Sequence regularAll() {
        final List<UTF8Char> R = new ArrayList<UTF8Char>();
        for (UTF8Char i : this) {
            R.add(new UTF8Char(i.ord()));
        }
        return new UTF8Sequence(R);
    }

    public boolean equalsWithRegular(UTF8Sequence o) {
        if (this.chars.length != o.chars.length) {
            return false;
        }
        for (int i = 0; i < this.chars.length; i++) {
            if (this.chars[i].ord() != o.chars[i].ord() ||
                this.chars[i].getByteLength() != o.chars[i].getByteLength()) {
                return false;
            }
        }
        return true;
    }

    private static void registerPairs(UTF8Char upper, UTF8Char lower) {
        TOLOWERS.put(upper, new UTF8Sequence(lower));
        TOUPPERS.put(lower, new UTF8Sequence(upper));
    }

    private static void registerPairs(char upper, char lower) {
        TOLOWERS.put(new UTF8Char(upper), new UTF8Sequence(lower));
        TOUPPERS.put(new UTF8Char(lower), new UTF8Sequence(upper));
    }

    static {
        LINESEPS = new UTF8Sequence[]{
            new UTF8Sequence(UTF8Char.VT),
            new UTF8Sequence(UTF8Char.LF),
            new UTF8Sequence(UTF8Char.CR),
            new UTF8Sequence(new UTF8Char[]{UTF8Char.CR, UTF8Char.LF})
        };
        WHITESPACES = new UTF8Sequence(new UTF8Char[]{
            UTF8Char.HT, UTF8Char.VT, UTF8Char.LF, UTF8Char.CR,
            new UTF8Char(' '), UTF8Char.NBSP, UTF8Char.IDSP
        });
        DECIMALS = new UTF8Sequence(new long[]{
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 1632, 1633, 1634, 1635,
            1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778, 1779, 1780,
            1781, 1782, 1783, 1784, 1785, 1984, 1985, 1986, 1987, 1988, 1989,
            1990, 1991, 1992, 1993, 2406, 2407, 2408, 2409, 2410, 2411, 2412,
            2413, 2414, 2415, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541,
            2542, 2543, 2662, 2663, 2664, 2665, 2666, 2667, 2668, 2669, 2670,
            2671, 2790, 2791, 2792, 2793, 2794, 2795, 2796, 2797, 2798, 2799,
            2918, 2919, 2920, 2921, 2922, 2923, 2924, 2925, 2926, 2927, 3046,
            3047, 3048, 3049, 3050, 3051, 3052, 3053, 3054, 3055, 3174, 3175,
            3176, 3177, 3178, 3179, 3180, 3181, 3182, 3183, 3302, 3303, 3304,
            3305, 3306, 3307, 3308, 3309, 3310, 3311, 3430, 3431, 3432, 3433,
            3434, 3435, 3436, 3437, 3438, 3439, 3558, 3559, 3560, 3561, 3562,
            3563, 3564, 3565, 3566, 3567, 3664, 3665, 3666, 3667, 3668, 3669,
            3670, 3671, 3672, 3673, 3792, 3793, 3794, 3795, 3796, 3797, 3798,
            3799, 3800, 3801, 3872, 3873, 3874, 3875, 3876, 3877, 3878, 3879,
            3880, 3881, 4160, 4161, 4162, 4163, 4164, 4165, 4166, 4167, 4168,
            4169, 4240, 4241, 4242, 4243, 4244, 4245, 4246, 4247, 4248, 4249,
            6112, 6113, 6114, 6115, 6116, 6117, 6118, 6119, 6120, 6121, 6160,
            6161, 6162, 6163, 6164, 6165, 6166, 6167, 6168, 6169, 6470, 6471,
            6472, 6473, 6474, 6475, 6476, 6477, 6478, 6479, 6608, 6609, 6610,
            6611, 6612, 6613, 6614, 6615, 6616, 6617, 6784, 6785, 6786, 6787,
            6788, 6789, 6790, 6791, 6792, 6793, 6800, 6801, 6802, 6803, 6804,
            6805, 6806, 6807, 6808, 6809, 6992, 6993, 6994, 6995, 6996, 6997,
            6998, 6999, 7000, 7001, 7088, 7089, 7090, 7091, 7092, 7093, 7094,
            7095, 7096, 7097, 7232, 7233, 7234, 7235, 7236, 7237, 7238, 7239,
            7240, 7241, 7248, 7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256,
            7257, 42528, 42529, 42530, 42531, 42532, 42533, 42534, 42535,
            42536, 42537, 43216, 43217, 43218, 43219, 43220, 43221, 43222,
            43223, 43224, 43225, 43264, 43265, 43266, 43267, 43268, 43269,
            43270, 43271, 43272, 43273, 43472, 43473, 43474, 43475, 43476,
            43477, 43478, 43479, 43480, 43481, 43504, 43505, 43506, 43507,
            43508, 43509, 43510, 43511, 43512, 43513, 43600, 43601, 43602,
            43603, 43604, 43605, 43606, 43607, 43608, 43609, 44016, 44017,
            44018, 44019, 44020, 44021, 44022, 44023, 44024, 44025, 65296,
            65297, 65298, 65299, 65300, 65301, 65302, 65303, 65304, 65305,
            66720, 66721, 66722, 66723, 66724, 66725, 66726, 66727, 66728,
            66729, 68912, 68913, 68914, 68915, 68916, 68917, 68918, 68919,
            68920, 68921, 69734, 69735, 69736, 69737, 69738, 69739, 69740,
            69741, 69742, 69743, 69872, 69873, 69874, 69875, 69876, 69877,
            69878, 69879, 69880, 69881, 69942, 69943, 69944, 69945, 69946,
            69947, 69948, 69949, 69950, 69951, 70096, 70097, 70098, 70099,
            70100, 70101, 70102, 70103, 70104, 70105, 70384, 70385, 70386,
            70387, 70388, 70389, 70390, 70391, 70392, 70393, 70736, 70737,
            70738, 70739, 70740, 70741, 70742, 70743, 70744, 70745, 70864,
            70865, 70866, 70867, 70868, 70869, 70870, 70871, 70872, 70873,
            71248, 71249, 71250, 71251, 71252, 71253, 71254, 71255, 71256,
            71257, 71360, 71361, 71362, 71363, 71364, 71365, 71366, 71367,
            71368, 71369, 71472, 71473, 71474, 71475, 71476, 71477, 71478,
            71479, 71480, 71481, 71904, 71905, 71906, 71907, 71908, 71909,
            71910, 71911, 71912, 71913, 72016, 72017, 72018, 72019, 72020,
            72021, 72022, 72023, 72024, 72025, 72784, 72785, 72786, 72787,
            72788, 72789, 72790, 72791, 72792, 72793, 73040, 73041, 73042,
            73043, 73044, 73045, 73046, 73047, 73048, 73049, 73120, 73121,
            73122, 73123, 73124, 73125, 73126, 73127, 73128, 73129, 73552,
            73553, 73554, 73555, 73556, 73557, 73558, 73559, 73560, 73561,
            92768, 92769, 92770, 92771, 92772, 92773, 92774, 92775, 92776,
            92777, 92864, 92865, 92866, 92867, 92868, 92869, 92870, 92871,
            92872, 92873, 93008, 93009, 93010, 93011, 93012, 93013, 93014,
            93015, 93016, 93017, 120782, 120783, 120784, 120785, 120786,
            120787, 120788, 120789, 120790, 120791, 120792, 120793, 120794,
            120795, 120796, 120797, 120798, 120799, 120800, 120801, 120802,
            120803, 120804, 120805, 120806, 120807, 120808, 120809, 120810,
            120811, 120812, 120813, 120814, 120815, 120816, 120817, 120818,
            120819, 120820, 120821, 120822, 120823, 120824, 120825, 120826,
            120827, 120828, 120829, 120830, 120831, 123200, 123201, 123202,
            123203, 123204, 123205, 123206, 123207, 123208, 123209, 123632,
            123633, 123634, 123635, 123636, 123637, 123638, 123639, 123640,
            123641, 124144, 124145, 124146, 124147, 124148, 124149, 124150,
            124151, 124152, 124153, 125264, 125265, 125266, 125267, 125268,
            125269, 125270, 125271, 125272, 125273, 130032, 130033, 130034,
            130035, 130036, 130037, 130038, 130039, 130040, 130041
        });
        DIGITS = new UTF8Sequence(new long[]{
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 178, 179, 185, 1632, 1633,
            1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1776, 1777, 1778,
            1779, 1780, 1781, 1782, 1783, 1784, 1785, 1984, 1985, 1986, 1987,
            1988, 1989, 1990, 1991, 1992, 1993, 2406, 2407, 2408, 2409, 2410,
            2411, 2412, 2413, 2414, 2415, 2534, 2535, 2536, 2537, 2538, 2539,
            2540, 2541, 2542, 2543, 2662, 2663, 2664, 2665, 2666, 2667, 2668,
            2669, 2670, 2671, 2790, 2791, 2792, 2793, 2794, 2795, 2796, 2797,
            2798, 2799, 2918, 2919, 2920, 2921, 2922, 2923, 2924, 2925, 2926,
            2927, 3046, 3047, 3048, 3049, 3050, 3051, 3052, 3053, 3054, 3055,
            3174, 3175, 3176, 3177, 3178, 3179, 3180, 3181, 3182, 3183, 3302,
            3303, 3304, 3305, 3306, 3307, 3308, 3309, 3310, 3311, 3430, 3431,
            3432, 3433, 3434, 3435, 3436, 3437, 3438, 3439, 3558, 3559, 3560,
            3561, 3562, 3563, 3564, 3565, 3566, 3567, 3664, 3665, 3666, 3667,
            3668, 3669, 3670, 3671, 3672, 3673, 3792, 3793, 3794, 3795, 3796,
            3797, 3798, 3799, 3800, 3801, 3872, 3873, 3874, 3875, 3876, 3877,
            3878, 3879, 3880, 3881, 4160, 4161, 4162, 4163, 4164, 4165, 4166,
            4167, 4168, 4169, 4240, 4241, 4242, 4243, 4244, 4245, 4246, 4247,
            4248, 4249, 4969, 4970, 4971, 4972, 4973, 4974, 4975, 4976, 4977,
            6112, 6113, 6114, 6115, 6116, 6117, 6118, 6119, 6120, 6121, 6160,
            6161, 6162, 6163, 6164, 6165, 6166, 6167, 6168, 6169, 6470, 6471,
            6472, 6473, 6474, 6475, 6476, 6477, 6478, 6479, 6608, 6609, 6610,
            6611, 6612, 6613, 6614, 6615, 6616, 6617, 6618, 6784, 6785, 6786,
            6787, 6788, 6789, 6790, 6791, 6792, 6793, 6800, 6801, 6802, 6803,
            6804, 6805, 6806, 6807, 6808, 6809, 6992, 6993, 6994, 6995, 6996,
            6997, 6998, 6999, 7000, 7001, 7088, 7089, 7090, 7091, 7092, 7093,
            7094, 7095, 7096, 7097, 7232, 7233, 7234, 7235, 7236, 7237, 7238,
            7239, 7240, 7241, 7248, 7249, 7250, 7251, 7252, 7253, 7254, 7255,
            7256, 7257, 8304, 8308, 8309, 8310, 8311, 8312, 8313, 8320, 8321,
            8322, 8323, 8324, 8325, 8326, 8327, 8328, 8329, 9312, 9313, 9314,
            9315, 9316, 9317, 9318, 9319, 9320, 9332, 9333, 9334, 9335, 9336,
            9337, 9338, 9339, 9340, 9352, 9353, 9354, 9355, 9356, 9357, 9358,
            9359, 9360, 9450, 9461, 9462, 9463, 9464, 9465, 9466, 9467, 9468,
            9469, 9471, 10102, 10103, 10104, 10105, 10106, 10107, 10108, 10109,
            10110, 10112, 10113, 10114, 10115, 10116, 10117, 10118, 10119,
            10120, 10122, 10123, 10124, 10125, 10126, 10127, 10128, 10129,
            10130, 42528, 42529, 42530, 42531, 42532, 42533, 42534, 42535,
            42536, 42537, 43216, 43217, 43218, 43219, 43220, 43221, 43222,
            43223, 43224, 43225, 43264, 43265, 43266, 43267, 43268, 43269,
            43270, 43271, 43272, 43273, 43472, 43473, 43474, 43475, 43476,
            43477, 43478, 43479, 43480, 43481, 43504, 43505, 43506, 43507,
            43508, 43509, 43510, 43511, 43512, 43513, 43600, 43601, 43602,
            43603, 43604, 43605, 43606, 43607, 43608, 43609, 44016, 44017,
            44018, 44019, 44020, 44021, 44022, 44023, 44024, 44025, 65296,
            65297, 65298, 65299, 65300, 65301, 65302, 65303, 65304, 65305,
            66720, 66721, 66722, 66723, 66724, 66725, 66726, 66727, 66728,
            66729, 68160, 68161, 68162, 68163, 68912, 68913, 68914, 68915,
            68916, 68917, 68918, 68919, 68920, 68921, 69216, 69217, 69218,
            69219, 69220, 69221, 69222, 69223, 69224, 69714, 69715, 69716,
            69717, 69718, 69719, 69720, 69721, 69722, 69734, 69735, 69736,
            69737, 69738, 69739, 69740, 69741, 69742, 69743, 69872, 69873,
            69874, 69875, 69876, 69877, 69878, 69879, 69880, 69881, 69942,
            69943, 69944, 69945, 69946, 69947, 69948, 69949, 69950, 69951,
            70096, 70097, 70098, 70099, 70100, 70101, 70102, 70103, 70104,
            70105, 70384, 70385, 70386, 70387, 70388, 70389, 70390, 70391,
            70392, 70393, 70736, 70737, 70738, 70739, 70740, 70741, 70742,
            70743, 70744, 70745, 70864, 70865, 70866, 70867, 70868, 70869,
            70870, 70871, 70872, 70873, 71248, 71249, 71250, 71251, 71252,
            71253, 71254, 71255, 71256, 71257, 71360, 71361, 71362, 71363,
            71364, 71365, 71366, 71367, 71368, 71369, 71472, 71473, 71474,
            71475, 71476, 71477, 71478, 71479, 71480, 71481, 71904, 71905,
            71906, 71907, 71908, 71909, 71910, 71911, 71912, 71913, 72016,
            72017, 72018, 72019, 72020, 72021, 72022, 72023, 72024, 72025,
            72784, 72785, 72786, 72787, 72788, 72789, 72790, 72791, 72792,
            72793, 73040, 73041, 73042, 73043, 73044, 73045, 73046, 73047,
            73048, 73049, 73120, 73121, 73122, 73123, 73124, 73125, 73126,
            73127, 73128, 73129, 73552, 73553, 73554, 73555, 73556, 73557,
            73558, 73559, 73560, 73561, 92768, 92769, 92770, 92771, 92772,
            92773, 92774, 92775, 92776, 92777, 92864, 92865, 92866, 92867,
            92868, 92869, 92870, 92871, 92872, 92873, 93008, 93009, 93010,
            93011, 93012, 93013, 93014, 93015, 93016, 93017, 120782, 120783,
            120784, 120785, 120786, 120787, 120788, 120789, 120790, 120791,
            120792, 120793, 120794, 120795, 120796, 120797, 120798, 120799,
            120800, 120801, 120802, 120803, 120804, 120805, 120806, 120807,
            120808, 120809, 120810, 120811, 120812, 120813, 120814, 120815,
            120816, 120817, 120818, 120819, 120820, 120821, 120822, 120823,
            120824, 120825, 120826, 120827, 120828, 120829, 120830, 120831,
            123200, 123201, 123202, 123203, 123204, 123205, 123206, 123207,
            123208, 123209, 123632, 123633, 123634, 123635, 123636, 123637,
            123638, 123639, 123640, 123641, 124144, 124145, 124146, 124147,
            124148, 124149, 124150, 124151, 124152, 124153, 125264, 125265,
            125266, 125267, 125268, 125269, 125270, 125271, 125272, 125273,
            127232, 127233, 127234, 127235, 127236, 127237, 127238, 127239,
            127240, 127241, 127242, 130032, 130033, 130034, 130035, 130036,
            130037, 130038, 130039, 130040, 130041
        });
        NUMERICS = new UTF8Sequence(new long[]{
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 178, 179, 185, 188, 189,
            190, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641,
            1776, 1777, 1778, 1779, 1780, 1781, 1782, 1783, 1784, 1785, 1984,
            1985, 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993, 2406, 2407,
            2408, 2409, 2410, 2411, 2412, 2413, 2414, 2415, 2534, 2535, 2536,
            2537, 2538, 2539, 2540, 2541, 2542, 2543, 2548, 2549, 2550, 2551,
            2552, 2553, 2662, 2663, 2664, 2665, 2666, 2667, 2668, 2669, 2670,
            2671, 2790, 2791, 2792, 2793, 2794, 2795, 2796, 2797, 2798, 2799,
            2918, 2919, 2920, 2921, 2922, 2923, 2924, 2925, 2926, 2927, 2930,
            2931, 2932, 2933, 2934, 2935, 3046, 3047, 3048, 3049, 3050, 3051,
            3052, 3053, 3054, 3055, 3056, 3057, 3058, 3174, 3175, 3176, 3177,
            3178, 3179, 3180, 3181, 3182, 3183, 3192, 3193, 3194, 3195, 3196,
            3197, 3198, 3302, 3303, 3304, 3305, 3306, 3307, 3308, 3309, 3310,
            3311, 3416, 3417, 3418, 3419, 3420, 3421, 3422, 3430, 3431, 3432,
            3433, 3434, 3435, 3436, 3437, 3438, 3439, 3440, 3441, 3442, 3443,
            3444, 3445, 3446, 3447, 3448, 3558, 3559, 3560, 3561, 3562, 3563,
            3564, 3565, 3566, 3567, 3664, 3665, 3666, 3667, 3668, 3669, 3670,
            3671, 3672, 3673, 3792, 3793, 3794, 3795, 3796, 3797, 3798, 3799,
            3800, 3801, 3872, 3873, 3874, 3875, 3876, 3877, 3878, 3879, 3880,
            3881, 3882, 3883, 3884, 3885, 3886, 3887, 3888, 3889, 3890, 3891,
            4160, 4161, 4162, 4163, 4164, 4165, 4166, 4167, 4168, 4169, 4240,
            4241, 4242, 4243, 4244, 4245, 4246, 4247, 4248, 4249, 4969, 4970,
            4971, 4972, 4973, 4974, 4975, 4976, 4977, 4978, 4979, 4980, 4981,
            4982, 4983, 4984, 4985, 4986, 4987, 4988, 5870, 5871, 5872, 6112,
            6113, 6114, 6115, 6116, 6117, 6118, 6119, 6120, 6121, 6128, 6129,
            6130, 6131, 6132, 6133, 6134, 6135, 6136, 6137, 6160, 6161, 6162,
            6163, 6164, 6165, 6166, 6167, 6168, 6169, 6470, 6471, 6472, 6473,
            6474, 6475, 6476, 6477, 6478, 6479, 6608, 6609, 6610, 6611, 6612,
            6613, 6614, 6615, 6616, 6617, 6618, 6784, 6785, 6786, 6787, 6788,
            6789, 6790, 6791, 6792, 6793, 6800, 6801, 6802, 6803, 6804, 6805,
            6806, 6807, 6808, 6809, 6992, 6993, 6994, 6995, 6996, 6997, 6998,
            6999, 7000, 7001, 7088, 7089, 7090, 7091, 7092, 7093, 7094, 7095,
            7096, 7097, 7232, 7233, 7234, 7235, 7236, 7237, 7238, 7239, 7240,
            7241, 7248, 7249, 7250, 7251, 7252, 7253, 7254, 7255, 7256, 7257,
            8304, 8308, 8309, 8310, 8311, 8312, 8313, 8320, 8321, 8322, 8323,
            8324, 8325, 8326, 8327, 8328, 8329, 8528, 8529, 8530, 8531, 8532,
            8533, 8534, 8535, 8536, 8537, 8538, 8539, 8540, 8541, 8542, 8543,
            8544, 8545, 8546, 8547, 8548, 8549, 8550, 8551, 8552, 8553, 8554,
            8555, 8556, 8557, 8558, 8559, 8560, 8561, 8562, 8563, 8564, 8565,
            8566, 8567, 8568, 8569, 8570, 8571, 8572, 8573, 8574, 8575, 8576,
            8577, 8578, 8581, 8582, 8583, 8584, 8585, 9312, 9313, 9314, 9315,
            9316, 9317, 9318, 9319, 9320, 9321, 9322, 9323, 9324, 9325, 9326,
            9327, 9328, 9329, 9330, 9331, 9332, 9333, 9334, 9335, 9336, 9337,
            9338, 9339, 9340, 9341, 9342, 9343, 9344, 9345, 9346, 9347, 9348,
            9349, 9350, 9351, 9352, 9353, 9354, 9355, 9356, 9357, 9358, 9359,
            9360, 9361, 9362, 9363, 9364, 9365, 9366, 9367, 9368, 9369, 9370,
            9371, 9450, 9451, 9452, 9453, 9454, 9455, 9456, 9457, 9458, 9459,
            9460, 9461, 9462, 9463, 9464, 9465, 9466, 9467, 9468, 9469, 9470,
            9471, 10102, 10103, 10104, 10105, 10106, 10107, 10108, 10109,
            10110, 10111, 10112, 10113, 10114, 10115, 10116, 10117, 10118,
            10119, 10120, 10121, 10122, 10123, 10124, 10125, 10126, 10127,
            10128, 10129, 10130, 10131, 11517, 12295, 12321, 12322, 12323,
            12324, 12325, 12326, 12327, 12328, 12329, 12344, 12345, 12346,
            12690, 12691, 12692, 12693, 12832, 12833, 12834, 12835, 12836,
            12837, 12838, 12839, 12840, 12841, 12872, 12873, 12874, 12875,
            12876, 12877, 12878, 12879, 12881, 12882, 12883, 12884, 12885,
            12886, 12887, 12888, 12889, 12890, 12891, 12892, 12893, 12894,
            12895, 12928, 12929, 12930, 12931, 12932, 12933, 12934, 12935,
            12936, 12937, 12977, 12978, 12979, 12980, 12981, 12982, 12983,
            12984, 12985, 12986, 12987, 12988, 12989, 12990, 12991, 13317,
            13443, 14378, 15181, 19968, 19971, 19975, 19977, 20004, 20061,
            20108, 20116, 20118, 20140, 20159, 20160, 20191, 20200, 20237,
            20336, 20457, 20486, 20740, 20806, 20841, 20843, 20845, 21313,
            21315, 21316, 21317, 21324, 21441, 21442, 21443, 21444, 22235,
            22769, 22777, 24186, 24318, 24319, 24332, 24333, 24334, 24336,
            25296, 25342, 25420, 26578, 27934, 28422, 29590, 30334, 30357,
            31213, 32902, 33836, 36014, 36019, 36144, 37390, 38057, 38433,
            38470, 38476, 38520, 38646, 42528, 42529, 42530, 42531, 42532,
            42533, 42534, 42535, 42536, 42537, 42726, 42727, 42728, 42729,
            42730, 42731, 42732, 42733, 42734, 42735, 43056, 43057, 43058,
            43059, 43060, 43061, 43216, 43217, 43218, 43219, 43220, 43221,
            43222, 43223, 43224, 43225, 43264, 43265, 43266, 43267, 43268,
            43269, 43270, 43271, 43272, 43273, 43472, 43473, 43474, 43475,
            43476, 43477, 43478, 43479, 43480, 43481, 43504, 43505, 43506,
            43507, 43508, 43509, 43510, 43511, 43512, 43513, 43600, 43601,
            43602, 43603, 43604, 43605, 43606, 43607, 43608, 43609, 44016,
            44017, 44018, 44019, 44020, 44021, 44022, 44023, 44024, 44025,
            63851, 63859, 63864, 63922, 63953, 63955, 63997, 65296, 65297,
            65298, 65299, 65300, 65301, 65302, 65303, 65304, 65305, 65799,
            65800, 65801, 65802, 65803, 65804, 65805, 65806, 65807, 65808,
            65809, 65810, 65811, 65812, 65813, 65814, 65815, 65816, 65817,
            65818, 65819, 65820, 65821, 65822, 65823, 65824, 65825, 65826,
            65827, 65828, 65829, 65830, 65831, 65832, 65833, 65834, 65835,
            65836, 65837, 65838, 65839, 65840, 65841, 65842, 65843, 65856,
            65857, 65858, 65859, 65860, 65861, 65862, 65863, 65864, 65865,
            65866, 65867, 65868, 65869, 65870, 65871, 65872, 65873, 65874,
            65875, 65876, 65877, 65878, 65879, 65880, 65881, 65882, 65883,
            65884, 65885, 65886, 65887, 65888, 65889, 65890, 65891, 65892,
            65893, 65894, 65895, 65896, 65897, 65898, 65899, 65900, 65901,
            65902, 65903, 65904, 65905, 65906, 65907, 65908, 65909, 65910,
            65911, 65912, 65930, 65931, 66273, 66274, 66275, 66276, 66277,
            66278, 66279, 66280, 66281, 66282, 66283, 66284, 66285, 66286,
            66287, 66288, 66289, 66290, 66291, 66292, 66293, 66294, 66295,
            66296, 66297, 66298, 66299, 66336, 66337, 66338, 66339, 66369,
            66378, 66513, 66514, 66515, 66516, 66517, 66720, 66721, 66722,
            66723, 66724, 66725, 66726, 66727, 66728, 66729, 67672, 67673,
            67674, 67675, 67676, 67677, 67678, 67679, 67705, 67706, 67707,
            67708, 67709, 67710, 67711, 67751, 67752, 67753, 67754, 67755,
            67756, 67757, 67758, 67759, 67835, 67836, 67837, 67838, 67839,
            67862, 67863, 67864, 67865, 67866, 67867, 68028, 68029, 68032,
            68033, 68034, 68035, 68036, 68037, 68038, 68039, 68040, 68041,
            68042, 68043, 68044, 68045, 68046, 68047, 68050, 68051, 68052,
            68053, 68054, 68055, 68056, 68057, 68058, 68059, 68060, 68061,
            68062, 68063, 68064, 68065, 68066, 68067, 68068, 68069, 68070,
            68071, 68072, 68073, 68074, 68075, 68076, 68077, 68078, 68079,
            68080, 68081, 68082, 68083, 68084, 68085, 68086, 68087, 68088,
            68089, 68090, 68091, 68092, 68093, 68094, 68095, 68160, 68161,
            68162, 68163, 68164, 68165, 68166, 68167, 68168, 68221, 68222,
            68253, 68254, 68255, 68331, 68332, 68333, 68334, 68335, 68440,
            68441, 68442, 68443, 68444, 68445, 68446, 68447, 68472, 68473,
            68474, 68475, 68476, 68477, 68478, 68479, 68521, 68522, 68523,
            68524, 68525, 68526, 68527, 68858, 68859, 68860, 68861, 68862,
            68863, 68912, 68913, 68914, 68915, 68916, 68917, 68918, 68919,
            68920, 68921, 69216, 69217, 69218, 69219, 69220, 69221, 69222,
            69223, 69224, 69225, 69226, 69227, 69228, 69229, 69230, 69231,
            69232, 69233, 69234, 69235, 69236, 69237, 69238, 69239, 69240,
            69241, 69242, 69243, 69244, 69245, 69246, 69405, 69406, 69407,
            69408, 69409, 69410, 69411, 69412, 69413, 69414, 69457, 69458,
            69459, 69460, 69573, 69574, 69575, 69576, 69577, 69578, 69579,
            69714, 69715, 69716, 69717, 69718, 69719, 69720, 69721, 69722,
            69723, 69724, 69725, 69726, 69727, 69728, 69729, 69730, 69731,
            69732, 69733, 69734, 69735, 69736, 69737, 69738, 69739, 69740,
            69741, 69742, 69743, 69872, 69873, 69874, 69875, 69876, 69877,
            69878, 69879, 69880, 69881, 69942, 69943, 69944, 69945, 69946,
            69947, 69948, 69949, 69950, 69951, 70096, 70097, 70098, 70099,
            70100, 70101, 70102, 70103, 70104, 70105, 70113, 70114, 70115,
            70116, 70117, 70118, 70119, 70120, 70121, 70122, 70123, 70124,
            70125, 70126, 70127, 70128, 70129, 70130, 70131, 70132, 70384,
            70385, 70386, 70387, 70388, 70389, 70390, 70391, 70392, 70393,
            70736, 70737, 70738, 70739, 70740, 70741, 70742, 70743, 70744,
            70745, 70864, 70865, 70866, 70867, 70868, 70869, 70870, 70871,
            70872, 70873, 71248, 71249, 71250, 71251, 71252, 71253, 71254,
            71255, 71256, 71257, 71360, 71361, 71362, 71363, 71364, 71365,
            71366, 71367, 71368, 71369, 71472, 71473, 71474, 71475, 71476,
            71477, 71478, 71479, 71480, 71481, 71482, 71483, 71904, 71905,
            71906, 71907, 71908, 71909, 71910, 71911, 71912, 71913, 71914,
            71915, 71916, 71917, 71918, 71919, 71920, 71921, 71922, 72016,
            72017, 72018, 72019, 72020, 72021, 72022, 72023, 72024, 72025,
            72784, 72785, 72786, 72787, 72788, 72789, 72790, 72791, 72792,
            72793, 72794, 72795, 72796, 72797, 72798, 72799, 72800, 72801,
            72802, 72803, 72804, 72805, 72806, 72807, 72808, 72809, 72810,
            72811, 72812, 73040, 73041, 73042, 73043, 73044, 73045, 73046,
            73047, 73048, 73049, 73120, 73121, 73122, 73123, 73124, 73125,
            73126, 73127, 73128, 73129, 73552, 73553, 73554, 73555, 73556,
            73557, 73558, 73559, 73560, 73561, 73664, 73665, 73666, 73667,
            73668, 73669, 73670, 73671, 73672, 73673, 73674, 73675, 73676,
            73677, 73678, 73679, 73680, 73681, 73682, 73683, 73684, 74752,
            74753, 74754, 74755, 74756, 74757, 74758, 74759, 74760, 74761,
            74762, 74763, 74764, 74765, 74766, 74767, 74768, 74769, 74770,
            74771, 74772, 74773, 74774, 74775, 74776, 74777, 74778, 74779,
            74780, 74781, 74782, 74783, 74784, 74785, 74786, 74787, 74788,
            74789, 74790, 74791, 74792, 74793, 74794, 74795, 74796, 74797,
            74798, 74799, 74800, 74801, 74802, 74803, 74804, 74805, 74806,
            74807, 74808, 74809, 74810, 74811, 74812, 74813, 74814, 74815,
            74816, 74817, 74818, 74819, 74820, 74821, 74822, 74823, 74824,
            74825, 74826, 74827, 74828, 74829, 74830, 74831, 74832, 74833,
            74834, 74835, 74836, 74837, 74838, 74839, 74840, 74841, 74842,
            74843, 74844, 74845, 74846, 74847, 74848, 74849, 74850, 74851,
            74852, 74853, 74854, 74855, 74856, 74857, 74858, 74859, 74860,
            74861, 74862, 92768, 92769, 92770, 92771, 92772, 92773, 92774,
            92775, 92776, 92777, 92864, 92865, 92866, 92867, 92868, 92869,
            92870, 92871, 92872, 92873, 93008, 93009, 93010, 93011, 93012,
            93013, 93014, 93015, 93016, 93017, 93019, 93020, 93021, 93022,
            93023, 93024, 93025, 93824, 93825, 93826, 93827, 93828, 93829,
            93830, 93831, 93832, 93833, 93834, 93835, 93836, 93837, 93838,
            93839, 93840, 93841, 93842, 93843, 93844, 93845, 93846, 119488,
            119489, 119490, 119491, 119492, 119493, 119494, 119495, 119496,
            119497, 119498, 119499, 119500, 119501, 119502, 119503, 119504,
            119505, 119506, 119507, 119520, 119521, 119522, 119523, 119524,
            119525, 119526, 119527, 119528, 119529, 119530, 119531, 119532,
            119533, 119534, 119535, 119536, 119537, 119538, 119539, 119648,
            119649, 119650, 119651, 119652, 119653, 119654, 119655, 119656,
            119657, 119658, 119659, 119660, 119661, 119662, 119663, 119664,
            119665, 119666, 119667, 119668, 119669, 119670, 119671, 119672,
            120782, 120783, 120784, 120785, 120786, 120787, 120788, 120789,
            120790, 120791, 120792, 120793, 120794, 120795, 120796, 120797,
            120798, 120799, 120800, 120801, 120802, 120803, 120804, 120805,
            120806, 120807, 120808, 120809, 120810, 120811, 120812, 120813,
            120814, 120815, 120816, 120817, 120818, 120819, 120820, 120821,
            120822, 120823, 120824, 120825, 120826, 120827, 120828, 120829,
            120830, 120831, 123200, 123201, 123202, 123203, 123204, 123205,
            123206, 123207, 123208, 123209, 123632, 123633, 123634, 123635,
            123636, 123637, 123638, 123639, 123640, 123641, 124144, 124145,
            124146, 124147, 124148, 124149, 124150, 124151, 124152, 124153,
            125127, 125128, 125129, 125130, 125131, 125132, 125133, 125134,
            125135, 125264, 125265, 125266, 125267, 125268, 125269, 125270,
            125271, 125272, 125273, 126065, 126066, 126067, 126068, 126069,
            126070, 126071, 126072, 126073, 126074, 126075, 126076, 126077,
            126078, 126079, 126080, 126081, 126082, 126083, 126084, 126085,
            126086, 126087, 126088, 126089, 126090, 126091, 126092, 126093,
            126094, 126095, 126096, 126097, 126098, 126099, 126100, 126101,
            126102, 126103, 126104, 126105, 126106, 126107, 126108, 126109,
            126110, 126111, 126112, 126113, 126114, 126115, 126116, 126117,
            126118, 126119, 126120, 126121, 126122, 126123, 126125, 126126,
            126127, 126129, 126130, 126131, 126132, 126209, 126210, 126211,
            126212, 126213, 126214, 126215, 126216, 126217, 126218, 126219,
            126220, 126221, 126222, 126223, 126224, 126225, 126226, 126227,
            126228, 126229, 126230, 126231, 126232, 126233, 126234, 126235,
            126236, 126237, 126238, 126239, 126240, 126241, 126242, 126243,
            126244, 126245, 126246, 126247, 126248, 126249, 126250, 126251,
            126252, 126253, 126255, 126256, 126257, 126258, 126259, 126260,
            126261, 126262, 126263, 126264, 126265, 126266, 126267, 126268,
            126269, 127232, 127233, 127234, 127235, 127236, 127237, 127238,
            127239, 127240, 127241, 127242, 127243, 127244, 130032, 130033,
            130034, 130035, 130036, 130037, 130038, 130039, 130040, 130041,
            131073, 131172, 131298, 131361, 133418, 133507, 133516, 133532,
            133866, 133885, 133913, 140176, 141720, 146203, 156269, 194704
        });
        TOLOWERS = new HashMap<UTF8Char, Iterable<UTF8Char>>();
        TOUPPERS = new HashMap<UTF8Char, Iterable<UTF8Char>>();
        registerPairs('A', 'a');
        registerPairs('B', 'b');
        registerPairs('C', 'c');
        registerPairs('D', 'd');
        registerPairs('E', 'e');
        registerPairs('F', 'f');
        registerPairs('G', 'g');
        registerPairs('H', 'h');
        registerPairs('I', 'i');
        registerPairs('J', 'j');
        registerPairs('K', 'k');
        registerPairs('L', 'l');
        registerPairs('M', 'm');
        registerPairs('N', 'n');
        registerPairs('O', 'o');
        registerPairs('P', 'p');
        registerPairs('Q', 'q');
        registerPairs('R', 'r');
        registerPairs('S', 's');
        registerPairs('T', 't');
        registerPairs('U', 'u');
        registerPairs('V', 'v');
        registerPairs('W', 'w');
        registerPairs('X', 'x');
        registerPairs('Y', 'y');
        registerPairs('Z', 'z');
        registerPairs(new UTF8Char(192), new UTF8Char(224));
        registerPairs(new UTF8Char(193), new UTF8Char(225));
        registerPairs(new UTF8Char(194), new UTF8Char(226));
        registerPairs(new UTF8Char(195), new UTF8Char(227));
        registerPairs(new UTF8Char(196), new UTF8Char(228));
        registerPairs(new UTF8Char(197), new UTF8Char(229));
        registerPairs(new UTF8Char(198), new UTF8Char(230));
        registerPairs(new UTF8Char(199), new UTF8Char(231));
        registerPairs(new UTF8Char(200), new UTF8Char(232));
        registerPairs(new UTF8Char(201), new UTF8Char(233));
        registerPairs(new UTF8Char(202), new UTF8Char(234));
        registerPairs(new UTF8Char(203), new UTF8Char(235));
        registerPairs(new UTF8Char(204), new UTF8Char(236));
        registerPairs(new UTF8Char(205), new UTF8Char(237));
        registerPairs(new UTF8Char(206), new UTF8Char(238));
        registerPairs(new UTF8Char(207), new UTF8Char(239));
        registerPairs(new UTF8Char(208), new UTF8Char(240));
        registerPairs(new UTF8Char(209), new UTF8Char(241));
        registerPairs(new UTF8Char(210), new UTF8Char(242));
        registerPairs(new UTF8Char(211), new UTF8Char(243));
        registerPairs(new UTF8Char(212), new UTF8Char(244));
        registerPairs(new UTF8Char(213), new UTF8Char(245));
        registerPairs(new UTF8Char(214), new UTF8Char(246));
        registerPairs(new UTF8Char(216), new UTF8Char(248));
        registerPairs(new UTF8Char(217), new UTF8Char(249));
        registerPairs(new UTF8Char(218), new UTF8Char(250));
        registerPairs(new UTF8Char(219), new UTF8Char(251));
        registerPairs(new UTF8Char(220), new UTF8Char(252));
        registerPairs(new UTF8Char(221), new UTF8Char(253));
        registerPairs(new UTF8Char(222), new UTF8Char(254));
        registerPairs(new UTF8Char(256), new UTF8Char(257));
        registerPairs(new UTF8Char(258), new UTF8Char(259));
        registerPairs(new UTF8Char(260), new UTF8Char(261));
        registerPairs(new UTF8Char(262), new UTF8Char(263));
        registerPairs(new UTF8Char(264), new UTF8Char(265));
        registerPairs(new UTF8Char(266), new UTF8Char(267));
        registerPairs(new UTF8Char(268), new UTF8Char(269));
        registerPairs(new UTF8Char(270), new UTF8Char(271));
        registerPairs(new UTF8Char(272), new UTF8Char(273));
        registerPairs(new UTF8Char(274), new UTF8Char(275));
        registerPairs(new UTF8Char(276), new UTF8Char(277));
        registerPairs(new UTF8Char(278), new UTF8Char(279));
        registerPairs(new UTF8Char(280), new UTF8Char(281));
        registerPairs(new UTF8Char(282), new UTF8Char(283));
        registerPairs(new UTF8Char(284), new UTF8Char(285));
        registerPairs(new UTF8Char(286), new UTF8Char(287));
        registerPairs(new UTF8Char(288), new UTF8Char(289));
        registerPairs(new UTF8Char(290), new UTF8Char(291));
        registerPairs(new UTF8Char(292), new UTF8Char(293));
        registerPairs(new UTF8Char(294), new UTF8Char(295));
        registerPairs(new UTF8Char(296), new UTF8Char(297));
        registerPairs(new UTF8Char(298), new UTF8Char(299));
        registerPairs(new UTF8Char(300), new UTF8Char(301));
        registerPairs(new UTF8Char(302), new UTF8Char(303));
        registerPairs(new UTF8Char(306), new UTF8Char(307));
        registerPairs(new UTF8Char(308), new UTF8Char(309));
        registerPairs(new UTF8Char(310), new UTF8Char(311));
        registerPairs(new UTF8Char(313), new UTF8Char(314));
        registerPairs(new UTF8Char(315), new UTF8Char(316));
        registerPairs(new UTF8Char(317), new UTF8Char(318));
        registerPairs(new UTF8Char(319), new UTF8Char(320));
        registerPairs(new UTF8Char(321), new UTF8Char(322));
        registerPairs(new UTF8Char(323), new UTF8Char(324));
        registerPairs(new UTF8Char(325), new UTF8Char(326));
        registerPairs(new UTF8Char(327), new UTF8Char(328));
        registerPairs(new UTF8Char(330), new UTF8Char(331));
        registerPairs(new UTF8Char(332), new UTF8Char(333));
        registerPairs(new UTF8Char(334), new UTF8Char(335));
        registerPairs(new UTF8Char(336), new UTF8Char(337));
        registerPairs(new UTF8Char(338), new UTF8Char(339));
        registerPairs(new UTF8Char(340), new UTF8Char(341));
        registerPairs(new UTF8Char(342), new UTF8Char(343));
        registerPairs(new UTF8Char(344), new UTF8Char(345));
        registerPairs(new UTF8Char(346), new UTF8Char(347));
        registerPairs(new UTF8Char(348), new UTF8Char(349));
        registerPairs(new UTF8Char(350), new UTF8Char(351));
        registerPairs(new UTF8Char(352), new UTF8Char(353));
        registerPairs(new UTF8Char(354), new UTF8Char(355));
        registerPairs(new UTF8Char(356), new UTF8Char(357));
        registerPairs(new UTF8Char(358), new UTF8Char(359));
        registerPairs(new UTF8Char(360), new UTF8Char(361));
        registerPairs(new UTF8Char(362), new UTF8Char(363));
        registerPairs(new UTF8Char(364), new UTF8Char(365));
        registerPairs(new UTF8Char(366), new UTF8Char(367));
        registerPairs(new UTF8Char(368), new UTF8Char(369));
        registerPairs(new UTF8Char(370), new UTF8Char(371));
        registerPairs(new UTF8Char(372), new UTF8Char(373));
        registerPairs(new UTF8Char(374), new UTF8Char(375));
        registerPairs(new UTF8Char(376), new UTF8Char(255));
        registerPairs(new UTF8Char(377), new UTF8Char(378));
        registerPairs(new UTF8Char(379), new UTF8Char(380));
        registerPairs(new UTF8Char(381), new UTF8Char(382));
        registerPairs(new UTF8Char(385), new UTF8Char(595));
        registerPairs(new UTF8Char(386), new UTF8Char(387));
        registerPairs(new UTF8Char(388), new UTF8Char(389));
        registerPairs(new UTF8Char(390), new UTF8Char(596));
        registerPairs(new UTF8Char(391), new UTF8Char(392));
        registerPairs(new UTF8Char(393), new UTF8Char(598));
        registerPairs(new UTF8Char(394), new UTF8Char(599));
        registerPairs(new UTF8Char(395), new UTF8Char(396));
        registerPairs(new UTF8Char(398), new UTF8Char(477));
        registerPairs(new UTF8Char(399), new UTF8Char(601));
        registerPairs(new UTF8Char(400), new UTF8Char(603));
        registerPairs(new UTF8Char(401), new UTF8Char(402));
        registerPairs(new UTF8Char(403), new UTF8Char(608));
        registerPairs(new UTF8Char(404), new UTF8Char(611));
        registerPairs(new UTF8Char(406), new UTF8Char(617));
        registerPairs(new UTF8Char(407), new UTF8Char(616));
        registerPairs(new UTF8Char(408), new UTF8Char(409));
        registerPairs(new UTF8Char(412), new UTF8Char(623));
        registerPairs(new UTF8Char(413), new UTF8Char(626));
        registerPairs(new UTF8Char(415), new UTF8Char(629));
        registerPairs(new UTF8Char(416), new UTF8Char(417));
        registerPairs(new UTF8Char(418), new UTF8Char(419));
        registerPairs(new UTF8Char(420), new UTF8Char(421));
        registerPairs(new UTF8Char(422), new UTF8Char(640));
        registerPairs(new UTF8Char(423), new UTF8Char(424));
        registerPairs(new UTF8Char(425), new UTF8Char(643));
        registerPairs(new UTF8Char(428), new UTF8Char(429));
        registerPairs(new UTF8Char(430), new UTF8Char(648));
        registerPairs(new UTF8Char(431), new UTF8Char(432));
        registerPairs(new UTF8Char(433), new UTF8Char(650));
        registerPairs(new UTF8Char(434), new UTF8Char(651));
        registerPairs(new UTF8Char(435), new UTF8Char(436));
        registerPairs(new UTF8Char(437), new UTF8Char(438));
        registerPairs(new UTF8Char(439), new UTF8Char(658));
        registerPairs(new UTF8Char(440), new UTF8Char(441));
        registerPairs(new UTF8Char(444), new UTF8Char(445));
        registerPairs(new UTF8Char(452), new UTF8Char(454));
        registerPairs(new UTF8Char(453), new UTF8Char(454));
        registerPairs(new UTF8Char(455), new UTF8Char(457));
        registerPairs(new UTF8Char(456), new UTF8Char(457));
        registerPairs(new UTF8Char(458), new UTF8Char(460));
        registerPairs(new UTF8Char(459), new UTF8Char(460));
        registerPairs(new UTF8Char(461), new UTF8Char(462));
        registerPairs(new UTF8Char(463), new UTF8Char(464));
        registerPairs(new UTF8Char(465), new UTF8Char(466));
        registerPairs(new UTF8Char(467), new UTF8Char(468));
        registerPairs(new UTF8Char(469), new UTF8Char(470));
        registerPairs(new UTF8Char(471), new UTF8Char(472));
        registerPairs(new UTF8Char(473), new UTF8Char(474));
        registerPairs(new UTF8Char(475), new UTF8Char(476));
        registerPairs(new UTF8Char(478), new UTF8Char(479));
        registerPairs(new UTF8Char(480), new UTF8Char(481));
        registerPairs(new UTF8Char(482), new UTF8Char(483));
        registerPairs(new UTF8Char(484), new UTF8Char(485));
        registerPairs(new UTF8Char(486), new UTF8Char(487));
        registerPairs(new UTF8Char(488), new UTF8Char(489));
        registerPairs(new UTF8Char(490), new UTF8Char(491));
        registerPairs(new UTF8Char(492), new UTF8Char(493));
        registerPairs(new UTF8Char(494), new UTF8Char(495));
        registerPairs(new UTF8Char(497), new UTF8Char(499));
        registerPairs(new UTF8Char(498), new UTF8Char(499));
        registerPairs(new UTF8Char(500), new UTF8Char(501));
        registerPairs(new UTF8Char(502), new UTF8Char(405));
        registerPairs(new UTF8Char(503), new UTF8Char(447));
        registerPairs(new UTF8Char(504), new UTF8Char(505));
        registerPairs(new UTF8Char(506), new UTF8Char(507));
        registerPairs(new UTF8Char(508), new UTF8Char(509));
        registerPairs(new UTF8Char(510), new UTF8Char(511));
        registerPairs(new UTF8Char(512), new UTF8Char(513));
        registerPairs(new UTF8Char(514), new UTF8Char(515));
        registerPairs(new UTF8Char(516), new UTF8Char(517));
        registerPairs(new UTF8Char(518), new UTF8Char(519));
        registerPairs(new UTF8Char(520), new UTF8Char(521));
        registerPairs(new UTF8Char(522), new UTF8Char(523));
        registerPairs(new UTF8Char(524), new UTF8Char(525));
        registerPairs(new UTF8Char(526), new UTF8Char(527));
        registerPairs(new UTF8Char(528), new UTF8Char(529));
        registerPairs(new UTF8Char(530), new UTF8Char(531));
        registerPairs(new UTF8Char(532), new UTF8Char(533));
        registerPairs(new UTF8Char(534), new UTF8Char(535));
        registerPairs(new UTF8Char(536), new UTF8Char(537));
        registerPairs(new UTF8Char(538), new UTF8Char(539));
        registerPairs(new UTF8Char(540), new UTF8Char(541));
        registerPairs(new UTF8Char(542), new UTF8Char(543));
        registerPairs(new UTF8Char(544), new UTF8Char(414));
        registerPairs(new UTF8Char(546), new UTF8Char(547));
        registerPairs(new UTF8Char(548), new UTF8Char(549));
        registerPairs(new UTF8Char(550), new UTF8Char(551));
        registerPairs(new UTF8Char(552), new UTF8Char(553));
        registerPairs(new UTF8Char(554), new UTF8Char(555));
        registerPairs(new UTF8Char(556), new UTF8Char(557));
        registerPairs(new UTF8Char(558), new UTF8Char(559));
        registerPairs(new UTF8Char(560), new UTF8Char(561));
        registerPairs(new UTF8Char(562), new UTF8Char(563));
        registerPairs(new UTF8Char(570), new UTF8Char(11365));
        registerPairs(new UTF8Char(571), new UTF8Char(572));
        registerPairs(new UTF8Char(573), new UTF8Char(410));
        registerPairs(new UTF8Char(574), new UTF8Char(11366));
        registerPairs(new UTF8Char(577), new UTF8Char(578));
        registerPairs(new UTF8Char(579), new UTF8Char(384));
        registerPairs(new UTF8Char(580), new UTF8Char(649));
        registerPairs(new UTF8Char(581), new UTF8Char(652));
        registerPairs(new UTF8Char(582), new UTF8Char(583));
        registerPairs(new UTF8Char(584), new UTF8Char(585));
        registerPairs(new UTF8Char(586), new UTF8Char(587));
        registerPairs(new UTF8Char(588), new UTF8Char(589));
        registerPairs(new UTF8Char(590), new UTF8Char(591));
        registerPairs(new UTF8Char(880), new UTF8Char(881));
        registerPairs(new UTF8Char(882), new UTF8Char(883));
        registerPairs(new UTF8Char(886), new UTF8Char(887));
        registerPairs(new UTF8Char(895), new UTF8Char(1011));
        registerPairs(new UTF8Char(902), new UTF8Char(940));
        registerPairs(new UTF8Char(904), new UTF8Char(941));
        registerPairs(new UTF8Char(905), new UTF8Char(942));
        registerPairs(new UTF8Char(906), new UTF8Char(943));
        registerPairs(new UTF8Char(908), new UTF8Char(972));
        registerPairs(new UTF8Char(910), new UTF8Char(973));
        registerPairs(new UTF8Char(911), new UTF8Char(974));
        registerPairs(new UTF8Char(913), new UTF8Char(945));
        registerPairs(new UTF8Char(914), new UTF8Char(946));
        registerPairs(new UTF8Char(915), new UTF8Char(947));
        registerPairs(new UTF8Char(916), new UTF8Char(948));
        registerPairs(new UTF8Char(917), new UTF8Char(949));
        registerPairs(new UTF8Char(918), new UTF8Char(950));
        registerPairs(new UTF8Char(919), new UTF8Char(951));
        registerPairs(new UTF8Char(920), new UTF8Char(952));
        registerPairs(new UTF8Char(921), new UTF8Char(953));
        registerPairs(new UTF8Char(922), new UTF8Char(954));
        registerPairs(new UTF8Char(923), new UTF8Char(955));
        registerPairs(new UTF8Char(924), new UTF8Char(956));
        registerPairs(new UTF8Char(925), new UTF8Char(957));
        registerPairs(new UTF8Char(926), new UTF8Char(958));
        registerPairs(new UTF8Char(927), new UTF8Char(959));
        registerPairs(new UTF8Char(928), new UTF8Char(960));
        registerPairs(new UTF8Char(929), new UTF8Char(961));
        registerPairs(new UTF8Char(931), new UTF8Char(963));
        registerPairs(new UTF8Char(932), new UTF8Char(964));
        registerPairs(new UTF8Char(933), new UTF8Char(965));
        registerPairs(new UTF8Char(934), new UTF8Char(966));
        registerPairs(new UTF8Char(935), new UTF8Char(967));
        registerPairs(new UTF8Char(936), new UTF8Char(968));
        registerPairs(new UTF8Char(937), new UTF8Char(969));
        registerPairs(new UTF8Char(938), new UTF8Char(970));
        registerPairs(new UTF8Char(939), new UTF8Char(971));
        registerPairs(new UTF8Char(975), new UTF8Char(983));
        registerPairs(new UTF8Char(984), new UTF8Char(985));
        registerPairs(new UTF8Char(986), new UTF8Char(987));
        registerPairs(new UTF8Char(988), new UTF8Char(989));
        registerPairs(new UTF8Char(990), new UTF8Char(991));
        registerPairs(new UTF8Char(992), new UTF8Char(993));
        registerPairs(new UTF8Char(994), new UTF8Char(995));
        registerPairs(new UTF8Char(996), new UTF8Char(997));
        registerPairs(new UTF8Char(998), new UTF8Char(999));
        registerPairs(new UTF8Char(1000), new UTF8Char(1001));
        registerPairs(new UTF8Char(1002), new UTF8Char(1003));
        registerPairs(new UTF8Char(1004), new UTF8Char(1005));
        registerPairs(new UTF8Char(1006), new UTF8Char(1007));
        registerPairs(new UTF8Char(1012), new UTF8Char(952));
        registerPairs(new UTF8Char(1015), new UTF8Char(1016));
        registerPairs(new UTF8Char(1017), new UTF8Char(1010));
        registerPairs(new UTF8Char(1018), new UTF8Char(1019));
        registerPairs(new UTF8Char(1021), new UTF8Char(891));
        registerPairs(new UTF8Char(1022), new UTF8Char(892));
        registerPairs(new UTF8Char(1023), new UTF8Char(893));
        registerPairs(new UTF8Char(1024), new UTF8Char(1104));
        registerPairs(new UTF8Char(1025), new UTF8Char(1105));
        registerPairs(new UTF8Char(1026), new UTF8Char(1106));
        registerPairs(new UTF8Char(1027), new UTF8Char(1107));
        registerPairs(new UTF8Char(1028), new UTF8Char(1108));
        registerPairs(new UTF8Char(1029), new UTF8Char(1109));
        registerPairs(new UTF8Char(1030), new UTF8Char(1110));
        registerPairs(new UTF8Char(1031), new UTF8Char(1111));
        registerPairs(new UTF8Char(1032), new UTF8Char(1112));
        registerPairs(new UTF8Char(1033), new UTF8Char(1113));
        registerPairs(new UTF8Char(1034), new UTF8Char(1114));
        registerPairs(new UTF8Char(1035), new UTF8Char(1115));
        registerPairs(new UTF8Char(1036), new UTF8Char(1116));
        registerPairs(new UTF8Char(1037), new UTF8Char(1117));
        registerPairs(new UTF8Char(1038), new UTF8Char(1118));
        registerPairs(new UTF8Char(1039), new UTF8Char(1119));
        registerPairs(new UTF8Char(1040), new UTF8Char(1072));
        registerPairs(new UTF8Char(1041), new UTF8Char(1073));
        registerPairs(new UTF8Char(1042), new UTF8Char(1074));
        registerPairs(new UTF8Char(1043), new UTF8Char(1075));
        registerPairs(new UTF8Char(1044), new UTF8Char(1076));
        registerPairs(new UTF8Char(1045), new UTF8Char(1077));
        registerPairs(new UTF8Char(1046), new UTF8Char(1078));
        registerPairs(new UTF8Char(1047), new UTF8Char(1079));
        registerPairs(new UTF8Char(1048), new UTF8Char(1080));
        registerPairs(new UTF8Char(1049), new UTF8Char(1081));
        registerPairs(new UTF8Char(1050), new UTF8Char(1082));
        registerPairs(new UTF8Char(1051), new UTF8Char(1083));
        registerPairs(new UTF8Char(1052), new UTF8Char(1084));
        registerPairs(new UTF8Char(1053), new UTF8Char(1085));
        registerPairs(new UTF8Char(1054), new UTF8Char(1086));
        registerPairs(new UTF8Char(1055), new UTF8Char(1087));
        registerPairs(new UTF8Char(1056), new UTF8Char(1088));
        registerPairs(new UTF8Char(1057), new UTF8Char(1089));
        registerPairs(new UTF8Char(1058), new UTF8Char(1090));
        registerPairs(new UTF8Char(1059), new UTF8Char(1091));
        registerPairs(new UTF8Char(1060), new UTF8Char(1092));
        registerPairs(new UTF8Char(1061), new UTF8Char(1093));
        registerPairs(new UTF8Char(1062), new UTF8Char(1094));
        registerPairs(new UTF8Char(1063), new UTF8Char(1095));
        registerPairs(new UTF8Char(1064), new UTF8Char(1096));
        registerPairs(new UTF8Char(1065), new UTF8Char(1097));
        registerPairs(new UTF8Char(1066), new UTF8Char(1098));
        registerPairs(new UTF8Char(1067), new UTF8Char(1099));
        registerPairs(new UTF8Char(1068), new UTF8Char(1100));
        registerPairs(new UTF8Char(1069), new UTF8Char(1101));
        registerPairs(new UTF8Char(1070), new UTF8Char(1102));
        registerPairs(new UTF8Char(1071), new UTF8Char(1103));
        registerPairs(new UTF8Char(1120), new UTF8Char(1121));
        registerPairs(new UTF8Char(1122), new UTF8Char(1123));
        registerPairs(new UTF8Char(1124), new UTF8Char(1125));
        registerPairs(new UTF8Char(1126), new UTF8Char(1127));
        registerPairs(new UTF8Char(1128), new UTF8Char(1129));
        registerPairs(new UTF8Char(1130), new UTF8Char(1131));
        registerPairs(new UTF8Char(1132), new UTF8Char(1133));
        registerPairs(new UTF8Char(1134), new UTF8Char(1135));
        registerPairs(new UTF8Char(1136), new UTF8Char(1137));
        registerPairs(new UTF8Char(1138), new UTF8Char(1139));
        registerPairs(new UTF8Char(1140), new UTF8Char(1141));
        registerPairs(new UTF8Char(1142), new UTF8Char(1143));
        registerPairs(new UTF8Char(1144), new UTF8Char(1145));
        registerPairs(new UTF8Char(1146), new UTF8Char(1147));
        registerPairs(new UTF8Char(1148), new UTF8Char(1149));
        registerPairs(new UTF8Char(1150), new UTF8Char(1151));
        registerPairs(new UTF8Char(1152), new UTF8Char(1153));
        registerPairs(new UTF8Char(1162), new UTF8Char(1163));
        registerPairs(new UTF8Char(1164), new UTF8Char(1165));
        registerPairs(new UTF8Char(1166), new UTF8Char(1167));
        registerPairs(new UTF8Char(1168), new UTF8Char(1169));
        registerPairs(new UTF8Char(1170), new UTF8Char(1171));
        registerPairs(new UTF8Char(1172), new UTF8Char(1173));
        registerPairs(new UTF8Char(1174), new UTF8Char(1175));
        registerPairs(new UTF8Char(1176), new UTF8Char(1177));
        registerPairs(new UTF8Char(1178), new UTF8Char(1179));
        registerPairs(new UTF8Char(1180), new UTF8Char(1181));
        registerPairs(new UTF8Char(1182), new UTF8Char(1183));
        registerPairs(new UTF8Char(1184), new UTF8Char(1185));
        registerPairs(new UTF8Char(1186), new UTF8Char(1187));
        registerPairs(new UTF8Char(1188), new UTF8Char(1189));
        registerPairs(new UTF8Char(1190), new UTF8Char(1191));
        registerPairs(new UTF8Char(1192), new UTF8Char(1193));
        registerPairs(new UTF8Char(1194), new UTF8Char(1195));
        registerPairs(new UTF8Char(1196), new UTF8Char(1197));
        registerPairs(new UTF8Char(1198), new UTF8Char(1199));
        registerPairs(new UTF8Char(1200), new UTF8Char(1201));
        registerPairs(new UTF8Char(1202), new UTF8Char(1203));
        registerPairs(new UTF8Char(1204), new UTF8Char(1205));
        registerPairs(new UTF8Char(1206), new UTF8Char(1207));
        registerPairs(new UTF8Char(1208), new UTF8Char(1209));
        registerPairs(new UTF8Char(1210), new UTF8Char(1211));
        registerPairs(new UTF8Char(1212), new UTF8Char(1213));
        registerPairs(new UTF8Char(1214), new UTF8Char(1215));
        registerPairs(new UTF8Char(1216), new UTF8Char(1231));
        registerPairs(new UTF8Char(1217), new UTF8Char(1218));
        registerPairs(new UTF8Char(1219), new UTF8Char(1220));
        registerPairs(new UTF8Char(1221), new UTF8Char(1222));
        registerPairs(new UTF8Char(1223), new UTF8Char(1224));
        registerPairs(new UTF8Char(1225), new UTF8Char(1226));
        registerPairs(new UTF8Char(1227), new UTF8Char(1228));
        registerPairs(new UTF8Char(1229), new UTF8Char(1230));
        registerPairs(new UTF8Char(1232), new UTF8Char(1233));
        registerPairs(new UTF8Char(1234), new UTF8Char(1235));
        registerPairs(new UTF8Char(1236), new UTF8Char(1237));
        registerPairs(new UTF8Char(1238), new UTF8Char(1239));
        registerPairs(new UTF8Char(1240), new UTF8Char(1241));
        registerPairs(new UTF8Char(1242), new UTF8Char(1243));
        registerPairs(new UTF8Char(1244), new UTF8Char(1245));
        registerPairs(new UTF8Char(1246), new UTF8Char(1247));
        registerPairs(new UTF8Char(1248), new UTF8Char(1249));
        registerPairs(new UTF8Char(1250), new UTF8Char(1251));
        registerPairs(new UTF8Char(1252), new UTF8Char(1253));
        registerPairs(new UTF8Char(1254), new UTF8Char(1255));
        registerPairs(new UTF8Char(1256), new UTF8Char(1257));
        registerPairs(new UTF8Char(1258), new UTF8Char(1259));
        registerPairs(new UTF8Char(1260), new UTF8Char(1261));
        registerPairs(new UTF8Char(1262), new UTF8Char(1263));
        registerPairs(new UTF8Char(1264), new UTF8Char(1265));
        registerPairs(new UTF8Char(1266), new UTF8Char(1267));
        registerPairs(new UTF8Char(1268), new UTF8Char(1269));
        registerPairs(new UTF8Char(1270), new UTF8Char(1271));
        registerPairs(new UTF8Char(1272), new UTF8Char(1273));
        registerPairs(new UTF8Char(1274), new UTF8Char(1275));
        registerPairs(new UTF8Char(1276), new UTF8Char(1277));
        registerPairs(new UTF8Char(1278), new UTF8Char(1279));
        registerPairs(new UTF8Char(1280), new UTF8Char(1281));
        registerPairs(new UTF8Char(1282), new UTF8Char(1283));
        registerPairs(new UTF8Char(1284), new UTF8Char(1285));
        registerPairs(new UTF8Char(1286), new UTF8Char(1287));
        registerPairs(new UTF8Char(1288), new UTF8Char(1289));
        registerPairs(new UTF8Char(1290), new UTF8Char(1291));
        registerPairs(new UTF8Char(1292), new UTF8Char(1293));
        registerPairs(new UTF8Char(1294), new UTF8Char(1295));
        registerPairs(new UTF8Char(1296), new UTF8Char(1297));
        registerPairs(new UTF8Char(1298), new UTF8Char(1299));
        registerPairs(new UTF8Char(1300), new UTF8Char(1301));
        registerPairs(new UTF8Char(1302), new UTF8Char(1303));
        registerPairs(new UTF8Char(1304), new UTF8Char(1305));
        registerPairs(new UTF8Char(1306), new UTF8Char(1307));
        registerPairs(new UTF8Char(1308), new UTF8Char(1309));
        registerPairs(new UTF8Char(1310), new UTF8Char(1311));
        registerPairs(new UTF8Char(1312), new UTF8Char(1313));
        registerPairs(new UTF8Char(1314), new UTF8Char(1315));
        registerPairs(new UTF8Char(1316), new UTF8Char(1317));
        registerPairs(new UTF8Char(1318), new UTF8Char(1319));
        registerPairs(new UTF8Char(1320), new UTF8Char(1321));
        registerPairs(new UTF8Char(1322), new UTF8Char(1323));
        registerPairs(new UTF8Char(1324), new UTF8Char(1325));
        registerPairs(new UTF8Char(1326), new UTF8Char(1327));
        registerPairs(new UTF8Char(1329), new UTF8Char(1377));
        registerPairs(new UTF8Char(1330), new UTF8Char(1378));
        registerPairs(new UTF8Char(1331), new UTF8Char(1379));
        registerPairs(new UTF8Char(1332), new UTF8Char(1380));
        registerPairs(new UTF8Char(1333), new UTF8Char(1381));
        registerPairs(new UTF8Char(1334), new UTF8Char(1382));
        registerPairs(new UTF8Char(1335), new UTF8Char(1383));
        registerPairs(new UTF8Char(1336), new UTF8Char(1384));
        registerPairs(new UTF8Char(1337), new UTF8Char(1385));
        registerPairs(new UTF8Char(1338), new UTF8Char(1386));
        registerPairs(new UTF8Char(1339), new UTF8Char(1387));
        registerPairs(new UTF8Char(1340), new UTF8Char(1388));
        registerPairs(new UTF8Char(1341), new UTF8Char(1389));
        registerPairs(new UTF8Char(1342), new UTF8Char(1390));
        registerPairs(new UTF8Char(1343), new UTF8Char(1391));
        registerPairs(new UTF8Char(1344), new UTF8Char(1392));
        registerPairs(new UTF8Char(1345), new UTF8Char(1393));
        registerPairs(new UTF8Char(1346), new UTF8Char(1394));
        registerPairs(new UTF8Char(1347), new UTF8Char(1395));
        registerPairs(new UTF8Char(1348), new UTF8Char(1396));
        registerPairs(new UTF8Char(1349), new UTF8Char(1397));
        registerPairs(new UTF8Char(1350), new UTF8Char(1398));
        registerPairs(new UTF8Char(1351), new UTF8Char(1399));
        registerPairs(new UTF8Char(1352), new UTF8Char(1400));
        registerPairs(new UTF8Char(1353), new UTF8Char(1401));
        registerPairs(new UTF8Char(1354), new UTF8Char(1402));
        registerPairs(new UTF8Char(1355), new UTF8Char(1403));
        registerPairs(new UTF8Char(1356), new UTF8Char(1404));
        registerPairs(new UTF8Char(1357), new UTF8Char(1405));
        registerPairs(new UTF8Char(1358), new UTF8Char(1406));
        registerPairs(new UTF8Char(1359), new UTF8Char(1407));
        registerPairs(new UTF8Char(1360), new UTF8Char(1408));
        registerPairs(new UTF8Char(1361), new UTF8Char(1409));
        registerPairs(new UTF8Char(1362), new UTF8Char(1410));
        registerPairs(new UTF8Char(1363), new UTF8Char(1411));
        registerPairs(new UTF8Char(1364), new UTF8Char(1412));
        registerPairs(new UTF8Char(1365), new UTF8Char(1413));
        registerPairs(new UTF8Char(1366), new UTF8Char(1414));
        registerPairs(new UTF8Char(4256), new UTF8Char(11520));
        registerPairs(new UTF8Char(4257), new UTF8Char(11521));
        registerPairs(new UTF8Char(4258), new UTF8Char(11522));
        registerPairs(new UTF8Char(4259), new UTF8Char(11523));
        registerPairs(new UTF8Char(4260), new UTF8Char(11524));
        registerPairs(new UTF8Char(4261), new UTF8Char(11525));
        registerPairs(new UTF8Char(4262), new UTF8Char(11526));
        registerPairs(new UTF8Char(4263), new UTF8Char(11527));
        registerPairs(new UTF8Char(4264), new UTF8Char(11528));
        registerPairs(new UTF8Char(4265), new UTF8Char(11529));
        registerPairs(new UTF8Char(4266), new UTF8Char(11530));
        registerPairs(new UTF8Char(4267), new UTF8Char(11531));
        registerPairs(new UTF8Char(4268), new UTF8Char(11532));
        registerPairs(new UTF8Char(4269), new UTF8Char(11533));
        registerPairs(new UTF8Char(4270), new UTF8Char(11534));
        registerPairs(new UTF8Char(4271), new UTF8Char(11535));
        registerPairs(new UTF8Char(4272), new UTF8Char(11536));
        registerPairs(new UTF8Char(4273), new UTF8Char(11537));
        registerPairs(new UTF8Char(4274), new UTF8Char(11538));
        registerPairs(new UTF8Char(4275), new UTF8Char(11539));
        registerPairs(new UTF8Char(4276), new UTF8Char(11540));
        registerPairs(new UTF8Char(4277), new UTF8Char(11541));
        registerPairs(new UTF8Char(4278), new UTF8Char(11542));
        registerPairs(new UTF8Char(4279), new UTF8Char(11543));
        registerPairs(new UTF8Char(4280), new UTF8Char(11544));
        registerPairs(new UTF8Char(4281), new UTF8Char(11545));
        registerPairs(new UTF8Char(4282), new UTF8Char(11546));
        registerPairs(new UTF8Char(4283), new UTF8Char(11547));
        registerPairs(new UTF8Char(4284), new UTF8Char(11548));
        registerPairs(new UTF8Char(4285), new UTF8Char(11549));
        registerPairs(new UTF8Char(4286), new UTF8Char(11550));
        registerPairs(new UTF8Char(4287), new UTF8Char(11551));
        registerPairs(new UTF8Char(4288), new UTF8Char(11552));
        registerPairs(new UTF8Char(4289), new UTF8Char(11553));
        registerPairs(new UTF8Char(4290), new UTF8Char(11554));
        registerPairs(new UTF8Char(4291), new UTF8Char(11555));
        registerPairs(new UTF8Char(4292), new UTF8Char(11556));
        registerPairs(new UTF8Char(4293), new UTF8Char(11557));
        registerPairs(new UTF8Char(4295), new UTF8Char(11559));
        registerPairs(new UTF8Char(4301), new UTF8Char(11565));
        registerPairs(new UTF8Char(5024), new UTF8Char(43888));
        registerPairs(new UTF8Char(5025), new UTF8Char(43889));
        registerPairs(new UTF8Char(5026), new UTF8Char(43890));
        registerPairs(new UTF8Char(5027), new UTF8Char(43891));
        registerPairs(new UTF8Char(5028), new UTF8Char(43892));
        registerPairs(new UTF8Char(5029), new UTF8Char(43893));
        registerPairs(new UTF8Char(5030), new UTF8Char(43894));
        registerPairs(new UTF8Char(5031), new UTF8Char(43895));
        registerPairs(new UTF8Char(5032), new UTF8Char(43896));
        registerPairs(new UTF8Char(5033), new UTF8Char(43897));
        registerPairs(new UTF8Char(5034), new UTF8Char(43898));
        registerPairs(new UTF8Char(5035), new UTF8Char(43899));
        registerPairs(new UTF8Char(5036), new UTF8Char(43900));
        registerPairs(new UTF8Char(5037), new UTF8Char(43901));
        registerPairs(new UTF8Char(5038), new UTF8Char(43902));
        registerPairs(new UTF8Char(5039), new UTF8Char(43903));
        registerPairs(new UTF8Char(5040), new UTF8Char(43904));
        registerPairs(new UTF8Char(5041), new UTF8Char(43905));
        registerPairs(new UTF8Char(5042), new UTF8Char(43906));
        registerPairs(new UTF8Char(5043), new UTF8Char(43907));
        registerPairs(new UTF8Char(5044), new UTF8Char(43908));
        registerPairs(new UTF8Char(5045), new UTF8Char(43909));
        registerPairs(new UTF8Char(5046), new UTF8Char(43910));
        registerPairs(new UTF8Char(5047), new UTF8Char(43911));
        registerPairs(new UTF8Char(5048), new UTF8Char(43912));
        registerPairs(new UTF8Char(5049), new UTF8Char(43913));
        registerPairs(new UTF8Char(5050), new UTF8Char(43914));
        registerPairs(new UTF8Char(5051), new UTF8Char(43915));
        registerPairs(new UTF8Char(5052), new UTF8Char(43916));
        registerPairs(new UTF8Char(5053), new UTF8Char(43917));
        registerPairs(new UTF8Char(5054), new UTF8Char(43918));
        registerPairs(new UTF8Char(5055), new UTF8Char(43919));
        registerPairs(new UTF8Char(5056), new UTF8Char(43920));
        registerPairs(new UTF8Char(5057), new UTF8Char(43921));
        registerPairs(new UTF8Char(5058), new UTF8Char(43922));
        registerPairs(new UTF8Char(5059), new UTF8Char(43923));
        registerPairs(new UTF8Char(5060), new UTF8Char(43924));
        registerPairs(new UTF8Char(5061), new UTF8Char(43925));
        registerPairs(new UTF8Char(5062), new UTF8Char(43926));
        registerPairs(new UTF8Char(5063), new UTF8Char(43927));
        registerPairs(new UTF8Char(5064), new UTF8Char(43928));
        registerPairs(new UTF8Char(5065), new UTF8Char(43929));
        registerPairs(new UTF8Char(5066), new UTF8Char(43930));
        registerPairs(new UTF8Char(5067), new UTF8Char(43931));
        registerPairs(new UTF8Char(5068), new UTF8Char(43932));
        registerPairs(new UTF8Char(5069), new UTF8Char(43933));
        registerPairs(new UTF8Char(5070), new UTF8Char(43934));
        registerPairs(new UTF8Char(5071), new UTF8Char(43935));
        registerPairs(new UTF8Char(5072), new UTF8Char(43936));
        registerPairs(new UTF8Char(5073), new UTF8Char(43937));
        registerPairs(new UTF8Char(5074), new UTF8Char(43938));
        registerPairs(new UTF8Char(5075), new UTF8Char(43939));
        registerPairs(new UTF8Char(5076), new UTF8Char(43940));
        registerPairs(new UTF8Char(5077), new UTF8Char(43941));
        registerPairs(new UTF8Char(5078), new UTF8Char(43942));
        registerPairs(new UTF8Char(5079), new UTF8Char(43943));
        registerPairs(new UTF8Char(5080), new UTF8Char(43944));
        registerPairs(new UTF8Char(5081), new UTF8Char(43945));
        registerPairs(new UTF8Char(5082), new UTF8Char(43946));
        registerPairs(new UTF8Char(5083), new UTF8Char(43947));
        registerPairs(new UTF8Char(5084), new UTF8Char(43948));
        registerPairs(new UTF8Char(5085), new UTF8Char(43949));
        registerPairs(new UTF8Char(5086), new UTF8Char(43950));
        registerPairs(new UTF8Char(5087), new UTF8Char(43951));
        registerPairs(new UTF8Char(5088), new UTF8Char(43952));
        registerPairs(new UTF8Char(5089), new UTF8Char(43953));
        registerPairs(new UTF8Char(5090), new UTF8Char(43954));
        registerPairs(new UTF8Char(5091), new UTF8Char(43955));
        registerPairs(new UTF8Char(5092), new UTF8Char(43956));
        registerPairs(new UTF8Char(5093), new UTF8Char(43957));
        registerPairs(new UTF8Char(5094), new UTF8Char(43958));
        registerPairs(new UTF8Char(5095), new UTF8Char(43959));
        registerPairs(new UTF8Char(5096), new UTF8Char(43960));
        registerPairs(new UTF8Char(5097), new UTF8Char(43961));
        registerPairs(new UTF8Char(5098), new UTF8Char(43962));
        registerPairs(new UTF8Char(5099), new UTF8Char(43963));
        registerPairs(new UTF8Char(5100), new UTF8Char(43964));
        registerPairs(new UTF8Char(5101), new UTF8Char(43965));
        registerPairs(new UTF8Char(5102), new UTF8Char(43966));
        registerPairs(new UTF8Char(5103), new UTF8Char(43967));
        registerPairs(new UTF8Char(5104), new UTF8Char(5112));
        registerPairs(new UTF8Char(5105), new UTF8Char(5113));
        registerPairs(new UTF8Char(5106), new UTF8Char(5114));
        registerPairs(new UTF8Char(5107), new UTF8Char(5115));
        registerPairs(new UTF8Char(5108), new UTF8Char(5116));
        registerPairs(new UTF8Char(5109), new UTF8Char(5117));
        registerPairs(new UTF8Char(7312), new UTF8Char(4304));
        registerPairs(new UTF8Char(7313), new UTF8Char(4305));
        registerPairs(new UTF8Char(7314), new UTF8Char(4306));
        registerPairs(new UTF8Char(7315), new UTF8Char(4307));
        registerPairs(new UTF8Char(7316), new UTF8Char(4308));
        registerPairs(new UTF8Char(7317), new UTF8Char(4309));
        registerPairs(new UTF8Char(7318), new UTF8Char(4310));
        registerPairs(new UTF8Char(7319), new UTF8Char(4311));
        registerPairs(new UTF8Char(7320), new UTF8Char(4312));
        registerPairs(new UTF8Char(7321), new UTF8Char(4313));
        registerPairs(new UTF8Char(7322), new UTF8Char(4314));
        registerPairs(new UTF8Char(7323), new UTF8Char(4315));
        registerPairs(new UTF8Char(7324), new UTF8Char(4316));
        registerPairs(new UTF8Char(7325), new UTF8Char(4317));
        registerPairs(new UTF8Char(7326), new UTF8Char(4318));
        registerPairs(new UTF8Char(7327), new UTF8Char(4319));
        registerPairs(new UTF8Char(7328), new UTF8Char(4320));
        registerPairs(new UTF8Char(7329), new UTF8Char(4321));
        registerPairs(new UTF8Char(7330), new UTF8Char(4322));
        registerPairs(new UTF8Char(7331), new UTF8Char(4323));
        registerPairs(new UTF8Char(7332), new UTF8Char(4324));
        registerPairs(new UTF8Char(7333), new UTF8Char(4325));
        registerPairs(new UTF8Char(7334), new UTF8Char(4326));
        registerPairs(new UTF8Char(7335), new UTF8Char(4327));
        registerPairs(new UTF8Char(7336), new UTF8Char(4328));
        registerPairs(new UTF8Char(7337), new UTF8Char(4329));
        registerPairs(new UTF8Char(7338), new UTF8Char(4330));
        registerPairs(new UTF8Char(7339), new UTF8Char(4331));
        registerPairs(new UTF8Char(7340), new UTF8Char(4332));
        registerPairs(new UTF8Char(7341), new UTF8Char(4333));
        registerPairs(new UTF8Char(7342), new UTF8Char(4334));
        registerPairs(new UTF8Char(7343), new UTF8Char(4335));
        registerPairs(new UTF8Char(7344), new UTF8Char(4336));
        registerPairs(new UTF8Char(7345), new UTF8Char(4337));
        registerPairs(new UTF8Char(7346), new UTF8Char(4338));
        registerPairs(new UTF8Char(7347), new UTF8Char(4339));
        registerPairs(new UTF8Char(7348), new UTF8Char(4340));
        registerPairs(new UTF8Char(7349), new UTF8Char(4341));
        registerPairs(new UTF8Char(7350), new UTF8Char(4342));
        registerPairs(new UTF8Char(7351), new UTF8Char(4343));
        registerPairs(new UTF8Char(7352), new UTF8Char(4344));
        registerPairs(new UTF8Char(7353), new UTF8Char(4345));
        registerPairs(new UTF8Char(7354), new UTF8Char(4346));
        registerPairs(new UTF8Char(7357), new UTF8Char(4349));
        registerPairs(new UTF8Char(7358), new UTF8Char(4350));
        registerPairs(new UTF8Char(7359), new UTF8Char(4351));
        registerPairs(new UTF8Char(7680), new UTF8Char(7681));
        registerPairs(new UTF8Char(7682), new UTF8Char(7683));
        registerPairs(new UTF8Char(7684), new UTF8Char(7685));
        registerPairs(new UTF8Char(7686), new UTF8Char(7687));
        registerPairs(new UTF8Char(7688), new UTF8Char(7689));
        registerPairs(new UTF8Char(7690), new UTF8Char(7691));
        registerPairs(new UTF8Char(7692), new UTF8Char(7693));
        registerPairs(new UTF8Char(7694), new UTF8Char(7695));
        registerPairs(new UTF8Char(7696), new UTF8Char(7697));
        registerPairs(new UTF8Char(7698), new UTF8Char(7699));
        registerPairs(new UTF8Char(7700), new UTF8Char(7701));
        registerPairs(new UTF8Char(7702), new UTF8Char(7703));
        registerPairs(new UTF8Char(7704), new UTF8Char(7705));
        registerPairs(new UTF8Char(7706), new UTF8Char(7707));
        registerPairs(new UTF8Char(7708), new UTF8Char(7709));
        registerPairs(new UTF8Char(7710), new UTF8Char(7711));
        registerPairs(new UTF8Char(7712), new UTF8Char(7713));
        registerPairs(new UTF8Char(7714), new UTF8Char(7715));
        registerPairs(new UTF8Char(7716), new UTF8Char(7717));
        registerPairs(new UTF8Char(7718), new UTF8Char(7719));
        registerPairs(new UTF8Char(7720), new UTF8Char(7721));
        registerPairs(new UTF8Char(7722), new UTF8Char(7723));
        registerPairs(new UTF8Char(7724), new UTF8Char(7725));
        registerPairs(new UTF8Char(7726), new UTF8Char(7727));
        registerPairs(new UTF8Char(7728), new UTF8Char(7729));
        registerPairs(new UTF8Char(7730), new UTF8Char(7731));
        registerPairs(new UTF8Char(7732), new UTF8Char(7733));
        registerPairs(new UTF8Char(7734), new UTF8Char(7735));
        registerPairs(new UTF8Char(7736), new UTF8Char(7737));
        registerPairs(new UTF8Char(7738), new UTF8Char(7739));
        registerPairs(new UTF8Char(7740), new UTF8Char(7741));
        registerPairs(new UTF8Char(7742), new UTF8Char(7743));
        registerPairs(new UTF8Char(7744), new UTF8Char(7745));
        registerPairs(new UTF8Char(7746), new UTF8Char(7747));
        registerPairs(new UTF8Char(7748), new UTF8Char(7749));
        registerPairs(new UTF8Char(7750), new UTF8Char(7751));
        registerPairs(new UTF8Char(7752), new UTF8Char(7753));
        registerPairs(new UTF8Char(7754), new UTF8Char(7755));
        registerPairs(new UTF8Char(7756), new UTF8Char(7757));
        registerPairs(new UTF8Char(7758), new UTF8Char(7759));
        registerPairs(new UTF8Char(7760), new UTF8Char(7761));
        registerPairs(new UTF8Char(7762), new UTF8Char(7763));
        registerPairs(new UTF8Char(7764), new UTF8Char(7765));
        registerPairs(new UTF8Char(7766), new UTF8Char(7767));
        registerPairs(new UTF8Char(7768), new UTF8Char(7769));
        registerPairs(new UTF8Char(7770), new UTF8Char(7771));
        registerPairs(new UTF8Char(7772), new UTF8Char(7773));
        registerPairs(new UTF8Char(7774), new UTF8Char(7775));
        registerPairs(new UTF8Char(7776), new UTF8Char(7777));
        registerPairs(new UTF8Char(7778), new UTF8Char(7779));
        registerPairs(new UTF8Char(7780), new UTF8Char(7781));
        registerPairs(new UTF8Char(7782), new UTF8Char(7783));
        registerPairs(new UTF8Char(7784), new UTF8Char(7785));
        registerPairs(new UTF8Char(7786), new UTF8Char(7787));
        registerPairs(new UTF8Char(7788), new UTF8Char(7789));
        registerPairs(new UTF8Char(7790), new UTF8Char(7791));
        registerPairs(new UTF8Char(7792), new UTF8Char(7793));
        registerPairs(new UTF8Char(7794), new UTF8Char(7795));
        registerPairs(new UTF8Char(7796), new UTF8Char(7797));
        registerPairs(new UTF8Char(7798), new UTF8Char(7799));
        registerPairs(new UTF8Char(7800), new UTF8Char(7801));
        registerPairs(new UTF8Char(7802), new UTF8Char(7803));
        registerPairs(new UTF8Char(7804), new UTF8Char(7805));
        registerPairs(new UTF8Char(7806), new UTF8Char(7807));
        registerPairs(new UTF8Char(7808), new UTF8Char(7809));
        registerPairs(new UTF8Char(7810), new UTF8Char(7811));
        registerPairs(new UTF8Char(7812), new UTF8Char(7813));
        registerPairs(new UTF8Char(7814), new UTF8Char(7815));
        registerPairs(new UTF8Char(7816), new UTF8Char(7817));
        registerPairs(new UTF8Char(7818), new UTF8Char(7819));
        registerPairs(new UTF8Char(7820), new UTF8Char(7821));
        registerPairs(new UTF8Char(7822), new UTF8Char(7823));
        registerPairs(new UTF8Char(7824), new UTF8Char(7825));
        registerPairs(new UTF8Char(7826), new UTF8Char(7827));
        registerPairs(new UTF8Char(7828), new UTF8Char(7829));
        registerPairs(new UTF8Char(7838), new UTF8Char(223));
        registerPairs(new UTF8Char(7840), new UTF8Char(7841));
        registerPairs(new UTF8Char(7842), new UTF8Char(7843));
        registerPairs(new UTF8Char(7844), new UTF8Char(7845));
        registerPairs(new UTF8Char(7846), new UTF8Char(7847));
        registerPairs(new UTF8Char(7848), new UTF8Char(7849));
        registerPairs(new UTF8Char(7850), new UTF8Char(7851));
        registerPairs(new UTF8Char(7852), new UTF8Char(7853));
        registerPairs(new UTF8Char(7854), new UTF8Char(7855));
        registerPairs(new UTF8Char(7856), new UTF8Char(7857));
        registerPairs(new UTF8Char(7858), new UTF8Char(7859));
        registerPairs(new UTF8Char(7860), new UTF8Char(7861));
        registerPairs(new UTF8Char(7862), new UTF8Char(7863));
        registerPairs(new UTF8Char(7864), new UTF8Char(7865));
        registerPairs(new UTF8Char(7866), new UTF8Char(7867));
        registerPairs(new UTF8Char(7868), new UTF8Char(7869));
        registerPairs(new UTF8Char(7870), new UTF8Char(7871));
        registerPairs(new UTF8Char(7872), new UTF8Char(7873));
        registerPairs(new UTF8Char(7874), new UTF8Char(7875));
        registerPairs(new UTF8Char(7876), new UTF8Char(7877));
        registerPairs(new UTF8Char(7878), new UTF8Char(7879));
        registerPairs(new UTF8Char(7880), new UTF8Char(7881));
        registerPairs(new UTF8Char(7882), new UTF8Char(7883));
        registerPairs(new UTF8Char(7884), new UTF8Char(7885));
        registerPairs(new UTF8Char(7886), new UTF8Char(7887));
        registerPairs(new UTF8Char(7888), new UTF8Char(7889));
        registerPairs(new UTF8Char(7890), new UTF8Char(7891));
        registerPairs(new UTF8Char(7892), new UTF8Char(7893));
        registerPairs(new UTF8Char(7894), new UTF8Char(7895));
        registerPairs(new UTF8Char(7896), new UTF8Char(7897));
        registerPairs(new UTF8Char(7898), new UTF8Char(7899));
        registerPairs(new UTF8Char(7900), new UTF8Char(7901));
        registerPairs(new UTF8Char(7902), new UTF8Char(7903));
        registerPairs(new UTF8Char(7904), new UTF8Char(7905));
        registerPairs(new UTF8Char(7906), new UTF8Char(7907));
        registerPairs(new UTF8Char(7908), new UTF8Char(7909));
        registerPairs(new UTF8Char(7910), new UTF8Char(7911));
        registerPairs(new UTF8Char(7912), new UTF8Char(7913));
        registerPairs(new UTF8Char(7914), new UTF8Char(7915));
        registerPairs(new UTF8Char(7916), new UTF8Char(7917));
        registerPairs(new UTF8Char(7918), new UTF8Char(7919));
        registerPairs(new UTF8Char(7920), new UTF8Char(7921));
        registerPairs(new UTF8Char(7922), new UTF8Char(7923));
        registerPairs(new UTF8Char(7924), new UTF8Char(7925));
        registerPairs(new UTF8Char(7926), new UTF8Char(7927));
        registerPairs(new UTF8Char(7928), new UTF8Char(7929));
        registerPairs(new UTF8Char(7930), new UTF8Char(7931));
        registerPairs(new UTF8Char(7932), new UTF8Char(7933));
        registerPairs(new UTF8Char(7934), new UTF8Char(7935));
        registerPairs(new UTF8Char(7944), new UTF8Char(7936));
        registerPairs(new UTF8Char(7945), new UTF8Char(7937));
        registerPairs(new UTF8Char(7946), new UTF8Char(7938));
        registerPairs(new UTF8Char(7947), new UTF8Char(7939));
        registerPairs(new UTF8Char(7948), new UTF8Char(7940));
        registerPairs(new UTF8Char(7949), new UTF8Char(7941));
        registerPairs(new UTF8Char(7950), new UTF8Char(7942));
        registerPairs(new UTF8Char(7951), new UTF8Char(7943));
        registerPairs(new UTF8Char(7960), new UTF8Char(7952));
        registerPairs(new UTF8Char(7961), new UTF8Char(7953));
        registerPairs(new UTF8Char(7962), new UTF8Char(7954));
        registerPairs(new UTF8Char(7963), new UTF8Char(7955));
        registerPairs(new UTF8Char(7964), new UTF8Char(7956));
        registerPairs(new UTF8Char(7965), new UTF8Char(7957));
        registerPairs(new UTF8Char(7976), new UTF8Char(7968));
        registerPairs(new UTF8Char(7977), new UTF8Char(7969));
        registerPairs(new UTF8Char(7978), new UTF8Char(7970));
        registerPairs(new UTF8Char(7979), new UTF8Char(7971));
        registerPairs(new UTF8Char(7980), new UTF8Char(7972));
        registerPairs(new UTF8Char(7981), new UTF8Char(7973));
        registerPairs(new UTF8Char(7982), new UTF8Char(7974));
        registerPairs(new UTF8Char(7983), new UTF8Char(7975));
        registerPairs(new UTF8Char(7992), new UTF8Char(7984));
        registerPairs(new UTF8Char(7993), new UTF8Char(7985));
        registerPairs(new UTF8Char(7994), new UTF8Char(7986));
        registerPairs(new UTF8Char(7995), new UTF8Char(7987));
        registerPairs(new UTF8Char(7996), new UTF8Char(7988));
        registerPairs(new UTF8Char(7997), new UTF8Char(7989));
        registerPairs(new UTF8Char(7998), new UTF8Char(7990));
        registerPairs(new UTF8Char(7999), new UTF8Char(7991));
        registerPairs(new UTF8Char(8008), new UTF8Char(8000));
        registerPairs(new UTF8Char(8009), new UTF8Char(8001));
        registerPairs(new UTF8Char(8010), new UTF8Char(8002));
        registerPairs(new UTF8Char(8011), new UTF8Char(8003));
        registerPairs(new UTF8Char(8012), new UTF8Char(8004));
        registerPairs(new UTF8Char(8013), new UTF8Char(8005));
        registerPairs(new UTF8Char(8025), new UTF8Char(8017));
        registerPairs(new UTF8Char(8027), new UTF8Char(8019));
        registerPairs(new UTF8Char(8029), new UTF8Char(8021));
        registerPairs(new UTF8Char(8031), new UTF8Char(8023));
        registerPairs(new UTF8Char(8040), new UTF8Char(8032));
        registerPairs(new UTF8Char(8041), new UTF8Char(8033));
        registerPairs(new UTF8Char(8042), new UTF8Char(8034));
        registerPairs(new UTF8Char(8043), new UTF8Char(8035));
        registerPairs(new UTF8Char(8044), new UTF8Char(8036));
        registerPairs(new UTF8Char(8045), new UTF8Char(8037));
        registerPairs(new UTF8Char(8046), new UTF8Char(8038));
        registerPairs(new UTF8Char(8047), new UTF8Char(8039));
        registerPairs(new UTF8Char(8072), new UTF8Char(8064));
        registerPairs(new UTF8Char(8073), new UTF8Char(8065));
        registerPairs(new UTF8Char(8074), new UTF8Char(8066));
        registerPairs(new UTF8Char(8075), new UTF8Char(8067));
        registerPairs(new UTF8Char(8076), new UTF8Char(8068));
        registerPairs(new UTF8Char(8077), new UTF8Char(8069));
        registerPairs(new UTF8Char(8078), new UTF8Char(8070));
        registerPairs(new UTF8Char(8079), new UTF8Char(8071));
        registerPairs(new UTF8Char(8088), new UTF8Char(8080));
        registerPairs(new UTF8Char(8089), new UTF8Char(8081));
        registerPairs(new UTF8Char(8090), new UTF8Char(8082));
        registerPairs(new UTF8Char(8091), new UTF8Char(8083));
        registerPairs(new UTF8Char(8092), new UTF8Char(8084));
        registerPairs(new UTF8Char(8093), new UTF8Char(8085));
        registerPairs(new UTF8Char(8094), new UTF8Char(8086));
        registerPairs(new UTF8Char(8095), new UTF8Char(8087));
        registerPairs(new UTF8Char(8104), new UTF8Char(8096));
        registerPairs(new UTF8Char(8105), new UTF8Char(8097));
        registerPairs(new UTF8Char(8106), new UTF8Char(8098));
        registerPairs(new UTF8Char(8107), new UTF8Char(8099));
        registerPairs(new UTF8Char(8108), new UTF8Char(8100));
        registerPairs(new UTF8Char(8109), new UTF8Char(8101));
        registerPairs(new UTF8Char(8110), new UTF8Char(8102));
        registerPairs(new UTF8Char(8111), new UTF8Char(8103));
        registerPairs(new UTF8Char(8120), new UTF8Char(8112));
        registerPairs(new UTF8Char(8121), new UTF8Char(8113));
        registerPairs(new UTF8Char(8122), new UTF8Char(8048));
        registerPairs(new UTF8Char(8123), new UTF8Char(8049));
        registerPairs(new UTF8Char(8124), new UTF8Char(8115));
        registerPairs(new UTF8Char(8136), new UTF8Char(8050));
        registerPairs(new UTF8Char(8137), new UTF8Char(8051));
        registerPairs(new UTF8Char(8138), new UTF8Char(8052));
        registerPairs(new UTF8Char(8139), new UTF8Char(8053));
        registerPairs(new UTF8Char(8140), new UTF8Char(8131));
        registerPairs(new UTF8Char(8152), new UTF8Char(8144));
        registerPairs(new UTF8Char(8153), new UTF8Char(8145));
        registerPairs(new UTF8Char(8154), new UTF8Char(8054));
        registerPairs(new UTF8Char(8155), new UTF8Char(8055));
        registerPairs(new UTF8Char(8168), new UTF8Char(8160));
        registerPairs(new UTF8Char(8169), new UTF8Char(8161));
        registerPairs(new UTF8Char(8170), new UTF8Char(8058));
        registerPairs(new UTF8Char(8171), new UTF8Char(8059));
        registerPairs(new UTF8Char(8172), new UTF8Char(8165));
        registerPairs(new UTF8Char(8184), new UTF8Char(8056));
        registerPairs(new UTF8Char(8185), new UTF8Char(8057));
        registerPairs(new UTF8Char(8186), new UTF8Char(8060));
        registerPairs(new UTF8Char(8187), new UTF8Char(8061));
        registerPairs(new UTF8Char(8188), new UTF8Char(8179));
        registerPairs(new UTF8Char(8486), new UTF8Char(969));
        registerPairs(new UTF8Char(8490), new UTF8Char(107));
        registerPairs(new UTF8Char(8491), new UTF8Char(229));
        registerPairs(new UTF8Char(8498), new UTF8Char(8526));
        registerPairs(new UTF8Char(8544), new UTF8Char(8560));
        registerPairs(new UTF8Char(8545), new UTF8Char(8561));
        registerPairs(new UTF8Char(8546), new UTF8Char(8562));
        registerPairs(new UTF8Char(8547), new UTF8Char(8563));
        registerPairs(new UTF8Char(8548), new UTF8Char(8564));
        registerPairs(new UTF8Char(8549), new UTF8Char(8565));
        registerPairs(new UTF8Char(8550), new UTF8Char(8566));
        registerPairs(new UTF8Char(8551), new UTF8Char(8567));
        registerPairs(new UTF8Char(8552), new UTF8Char(8568));
        registerPairs(new UTF8Char(8553), new UTF8Char(8569));
        registerPairs(new UTF8Char(8554), new UTF8Char(8570));
        registerPairs(new UTF8Char(8555), new UTF8Char(8571));
        registerPairs(new UTF8Char(8556), new UTF8Char(8572));
        registerPairs(new UTF8Char(8557), new UTF8Char(8573));
        registerPairs(new UTF8Char(8558), new UTF8Char(8574));
        registerPairs(new UTF8Char(8559), new UTF8Char(8575));
        registerPairs(new UTF8Char(8579), new UTF8Char(8580));
        registerPairs(new UTF8Char(9398), new UTF8Char(9424));
        registerPairs(new UTF8Char(9399), new UTF8Char(9425));
        registerPairs(new UTF8Char(9400), new UTF8Char(9426));
        registerPairs(new UTF8Char(9401), new UTF8Char(9427));
        registerPairs(new UTF8Char(9402), new UTF8Char(9428));
        registerPairs(new UTF8Char(9403), new UTF8Char(9429));
        registerPairs(new UTF8Char(9404), new UTF8Char(9430));
        registerPairs(new UTF8Char(9405), new UTF8Char(9431));
        registerPairs(new UTF8Char(9406), new UTF8Char(9432));
        registerPairs(new UTF8Char(9407), new UTF8Char(9433));
        registerPairs(new UTF8Char(9408), new UTF8Char(9434));
        registerPairs(new UTF8Char(9409), new UTF8Char(9435));
        registerPairs(new UTF8Char(9410), new UTF8Char(9436));
        registerPairs(new UTF8Char(9411), new UTF8Char(9437));
        registerPairs(new UTF8Char(9412), new UTF8Char(9438));
        registerPairs(new UTF8Char(9413), new UTF8Char(9439));
        registerPairs(new UTF8Char(9414), new UTF8Char(9440));
        registerPairs(new UTF8Char(9415), new UTF8Char(9441));
        registerPairs(new UTF8Char(9416), new UTF8Char(9442));
        registerPairs(new UTF8Char(9417), new UTF8Char(9443));
        registerPairs(new UTF8Char(9418), new UTF8Char(9444));
        registerPairs(new UTF8Char(9419), new UTF8Char(9445));
        registerPairs(new UTF8Char(9420), new UTF8Char(9446));
        registerPairs(new UTF8Char(9421), new UTF8Char(9447));
        registerPairs(new UTF8Char(9422), new UTF8Char(9448));
        registerPairs(new UTF8Char(9423), new UTF8Char(9449));
        registerPairs(new UTF8Char(11264), new UTF8Char(11312));
        registerPairs(new UTF8Char(11265), new UTF8Char(11313));
        registerPairs(new UTF8Char(11266), new UTF8Char(11314));
        registerPairs(new UTF8Char(11267), new UTF8Char(11315));
        registerPairs(new UTF8Char(11268), new UTF8Char(11316));
        registerPairs(new UTF8Char(11269), new UTF8Char(11317));
        registerPairs(new UTF8Char(11270), new UTF8Char(11318));
        registerPairs(new UTF8Char(11271), new UTF8Char(11319));
        registerPairs(new UTF8Char(11272), new UTF8Char(11320));
        registerPairs(new UTF8Char(11273), new UTF8Char(11321));
        registerPairs(new UTF8Char(11274), new UTF8Char(11322));
        registerPairs(new UTF8Char(11275), new UTF8Char(11323));
        registerPairs(new UTF8Char(11276), new UTF8Char(11324));
        registerPairs(new UTF8Char(11277), new UTF8Char(11325));
        registerPairs(new UTF8Char(11278), new UTF8Char(11326));
        registerPairs(new UTF8Char(11279), new UTF8Char(11327));
        registerPairs(new UTF8Char(11280), new UTF8Char(11328));
        registerPairs(new UTF8Char(11281), new UTF8Char(11329));
        registerPairs(new UTF8Char(11282), new UTF8Char(11330));
        registerPairs(new UTF8Char(11283), new UTF8Char(11331));
        registerPairs(new UTF8Char(11284), new UTF8Char(11332));
        registerPairs(new UTF8Char(11285), new UTF8Char(11333));
        registerPairs(new UTF8Char(11286), new UTF8Char(11334));
        registerPairs(new UTF8Char(11287), new UTF8Char(11335));
        registerPairs(new UTF8Char(11288), new UTF8Char(11336));
        registerPairs(new UTF8Char(11289), new UTF8Char(11337));
        registerPairs(new UTF8Char(11290), new UTF8Char(11338));
        registerPairs(new UTF8Char(11291), new UTF8Char(11339));
        registerPairs(new UTF8Char(11292), new UTF8Char(11340));
        registerPairs(new UTF8Char(11293), new UTF8Char(11341));
        registerPairs(new UTF8Char(11294), new UTF8Char(11342));
        registerPairs(new UTF8Char(11295), new UTF8Char(11343));
        registerPairs(new UTF8Char(11296), new UTF8Char(11344));
        registerPairs(new UTF8Char(11297), new UTF8Char(11345));
        registerPairs(new UTF8Char(11298), new UTF8Char(11346));
        registerPairs(new UTF8Char(11299), new UTF8Char(11347));
        registerPairs(new UTF8Char(11300), new UTF8Char(11348));
        registerPairs(new UTF8Char(11301), new UTF8Char(11349));
        registerPairs(new UTF8Char(11302), new UTF8Char(11350));
        registerPairs(new UTF8Char(11303), new UTF8Char(11351));
        registerPairs(new UTF8Char(11304), new UTF8Char(11352));
        registerPairs(new UTF8Char(11305), new UTF8Char(11353));
        registerPairs(new UTF8Char(11306), new UTF8Char(11354));
        registerPairs(new UTF8Char(11307), new UTF8Char(11355));
        registerPairs(new UTF8Char(11308), new UTF8Char(11356));
        registerPairs(new UTF8Char(11309), new UTF8Char(11357));
        registerPairs(new UTF8Char(11310), new UTF8Char(11358));
        registerPairs(new UTF8Char(11360), new UTF8Char(11361));
        registerPairs(new UTF8Char(11362), new UTF8Char(619));
        registerPairs(new UTF8Char(11363), new UTF8Char(7549));
        registerPairs(new UTF8Char(11364), new UTF8Char(637));
        registerPairs(new UTF8Char(11367), new UTF8Char(11368));
        registerPairs(new UTF8Char(11369), new UTF8Char(11370));
        registerPairs(new UTF8Char(11371), new UTF8Char(11372));
        registerPairs(new UTF8Char(11373), new UTF8Char(593));
        registerPairs(new UTF8Char(11374), new UTF8Char(625));
        registerPairs(new UTF8Char(11375), new UTF8Char(592));
        registerPairs(new UTF8Char(11376), new UTF8Char(594));
        registerPairs(new UTF8Char(11378), new UTF8Char(11379));
        registerPairs(new UTF8Char(11381), new UTF8Char(11382));
        registerPairs(new UTF8Char(11390), new UTF8Char(575));
        registerPairs(new UTF8Char(11391), new UTF8Char(576));
        registerPairs(new UTF8Char(11392), new UTF8Char(11393));
        registerPairs(new UTF8Char(11394), new UTF8Char(11395));
        registerPairs(new UTF8Char(11396), new UTF8Char(11397));
        registerPairs(new UTF8Char(11398), new UTF8Char(11399));
        registerPairs(new UTF8Char(11400), new UTF8Char(11401));
        registerPairs(new UTF8Char(11402), new UTF8Char(11403));
        registerPairs(new UTF8Char(11404), new UTF8Char(11405));
        registerPairs(new UTF8Char(11406), new UTF8Char(11407));
        registerPairs(new UTF8Char(11408), new UTF8Char(11409));
        registerPairs(new UTF8Char(11410), new UTF8Char(11411));
        registerPairs(new UTF8Char(11412), new UTF8Char(11413));
        registerPairs(new UTF8Char(11414), new UTF8Char(11415));
        registerPairs(new UTF8Char(11416), new UTF8Char(11417));
        registerPairs(new UTF8Char(11418), new UTF8Char(11419));
        registerPairs(new UTF8Char(11420), new UTF8Char(11421));
        registerPairs(new UTF8Char(11422), new UTF8Char(11423));
        registerPairs(new UTF8Char(11424), new UTF8Char(11425));
        registerPairs(new UTF8Char(11426), new UTF8Char(11427));
        registerPairs(new UTF8Char(11428), new UTF8Char(11429));
        registerPairs(new UTF8Char(11430), new UTF8Char(11431));
        registerPairs(new UTF8Char(11432), new UTF8Char(11433));
        registerPairs(new UTF8Char(11434), new UTF8Char(11435));
        registerPairs(new UTF8Char(11436), new UTF8Char(11437));
        registerPairs(new UTF8Char(11438), new UTF8Char(11439));
        registerPairs(new UTF8Char(11440), new UTF8Char(11441));
        registerPairs(new UTF8Char(11442), new UTF8Char(11443));
        registerPairs(new UTF8Char(11444), new UTF8Char(11445));
        registerPairs(new UTF8Char(11446), new UTF8Char(11447));
        registerPairs(new UTF8Char(11448), new UTF8Char(11449));
        registerPairs(new UTF8Char(11450), new UTF8Char(11451));
        registerPairs(new UTF8Char(11452), new UTF8Char(11453));
        registerPairs(new UTF8Char(11454), new UTF8Char(11455));
        registerPairs(new UTF8Char(11456), new UTF8Char(11457));
        registerPairs(new UTF8Char(11458), new UTF8Char(11459));
        registerPairs(new UTF8Char(11460), new UTF8Char(11461));
        registerPairs(new UTF8Char(11462), new UTF8Char(11463));
        registerPairs(new UTF8Char(11464), new UTF8Char(11465));
        registerPairs(new UTF8Char(11466), new UTF8Char(11467));
        registerPairs(new UTF8Char(11468), new UTF8Char(11469));
        registerPairs(new UTF8Char(11470), new UTF8Char(11471));
        registerPairs(new UTF8Char(11472), new UTF8Char(11473));
        registerPairs(new UTF8Char(11474), new UTF8Char(11475));
        registerPairs(new UTF8Char(11476), new UTF8Char(11477));
        registerPairs(new UTF8Char(11478), new UTF8Char(11479));
        registerPairs(new UTF8Char(11480), new UTF8Char(11481));
        registerPairs(new UTF8Char(11482), new UTF8Char(11483));
        registerPairs(new UTF8Char(11484), new UTF8Char(11485));
        registerPairs(new UTF8Char(11486), new UTF8Char(11487));
        registerPairs(new UTF8Char(11488), new UTF8Char(11489));
        registerPairs(new UTF8Char(11490), new UTF8Char(11491));
        registerPairs(new UTF8Char(11499), new UTF8Char(11500));
        registerPairs(new UTF8Char(11501), new UTF8Char(11502));
        registerPairs(new UTF8Char(11506), new UTF8Char(11507));
        registerPairs(new UTF8Char(42560), new UTF8Char(42561));
        registerPairs(new UTF8Char(42562), new UTF8Char(42563));
        registerPairs(new UTF8Char(42564), new UTF8Char(42565));
        registerPairs(new UTF8Char(42566), new UTF8Char(42567));
        registerPairs(new UTF8Char(42568), new UTF8Char(42569));
        registerPairs(new UTF8Char(42570), new UTF8Char(42571));
        registerPairs(new UTF8Char(42572), new UTF8Char(42573));
        registerPairs(new UTF8Char(42574), new UTF8Char(42575));
        registerPairs(new UTF8Char(42576), new UTF8Char(42577));
        registerPairs(new UTF8Char(42578), new UTF8Char(42579));
        registerPairs(new UTF8Char(42580), new UTF8Char(42581));
        registerPairs(new UTF8Char(42582), new UTF8Char(42583));
        registerPairs(new UTF8Char(42584), new UTF8Char(42585));
        registerPairs(new UTF8Char(42586), new UTF8Char(42587));
        registerPairs(new UTF8Char(42588), new UTF8Char(42589));
        registerPairs(new UTF8Char(42590), new UTF8Char(42591));
        registerPairs(new UTF8Char(42592), new UTF8Char(42593));
        registerPairs(new UTF8Char(42594), new UTF8Char(42595));
        registerPairs(new UTF8Char(42596), new UTF8Char(42597));
        registerPairs(new UTF8Char(42598), new UTF8Char(42599));
        registerPairs(new UTF8Char(42600), new UTF8Char(42601));
        registerPairs(new UTF8Char(42602), new UTF8Char(42603));
        registerPairs(new UTF8Char(42604), new UTF8Char(42605));
        registerPairs(new UTF8Char(42624), new UTF8Char(42625));
        registerPairs(new UTF8Char(42626), new UTF8Char(42627));
        registerPairs(new UTF8Char(42628), new UTF8Char(42629));
        registerPairs(new UTF8Char(42630), new UTF8Char(42631));
        registerPairs(new UTF8Char(42632), new UTF8Char(42633));
        registerPairs(new UTF8Char(42634), new UTF8Char(42635));
        registerPairs(new UTF8Char(42636), new UTF8Char(42637));
        registerPairs(new UTF8Char(42638), new UTF8Char(42639));
        registerPairs(new UTF8Char(42640), new UTF8Char(42641));
        registerPairs(new UTF8Char(42642), new UTF8Char(42643));
        registerPairs(new UTF8Char(42644), new UTF8Char(42645));
        registerPairs(new UTF8Char(42646), new UTF8Char(42647));
        registerPairs(new UTF8Char(42648), new UTF8Char(42649));
        registerPairs(new UTF8Char(42650), new UTF8Char(42651));
        registerPairs(new UTF8Char(42786), new UTF8Char(42787));
        registerPairs(new UTF8Char(42788), new UTF8Char(42789));
        registerPairs(new UTF8Char(42790), new UTF8Char(42791));
        registerPairs(new UTF8Char(42792), new UTF8Char(42793));
        registerPairs(new UTF8Char(42794), new UTF8Char(42795));
        registerPairs(new UTF8Char(42796), new UTF8Char(42797));
        registerPairs(new UTF8Char(42798), new UTF8Char(42799));
        registerPairs(new UTF8Char(42802), new UTF8Char(42803));
        registerPairs(new UTF8Char(42804), new UTF8Char(42805));
        registerPairs(new UTF8Char(42806), new UTF8Char(42807));
        registerPairs(new UTF8Char(42808), new UTF8Char(42809));
        registerPairs(new UTF8Char(42810), new UTF8Char(42811));
        registerPairs(new UTF8Char(42812), new UTF8Char(42813));
        registerPairs(new UTF8Char(42814), new UTF8Char(42815));
        registerPairs(new UTF8Char(42816), new UTF8Char(42817));
        registerPairs(new UTF8Char(42818), new UTF8Char(42819));
        registerPairs(new UTF8Char(42820), new UTF8Char(42821));
        registerPairs(new UTF8Char(42822), new UTF8Char(42823));
        registerPairs(new UTF8Char(42824), new UTF8Char(42825));
        registerPairs(new UTF8Char(42826), new UTF8Char(42827));
        registerPairs(new UTF8Char(42828), new UTF8Char(42829));
        registerPairs(new UTF8Char(42830), new UTF8Char(42831));
        registerPairs(new UTF8Char(42832), new UTF8Char(42833));
        registerPairs(new UTF8Char(42834), new UTF8Char(42835));
        registerPairs(new UTF8Char(42836), new UTF8Char(42837));
        registerPairs(new UTF8Char(42838), new UTF8Char(42839));
        registerPairs(new UTF8Char(42840), new UTF8Char(42841));
        registerPairs(new UTF8Char(42842), new UTF8Char(42843));
        registerPairs(new UTF8Char(42844), new UTF8Char(42845));
        registerPairs(new UTF8Char(42846), new UTF8Char(42847));
        registerPairs(new UTF8Char(42848), new UTF8Char(42849));
        registerPairs(new UTF8Char(42850), new UTF8Char(42851));
        registerPairs(new UTF8Char(42852), new UTF8Char(42853));
        registerPairs(new UTF8Char(42854), new UTF8Char(42855));
        registerPairs(new UTF8Char(42856), new UTF8Char(42857));
        registerPairs(new UTF8Char(42858), new UTF8Char(42859));
        registerPairs(new UTF8Char(42860), new UTF8Char(42861));
        registerPairs(new UTF8Char(42862), new UTF8Char(42863));
        registerPairs(new UTF8Char(42873), new UTF8Char(42874));
        registerPairs(new UTF8Char(42875), new UTF8Char(42876));
        registerPairs(new UTF8Char(42877), new UTF8Char(7545));
        registerPairs(new UTF8Char(42878), new UTF8Char(42879));
        registerPairs(new UTF8Char(42880), new UTF8Char(42881));
        registerPairs(new UTF8Char(42882), new UTF8Char(42883));
        registerPairs(new UTF8Char(42884), new UTF8Char(42885));
        registerPairs(new UTF8Char(42886), new UTF8Char(42887));
        registerPairs(new UTF8Char(42891), new UTF8Char(42892));
        registerPairs(new UTF8Char(42893), new UTF8Char(613));
        registerPairs(new UTF8Char(42896), new UTF8Char(42897));
        registerPairs(new UTF8Char(42898), new UTF8Char(42899));
        registerPairs(new UTF8Char(42902), new UTF8Char(42903));
        registerPairs(new UTF8Char(42904), new UTF8Char(42905));
        registerPairs(new UTF8Char(42906), new UTF8Char(42907));
        registerPairs(new UTF8Char(42908), new UTF8Char(42909));
        registerPairs(new UTF8Char(42910), new UTF8Char(42911));
        registerPairs(new UTF8Char(42912), new UTF8Char(42913));
        registerPairs(new UTF8Char(42914), new UTF8Char(42915));
        registerPairs(new UTF8Char(42916), new UTF8Char(42917));
        registerPairs(new UTF8Char(42918), new UTF8Char(42919));
        registerPairs(new UTF8Char(42920), new UTF8Char(42921));
        registerPairs(new UTF8Char(42922), new UTF8Char(614));
        registerPairs(new UTF8Char(42923), new UTF8Char(604));
        registerPairs(new UTF8Char(42924), new UTF8Char(609));
        registerPairs(new UTF8Char(42925), new UTF8Char(620));
        registerPairs(new UTF8Char(42926), new UTF8Char(618));
        registerPairs(new UTF8Char(42928), new UTF8Char(670));
        registerPairs(new UTF8Char(42929), new UTF8Char(647));
        registerPairs(new UTF8Char(42930), new UTF8Char(669));
        registerPairs(new UTF8Char(42931), new UTF8Char(43859));
        registerPairs(new UTF8Char(42932), new UTF8Char(42933));
        registerPairs(new UTF8Char(42934), new UTF8Char(42935));
        registerPairs(new UTF8Char(42936), new UTF8Char(42937));
        registerPairs(new UTF8Char(42938), new UTF8Char(42939));
        registerPairs(new UTF8Char(42940), new UTF8Char(42941));
        registerPairs(new UTF8Char(42942), new UTF8Char(42943));
        registerPairs(new UTF8Char(42946), new UTF8Char(42947));
        registerPairs(new UTF8Char(42948), new UTF8Char(42900));
        registerPairs(new UTF8Char(42949), new UTF8Char(642));
        registerPairs(new UTF8Char(42950), new UTF8Char(7566));
        registerPairs(new UTF8Char(65313), new UTF8Char(65345));
        registerPairs(new UTF8Char(65314), new UTF8Char(65346));
        registerPairs(new UTF8Char(65315), new UTF8Char(65347));
        registerPairs(new UTF8Char(65316), new UTF8Char(65348));
        registerPairs(new UTF8Char(65317), new UTF8Char(65349));
        registerPairs(new UTF8Char(65318), new UTF8Char(65350));
        registerPairs(new UTF8Char(65319), new UTF8Char(65351));
        registerPairs(new UTF8Char(65320), new UTF8Char(65352));
        registerPairs(new UTF8Char(65321), new UTF8Char(65353));
        registerPairs(new UTF8Char(65322), new UTF8Char(65354));
        registerPairs(new UTF8Char(65323), new UTF8Char(65355));
        registerPairs(new UTF8Char(65324), new UTF8Char(65356));
        registerPairs(new UTF8Char(65325), new UTF8Char(65357));
        registerPairs(new UTF8Char(65326), new UTF8Char(65358));
        registerPairs(new UTF8Char(65327), new UTF8Char(65359));
        registerPairs(new UTF8Char(65328), new UTF8Char(65360));
        registerPairs(new UTF8Char(65329), new UTF8Char(65361));
        registerPairs(new UTF8Char(65330), new UTF8Char(65362));
        registerPairs(new UTF8Char(65331), new UTF8Char(65363));
        registerPairs(new UTF8Char(65332), new UTF8Char(65364));
        registerPairs(new UTF8Char(65333), new UTF8Char(65365));
        registerPairs(new UTF8Char(65334), new UTF8Char(65366));
        registerPairs(new UTF8Char(65335), new UTF8Char(65367));
        registerPairs(new UTF8Char(65336), new UTF8Char(65368));
        registerPairs(new UTF8Char(65337), new UTF8Char(65369));
        registerPairs(new UTF8Char(65338), new UTF8Char(65370));
    }
}
