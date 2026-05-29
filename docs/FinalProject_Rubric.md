# CS105 Pellet Pursuit — Grading Rubric

## Overview
100-point rubric for the Pellet Pursuit final project.
Students implement core engine methods, three ghost personalities, difficulty
tuning, a custom bonus item, and file I/O, and deliver a short in-class demo.
Extension tasks are a bonus.

---

## 1. Engine Implementations — 50 points

Short but critical methods spread across the phase guides. Each one has a
visible, testable consequence when missing.

| Method | File | Points | Broken state without it |
|--------|------|--------|------------------------|
| `handleKey()` + `isWall()` + `canMove` | `Player.java`, `GameMap.java` | 10 | Player sits still, ignores input, phases through walls once moving |
| `draw()` tile loop + `eatDot()` | `GameMap.java` | 5 | Maze invisible; dots never disappear and level never ends |
| `collidesWith()` | `Ghost.java` | 5 | Ghosts never kill or get eaten |
| `updateBonusItems()` | `GameApp.java` | 5 | Bonus items never collected or removed |
| `saveToFile()` + `loadFromFile()` | `ScoreTree.java` | 10 | Scores reset every run |
| `insert()` + `collectDescending()` | `ScoreTree.java` | 15 | Leaderboard never populates |

- **45–50:** All six groups fully correct and visibly working.
- **30–40:** Four or five groups correct; one or two missing or broken.
- **15–25:** Two or three groups correct; others still use placeholders.
- **0–10:** One or fewer groups correct; game is largely non-functional.

---

## 2. Ghost Implementations — 30 points

All three ghosts (`Patrol`, `Shy`, `Ambush`) must behave **distinctly** from
each other and from the chase-only placeholder. `collidesWith()` (category 1)
must be working for ghost interactions to be gradeable.

- **25–30:** All three ghosts behave as specified. Frightened mode works
  correctly for each. No crashes or infinite loops.
- **15–20:** Two ghosts fully correct; one is partially implemented or has
  a minor behavior bug.
- **5–10:** One ghost fully correct; others still use the placeholder or
  have major bugs.
- **0:** No ghost implementations beyond the placeholder, or code crashes.

**Per-ghost breakdown (use to allocate partial credit):**

| Ghost | Full | Partial | None |
|-------|------|---------|------|
| Patrol (pink) — patrol corner + chase when close | 10 | 5 | 0 |
| Shy (cyan) — flee until cornered, then attack | 10 | 5 | 0 |
| Ambush (orange) — target ahead of player's heading | 10 | 5 | 0 |

---

## 3. Difficulty & Game Feel — 10 points

| Item | Points |
|------|--------|
| Maze is visibly different from the default layout | 5 |
| Custom bonus item works, differs from Cherry (points, color, label), and has a custom `draw()` with a time-based visual feature | 5 |

`LevelConfig` tuning is not separately scored but is part of the In-Class Demo
— students should be prepared to explain what values they changed, why, and how
the game feel differs between level 1 and later levels.

---

## 4. In-Class Demo — 5 points

Two minutes at the projector. Run your game and explain one design decision
(ghost personality, maze layout, bonus item, or difficulty tuning).

| Score | Criteria |
|-------|----------|
| 5 | Game runs, student explains their choice clearly |
| 3 | Game runs but explanation is vague or incomplete |
| 0 | No-show, game crashes, or explanation is absent |

---

## 5. Documentation — 5 points

Students are expected to comment their own implementations, not the
pre-existing engine code.

- **5:** Each student-written method has a short comment explaining the
  approach (not just restating the code).
- **3:** Some methods commented but not all, or comments only restate
  what the code does.
- **0–2:** Very few or no student-written comments.

---

## 6. Extension Work — 5 points bonus

*Base tasks are worth 100 points. Extension points are a bonus (max 5).*

Students choose **one** of the three extension paths:

### Option 1 — Pellet Pursuit add-ons

