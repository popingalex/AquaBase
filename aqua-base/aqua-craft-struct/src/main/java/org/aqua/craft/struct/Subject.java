package org.aqua.craft.struct;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Subject {
    enum SubjectType {
        STRUCT, NODE, CONTENT
    }
    abstract SubjectType type();

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Operator {
        enum OperatorType {
            ITERATOR, CURSOR, CENTER, CONTENT, OTHERS
        }
        abstract OperatorType type();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FIELD {
        enum FieldType {
            CURSOR, CENTER, CONTENT, OTHERS
        }
        abstract FieldType type();
    }
}
