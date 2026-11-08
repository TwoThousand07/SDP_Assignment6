package edu.astana.visitorfs;

import java.util.Objects;

public abstract class AbstractElement implements Element {
    private final String name;
    private DirectoryNode parentDirectory;

    protected AbstractElement(String name) {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || name.equals(".") || name.equals("..") || name.contains("/") || name.contains("\\")) {
            throw new IllegalArgumentException("Name must be a nonblank path segment");
        }
        this.name = name;
    }

    @Override
    public final String getName() {
        return name;
    }

    final DirectoryNode getParentDirectory() {
        return parentDirectory;
    }

    final void setParentDirectory(DirectoryNode parentDirectory) {
        this.parentDirectory = parentDirectory;
    }
}
