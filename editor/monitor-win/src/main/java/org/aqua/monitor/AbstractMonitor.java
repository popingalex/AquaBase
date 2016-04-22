package org.aqua.monitor;

import java.util.List;

import org.aqua.craft.wrapper.IComponent;
import org.aqua.struct.IContent;

public abstract class AbstractMonitor<Subject, Node> {
    protected final Subject subject;

    public AbstractMonitor(Subject subject) {
        this.subject = subject;
    }
    public Subject getSubject() {
        return subject;
    }
    public String getSubjectTag() {
        return subject.getClass().getSimpleName() + ":" + subject.hashCode();
    }
    public abstract Node getRoot();
    public abstract Node getCursor();
    public abstract IContent getContent(Object node);
    public abstract List<Node> getBranch(Object node);
    public abstract IComponent getPaintable();

}
