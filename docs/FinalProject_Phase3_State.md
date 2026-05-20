# JavaFX Game State and Collision Logic

## Goal
Explain how to track game state, detect collisions, and show game status to the player.

## Common game state variables
```java
boolean gameOver = false;
boolean gameStarted = false;
int score = 0;
int lives = 3;
```

## Collision helper method
Use the same method for player/enemy, player/wall, or player/goal collisions.

```java
boolean intersects(double x1, double y1, double w1, double h1,
                   double x2, double y2, double w2, double h2) {
    return x1 < x2 + w2 &&
           x1 + w1 > x2 &&
           y1 < y2 + h2 &&
           y1 + h1 > y2;
}
```

## Managing game states
### Start screen
```java
if (!gameStarted) {
    gc.setFill(Color.WHITE);
    gc.fillText("Press ENTER to start", 300, 320);
    if (keysPressed.contains(KeyCode.ENTER)) {
        gameStarted = true;
    }
    return;
}
```

### Game over
```java
if (gameOver) {
    gc.setFill(Color.WHITE);
    gc.fillText("Game Over", 360, 280);
    gc.fillText("Press ENTER to restart", 300, 310);
    if (keysPressed.contains(KeyCode.ENTER)) {
        resetGame();
    }
    return;
}
```

## Restart and reset methods
```java
void resetPlayer() {
    playerX = TILE_SIZE + 4;
    playerY = TILE_SIZE + 4;
}

void resetGame() {
    score = 0;
    lives = 3;
    gameOver = false;
    gameStarted = false;
    resetPlayer();
    createEnemies();
}
```

## Drawing status text
Add clear feedback so players always know the goal.

```java
gc.setFill(Color.WHITE);
gc.fillText("Score: " + score, 10, 18);
gc.fillText("Lives: " + lives, 10, 34);
```

## Using state in update()
- only move player when `gameStarted && !gameOver`
- adjust score when the player reaches a goal
- subtract life when an enemy hits the player
- set `gameOver` when lives reach zero

---

## In Pellet Pursuit

`GameApp.java` uses a `State` enum instead of boolean flags, which
scales better when there are many distinct states:

```java
enum State { START, PLAYING, PAUSED, DEAD_PAUSE, LEVEL_CLEAR, WIN, GAME_OVER }
```

The main `update()` and `draw()` methods both switch on the current state,
so each state's logic is kept in one place. This is the same idea as the
`gameStarted` / `gameOver` booleans in the example above — just more explicit.

### Score and lives
`score` and `lives` are tracked as fields in `GameApp`. When lives
reach zero, the state transitions to `GAME_OVER`; when all dots are eaten it
transitions to `LEVEL_CLEAR`.

### Your task
Open `GameApp.java` and find the `PLAYING` case in `update()`. Locate the
call that handles dot collection and note: what method does it call, and what
point values does it return for a regular dot vs. a power pellet? Then find
where `lives` is decremented — follow the code to identify which state the
game transitions to immediately after a life is lost.

---

## Creating a custom bonus item

Bonus items are collectibles that appear mid-level for a limited time.
The project includes `Cherry` as a worked example. Your job is to replace it
with something of your own.

### How BonusItem works

`BonusItem` is an abstract class with three methods to implement:

