package solution;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class OrderedSet<T> {

    private static final class Node<T> {
        @Nonnull
        private final T item;
        @Nullable
        private Node<T> prev = null;
        @Nullable
        private Node<T> next = null;

        private Node(@Nonnull T item, @Nullable Node<T> prev, @Nullable Node<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        private static <T> Node<T> after(@Nonnull Node<T> prev, @Nonnull T item) {
            return new Node<>(item, prev, null);
        }
    }

    @Nullable
    private Node<T> oldEnd;
    @Nullable
    private Node<T> newEnd;

    private int length = 0;

    @Nonnull
    //TODO @mark: length annotation?
    private Map<Integer, Node<T>> hashLookup;

    private OrderedSet() {
        //noinspection unchecked
        hashLookup = (List<Node<T>>[]) new List[LOOKUP_SIZE];
        for (int i = 0; i < LOOKUP_SIZE; i++) {
            hashLookup[i] = new LinkedList<>();
        }
    }

    public static <T> OrderedSet<T> empty() {
        return new OrderedSet<>();
    }

    private int findLookupPosition(@Nonnull T item) {
        //TODO @mark: is this necessary?
        return ((item.hashCode() % LOOKUP_SIZE) + LOOKUP_SIZE) % LOOKUP_SIZE;
    }

    public int size() {
        return length;
    }

    public void pushAtNewEnd(@Nonnull T item) {
        Node<T> node;
        if (newEnd == null) {
            Validate.isTrue(oldEnd == null);
            node = new Node<>(item, null, null);
            oldEnd = node;
            newEnd = node;
        } else {
            Validate.notNull(oldEnd);
            node = Node.after(newEnd, item);
            newEnd.next = node;
            newEnd = node;
        }
        length++;
        int lookupPos = findLookupPosition(item);
        hashLookup[lookupPos].add(node);
    }

    public boolean deleteItem(@Nonnull T item) {
        int lookupPos = findLookupPosition(item);
        List<Node<T>> candidates = hashLookup[lookupPos];
        Node<T> found = null;
        for (int i = 0; i < candidates.size(); i++) {
            Node<T> candidate = candidates.get(i);
            if (candidate.item.equals(item)) {
                found = candidates.get(i);
                candidates.remove(i);
                break;
            }
        }
        if (found == null) {
            return false;
        }
        //TODO @mark: rest is guesswork
        if (found.prev == null) {
            oldEnd = found.next;
        } else {

        }
        length--;
        return true;
    }

    public void popAtOldEnd() {
        length--;
        throw new NotImplementedException("todo: ");  //TODO @mark:
    }

    public int findPosition(@Nonnull T item) {
        int lookupPos = findLookupPosition(item);
        throw new NotImplementedException("todo: ");  //TODO @mark:
    }
}
