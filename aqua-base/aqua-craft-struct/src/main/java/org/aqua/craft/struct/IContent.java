package org.aqua.craft.struct;

public interface IContent {
    Object serialize();
    void deserialize(Object data);
}
