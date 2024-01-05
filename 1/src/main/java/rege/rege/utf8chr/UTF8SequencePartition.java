package rege.rege.utf8chr;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class UTF8SequencePartition
implements List<UTF8Sequence>, Comparable<UTF8SequencePartition> {
    public final UTF8Sequence before;
    public final UTF8Sequence separator;
    public final UTF8Sequence after;

    UTF8SequencePartition(UTF8Sequence before, UTF8Sequence separator,
                          UTF8Sequence after) {
        this.before = before;
        this.separator = separator;
        this.after = after;
    }

    /**
     * @return Always returns {@code 3}.
     */
    //@Override
    public int size() {
        return 3;
    }

    /**
     * @return Always returns {@code false}.
     */
    //@Override
    public boolean isEmpty() {
        return false;
    }

    //@Override
    public boolean contains(Object o) {
        return this.before.equals(o) || this.separator.equals(o) ||
               this.after.equals(o);
    }

    //@Override
    public Iterator<UTF8Sequence> iterator() {
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    //@Override
    public Object[] toArray() {
        return new Object[]{this.before, this.separator, this.after};
    }

    //@Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        return (T[])(this.toArray());
    }

    //@Override
    public boolean add(UTF8Sequence e) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'containsAll'");
    }

    //@Override
    public boolean addAll(Collection<? extends UTF8Sequence> c) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean addAll(int index, Collection<? extends UTF8Sequence> c) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public void clear() {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public UTF8Sequence get(int index) throws IndexOutOfBoundsException {
        switch (index) {
            case 0: return this.before;
            case 1: return this.separator;
            case 2: return this.after;
            default: throw new IndexOutOfBoundsException();
        }
    }

    //@Override
    public UTF8Sequence set(int index, UTF8Sequence element) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public void add(int index, UTF8Sequence element) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public UTF8Sequence remove(int index) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public int indexOf(Object o) {
        if (this.before.equals(o)) {
            return 0;
        }
        if (this.separator.equals(o)) {
            return 1;
        }
        if (this.after.equals(o)) {
            return 2;
        }
        return -1;
    }

    //@Override
    public int lastIndexOf(Object o) {
        if (this.after.equals(o)) {
            return 2;
        }
        if (this.separator.equals(o)) {
            return 1;
        }
        if (this.before.equals(o)) {
            return 0;
        }
        return -1;
    }

    //@Override
    public ListIterator<UTF8Sequence> listIterator() {
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    //@Override
    public ListIterator<UTF8Sequence> listIterator(int index) {
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    //@Override
    public List<UTF8Sequence> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Partition is immutable");
    }

    //@Override
    public boolean equals(Object o) {
        return (o instanceof UTF8SequencePartition) &&
               this.compareTo((UTF8SequencePartition)o) == 0;
    }

    //@Override
    public int hashCode() {
        return Arrays.hashCode(new UTF8Sequence[]{this.before,
                                                  this.separator, this.after});
    }

    //@Override
    public int compareTo(UTF8SequencePartition o) {
        int cmp;
        if ((cmp = this.before.compareTo(o.before)) != 0) {
            return cmp;
        }
        if ((cmp = this.separator.compareTo(o.separator)) != 0) {
            return cmp;
        }
        return this.after.compareTo(o.after);
    }

    //@Override
    public String toString() {
        final StringBuilder SB = new StringBuilder();
        SB.append('(');
        SB.append(this.before.toString());
        SB.append(',');
        SB.append(' ');
        SB.append(this.separator.toString());
        SB.append(',');
        SB.append(' ');
        SB.append(this.after.toString());
        SB.append(')');
        return SB.toString();
    }
}