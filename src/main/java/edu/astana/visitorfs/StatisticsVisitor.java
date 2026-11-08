package edu.astana.visitorfs;

import java.util.Objects;

public final class StatisticsVisitor implements Visitor {
    private long fileCount;
    private long directoryCount;
    private int maximumDepth;
    private int currentDepth;

    public Statistics collect(Element root) {
        Objects.requireNonNull(root, "root");
        fileCount = 0;
        directoryCount = 0;
        maximumDepth = 0;
        currentDepth = 0;
        root.accept(this);
        return new Statistics(fileCount, directoryCount, maximumDepth);
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
        maximumDepth = Math.max(maximumDepth, currentDepth);
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        maximumDepth = Math.max(maximumDepth, currentDepth);
        for (Element child : directory.getChildren()) {
            currentDepth++;
            child.accept(this);
            currentDepth--;
        }
    }

    public record Statistics(long fileCount, long directoryCount, int maximumDepth) {
    }
}
