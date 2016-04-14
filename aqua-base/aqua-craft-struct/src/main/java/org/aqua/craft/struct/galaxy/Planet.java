package org.aqua.craft.struct.galaxy;

import java.util.HashSet;
import java.util.Set;

import org.aqua.craft.struct.IContent;
import org.aqua.craft.struct.Subject;
import org.aqua.craft.struct.Subject.FIELD.FieldType;
import org.aqua.craft.struct.Subject.SubjectType;

@Subject(type = SubjectType.NODE)
public class Planet implements IContent {
    @Subject.FIELD(type = FieldType.CONTENT)
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