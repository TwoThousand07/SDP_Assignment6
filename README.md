# Visitor Pattern - Advanced File System

An in-memory hierarchical file system implemented with the Visitor pattern. The project uses Java 17, Maven, and JUnit 5.

## Run

Run the automated tests from this directory:

```text
mvn test
```

Run the deterministic demonstration:

```text
mvn -q -DskipTests compile
java -cp target/classes edu.astana.visitorfs.App
```

## Operations

- `SizeVisitor.calculate(root)` sums file sizes in the selected subtree.
- `SearchVisitor.search(root)` combines a filename predicate with an inclusive minimum size and returns sorted root-relative paths.
- `StatisticsVisitor.collect(root)` returns file count, directory count, and maximum depth. A selected root has depth zero; files contribute to maximum depth.

Each operation object can be reused. Its public operation method clears its previous state before traversing. Search paths use `/`, omit the selected root directory name, and sort by Java's natural string order.

## Tree rules

Names must be nonblank single path segments. File sizes cannot be negative. A directory rejects duplicate sibling names, a second parent, and cycles. `getChildren()` returns an immutable snapshot; changes go through `addChild` and `removeChild`.

## Traversal and extension

Each visitor owns its traversal. This keeps `FileNode` and `DirectoryNode` focused on the element contract and lets each operation manage its own state. Adding another operation is a new visitor class. Adding a new element type requires a new `Visitor` overload and an implementation of `accept` for the new type, after which each visitor must decide how to handle it. The compiler helps find missing implementations, while the visitor interface is less stable when element types change.

## Clean Code practices used

1. Intent-revealing operation names: `calculate`, `search`, and `collect` state what each visitor returns.
2. Single responsibility: each visitor implements one filesystem operation.
3. Encapsulation: directory children are exposed as immutable snapshots, and tree mutations are validated by `DirectoryNode`.
4. Guard clauses: constructors and mutation methods reject invalid values before changing the tree.
5. Deterministic output: search sorts paths and uses a documented root-relative format.

## Test suite

JUnit 5 tests cover double dispatch, nested size accumulation, combined search conditions, deterministic paths, statistics and depth, empty/no-match cases, visitor reuse, and tree integrity.

- `VisitorDispatchTest`
- `SizeVisitorTest`
- `SearchVisitorTest`
- `StatisticsVisitorTest`
- `DirectoryNodeTest`
