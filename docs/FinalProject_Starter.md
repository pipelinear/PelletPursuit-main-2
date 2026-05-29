# Pellet Pursuit Starter Guide

## Goal
Understand how the project is organized and identify the files you will edit.

---

## How the project is organized

Unlike a single-file starter, Pellet Pursuit is split into classes that each
have one job. You do not need to understand every file to get started — focus
on the ones listed under **Your tasks** below.

**All the files you will edit live in `src/main/java/game/`.** When you build
or run the project, Java compiles them into `target/` — those compiled files
are read-only and will be overwritten every run. If IntelliJ opens a file from
`target/`, close it and open the matching one from `src/main/java/game/` instead.

```
src/main/java/game/
  GameApp.java  — JavaFX Application: game loop, score, lives, HUD (heads-up display)
  Player.java             — player movement and dot eating
  Ghost.java              — abstract ghost: movement engine, frighten/dead states  ← Phase 2
    Shadow.java           — WORKED EXAMPLE — read this first
    Patrol.java           — TODO: implement chooseTarget()
    Shy.java              — TODO: implement chooseTarget()
    Ambush.java           — TODO: implement chooseTarget()
  GameMap.java            — tile maze layout and dot tracking
  LevelConfig.java        — difficulty values per level
  BonusItem.java          — abstract bonus item + Cherry example
  ScoreNode.java          — BST node for the high-score table
  ScoreTree.java          — BST high-score table with file persistence
```

**Navigating in IntelliJ:** press `Cmd+F12` (Mac) or `Ctrl+F12` (Windows) to
open a searchable list of every method and field in the current file. Start
typing a method name and press Enter to jump to it — useful in longer files
like `GameApp.java`.

**Reading a guide and editing code at the same time:** open the phase guide
from the `docs/` folder in the Project panel on the left. IntelliJ renders
`.md` files as formatted text — if you see raw markdown symbols instead,
click the small preview icon in the top-right corner of the editor (it looks
like a split rectangle). To put the guide and a source file side by side,
right-click the source file's tab and choose **Split Right**; drag the tab
back to recombine when you're done.

---

## The game loop

`GameApp.java` contains a JavaFX `AnimationTimer` that fires ~60
times per second. Each tick calls two methods:

```java
void update(double dt)   // move things, check collisions, change state
void draw()              // clear screen, draw everything
```

`dt` is the elapsed time in seconds since the last frame (~0.016 s at 60 fps).
Multiplying speeds by `dt` keeps movement frame-rate independent.

---

## Your tasks at a glance

Each phase has a detailed guide in `docs/` — here's the short version so you
know what you're getting into before you read them.

### Phase 1 — [Movement & maps](FinalProject_Phase1_TileMap.md)
- Implement `handleKey()` in `Player.java` — arrow keys queue a direction
- Implement `isWall()` in `GameMap.java` — one line, tile lookup
- Implement `canMove` in `Player.update()` — connects `isWall()` to movement
- Design your maze in `DEFAULT_LAYOUT`
- Implement `draw()` in `GameMap.java` — renders walls, dots, power pellets

### Phase 2 — [Enemies](FinalProject_Phase2_Enemies.md)
- Implement `collidesWith()` in `Ghost.java` — distance check
- Implement `chooseTarget()` in `Patrol.java`, `Shy.java`, `Ambush.java`
- Tune `LevelConfig.forLevel()` so difficulty scales across levels

### Phase 3 — [Bonus items & file I/O](FinalProject_Phase3_State.md)
- Create a custom bonus item by extending `BonusItem`
- Implement `updateBonusItems()` in `GameApp.java` — iterate the list, collect, remove expired items
- Implement `saveToFile()` and `loadFromFile()` in `ScoreTree.java`

### Phase 4 — [Recursion & BST](FinalProject_Phase4_RecursionBST.md)
- Implement `insert()` and `collectDescending()` in `ScoreTree.java` — builds the high-score leaderboard shown on the Game Over screen

---

## Personalizing your game

These are fast, low-effort changes that make your submission feel like yours
rather than a copy of everyone else's. None require reading the engine code.

**When to do this:** once Phase 1 is working and your game runs. Ghost names
and wall color can wait until Phase 2 is done.

| What | Where | How |
|------|-------|-----|
| Game title & tagline | `GameApp.java` top | Change `GAME_TITLE` and `GAME_SUBTITLE` — shown on the start screen and window bar |
| Scores file name | `GameApp.java` top | Change `SCORES_FILE` — the file your leaderboard is saved to |
| Leaderboard heading | `GameApp.java` top | Change `LEADERBOARD_TITLE` — shown above high scores on the Game Over screen |
| Starting lives | `GameApp.java` top | Change `STARTING_LIVES` — try 5 for an easier game, 1 for a brutal one |
| HUD colors | `GameApp.java` top | Change `HUD_COLOR` and `HUD_TEXT` — the score/lives bar |
| Death / win messages | `GameApp.java` top | Change `MSG_DEAD`, `MSG_READY`, `MSG_WIN` |
| Player color | `Player.java` | Set `bodyColor` to any `Color` in the field declaration |
| Ghost names | `Patrol.java`, `Shy.java`, `Ambush.java` | Change the string in `getName()` — shown on screen when a ghost catches you |
| Wall color | `GameMap.java` | Change the hex code in the no-arg `draw()` (Step 10 in Phase 1) |
| Dot color | `GameMap.java` | Set `dotColor` to any `Color` in the field declaration |
| Custom sprite images | Extension (see Rubric) | Drop a PNG in `src/main/resources/game/images/` and call `loadImage()` |

---

## Getting started

1. Run the game (`./mvnw javafx:run`). The player sits still and ignores
   input — that's expected.
2. Open the Phase 1 guide (`docs/FinalProject_Phase1_TileMap.md`) and start
   with `handleKey()` in `Player.java`.

---

## Git and GitHub

**Git** is the tool that tracks every change you make to your code. When you
cloned this project, you used git. Every saved version you can go back to is
stored by git.

**GitHub** is a website that hosts git projects online — like Google Drive for
code, but with full history. Your GitHub profile is a portfolio that colleges
and employers actually look at.

If you find a bug in the project or have a question, open an issue here:
[github.com/SBHS-Computer-Science-Academy/PelletPursuit/issues](https://github.com/SBHS-Computer-Science-Academy/PelletPursuit/issues)

---

## Pulling updates

This project is updated as new phases are released — guides get improved and
bugs in the starter code get fixed. **Before starting each new phase, pull the
latest changes** so you have the most up-to-date version.

**In IntelliJ:** Git menu → Pull

**In the terminal:**
```
git pull
```

Your own code is safe: `git pull` only touches files that were changed in the
project — it won't overwrite anything you have edited yourself.
