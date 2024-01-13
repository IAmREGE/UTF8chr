package rege.rege.utf8chr;

import java.util.HashMap;
import java.util.Map;

/**
 * @author REGE
 * @since 0.0.1a1
 */
public class UTF8DecodeErrorHandler {
    public static final
    Map<String, UTF8DecodeErrorHandlerProvider> HANDLER;

    private UTF8DecodeErrorHandler() {
        throw new UnsupportedOperationException();
    }

    static {
        HANDLER = new HashMap<String, UTF8DecodeErrorHandlerProvider>();
        HANDLER.put("strict", new UTF8DecodeErrorHandlerProvider() {
            //@Override
            public Map.Entry<Integer, Iterable<UTF8Char>>
            provide(int origPos, byte[] bytes) throws UTF8CharDecodeException {
                final StringBuilder SB = new StringBuilder();
                SB.append((bytes.length == 1) ?
                          "Malformed byte " : "Malformed bytes ");
                for (int i = 0; i < bytes.length; i++) {
                    if (bytes[i] >= 0 && bytes[i] < 16) {
                        SB.append('0');
                    }
                    SB.append(Long.toHexString(Byte.toUnsignedLong(bytes[i])));
                    SB.append(' ');
                }
                SB.append("at position ");
                SB.append(origPos);
                throw new UTF8CharDecodeException(SB.toString());
            }
        });
        HANDLER.put("ignore", new UTF8DecodeErrorHandlerProvider() {
            //@Override
            public Map.Entry<Integer, Iterable<UTF8Char>>
            provide(int origPos, byte[] bytes) {
                return new UTF8DecodeErrorHandlerProvider.ProvidedEntry(
                    Integer.valueOf(origPos + 1), new UTF8Sequence()
                );
            }
        });
        HANDLER.put("replace", new UTF8DecodeErrorHandlerProvider() {
            //@Override
            public Map.Entry<Integer, Iterable<UTF8Char>>
            provide(int origPos, byte[] bytes) {
                return new UTF8DecodeErrorHandlerProvider.ProvidedEntry(
                    Integer.valueOf(origPos + bytes.length),
                    new UTF8Sequence('\ufffd')
                );
            }
        });
        HANDLER.put("surrogateescape", new UTF8DecodeErrorHandlerProvider() {
            //@Override
            public Map.Entry<Integer, Iterable<UTF8Char>>
            provide(int origPos, byte[] bytes) {
                return new UTF8DecodeErrorHandlerProvider.ProvidedEntry(
                    Integer.valueOf(origPos + 1),
                    new UTF8Sequence(new UTF8Char(
                        (bytes[0] < 0) ? 0xdd00L + bytes[0] : bytes[0]
                    ))
                );
            }
        });
        HANDLER.put("backslashreplace", new UTF8DecodeErrorHandlerProvider() {
            //@Override
            public Map.Entry<Integer, Iterable<UTF8Char>>
            provide(int origPos, byte[] bytes) {
                final byte HIGH = (byte)(bytes[0] >>> 4);
                final byte LOW = (byte)(bytes[0] & (byte)0xf);
                return new UTF8DecodeErrorHandlerProvider.ProvidedEntry(
                    Integer.valueOf(origPos + 1),
                    new UTF8Sequence(new long[]{
                        92, 120, HIGH + (byte)((HIGH > (byte)9) ? 87 : 48),
                        LOW + (byte)((LOW > (byte)9) ? 87 : 48)
                    })
                );
            }
        });
    }
}