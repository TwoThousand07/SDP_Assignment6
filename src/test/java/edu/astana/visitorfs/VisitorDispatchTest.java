package edu.astana.visitorfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VisitorDispatchTest {
    @Test
    @DisplayName("T1: both element types call their matching visitor methods")
    void dispatchesToMatchingVisitMethods() {
        List<String> visited = new ArrayList<>();
        Visitor visitor = new Visitor() {
            @Override
            public void visit(FileNode file) {
                visited.add("file:" + file.getName());
            }

            @Override
            public void visit(DirectoryNode directory) {
                visited.add("directory:" + directory.getName());
            }
        };

        new FileNode("notes.txt", 5).accept(visitor);
        new DirectoryNode("docs").accept(visitor);

        assertEquals(List.of("file:notes.txt", "directory:docs"), visited);
    }
}
