package com.phpjvm;

/**
 * Mutable reference target used to implement PHP pass-by-reference.
 */
public interface PhpRef {
    BasePhpValue get();

    void set(BasePhpValue v);
}
