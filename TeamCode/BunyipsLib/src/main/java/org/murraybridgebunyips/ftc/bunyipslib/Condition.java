package org.murraybridgebunyips.ftc.bunyipslib;

/**
 * Functional interface for a condition used in While polling
 */
@FunctionalInterface
public interface Condition {
    boolean check();
}
