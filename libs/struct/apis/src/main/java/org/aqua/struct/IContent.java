package org.aqua.struct;

public interface IContent {
    Object serialize();
    void deserialize(Object data);
}
