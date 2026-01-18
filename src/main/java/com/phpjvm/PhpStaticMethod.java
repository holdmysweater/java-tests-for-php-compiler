package com.phpjvm;

@FunctionalInterface
public interface PhpStaticMethod {
    BasePhpValue invoke(PhpClass calledClass, BasePhpValue[] args);
}
