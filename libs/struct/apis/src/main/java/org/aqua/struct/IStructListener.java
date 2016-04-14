package org.aqua.struct;


// TODO where to use it?
public interface IStructListener {
    void attachingNode(Object node, Object parent);
    IContent attachingContent(Object node);
}
