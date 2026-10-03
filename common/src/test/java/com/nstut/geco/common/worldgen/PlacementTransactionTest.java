package com.nstut.geco.common.worldgen;

import java.util.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static com.nstut.geco.common.worldgen.PlacementTransaction.*;

@org.junit.jupiter.api.Tag("unit")
class PlacementTransactionTest {
    private static final List<Change<String, String>> CHANGES = List.of(
            new Change<>("trunk", "sapling", "log"), new Change<>("branch", "air", "leaves"));
    private static class Fake implements World<String, String> {
        final Map<String, String> states = new HashMap<>(Map.of("trunk", "sapling", "branch", "air"));
        String failedPosition;
        boolean dishonest;
        boolean throwsOnce;
        int writes;
        public String read(String pos) { return states.get(pos); }
        public boolean write(String pos, String state) {
            writes++;
            if (pos.equals(failedPosition) && state.equals("leaves")) {
                if (throwsOnce) { throwsOnce = false; throw new IllegalArgumentException("injected failure"); }
                return dishonest;
            }
            states.put(pos, state);
            return true;
        }
    }
    @Test void commitsAllCells() {
        Fake world = new Fake();
        assertTrue(apply(world, CHANGES));
        assertEquals(Map.of("trunk", "log", "branch", "leaves"), world.states);
    }
    @Test void restoresSaplingAndEveryPriorCellOnFalse() {
        Fake world = new Fake(); world.failedPosition = "branch";
        assertFalse(apply(world, CHANGES));
        assertEquals(Map.of("trunk", "sapling", "branch", "air"), world.states);
    }
    @Test void detectsSuccessWithoutAWrite() {
        Fake world = new Fake(); world.failedPosition = "branch"; world.dishonest = true;
        assertFalse(apply(world, CHANGES));
        assertEquals("sapling", world.read("trunk"));
    }
    @Test void restoresSaplingBeforePropagatingAnException() {
        Fake world = new Fake(); world.failedPosition = "branch"; world.throwsOnce = true;
        assertThrows(IllegalArgumentException.class, () -> apply(world, CHANGES));
        assertEquals(Map.of("trunk", "sapling", "branch", "air"), world.states);
    }
    @Test void revalidatesTheWholeFootprintBeforeAnyWrite() {
        Fake world = new Fake(); world.states.put("branch", "chest");
        assertFalse(apply(world, CHANGES));
        assertEquals(0, world.writes);
        assertEquals("sapling", world.read("trunk"));
    }
    @Test void doesNotTreatAnUnchangedCellAsAWriteFailure() {
        Fake world = new Fake();
        assertTrue(apply(world, List.of(new Change<>("trunk", "sapling", "sapling"))));
        assertEquals(0, world.writes);
    }
    @Test void checksAndRollsBackAWriteThatMutatesThenReturnsFalse() {
        Map<String, String> states = new HashMap<>(Map.of("trunk", "sapling", "branch", "air"));
        World<String, String> world = new World<>() {
            public String read(String pos) { return states.get(pos); }
            public boolean write(String pos, String state) {
                states.put(pos, state);
                return !pos.equals("branch") || !state.equals("leaves");
            }
        };
        assertFalse(apply(world, CHANGES));
        assertEquals(Map.of("trunk", "sapling", "branch", "air"), states);
    }
    @Test void restoresTheSaplingEvenWhenAnotherCellRefusesRollback() {
        Map<String, String> states = new HashMap<>(Map.of("trunk", "sapling", "branch", "air"));
        World<String, String> world = new World<>() {
            public String read(String pos) { return states.get(pos); }
            public boolean write(String pos, String state) {
                if (pos.equals("branch") && state.equals("air")) return false;
                states.put(pos, state);
                return !pos.equals("branch");
            }
        };
        assertThrows(IllegalStateException.class, () -> apply(world, CHANGES));
        assertEquals("sapling", states.get("trunk"));
    }

    @Test void keepsOriginalFailureAndAllRollbackFailuresWhileStillRestoringSapling() {
        Map<String, String> states = new HashMap<>(Map.of("trunk", "sapling", "branch", "air"));
        World<String, String> world = new World<>() {
            public String read(String pos) { return states.get(pos); }
            public boolean write(String pos, String state) {
                if (pos.equals("branch") && state.equals("air"))
                    throw new IllegalStateException("branch restoration failed");
                states.put(pos, state);
                if (pos.equals("branch")) throw new IllegalArgumentException("placement failed");
                if (state.equals("sapling")) throw new IllegalStateException("notification failed after restoration");
                return true;
            }
        };
        var failure = assertThrows(IllegalArgumentException.class, () -> apply(world, CHANGES));
        assertEquals("placement failed", failure.getMessage());
        assertEquals("sapling", states.get("trunk"));
        assertEquals(1, failure.getSuppressed().length);
        assertEquals("branch restoration failed", failure.getSuppressed()[0].getMessage());
        assertEquals(1, failure.getSuppressed()[0].getSuppressed().length);
    }
}
