package edu.astana.visitorfs;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchVisitorTest {
    @Test
    @DisplayName("T3 and T4: search combines inclusive size and name criteria with sorted relative paths")
    void filtersAndSortsRootRelativePaths() {
        DirectoryNode root = new DirectoryNode("workspace");
        DirectoryNode logs = new DirectoryNode("logs");
        logs.addChild(new FileNode("app.txt", 300));
        root.addChild(logs);

        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("readme.txt", 300));
        docs.addChild(new FileNode("large.md", 900));
        root.addChild(docs);

        DirectoryNode data = new DirectoryNode("data");
        data.addChild(new FileNode("small.txt", 299));
        root.addChild(data);

        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 300);

        assertEquals(List.of("docs/readme.txt", "logs/app.txt"), visitor.search(root));
    }

    @Test
    @DisplayName("T6: empty trees and no-match searches return an empty list")
    void handlesEmptyAndNoMatchSearches() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 10);
        assertEquals(List.of(), visitor.search(new DirectoryNode("empty")));

        DirectoryNode root = new DirectoryNode("root");
        root.addChild(new FileNode("image.png", 50));
        assertEquals(List.of(), visitor.search(root));
    }

    @Test
    @DisplayName("T7: repeated searches do not retain previous matches")
    void clearsMatchesBetweenSearches() {
        SearchVisitor visitor = new SearchVisitor(name -> name.endsWith(".txt"), 0);
        DirectoryNode first = new DirectoryNode("first");
        first.addChild(new FileNode("one.txt", 1));
        DirectoryNode second = new DirectoryNode("second");
        second.addChild(new FileNode("two.txt", 2));

        assertEquals(List.of("one.txt"), visitor.search(first));
        assertEquals(List.of("two.txt"), visitor.search(second));
    }
}
