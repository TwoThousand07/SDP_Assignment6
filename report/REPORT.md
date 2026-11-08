# Assignment 06: Visitor Pattern - Advanced File System

**Course:** ShP-2216, Software Design Patterns  
**Student:** Yerzhan Aitimov
**GitHub repository:** https://github.com/TwoThousand07/SDP_Assignment6

## Purpose and design

This project models an in-memory hierarchical file system. `FileNode` stores a name and non-negative byte size. `DirectoryNode` stores named child elements and supports arbitrary nesting. The `Element` interface defines `accept(Visitor)`, and `Visitor` declares an overloaded method for each concrete element type. The `AbstractElement` base class holds the shared name and parent reference.

The three operations are independent visitors:

| Operation | Visitor | Behavior |
| --- | --- | --- |
| Subtree size | `SizeVisitor` | Adds each file size once; directories add no size of their own. |
| Search | `SearchVisitor` | Combines a filename predicate and an inclusive minimum size, then returns sorted root-relative paths. |
| Statistics | `StatisticsVisitor` | Counts files and directories, including a selected directory root, and reports maximum depth with root depth zero. |

## Double dispatch and traversal

Each concrete node implements `accept` by calling the matching overloaded `visit` method with itself. Java selects that overload from the node's compile-time type, providing the second dispatch step without `instanceof`, reflection, or casts. Each visitor owns recursive traversal. This keeps traversal out of the data classes and allows each operation to manage its own counters or path state.

Search paths omit the chosen root directory name, use `/` between names, and are sorted lexicographically. All operation methods reset their internal state before walking, so visitor instances can be reused safely.

## Clean Code practices

1. **Intent-revealing names:** `calculate`, `search`, and `collect` describe the operation each public visitor method performs.
2. **Single responsibility:** each visitor implements one operation, while `DirectoryNode` owns tree mutation rules.
3. **Encapsulation:** `getChildren()` returns an immutable snapshot, and the internal child list cannot be edited by callers.
4. **Guard clauses:** constructors and `addChild` check invalid input before it changes the tree.
5. **Deterministic output:** `SearchVisitor` sorts returned paths and documents the root-relative path format.

Adding another operation is straightforward: create a visitor and implement the two existing overloads. Adding a new element type requires a new `Visitor` overload and an `accept` implementation for the new type; all existing visitors then need a decision for that type. This makes the visitor interface less convenient when the element hierarchy changes, while compiler errors help locate visitors that need updating.

## Tree integrity and verification

`DirectoryNode.addChild` rejects duplicate sibling names, elements that already have a parent, and changes that would form a cycle. `removeChild` clears ownership and allows a detached element to be added elsewhere. Child lists are exposed as immutable snapshots. File sizes and names are validated when elements are created.

Run the automated suite from the project root:

```text
mvn test
```

The five JUnit test classes are `VisitorDispatchTest`, `SizeVisitorTest`, `SearchVisitorTest`, `StatisticsVisitorTest`, and `DirectoryNodeTest`. They cover all eight requested verification targets, including repeat executions and invalid mutations.

The suite result for this version is **14 tests, 0 failures, 0 errors, 0 skipped; BUILD SUCCESS**.

## Demonstration output

The application uses its own sample tree and produces:

```text
Total size: 2910 bytes
Matches:
assets/changelog.txt
docs/readme.txt
Files: 5
Directories: 4
Maximum depth: 3
```

Run it after compiling with `mvn -q -DskipTests compile`:

```text
java -cp target/classes edu.astana.visitorfs.App
```
