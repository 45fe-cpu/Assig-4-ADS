# Assignment 4 — Graph, BFS and DFS (Java)

## Overview
Implementation of a graph toolkit with BFS and DFS algorithms.
Includes traversal, shortest paths in unweighted graphs, connected components,
cycle detection, and an applied problem (Social Reachability).

## Features
- Graph ADT (directed/undirected, weighted/unweighted)
- Adjacency list + adjacency matrix export
- BFS: order, distance, parent, shortest path
- DFS: recursive and iterative
- Connected components (undirected)
- Cycle detection (undirected)
- Applied problem: Social Reachability

## Build & Run
```bash
javac -d out $(find src -name "*.java")
java -cp out Main
Tests
java -cp out Tests
Determinism
Neighbors are processed in sorted order using TreeMap/TreeSet.

---

## 3) Коммит тестов и README
```bash
git add .
git commit -m "Add tests, stress test, and README"
git push