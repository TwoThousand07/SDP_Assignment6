package edu.astana.visitorfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DirectoryNodeTest {
    @Test
    @DisplayName("T8: negative file sizes are rejected")
    void rejectsNegativeFileSizes() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.bin", -1));
    }

    @Test
    @DisplayName("T8: duplicate sibling names are rejected")
    void rejectsDuplicateSiblingNames() {
        DirectoryNode directory = new DirectoryNode("root");
        directory.addChild(new FileNode("same", 1));

        assertThrows(IllegalArgumentException.class, () -> directory.addChild(new DirectoryNode("same")));
        assertEquals(1, directory.getChildren().size());
    }

    @Test
    @DisplayName("T8: an element cannot have two parents")
    void rejectsMultipleParentsAndAllowsReparentingAfterRemoval() {
        DirectoryNode first = new DirectoryNode("first");
        DirectoryNode second = new DirectoryNode("second");
        FileNode file = new FileNode("data.bin", 4);
        first.addChild(file);

        assertThrows(IllegalArgumentException.class, () -> second.addChild(file));
        assertTrue(first.removeChild(file));
        second.addChild(file);
        assertEquals(List.of(file), second.getChildren());
    }

    @Test
    @DisplayName("T8: cycles and self-parenting are rejected")
    void rejectsCycles() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode child = new DirectoryNode("child");
        root.addChild(child);

        assertThrows(IllegalArgumentException.class, () -> child.addChild(root));
        assertThrows(IllegalArgumentException.class, () -> root.addChild(root));
    }

    @Test
    @DisplayName("T8: valid nested directories remain supported and children are protected")
    void supportsNestingAndProtectsChildCollection() {
        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode child = new DirectoryNode("child");
        root.addChild(child);
        child.addChild(new FileNode("data.bin", 4));

        assertEquals(1, child.getChildren().size());
        assertThrows(UnsupportedOperationException.class, () -> root.getChildren().add(new FileNode("x", 1)));
        assertFalse(root.removeChild(new DirectoryNode("other")));
        assertEquals(1, root.getChildren().size());
    }

    @Test
    @DisplayName("invalid path segment names are rejected")
    void rejectsInvalidNames() {
        assertThrows(IllegalArgumentException.class, () -> new DirectoryNode("../outside"));
        assertThrows(IllegalArgumentException.class, () -> new FileNode("", 0));
    }
}
