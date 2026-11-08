package edu.astana.visitorfs;

public interface Visitor {
    void visit(FileNode file);

    void visit(DirectoryNode directory);
}
