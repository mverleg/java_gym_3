package solution;

import noedit.Data;
import noedit.Registers;
import org.checkerframework.checker.index.qual.Positive;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This solution represents a special CPU cache manager. It can store and lookup data.
 *
 * It should work as follows:
 * <ol>
 * <li>When storing a value, it should go into the first register.
 * <li>If a register is full, the oldest item is pushed to the next register (which may in turn 'overflow').
 * <li>When looking up an item, if the item is in cache, the register number is returned. If it is not in cache
 * (either because it was never encountered, or because it dropped out of the last register), then an empty
 * optional is returned.
 * </ol>
 *
 * Operations have to be fast for large numbers of items (linear at worst). Bonus points for minimizing temporary (heap) data.
 *
 * (This is not really how CPU cache works).
 */
public class Solution {

    @Nonnull
    private final Registers registers;
    private final int totalRegisterSize;
    @Nonnull
    private final UniqueQueue<Data> cache;

    public Solution(@Nonnull Registers registers) {
        this.registers = registers;
        int size = 0;
        for (int i = 0; i < registers.registerCount(); i++) {
            size += registers.registerSize(i);
        }
        totalRegisterSize = size;
        cache = UniqueQueue.empty();
    }

    /**
     * Store the item into the first register, overflowing as necessary.
     */
    public void store(@Nonnull Data storeItem) {
        cache.pushAtNewEnd(storeItem);
        if (cache.size() > totalRegisterSize) {
            cache.popAtOldEnd();
        }
    }

    /**
     * Return the register that an item if cached in, or empty if not cached.
     */
    @Nonnull
    public Optional<Integer> lookup(@Nonnull Data searchItem) {
        Optional<Integer> maybePos = cache.findPosition(searchItem);
        if (maybePos.isEmpty()) {
            return Optional.empty();
        }
        int pos = maybePos.get();
        for (int regNr = 0; regNr < registers.registerCount(); regNr++) {
            int registerSize = registers.registerSize(regNr);
            if (pos < registerSize) {
                return Optional.of(regNr);
            }
            pos -= registerSize;
        }
        throw new IllegalStateException("no register found; this should not happen");
    }
}
