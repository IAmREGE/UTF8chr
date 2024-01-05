package rege.rege.utf8chr;

public class UTF8Char implements Comparable<UTF8Char> {
    private final byte[] unit;

    /**
     * End of File
     */
    public static final UTF8Char EOF = new UTF8Char(-1L);
    /**
     * Null char
     */
    public static final UTF8Char NUL = new UTF8Char(0);
    /**
     * Start of Heading
     */
    public static final UTF8Char SOH = new UTF8Char(1);
    /**
     * Start of Text
     */
    public static final UTF8Char STX = new UTF8Char(2);
    /**
     * End of Text
     */
    public static final UTF8Char ETX = new UTF8Char(3);
    /**
     * End of Transmission
     */
    public static final UTF8Char EOT = new UTF8Char(4);
    /**
     * Enquiry
     */
    public static final UTF8Char ENQ = new UTF8Char(5);
    /**
     * Acknowledgment
     */
    public static final UTF8Char ACK = new UTF8Char(6);
    /**
     * Bell
     */
    public static final UTF8Char BEL = new UTF8Char(7);
    /**
     * Back Space
     */
    public static final UTF8Char BS = new UTF8Char(8);
    /**
     * Horizontal Tab
     */
    public static final UTF8Char HT = new UTF8Char(9);
    /**
     * Line Feed
     */
    public static final UTF8Char LF = new UTF8Char(10);
    /**
     * Vertical Tab
     */
    public static final UTF8Char VT = new UTF8Char(11);
    /**
     * Form Feed
     */
    public static final UTF8Char FF = new UTF8Char(12);
    /**
     * Carriage Return
     */
    public static final UTF8Char CR = new UTF8Char(13);
    /**
     * Shift Out / X-On
     */
    public static final UTF8Char SO = new UTF8Char(14);
    /**
     * Shift In / X-Off
     */
    public static final UTF8Char SI = new UTF8Char(15);
    /**
     * Data Line Escape
     */
    public static final UTF8Char DLE = new UTF8Char(16);
    /**
     * Device Control 1 (oft. XON)
     */
    public static final UTF8Char DC1 = new UTF8Char(17);
    /**
     * Device Control 2
     */
    public static final UTF8Char DC2 = new UTF8Char(18);
    /**
     * Device Control 3 (oft. XOFF)
     */
    public static final UTF8Char DC3 = new UTF8Char(19);
    /**
     * Device Control 4
     */
    public static final UTF8Char DC4 = new UTF8Char(20);
    /**
     * Negative Acknowledgement
     */
    public static final UTF8Char NAK = new UTF8Char(21);
    /**
     * Synchronous Idle
     */
    public static final UTF8Char SYN = new UTF8Char(22);
    /**
     * End of Transmit Block
     */
    public static final UTF8Char ETB = new UTF8Char(23);
    /**
     * Cancel
     */
    public static final UTF8Char CAN = new UTF8Char(24);
    /**
     * End of Medium
     */
    public static final UTF8Char EM = new UTF8Char(25);
    /**
     * Substitute
     */
    public static final UTF8Char SUB = new UTF8Char(26);
    /**
     * Escape
     */
    public static final UTF8Char ESC = new UTF8Char(27);
    /**
     * File Separator
     */
    public static final UTF8Char FS = new UTF8Char(28);
    /**
     * Group Separator
     */
    public static final UTF8Char GS = new UTF8Char(29);
    /**
     * Record Separator
     */
    public static final UTF8Char RS = new UTF8Char(30);
    /**
     * Unit Separator
     */
    public static final UTF8Char US = new UTF8Char(31);
    /**
     * Delete
     */
    public static final UTF8Char DEL = new UTF8Char(127);
    public static final UTF8Char PAD = new UTF8Char(128);
    public static final UTF8Char HOP = new UTF8Char(129);
    public static final UTF8Char BPH = new UTF8Char(130);
    public static final UTF8Char NBH = new UTF8Char(131);
    public static final UTF8Char IND = new UTF8Char(132);
    public static final UTF8Char NEL = new UTF8Char(133);
    public static final UTF8Char SSA = new UTF8Char(134);
    public static final UTF8Char ESA = new UTF8Char(135);
    public static final UTF8Char HTS = new UTF8Char(136);
    public static final UTF8Char HTJ = new UTF8Char(137);
    public static final UTF8Char VTS = new UTF8Char(138);
    public static final UTF8Char PLD = new UTF8Char(139);
    public static final UTF8Char PLU = new UTF8Char(140);
    public static final UTF8Char RI = new UTF8Char(141);
    public static final UTF8Char SS2 = new UTF8Char(142);
    public static final UTF8Char SS3 = new UTF8Char(143);
    public static final UTF8Char DCS = new UTF8Char(144);
    public static final UTF8Char PU1 = new UTF8Char(145);
    public static final UTF8Char PU2 = new UTF8Char(146);
    public static final UTF8Char STS = new UTF8Char(147);
    public static final UTF8Char CCH = new UTF8Char(148);
    public static final UTF8Char MW = new UTF8Char(149);
    public static final UTF8Char SPA = new UTF8Char(150);
    public static final UTF8Char EPA = new UTF8Char(151);
    public static final UTF8Char SOS = new UTF8Char(152);
    public static final UTF8Char SGCI = new UTF8Char(153);
    public static final UTF8Char SCI = new UTF8Char(154);
    public static final UTF8Char CSI = new UTF8Char(155);
    public static final UTF8Char ST = new UTF8Char(156);
    public static final UTF8Char OSC = new UTF8Char(157);
    public static final UTF8Char PM = new UTF8Char(158);
    public static final UTF8Char APC = new UTF8Char(159);
    public static final UTF8Char NBSP = new UTF8Char(160);
    public static final UTF8Char ZWSP = new UTF8Char(0x200b);
    public static final UTF8Char ZWNJ = new UTF8Char(0x200c);
    public static final UTF8Char ZWJ = new UTF8Char(0x200d);
    public static final UTF8Char LRM = new UTF8Char(0x200e);
    public static final UTF8Char RLM = new UTF8Char(0x200f);
    public static final UTF8Char LSEP = new UTF8Char(0x2028);
    public static final UTF8Char RSEP = new UTF8Char(0x2029);
    public static final UTF8Char LRE = new UTF8Char(0x202a);
    public static final UTF8Char RLE = new UTF8Char(0x202b);
    public static final UTF8Char PDF = new UTF8Char(0x202c);
    public static final UTF8Char LRO = new UTF8Char(0x202d);
    public static final UTF8Char RLO = new UTF8Char(0x202e);
    public static final UTF8Char IDSP = new UTF8Char(0x3000);
    public static final UTF8Char BOM = new UTF8Char(0xfeff);
    public static final UTF8Char OBJ = new UTF8Char(0xfffc);

