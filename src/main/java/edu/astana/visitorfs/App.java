package edu.astana.visitorfs;

public final class App {
    private App() {
    }

    public static void main(String[] args) {
        DirectoryNode root = createSampleFileSystem();

        System.out.println("Total size: " + new SizeVisitor().calculate(root) + " bytes");
        System.out.println("Matches:");
        SearchVisitor searchVisitor = new SearchVisitor(name -> name.endsWith(".txt"), 200);
        for (String path : searchVisitor.search(root)) {
            System.out.println(path);
        }

        StatisticsVisitor.Statistics statistics = new StatisticsVisitor().collect(root);
        System.out.println("Files: " + statistics.fileCount());
        System.out.println("Directories: " + statistics.directoryCount());
        System.out.println("Maximum depth: " + statistics.maximumDepth());
    }

    private static DirectoryNode createSampleFileSystem() {
        DirectoryNode root = new DirectoryNode("workspace");
        DirectoryNode docs = new DirectoryNode("docs");
        docs.addChild(new FileNode("readme.txt", 420));
        docs.addChild(new FileNode("notes.md", 180));

        DirectoryNode assets = new DirectoryNode("assets");
        assets.addChild(new FileNode("logo.png", 850));
        assets.addChild(new FileNode("changelog.txt", 260));

        DirectoryNode exports = new DirectoryNode("exports");
        exports.addChild(new FileNode("report.pdf", 1200));
        assets.addChild(exports);

        root.addChild(assets);
        root.addChild(docs);
        return root;
    }
}
