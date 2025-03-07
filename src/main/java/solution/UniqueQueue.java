package solution;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("WeakerAccess")
public final class UniqueQueue<T> {

    private static final class Node<T> {
        @Nonnull
        private final T item;
        @Nullable
        private Node<T> older;
        @Nullable
        private Node<T> newer;

        private Node(@Nonnull T item, @Nullable Node<T> older, @Nullable Node<T> newer) {
            this.item = item;
            this.older = older;
            this.newer = newer;
        }

        private static <T> Node<T> after(@Nonnull Node<T> prev, @Nonnull T item) {
            return new Node<>(item, prev, null);
        }

        @Override
        public String toString() {
            return item.toString();
        }
    }

    @Nullable
    private Node<T> oldEnd;
    @Nullable
    private Node<T> newEnd;

    @Nonnull
    private Map<T, Node<T>> hashLookup;

    private UniqueQueue() {
        hashLookup = new HashMap<>(4096);
    }

    public static <T> UniqueQueue<T> empty() {
        return new UniqueQueue<>();
    }

    /**
     * Get the number of items in the ordered set.
     *
     * @implNote O(1)
     */
    public int size() {
        return hashLookup.size();
    }

    /**
     * Add an item at the 'start' of the queue (newest side).
     *
     * If the item already exists, it is moved.
     *
     * @implNote O(1) amortized, O(n) worst case
     */
    public void pushAtNewEnd(@Nonnull T item) {
        // Check 'already newest' special case at beginning.
        if (newEnd != null && item.equals(newEnd.item)) {
            return;
        }
        Node<T> newNode;
        if (newEnd == null) {
            // The queue is empty.
            assert oldEnd == null;
            newNode = new Node<>(item, null, null);
            oldEnd = newNode;
            newEnd = newNode;
            hashLookup.put(item, newNode);
            return;
        }
        // The queue is not empty.
        assert oldEnd != null;
        Node<T> existingNode = hashLookup.get(item);
        if (existingNode == null) {
            // Not in queue, insert at new end.
            newNode = Node.after(newEnd, item);
            newEnd.newer = newNode;
            newEnd = newNode;
            hashLookup.put(item, newNode);
            return;
        }
        // Already in the queue, and not newest.
        assert existingNode.newer != null: "existing node is not at new end, so it must have a previous item";
        existingNode.newer.older = existingNode.older;
        if (existingNode.older != null) {
            existingNode.older.newer = existingNode.newer;
        } else {
            oldEnd = existingNode.newer;
        }
        existingNode.older = newEnd;
        existingNode.newer = null;
        newEnd.newer = existingNode;
        newEnd = existingNode;
    }

    /**
     * Delete an item from anywhere in the queue.
     *
     * @implNote O(1) expected, O(n) worst case
     */
    public boolean deleteItem(@Nonnull T item) {
        //TODO @mark: I think start/end detection could be easier
        Node<T> node = hashLookup.get(item);
        if (node == null) {
            return false;
        }
        assert newEnd != null && oldEnd != null;
        if (node.newer == null) {
            newEnd = node.older;
        } else {
            node.newer.older = node.older;
        }
        if (node.older == null) {
            oldEnd = node.newer;
        } else {
            node.older.newer = node.newer;
        }
        hashLookup.remove(item);
        return true;
    }

    /**
     * Delete the oldest item from the queue.
     *
     * @implNote O(1) expected, O(n) worst case
     */
    @Nonnull
    public Optional<T> popAtOldEnd() {
        // If empty.
        if (oldEnd == null) {
            assert newEnd == null;
            return Optional.empty();
        }
        // One or more items
        T item = oldEnd.item;
        if (size() == 1) {
            // Only one item.
            newEnd = null;
        } else {
            // At least two items.
            assert oldEnd.newer != null: "must be at least two items, so second-last should have a newer";
            oldEnd.newer.older = null;
        }
        Node<T> nowOldest = oldEnd.newer;
        oldEnd.newer = null;
        oldEnd = nowOldest;
        hashLookup.remove(item);
        return Optional.of(item);
    }

    /**
     * Find the position of an item, as the index from the new end.
     *
     * @implNote O(n) if it exists, O(n) expected if not.
     */
    @Nonnull
    public Optional<Integer> findPosition(@Nonnull T item) {
        // Queue is empty.
        if (newEnd == null) {
            assert oldEnd == null;
            return Optional.empty();
        }
        // Item not in queue.
        if (!hashLookup.containsKey(item)) {
            return Optional.empty();
        }
        // Item in queue.
        int index = 0;
        Node<T> current = newEnd;
        //noinspection ConditionalBreakInInfiniteLoop
        while (true) {
            if (current.item.equals(item)) {
                break;
            }
            index++;
            current = current.older;
            assert current != null: "The item should exit but was not found";
        }
        return Optional.of(index);
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder("[");
        Node<T> current = oldEnd;
        boolean isFirst = true;
        int count = 0;
        while (current != null) {
            if (isFirst) {
                isFirst = false;
            } else {
                txt.append(", ");
            }
            txt.append(current.older == null ? "x" : current.older).append("<");  //TODO @mark: TEMPORARY! REMOVE THIS!
            txt.append(current).append(hashLookup.containsKey(current.item) ? "" : "!!");
            txt.append(">").append(current.newer == null ? "x" : current.newer);  //TODO @mark: TEMPORARY! REMOVE THIS!
            current = current.newer;
            count++;
        }
        txt.append(", ??".repeat(Math.max(0, hashLookup.size() - count)));
        return txt.append("]").toString();
    }
}