> **Reference:** [W3Schools — Java Abstract Classes](https://www.w3schools.com/java/java_abstract.asp)

```java
public abstract int    getPoints();  // points awarded on collection
public abstract String getLabel();   // short text drawn on the icon
public abstract Color  getColor();   // fill color of the circle
```

The lifetime (how long it stays on screen) is set in the constructor.

### The Cherry example

```java
class Cherry extends BonusItem {
    public Cherry(double pixelX, double pixelY) {
        super(pixelX, pixelY, GameMap.TILE, 8.0); // size=TILE, lifetime=8s
    }
    @Override public int    getPoints() { return 200; }
    @Override public String getLabel()  { return "200"; }
    @Override public Color  getColor()  { return Color.RED; }
}
```

### Your task
1. Add a new class below `Cherry` in `BonusItem.java` that extends `BonusItem`.
2. Override `getPoints()`, `getLabel()`, and `getColor()` with values that
   differ from Cherry's.
3. In `GameApp.java`, search for `new Cherry` and replace it with
   `new YourClassName`.

> **Going further:** override `draw()` to add a visual feature that reflects
> the time remaining. `getLifetime()` returns how many seconds are left — use
> it to make the item look different as it counts down (shrinking circle,
> countdown label, pulsing size). Call `super.draw(gc)` first to keep the base
> circle, then add your feature on top.

---

## Implementing `updateBonusItems()`

Once your custom bonus item class exists, you need to wire up the collection
logic in `GameApp.java`. Find the `updateBonusItems()` stub and
implement it in two steps.

### Step 1 — update each item and award points on contact

Loop over `bonusItems`, tick each item's countdown timer, and add points if
the player is touching it:

```java
for (BonusItem item : bonusItems) {
    item.update(dt, map);
    if (item.collidesWith(player)) {
        score += item.getPoints();
        audio.playBonus();
    }
}
```

### Step 2 — remove collected or expired items

You cannot call `bonusItems.remove()` *inside* the loop above — Java throws
a `ConcurrentModificationException` if you modify a list while a `for-each`
is still iterating it. The safe pattern is to collect the items to remove
first, then delete them after the loop:

```java
List<BonusItem> toRemove = new ArrayList<>();
for (BonusItem item : bonusItems) {
    if (item.collidesWith(player) || item.isExpired()) {
        toRemove.add(item);
    }
}
bonusItems.removeAll(toRemove);
```

> **Going further — the one-liner version**
>
> Java has a built-in method called `removeIf` that combines Steps 1 and 2
> into a single line:
> ```java
> bonusItems.removeIf(item -> item.collidesWith(player) || item.isExpired());
> ```
> The `item -> ...` syntax is called a **lambda expression** — a short,
> unnamed function written inline. `removeIf` calls it on every element and
> removes the ones where it returns `true`. You'll see this pattern often in
> professional Java code.

---

## Saving and loading the high-score table

`ScoreTree.java` stores scores in memory while the game runs — but they
disappear when the program closes. `saveToFile()` and `loadFromFile()`
persist them to a plain text file so the leaderboard survives between sessions.

Each line in the file stores one entry: `<score> <level>` — for example `1500 2`.

### Writing to a file

`BufferedWriter` writes text line by line. Wrap it in a `try-with-resources`
block so the file is closed automatically even if something goes wrong:

```java
try (BufferedWriter writer = Files.newBufferedWriter(path)) {
    writer.write("some text");
    writer.newLine();   // writes a line break
} catch (IOException e) {
    e.printStackTrace();
}
```

`collectInOrder()` is already implemented in `ScoreTree.java` — call it to
get a `List<String>` of formatted score lines, then write each one.

### Reading from a file

`Files.readAllLines(path)` reads every line into a `List<String>`:

```java
try {
    List<String> lines = Files.readAllLines(path);
    for (String line : lines) {
        // parse each line here
    }
} catch (IOException e) {
    e.printStackTrace();
}
```

For each line, `split(" ")` separates the two numbers, and
`Integer.parseInt()` converts each string to an int.

### Skipping duplicates with HashSet

A **`HashSet`** is a collection that holds each value **at most once**.
Unlike a `List`, adding a value that is already present does nothing.
`add()` returns `true` if the item was new, `false` if it was already there —
which makes it easy to skip duplicates in a single check:

```java
Set<String> seen = new HashSet<>();
for (String line : lines) {
    if (seen.add(line)) {   // false if already seen — skip it
        // process line
    }
}
```

> **Reference:** [W3Schools — Java HashSet](https://www.w3schools.com/java/java_hashset.asp)

### Your task

**`saveToFile(Path path)`** — write every score to the file, one per line.
Use `collectInOrder()` to get the lines, then write them with `BufferedWriter`.

**`loadFromFile(Path path)`** — read scores back from the file.
Return early if the file does not exist, use a `HashSet` to skip duplicates,
and call `insert()` for each unique entry.

> **Note:** `loadFromFile()` calls `insert()` to rebuild the tree. If you
> haven't implemented `insert()` yet (Phase 4), scores will be read from disk
> but silently discarded — implement Phase 4 first if you want to test
> persistence end-to-end.

---

When you're ready: **[Phase 4 — Recursion & BST](FinalProject_Phase4_RecursionBST.md)**
