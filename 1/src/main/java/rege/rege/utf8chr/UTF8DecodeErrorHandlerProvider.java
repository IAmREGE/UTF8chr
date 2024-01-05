package rege.rege.utf8chr;

import java.util.Map.Entry;

public interface UTF8DecodeErrorHandlerProvider {
    public Entry<Integer,Iterable<UTF8Char>>provide(int origPos, byte[] bytes);

    public static final class ProvidedEntry
    implements Entry<Integer, Iterable<UTF8Char>> {
        public final Integer newPos;
        public final UTF8Sequence replaceSeq;

        public ProvidedEntry(Integer newPos, UTF8Sequence replaceSeq) {
            this.newPos = newPos;
            this.replaceSeq = replaceSeq;
        }

        //@Override
        public final Integer getKey() {
            return this.newPos;
        }

        //@Override
        public final UTF8Sequence getValue() {
            return this.replaceSeq;
        }

        //@Override
        public final UTF8Sequence setValue(Iterable<UTF8Char> value) {
            throw new UnsupportedOperationException();
        }
    }
}