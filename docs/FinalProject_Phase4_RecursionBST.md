# Phase 4 — Recursion & BST

## Goal
Implement a recursive binary search tree that keeps the leaderboard sorted automatically.

## Recursion basics
A recursive method calls itself with a smaller problem.
It needs a base case to stop.

> **Reference:** [W3Schools — Java Recursion](https://www.w3schools.com/java/java_recursion.asp)

### Example: countdown
```java
void countdown(int n) {
    if (n <= 0) {
        System.out.println("Blastoff!");
        return;
    }
    System.out.println(n);
    countdown(n - 1);
}
```

### Example: factorial
```java
int factorial(int n) {
    if (n <= 1) return 1;
    return n * factorial(n - 1);
}
```

## Recursive game-related example
A flood-fill algorithm can color a connected area on a 2D grid — the same
kind of grid you built in Phase 1.

```java
void floodFill(int row, int col) {
    if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
    if (map[row][col] != 0) return;
    map[row][col] = 3; // fill value
    floodFill(row - 1, col);
    floodFill(row + 1, col);
    floodFill(row, col - 1);
    floodFill(row, col + 1);
}
```

## Linked lists

Before looking at BSTs, it helps to understand the simpler structure they're
built from: the **linked list**.

A linked list is a chain of nodes. Each node holds a value and a reference
to the **next** node in the chain. The last node's `next` is `null`.

```java
class Node {
    int value;
    Node next;   // reference to the next node, or null at the end
}
```

Because each node only knows its successor, you traverse a linked list from
front to back — you cannot jump directly to the middle.

Java's built-in `LinkedList` class implements this for you:

```java
LinkedList<String> list = new LinkedList<>();
list.addLast("first");
list.addLast("second");
list.addLast("third");
System.out.println(list.getFirst());  // "first"
System.out.println(list.size());      // 3
```

> **Reference:** [W3Schools — Java LinkedList](https://www.w3schools.com/java/java_linkedlist.asp)

`LinkedList` can also act as a **queue** — a structure where items join at
the back and leave from the front (first in, first out, like a checkout line).
The game engine uses a `LinkedList` as a queue internally to navigate the
maze, but you don't need to know the details.

---

## Binary search tree (BST) leaderboard

A BST is the same linked-node idea as a linked list, but each node has
**two** references instead of one — `left` for smaller values and `right`
for larger ones. This property keeps the tree sorted automatically.

```
        500
       /   \
     200   800
    /   \
  100   350
```

Inserting a score: walk left when the new score is smaller, right when it is
larger, until you find an empty spot.

Traversing in order (left → node → right) always yields scores from smallest
to largest.

### Node class
```java
class ScoreNode {
    int score;
    ScoreNode left;
    ScoreNode right;

    ScoreNode(int score) {
        this.score = score;
    }
}
```

### Insert method
```java
ScoreNode insert(ScoreNode root, int score) {
    if (root == null) return new ScoreNode(score);
    if (score < root.score) {
        root.left = insert(root.left, score);
    } else {
        root.right = insert(root.right, score);
    }
    return root;
}
```

### In-order traversal
```java
void printInOrder(ScoreNode node) {
    if (node == null) return;
    printInOrder(node.left);
    System.out.println(node.score);
    printInOrder(node.right);
}
```

---

## References as pointers

Java doesn't have explicit pointers, but object references work the same way.
When `ScoreNode` declares:

```java
ScoreNode left;
ScoreNode right;
```

`left` and `right` are references — they store the memory address of another
`ScoreNode`, exactly like a pointer in C. Setting `node.left = new ScoreNode(…)`
makes `left` point to a new node. Setting it to `null` is the equivalent of a
null pointer.

The BST is therefore a **linked structure**: every node holds two references
that chain it to its children — the same idea as a linked list node's single
`next` reference, just with two children instead of one.

| Structure | References per node | Ordered? |
|-----------|-------------------|----------|
| Linked list | 1 (`next`) | insertion order |
| BST | 2 (`left`, `right`) | sorted by value |

---

## In Pellet Pursuit

`ScoreTree.java` uses the exact same BST structure from the tutorial.
Two methods have `TODO` stubs for you to implement.

### ScoreNode.java
Compare the tutorial's `ScoreNode` to the one in `ScoreNode.java` — they are
nearly identical. The only addition is a `level` field alongside `score`.

### Your task

**`insert(ScoreNode node, int score, int level)`** — recursive BST insert.

Match the tutorial's insert pattern exactly:
- Base case: `node == null` → return `new ScoreNode(score, level)`
- Recursive case: go left if `score < node.score`, otherwise go right
- Always return `node` at the end

**`collectDescending(ScoreNode node, List<ScoreNode> result, int n)`** — reverse in-order traversal.

This is the mirror of the tutorial's `printInOrder`, but visiting **right first**
so scores come out highest-to-lowest, and stopping once `result` has `n` items:
- Base cases: `node == null` or `result.size() >= n` → return
- Visit right subtree → add node → visit left subtree

Once both methods are implemented the leaderboard on the Game Over screen
will show the top 5 scores in descending order.

### Extension tasks
- Add a `search(int score)` method that returns whether a score exists in the tree (recursive, like `insert`).
- Add a `max()` method that returns the highest score by walking right until there is no right child.
- Visualize the tree on screen: draw each node as a circle with lines connecting parent to child.
