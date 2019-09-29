package solution;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UniqueQueueTest {

    @Test
    void testUniqueQueue() {
        UniqueQueue<Integer> uq = UniqueQueue.empty();
        Optional<Integer> val;

        uq.pushAtNewEnd(4);
        uq.pushAtNewEnd(3);
        uq.pushAtNewEnd(2);
        uq.pushAtNewEnd(1);
        uq.pushAtNewEnd(2);
        uq.pushAtNewEnd(3);
        uq.pushAtNewEnd(4);

        // 1 2 3 4
        assertEquals(4, uq.size());
        assertEquals(Optional.of(3), uq.findPosition(1));
        assertEquals(Optional.of(2), uq.findPosition(2));
        assertEquals(Optional.of(1), uq.findPosition(3));
        assertEquals(Optional.of(0), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        val = uq.popAtOldEnd();
        // 2 3 4
        assertEquals(3, uq.size());
        assertEquals(Optional.of(3), val);
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.of(2), uq.findPosition(2));
        assertEquals(Optional.of(1), uq.findPosition(3));
        assertEquals(Optional.of(0), uq.findPosition(4));


        uq.pushAtNewEnd(3);
        // 2 4 3
        assertEquals(3, uq.size());
        assertEquals(Optional.of(3), val);
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.of(2), uq.findPosition(2));
        assertEquals(Optional.of(0), uq.findPosition(3));
        assertEquals(Optional.of(1), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        uq.pushAtNewEnd(5);
        // 2 4 3 5
        assertEquals(4, uq.size());
        assertEquals(Optional.of(3), val);
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.of(3), uq.findPosition(2));
        assertEquals(Optional.of(1), uq.findPosition(3));
        assertEquals(Optional.of(2), uq.findPosition(4));
        assertEquals(Optional.of(0), uq.findPosition(5));

        assertTrue(uq.deleteItem(5));
        // 2 4 3
        assertEquals(3, uq.size());
        assertEquals(Optional.empty(), uq.findPosition(5));

        assertFalse(uq.deleteItem(6));
        assertTrue(uq.deleteItem(3));
        assertTrue(uq.deleteItem(3));
        // 2 4
        assertEquals(2, uq.size());
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.of(1), uq.findPosition(2));
        assertEquals(Optional.empty(), uq.findPosition(3));
        assertEquals(Optional.of(0), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        assertTrue(uq.deleteItem(2));
        // 4
        assertEquals(1, uq.size());
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.empty(), uq.findPosition(2));
        assertEquals(Optional.empty(), uq.findPosition(3));
        assertEquals(Optional.of(0), uq.findPosition(4));

        uq.pushAtNewEnd(1);
        // 4 1
        assertEquals(2, uq.size());
        assertEquals(Optional.of(0), uq.findPosition(1));
        assertEquals(Optional.empty(), uq.findPosition(2));
        assertEquals(Optional.empty(), uq.findPosition(3));
        assertEquals(Optional.of(1), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        assertTrue(uq.deleteItem(1));
        // 4
        assertEquals(1, uq.size());
        g co
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.empty(), uq.findPosition(2));
        assertEquals(Optional.empty(), uq.findPosition(3));
        assertEquals(Optional.of(0), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        val = uq.popAtOldEnd();
        // (empty)
        assertEquals(0, uq.size());
        assertEquals(Optional.of(4), val);
        assertEquals(Optional.empty(), uq.findPosition(1));
        assertEquals(Optional.empty(), uq.findPosition(2));
        assertEquals(Optional.empty(), uq.findPosition(3));
        assertEquals(Optional.empty(), uq.findPosition(4));
        assertEquals(Optional.empty(), uq.findPosition(5));

        val = uq.popAtOldEnd();
        // (empty)
        assertEquals(0, uq.size());
        assertEquals(Optional.empty(), val);

        uq.pushAtNewEnd(5);
        uq.pushAtNewEnd(5);
        // 5
        val = uq.popAtOldEnd();
        // (empty)
        assertEquals(0, uq.size());
        assertEquals(Optional.of(5), val);
    }
}
