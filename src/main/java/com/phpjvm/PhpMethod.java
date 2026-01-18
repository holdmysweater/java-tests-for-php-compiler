package com.phpjvm;

@FunctionalInterface
public interface PhpMethod {
    BasePhpValue invoke(PhpObject self, BasePhpValue[] args);
}
