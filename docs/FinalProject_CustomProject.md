# Custom Project Track

> **This track is by approval only and spots are limited.**
> Read this entire page before asking. If you're on the fence, keep working on
> Pellet Pursuit — the custom track is harder, not easier.

---

## What it is

Instead of completing Phases 2–4 of Pellet Pursuit, you build a different
Java project of your own design. You still present at the in-class demo and
you are graded on the same four categories as everyone else (engine
implementations, AI/logic, game feel, demo). The rubric items translate — they
don't disappear.

This is the right choice if you have a clear idea, you find Pellet Pursuit
genuinely uninteresting, and you're confident you can build your own scaffold
in the remaining class time. It is the wrong choice if you just want to skip
the BST or the ghost AI.

---

## Gate: what you must complete first

You must have a **working Phase 1** before you may apply:

- Player moves with arrow keys and WASD
- Walls stop the player (no phasing through)
- Maze is visible — walls, dots, and power pellets all render
- Dots disappear when eaten; level ends when all are gone

Show this to the instructor before writing your proposal. If Phase 1 isn't
working, finish it first.

---

## The four required structures

Every submission — Pellet Pursuit or custom — must demonstrate all four of
these. Your proposal must name *exactly where* each one appears in your design.

| # | Structure | What it must do |
|---|-----------|----------------|
| 1 | **2D array** | Store a grid of data that drives what is drawn or simulated (tile map, board, pixel canvas, dungeon layout, etc.) |
| 2 | **Abstract class hierarchy** | At least one abstract base class with **three or more concrete subclasses** that behave differently from each other |
| 3 | **Recursive BST + file I/O** | `insert()` and `collectDescending()` implemented recursively; scores (or equivalent) saved to a file and loaded back on startup |
| 4 | **ArrayList with iteration and removal** | A collection of live objects updated every frame; items removed without `ConcurrentModificationException` |

Proposals that say "I'll use a HashMap instead of a BST" or "I don't need a
grid" will be rejected. These structures exist because the course assessed
them — your project must too.

---

## Writing your proposal

Fill in this template and hand it to the instructor. Keep it short — a few
sentences per row is enough. Vague answers ("I'll use a grid somewhere") will
be sent back for revision.

```
Project name: 
One-sentence description: 

2D array
  Class/file it lives in:
  What each cell represents:
  How it drives what appears on screen:

Abstract hierarchy
  Base class name:
  Three subclass names and how each behaves differently:

Recursive BST + file I/O
  What gets stored in the BST (score, item, character?):
  Where insert() is called:
  Where collectDescending() is used:
  File name scores/data are saved to:

ArrayList with iteration and removal
  What objects are in the list:
  What triggers removal:
  How you avoid ConcurrentModificationException:
```

---

## Grading

Approved custom projects are graded on the same rubric as Pellet Pursuit.
The four categories map like this:

| Rubric category | Pellet Pursuit | Custom project equivalent |
|-----------------|---------------|--------------------------|
| Engine implementations (50 pts) | The six method groups | The equivalent implementations named in your proposal |
| AI/Logic (30 pts) | Three ghost personalities | Three subclass behaviors that are visibly distinct |
| Difficulty & feel (10 pts) | Maze design, bonus item | Something you tuned or designed that affects game feel |
| Demo (5 pts) | Run and explain one decision | Run and explain one decision |

Documentation and code-quality expectations are identical.

---

## Practical advice

- **Start with a sketch.** Before writing any code, draw your grid, name your
  subclasses, and decide what goes in the BST. A clear design catches missing
  requirements early.
- **Build the grid first.** A working 2D array that draws something on screen
  is your Phase 1 equivalent — it proves the engine is alive.
- **Don't delay the BST.** It is the most time-consuming piece. Start it in
  parallel with the grid, not after everything else is done.
- **Ask early.** If you're stuck for more than 15 minutes on something, ask.
  There's no worked example to fall back on, so you'll need to ask more than
  Pellet Pursuit students do.
