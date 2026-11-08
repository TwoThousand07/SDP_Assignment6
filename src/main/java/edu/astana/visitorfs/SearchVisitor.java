package edu.astana.visitorfs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public final class SearchVisitor implements Visitor {
    private final Predicate<String> filenameCriterion;
    private final long minimumSize;
    private final List<String> matches = new ArrayList<>();
    private final Deque<String> path = new ArrayDeque<>();
    private DirectoryNode rootDirectory;

    public SearchVisitor(Predicate<String> filenameCriterion, long minimumSize) {
        this.filenameCriterion = Objects.requireNonNull(filenameCriterion, "filenameCriterion");
        if (minimumSize < 0) {
            throw new IllegalArgumentException("Minimum size cannot be negative");
        }
        this.minimumSize = minimumSize;
    }

    public List<String> search(DirectoryNode root) {
        rootDirectory = Objects.requireNonNull(root, "root");
        matches.clear();
        path.clear();
        root.accept(this);
        Collections.sort(matches);
        return List.copyOf(matches);
    }

    @Override
    public void visit(FileNode file) {
        path.addLast(file.getName());
        if (file.getSize() >= minimumSize && filenameCriterion.test(file.getName())) {
            matches.add(String.join("/", path));
        }
        path.removeLast();
    }

    @Override
    public void visit(DirectoryNode directory) {
        boolean selectedRoot = directory == rootDirectory;
        if (!selectedRoot) {
            path.addLast(directory.getName());
        }
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        if (!selectedRoot) {
            path.removeLast();
        }
    }
}
