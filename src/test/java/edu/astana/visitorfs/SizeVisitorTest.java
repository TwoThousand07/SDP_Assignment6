package edu.astana.visitorfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SizeVisitorTest {
    @Test
    @DisplayName("T2: size includes each file at every nesting level")
    void sumsNestedFileSizes() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("first.bin", 10));
        DirectoryNode nested = new DirectoryNode("nested");
        nested.addChild(new FileNode("second.bin", 20));
        DirectoryNode deeper = new DirectoryNode("deeper");
        deeper.addChild(new FileNode("third.bin", 30));
        nested.addChild(deeper);
        root.addChild(nested);

        assertEquals(60, new SizeVisitor().calculate(root));
    }

    @Test
    @DisplayName("T7: size visitor clears its result between executions")
    void canBeReusedWithoutAccumulatingResults() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("data.bin", 15));
        SizeVisitor visitor = new SizeVisitor();

        assertEquals(15, visitor.calculate(root));
        assertEquals(15, visitor.calculate(root));
        assertEquals(0, visitor.calculate(new DirectoryNode("empty")));
        assertEquals(15, visitor.calculate(root));
    }
}