| Task | Points |
|------|--------|
| Per-level layouts (`getLayout()` returns different maze per level) | +1 |
| Player movement re-implemented (all directions + tunnel wrapping) | +2 |
| BFS replaced with a different pathfinding strategy (must behave differently) | +2 |
| Custom sprite images (PNG in `resources/game/images/`, loaded via `loadImage()`) | +1 |

### Option 2 — Free-form project (early finishers)

Student completed all four phases and built an additional project of their
own choosing with remaining class time.

- **+5** Project runs and student explains it clearly at the demo
- **+3** Project runs but explanation is vague or incomplete
- **+0** Nothing additional to show

No data-structure requirements — base tasks already cover them.

### Option 3 — Custom project track (approved proposals only)

Student completed Phase 1, submitted an approved proposal, and built a
different project for Phases 2–4. Graded against the same four rubric
categories as Pellet Pursuit — see mapping below.

**Apply the standard rubric categories** (sections 1–5) to the custom
project, with these translations:

| Standard category | Custom project equivalent |
|-------------------|--------------------------|
| Engine implementations | The method groups named in the approved proposal |
| Ghost personalities | Three concrete subclasses with visibly distinct behavior |
| Difficulty & game feel | Something tuned/designed that affects the experience |
| Demo | Run and explain one decision |

The custom project earns the same base 100 points as Pellet Pursuit; there
are no additional extension points for doing Option 3.

---

## Project Checklist

Use this before submitting. Each item should be verifiable by running the game.

**Engine implementations**
- [ ] `isWall()` returns true for wall tiles
- [ ] `canMove` in `Player.update()` uses `isWall()`
- [ ] `handleKey()` responds to all four arrow keys and WASD
- [ ] Walls stop the player and ghosts from passing through
- [ ] Maze is visible — walls, dots, and power pellets all render correctly
- [ ] Dots and power pellets disappear when the player walks over them; level ends when all are eaten
- [ ] Ghost kills player on contact; frightened ghosts can be eaten
- [ ] Bonus item appears, can be collected, and disappears when expired
- [ ] Scores persist between sessions (quit and relaunch to verify)
- [ ] Leaderboard on the Game Over screen shows top scores in descending order

**Ghost personalities**
- [ ] Patrol heads toward a corner when the player is far away
- [ ] Patrol switches to chasing when the player gets close
- [ ] Shy moves away from the player in open corridors
- [ ] Shy turns and chases when trapped in a dead end
- [ ] Ambush approaches from in front of the player, not from behind
- [ ] All three ghosts turn blue when a power pellet is eaten
- [ ] Frightened behavior is distinct from normal behavior for each ghost

**Difficulty, maze & bonus item**
- [ ] Maze is visibly different from the default layout
- [ ] Custom bonus item has unique points, color, and label (not Cherry)

**Personalization**
- [ ] `GAME_TITLE` changed from "Pellet Pursuit"
- [ ] Each ghost has a custom name in `getName()`

**Code & documentation**
- [ ] No compilation errors or runtime crashes
- [ ] Each student-written method has at least one explanatory comment
- [ ] No placeholder `return` statement left in a finished method

---

## Notes for instructors
- Grade engine implementations first — if `isWall()` or `collidesWith()` are
  missing, ghost behavior cannot be fairly assessed.
- Patrol's frightened mode (flee away) and Shy's frightened mode (chase instead
  of flee) are intentionally opposite — both are correct.
- A ghost that always chases (placeholder) scores 0 for that ghost even if the
  code compiles and runs.
- Extension points are additive and do not replace base task points.

**Custom project track:**
- Require Phase 1 to be demonstrably working before accepting a proposal —
  the gate exists to filter out students who want to escape the work, not to
  reward them for getting stuck early.
- Reject proposals that don't concretely name where each required structure
  appears. "I'll use a list somewhere" is not enough.
- Limit approvals to however many students you can realistically support
  during lab time without neglecting Pellet Pursuit students.
- Grade using the standard rubric; the category translations in section 6
  map each Pellet Pursuit concept to its custom-project equivalent.
