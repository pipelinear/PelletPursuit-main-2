# Publishing Your Final Project

Once your game is complete, you can share it as an online portfolio entry that you can show to future employers, colleges, or anyone else. The steps below are optional — your submitted ZIP is all that's required for the showcase.

---

## Part 0: Push Your Project to GitHub

Your GitHub repo is where your code lives online and what you'll link to from
your portfolio. Set it up once — then push updates whenever you finish a phase.

1. Log in to [github.com](https://github.com) and click **+** → **New repository**.
2. Name it after your game. Set it to **Public** and click **Create repository**.
   **Do not** check "Add a README" or "Add .gitignore" — you already have those.
3. In IntelliJ, go to **Git** menu → **Push**. In the push dialog, click
   **Define remote**, paste your new repo's URL (shown on the GitHub page after
   you create it), and click **OK** → **Push**.
4. Refresh your GitHub repo page — your code should be there.

From now on, push after finishing each phase: **Git** → **Push**
(or `Cmd+Shift+K` on Mac / `Ctrl+Shift+K` on Windows).

---

## Part 1: Build a Playable Executable

Rather than requiring people to install Java, you'll package your game into a self-contained app using `jpackage`. The result is a native installer — a `.dmg` on Mac or `.exe`/`.msi` on Windows — that anyone can run with a double-click.

### Steps

1. **Build your JAR** in IntelliJ: `Build > Build Artifacts` (or run `mvn package` in the terminal).

2. **Run jpackage** from the terminal. Replace the values in angle brackets with your own:

   ```bash
   jpackage \
     --input target/ \
     --name <YourGameName> \
     --main-jar <YourProject>.jar \
     --main-class game.GameApp \
     --type dmg
   ```

   Change `--type dmg` to `exe` or `msi` if you're on Windows.

3. The output will appear in a folder called `output/`. That file is what you'll upload to itch.io (see below).

> **Note:** jpackage produces an installer for the OS you build it on. Build on a Mac to get a Mac app, on Windows to get a Windows app.

---

## Part 2: Set Up Your Online Portfolio Entry

Your portfolio entry has three parts that work together.

### 1. GitHub Repository (your code)

Your GitHub repo is where you show *how* you built the game. Make sure it includes:

- **A README.md** at the root with:
  - A short description of your game and what it demonstrates (data structures, algorithms, etc.)
  - A screenshot or animated GIF of gameplay
  - A link to your itch.io page
  - Basic controls (WASD / arrow keys, etc.)
- **Clean, commented code** — this is what a reviewer will actually read

**Adding a GIF to your README:**
Record a short clip with QuickTime (Mac) or Xbox Game Bar (Windows), convert it to GIF using ezgif.com or a similar tool, and drag it into your repo via the GitHub web interface. Then reference it in your README:

```markdown
![Gameplay](gameplay.gif)
```

### 2. Gameplay Video

Record 30–60 seconds of actual gameplay showing your game's key features. This lets anyone see your project working without downloading anything.

- **Mac:** QuickTime Player > File > New Screen Recording
- **Windows:** Win + G (Xbox Game Bar) > Record

Upload to **YouTube** (unlisted is fine) or **Vimeo** and link it from your README and itch.io page.

### 3. itch.io Page (your playable/downloadable release)

[itch.io](https://itch.io) is a free platform designed for indie game portfolios. It gives you a clean public URL and a professional presentation for your executable.

**Setup steps:**

1. Create a free account at itch.io
2. Click **Upload New Project**
3. Set **Kind of project** to "Downloadable"
4. Upload your `jpackage` output (`.dmg`, `.exe`, or `.msi`)
5. Add screenshots and your gameplay video link
6. Set visibility to **Public** and publish

Your page URL will be something like `yourusername.itch.io/your-game-name`. Add this link to your GitHub README.

---

## Checklist Before Submitting

- [ ] Game launches from the executable with no errors
- [ ] GitHub repo has a README with description, screenshot/GIF, and itch.io link
- [ ] Gameplay video is recorded and linked
- [ ] itch.io page is public and has the executable uploaded
- [ ] Controls are documented somewhere visible (README or itch.io description)

---

## Summary

| What | Where | Purpose |
|------|-------|---------|
| Executable | itch.io | Playable download, showcase demo |
| Source code | GitHub | Shows your work and concepts |
| Gameplay video | YouTube / Vimeo | Quick preview, no install required |
