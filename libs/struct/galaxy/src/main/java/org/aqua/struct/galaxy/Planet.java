package org.aqua.struct.galaxy;

import java.util.HashSet;
import java.util.Set;

import org.aqua.struct.Field;
import org.aqua.struct.IContent;
import org.aqua.struct.Subject;

@Subject(type = Subject.Type.NODE)
public class Planet implements IContent {
    @Field(type = Field.Type.CONTENT)
    public IContent     content;
    public Set<Channel> channels = new HashSet<Channel>();

    @Override
    public Object serialize() {
        return (null == content) ? null : content.serialize();
    }
    @Override
    public void deserialize(Object data) {
        if (null != content) {
            content.deserialize(data);
        }
    }
}