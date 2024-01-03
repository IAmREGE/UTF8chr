package rege.rege.utf8chr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UTF8Sequence
implements Comparable<UTF8Sequence>, Iterable<UTF8Char> {
    private final UTF8Char[] chars;

    private static final Map<UTF8Char, Iterable<UTF8Char>> TOLOWERS;
    private static final Map<UTF8Char, Iterable<UTF8Char>> TOUPPERS;

    public UTF8Sequence(UTF8Char[] chars) {
        this.chars = new UTF8Char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            this.chars[i] = chars[i];
        }
    }

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

    public UTF8Sequence(long[] codepoints) {
        this.chars = new UTF8Char[codepoints.length];
        for (int i = 0; i < codepoints.length; i++) {
            this.chars[i] = new UTF8Char(codepoints[i]);
        }
    }

    public UTF8Sequence(Long[] codepoints) {
        this.chars = new UTF8Char[codepoints.length];
        for (int i = 0; i < codepoints.length; i++) {
            this.chars[i] = new UTF8Char(codepoints[i].longValue());
        }
    }

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

    public UTF8Sequence(long i) {
        this(Long.toString(i));
    }

    public UTF8Sequence(int i) {
        this(Integer.toString(i));
    }

    public UTF8Sequence(short i) {
        this(Short.toString(i));
    }

    public UTF8Sequence(byte i) {
        this(Byte.toString(i));
    }

    public UTF8Sequence(float i) {
        this(Float.toString(i));
    }

    public UTF8Sequence(double i) {
        this(Double.toString(i));
    }

    public UTF8Sequence(UTF8Char i) {
        this(new UTF8Char[]{i});
    }

    public UTF8Sequence(char i) {
        this(new UTF8Char[]{new UTF8Char(i)});
    }

    public UTF8Sequence() {
        this(new long[0]);
    }

    @Override
    public Iterator<UTF8Char> iterator() {
        List<UTF8Char> LIST = new ArrayList<UTF8Char>();
        for (int i = 0; i < this.chars.length; i++) {
            LIST.add(this.chars[i]);
        }
        return LIST.iterator();
    }

    public UTF8Sequence[] singles() {
        UTF8Sequence[] R = new UTF8Sequence[this.chars.length];
        for (int i = 0; i < R.length; i++) {
            R[i] = new UTF8Sequence(this.chars[i]);
        }
        return R;
    }

    public int length() {
        return this.chars.length;
    }

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

    public UTF8Char charAt(int i) throws IndexOutOfBoundsException {
        return this.chars[(i < 0) ? this.chars.length + i : i];
    }

    @Override
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

    public boolean isEmpty() {
        return this.chars.length == 0;
    }

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

    public UTF8Sequence repeat(int times) {
        if (times <= 0) {
            return new UTF8Sequence();
        }
        times--;
        final UTF8Sequence[] R = new UTF8Sequence[times];
        for (int i = 0; i < times; i++) {
            R[i] = this;
        }
        return this.concat(R);
    }

    public UTF8Sequence slice(Integer start, Integer stop, Integer step) {
        // TODO
        return new UTF8Sequence();
    }

    public UTF8Sequence slice(Integer start, Integer stop) {
        return this.slice(start, stop, Integer.valueOf(1));
    }

    public UTF8Sequence subSequence(int start, int stop)
    throws IndexOutOfBoundsException {
        if (start > stop) {
            throw new IndexOutOfBoundsException();
        }
        final UTF8Char[] NEWS = new UTF8Char[stop - start];
        for (int i = start; i < stop; i++) {
            NEWS[i - start] = this.chars[i];
        }
        return new UTF8Sequence(NEWS);
    }

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

    public UTF8Sequence deescape() {
        // TODO
        return new UTF8Sequence();
    }

    public UTF8Sequence toUpperCase() {
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

    public UTF8Sequence toLowerCase() {
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
        // TODO
        return null;
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