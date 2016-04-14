package org.aqua.struct;

public class Caster {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object source) {
        return (T) source;
    }
}
