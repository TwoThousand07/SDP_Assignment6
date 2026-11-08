package edu.astana.visitorfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class DirectoryNode extends AbstractElement {
    private final List<Element> children = new ArrayList<>();

    public DirectoryNode(String name) {
        super(name);
    }

    public void addChild(AbstractElement child) {
        Objects.requireNonNull(child, "child");
        if (child.getParentDirectory() != null) {
            throw new IllegalArgumentException("Element already has a parent directory");
        }
        for (Element existing : children) {
            if (existing.getName().equals(child.getName())) {
                throw new IllegalArgumentException("A child with this name already exists");
            }
        }
        for (DirectoryNode ancestor = this; ancestor != null; ancestor = ancestor.getParentDirectory()) {
            if (ancestor == child) {
                throw new IllegalArgumentException("Adding this element would create a cycle");
            }
        }
        children.add(child);
        child.setParentDirectory(this);
    }

    public boolean removeChild(AbstractElement child) {
        Objects.requireNonNull(child, "child");
        for (int index = 0; index < children.size(); index++) {
            if (children.get(index) == child) {
                children.remove(index);
                child.setParentDirectory(null);
                return true;
            }
        }
        return false;
    }

    public List<Element> getChildren() {
        return List.copyOf(children);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
