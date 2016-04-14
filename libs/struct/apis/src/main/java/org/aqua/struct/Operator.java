package org.aqua.struct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Operator {
    enum Type {
        ITERATOR, CURSOR, CENTER, CONTENT, OTHERS
    }
    abstract Type type();
}