package org.aqua.craft.struct;

public class Caster {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object source) {
        return (T) source;
    }
}
