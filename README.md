# Pellet Pursuit

A JavaFX maze game — collect dots, dodge ghosts, chase high scores. Any
similarities to well-known arcade games are strictly coincidental.

The engine compiles and launches, but the maze is invisible and the player
doesn't move yet — your job is to implement the gameplay across four phases,
from basic movement to ghost AI to a persistent high-score leaderboard.

## Running the game

Press the green **Run** button in IntelliJ, or in the terminal:

```bash
./mvnw javafx:run
```

> **First run only:** if the run dialog shows **"module not specified"** under
> **Build and run**, click that drop-down and select **24** to set the JDK
> (Java Development Kit). You only need to do this once.

You may see `WARNING: sun.misc.Unsafe` lines in the console — ignore them. They come from the graphics library, not your code, and don't affect the game.

## Controls

| Key | Action |
|-----|--------|
| Arrow keys | Move |
| SPACE | Pause / Resume |
| ENTER | Start / Restart |

---

## What you're building

Work through the four phases in order. Each one has a guide in `docs/`.

| Phase | Focus | Key files |
|-------|-------|-----------|
| 1 | Player controls, wall collision, maze design, tile drawing | `Player.java`, `GameMap.java` |
| 2 | Ghost personalities, collision detection, difficulty tuning | `Ghost.java`, `Patrol.java`, `Shy.java`, `Ambush.java` |
| 3 | Custom bonus item, collection logic, score file I/O | `BonusItem.java`, `ScoreTree.java`, `GameApp.java` |
| 4 | Recursive BST insert and leaderboard traversal | `ScoreTree.java` |

Once the gameplay is working, personalize your game — name it, theme it, tune
it. See the **Personalizing your game** section in `docs/FinalProject_Starter.md`.

### Going further

Three options once the base tasks are done — pick one:

- **Add-ons** — per-level layouts, custom pathfinding, sprite images (+5 pts bonus)
- **Early finisher** — build something new in Java if you finish all four phases
- **Custom project** — build a different project instead of Phases 2–4, by approval after Phase 1 — see `docs/FinalProject_CustomProject.md`

---

## File guide

```
src/main/java/game/
  Sprite.java       — base class: position, size, speed, helper methods
  Player.java       — player input, movement, dot eating
  Ghost.java        — abstract ghost: BFS, frighten/dead states  ← read me
    Shadow.java     — WORKED EXAMPLE: complete chooseTarget()
    Patrol.java     — TODO: implement chooseTarget()
    Shy.java        — TODO: implement chooseTarget()
    Ambush.java     — TODO: implement chooseTarget()
  GameMap.java      — tile maze, dot/power-pellet tracking
  LevelConfig.java  — difficulty values per level  ← edit me
  BonusItem.java    — abstract bonus item + Cherry example  ← extend me
  AudioManager.java — synthesised sound effects (no files needed)
  ScoreTree.java    — BST high-score table
  ScoreNode.java    — BST node
  GameApp.java      — JavaFX Application, game loop, HUD  ← customize me
```

---

## Documents

The guides in `docs/` are Markdown files (`.md`) — plain text with light
formatting. To read them with formatting in IntelliJ, open the file and click
the **Preview** icon in the top-right corner of the editor.

You can also read all the guides online:

https://sbhs-computer-science-academy.github.io/PelletPursuit/docs/

If IntelliJ or Maven gives you trouble:

https://sbhs-computer-science-academy.github.io/PelletPursuit/docs/Troubleshooting