    public UTF8Char(long codepoint, byte regular)
    throws IllegalArgumentException {
        switch (regular) {
            case 0: {
                if (codepoint != -1L) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,-1] for regular 0"
                    );
                }
                this.unit = new byte[0];
                return;
            }
            case 1: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0x7fL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0x7f] for regular 1"
                    );
                }
                this.unit = new byte[]{(byte)codepoint};
                return;
            }
            case 2: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0x7ffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0x7ff] for regular 2"
                    );
                }
                this.unit = new byte[]{
                    (byte)((codepoint >> 6L) | 0xc0L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            case 3: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0xffffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0xffff] for regular 3"
                    );
                }
                this.unit = new byte[]{
                    (byte)((codepoint >> 12L) | 0xe0L),
                    (byte)(((codepoint >> 6L) & 0x3fL) | 0x80L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            case 4: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1, -1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0x1fffffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0x1fffff] for regular 4"
                    );
                }
                this.unit = new byte[]{
                    (byte)((codepoint >> 18L) | 0xf0L),
                    (byte)(((codepoint >> 12L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 6L) & 0x3fL) | 0x80L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            case 5: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1, -1, -1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0x3ffffffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0x3ffffff] for regular 5"
                    );
                }
                this.unit = new byte[]{
                    (byte)((codepoint >> 24L) | 0xf8L),
                    (byte)(((codepoint >> 18L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 12L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 6L) & 0x3fL) | 0x80L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            case 6: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1, -1, -1, -1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0x7fffffffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0x7fffffff] for regular 6"
                    );
                }
                this.unit = new byte[]{
                    (byte)((codepoint >> 30L) | 0xfcL),
                    (byte)(((codepoint >> 24L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 18L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 12L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 6L) & 0x3fL) | 0x80L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            case 7: {
                if (codepoint == -1L) {
                    this.unit = new byte[]{-1, -1, -1, -1, -1, -1, -1};
                    return;
                }
                if (codepoint < 0L || codepoint > 0xfffffffffL) {
                    throw new IllegalArgumentException(
                        "Codepoint " + Long.toString(codepoint) +
                        " out of range [-1,0xfffffffff] for regular 7"
                    );
                }
                this.unit = new byte[]{
                    (byte)-2,
                    (byte)((codepoint >> 30L) | 0x80L),
                    (byte)(((codepoint >> 24L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 18L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 12L) & 0x3fL) | 0x80L),
                    (byte)(((codepoint >> 6L) & 0x3fL) | 0x80L),
                    (byte)((codepoint & 0x3fL) | 0x80L)
                };
                return;
            }
            default: throw new IllegalArgumentException(
                "Regular " + Byte.toString(regular) + " out of range [0,7]"
            );
        }
    }

    public UTF8Char(long codepoint) throws IllegalArgumentException {
        this(codepoint, (byte)((codepoint >= 0L) ? (
            (codepoint > 0x7f) ? (
                (codepoint > 0x7ffL) ? (
                    (codepoint > 0xffffL) ? (
                        (codepoint > 0x1fffffL) ? (
                            (codepoint > 0x3ffffffL) ? (
                                (codepoint > 0x7fffffffL) ? 7 : 6
                            ) : 5
                        ) : 4
                    ) : 3
                ) : 2
            ) : 1
        ) : 0));
    }

    public UTF8Char(char codepoint) {
        this((long)codepoint);
    }

    public long ord() {
        if (this.unit.length > 0 && this.unit[0] == (byte)-1) {
            return -1L;
        }
        switch (this.unit.length) {
            case 1: return this.unit[0];
            case 2: return ((((long)(this.unit[0])) & 0x1fL) << 6L) |
                           (((long)(this.unit[1])) & 0x3fL);
            case 3: return ((((long)(this.unit[0])) & 0xfL) << 12L) |
                           ((((long)(this.unit[1])) & 0x3fL) << 6L) |
                           (((long)(this.unit[2])) & 0x3fL);
            case 4: return ((((long)(this.unit[0])) & 7L) << 18L) |
                           ((((long)(this.unit[1])) & 0x3fL) << 12L) |
                           ((((long)(this.unit[2])) & 0x3fL) << 6L) |
                           (((long)(this.unit[3])) & 0x3fL);
            case 5: return ((((long)(this.unit[0])) & 3L) << 24L) |
                           ((((long)(this.unit[1])) & 0x3fL) << 18L) |
                           ((((long)(this.unit[2])) & 0x3fL) << 12L) |
                           ((((long)(this.unit[3])) & 0x3fL) << 6L) |
                           (((long)(this.unit[4])) & 0x3fL);
            case 6: return ((((long)(this.unit[0])) & 1L) << 30L) |
                           ((((long)(this.unit[1])) & 0x3fL) << 24L) |
                           ((((long)(this.unit[2])) & 0x3fL) << 18L) |
                           ((((long)(this.unit[3])) & 0x3fL) << 12L) |
                           ((((long)(this.unit[4])) & 0x3fL) << 6L) |
                           (((long)(this.unit[5])) & 0x3fL);
            case 7: return ((((long)(this.unit[1])) & 0x3fL) << 30L) |
                           ((((long)(this.unit[2])) & 0x3fL) << 24L) |
                           ((((long)(this.unit[3])) & 0x3fL) << 18L) |
                           ((((long)(this.unit[4])) & 0x3fL) << 12L) |
                           ((((long)(this.unit[5])) & 0x3fL) << 6L) |
                           (((long)(this.unit[6])) & 0x3fL);
            default: return -1;
        }
    }

    //@Override
    public int compareTo(UTF8Char o) {
        return Long.compare(this.ord(), o.ord());
    }

    //@Override
    public boolean equals(Object o) {
        if (o instanceof UTF8Char) {
            final UTF8Char CHR = (UTF8Char)o;
            return this.compareTo(CHR) == 0;
        }
        return false;
    }

    //@Override
    public int hashCode() {
        return Long.hashCode(this.ord());
    }

    public byte getByteLength() {
        return (byte)(this.unit.length);
    }

    public byte[] toByteArray() {
        final byte[] RES = new byte[this.unit.length];
        for (int i = 0; i < RES.length; i++) {
            RES[i] = this.unit[i];
        }
        return RES;
    }

    //@Override
    public String toString() {
        final long ORD = this.ord();
        if (ORD == -1L) {
            return "";
        }
        if (0L <= ORD && ORD <= 0xffffL) {
            if (0xd800L <= ORD && ORD < 0xdfffL) {
                return "\\u" + Long.toHexString(ORD);
            }
            return String.valueOf((char)ORD);
        }
        if (0xffffL < ORD && ORD <= 0x10ffffL) {
            return String.valueOf(new char[]{
                (char)(((ORD - 65536L) >> 10L) | 0xd800L),
                (char)(((ORD - 65536L) & 0x3ffL) | 0xdc00L)
            });
        }
        if (0x10ffffL < ORD && ORD <= 0xffffffffL) {
            return String.format("\\U%08x", Long.valueOf(ORD));
        }
        return this.toEvalString();
    }

    public String toEvalString() {
        return String.format("new %s(0x%x, %d)",
                             UTF8Char.class.getName(),Long.valueOf(this.ord()),
                             Integer.valueOf(this.unit.length));
    }
}