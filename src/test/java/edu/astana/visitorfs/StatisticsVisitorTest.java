package edu.astana.visitorfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatisticsVisitorTest {
    @Test
    @DisplayName("T5: statistics count the root and report the deepest file")
    void countsElementsAndMaximumDepth() {
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("top.txt", 1));
        DirectoryNode branch = new DirectoryNode("branch");
        DirectoryNode deeper = new DirectoryNode("deeper");
        deeper.addChild(new FileNode("deep.txt", 2));
        branch.addChild(deeper);
        root.addChild(branch);
        StatisticsVisitor visitor = new StatisticsVisitor();

        assertEquals(new StatisticsVisitor.Statistics(2, 3, 3), visitor.collect(root));
    }

    @Test
    @DisplayName("T6 and T7: statistics handle empty trees and clear state between runs")
    void handlesEmptyTreeAndCanBeReused() {
        StatisticsVisitor visitor = new StatisticsVisitor();
        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("one.txt", 1));

        assertEquals(new StatisticsVisitor.Statistics(1, 1, 1), visitor.collect(root));
        assertEquals(new StatisticsVisitor.Statistics(0, 1, 0), visitor.collect(new DirectoryNode("empty")));
        assertEquals(new StatisticsVisitor.Statistics(1, 1, 1), visitor.collect(root));
        assertEquals(new StatisticsVisitor.Statistics(1, 0, 0), visitor.collect(new FileNode("single.txt", 0)));
    }
}
