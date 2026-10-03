package com.nstut.geco.common.worldgen;

import java.util.List;
import java.util.Objects;

/** Prevalidated, reversible block writes. No block entities or entities are admitted by the planner. */
public final class PlacementTransaction {
    public record Change<P, S>(P position, S before, S after) {}
    public interface World<P, S> {
        S read(P position);
        boolean write(P position, S state);
    }
    private PlacementTransaction() {}
    public static <P, S> boolean apply(World<P, S> world, List<Change<P, S>> changes) {
        for (Change<P, S> change : changes)
            if (!Objects.equals(world.read(change.position()), change.before())) return false;
        int attempted = 0;
        boolean failed = false;
        try {
            for (Change<P, S> change : changes) {
                attempted++;
                if (Objects.equals(change.before(), change.after())) continue;
                if (!world.write(change.position(), change.after()) ||
                        !Objects.equals(world.read(change.position()), change.after())) {
                    failed = true;
                    break;
                }
            }
        } catch (RuntimeException failure) {
            try { rollback(world, changes, attempted); }
            catch (RuntimeException rollbackFailure) { failure.addSuppressed(rollbackFailure); }
            throw failure;
        }
        if (failed) {
            rollback(world, changes, attempted);
            return false;
        }
        return true;
    }
    private static <P, S> void rollback(World<P, S> world, List<Change<P, S>> changes, int attempted) {
        RuntimeException failure = null;
        for (int i = attempted - 1; i >= 0; i--) {
            Change<P, S> change = changes.get(i);
            try {
                if (!Objects.equals(world.read(change.position()), change.before()))
                    world.write(change.position(), change.before());
                if (!Objects.equals(world.read(change.position()), change.before()))
                    throw new IllegalStateException("Tree placement rollback failed at " + change.position());
            } catch (RuntimeException restoreFailure) {
                // Keep restoring other cells, especially the sapling, even if one cell refuses restoration.
                if (failure == null) failure = restoreFailure;
                else failure.addSuppressed(restoreFailure);
            }
        }
        if (failure != null) throw failure;
    }
}
