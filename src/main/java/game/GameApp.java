package game;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GameApp extends Application {

    // ── Customize your game ──────────────────────────────────────────────────
    // Change any of these to make your submission feel like your own.
    private static final String GAME_TITLE        = "Pellet Pursuit";
    private static final String GAME_SUBTITLE     = "your tagline here";
    private static final String MSG_READY         = "GET READY!";
    private static final String MSG_DEAD          = "OUCH!";
    private static final String MSG_WIN           = "YOU WIN!";
    private static final String LEADERBOARD_TITLE = "-- HIGH SCORES --";
    private static final Color  HUD_COLOR         = Color.web("#111111");
    private static final Color  HUD_TEXT          = Color.WHITE;
    private static final int    STARTING_LIVES    = 3;
    private static final Path   SCORES_FILE       = Path.of("scores.txt");
    // ─────────────────────────────────────────────────────────────────────────

    // --- Layout ---
    private static final int HUD_HEIGHT = 48;

    // Canvas and stage stored as fields so startLevel() can resize them for variable-size maps
    private Canvas canvas;
    private Stage  primaryStage;

    // --- Game state ---
    private enum State { READY, GET_READY, PLAYING, DEAD_PAUSE, LEVEL_CLEAR, GAME_OVER, WIN }

    private GameMap     map;
    private Player      player;
    private List<Ghost> ghosts;

    private int   score  = 0;
    private int   lives  = STARTING_LIVES;
    private int   level  = 1;
    private State state  = State.READY;
    private Ghost killerGhost = null;
    private double pauseTimer = 0;
    private static final double DEAD_PAUSE = 1.5;

    // --- Level config (difficulty scaling) ---
    private LevelConfig config = LevelConfig.forLevel(1);

    // --- Bonus items ---
    private final List<BonusItem> bonusItems = new ArrayList<>();
    private int dotsEaten = 0;

    // --- Ghost spawn stagger ---
    private double spawnTimer   = 0;
    private int    nextToSpawn  = 0;

    // --- Ghost combo scoring (200 → 400 → 800 → 1600 per power pellet) ---
    private int ghostsEatenThisPellet = 0;

    // --- Extra life ---
    private static final int EXTRA_LIFE_THRESHOLD = 10_000;
    private boolean extraLifeAwarded = false;

    // --- Score flashes (points that float up when a ghost is eaten) ---
    private final List<ScoreFlash> scoreFlashes = new ArrayList<>();

    // --- Level clear flash ---
    private static final double LEVEL_CLEAR_DURATION = 2.0;
    private static final int    MAX_LEVEL             = 3;

    // --- HUD notification (e.g. "+1 UP!") ---
    private String hudMessage     = null;
    private double hudMessageTimer = 0;

    // --- Persistent high scores ---

    private final AudioManager audio = new AudioManager();

    private boolean paused            = false;
    private boolean frightenedSirenOn = false;

    private ScoreTree scoreTree = new ScoreTree();
    private long lastNano = -1;

    // Convenience — canvas height tracks HUD + current map height
    private int canvasH() { return map.height + HUD_HEIGHT; }

    /**
     * Return the Tile[][] layout for a given level number.
     * Add new cases here to give each level a different maze.
     * All layouts must satisfy the rules in GameMap's constructor Javadoc.
     */
    protected GameMap.Tile[][] getLayout(int lvl) {
        return GameMap.DEFAULT_LAYOUT;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        initGame();

        canvas = new Canvas(map.width, canvasH());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, map.width, canvasH(), Color.BLACK);

        scene.setOnKeyPressed(e -> handleKey(e.getCode()));

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt = (lastNano < 0) ? 0 : (now - lastNano) / 1_000_000_000.0;
                lastNano = now;
                dt = Math.min(dt, 0.05);
                update(dt);
                render(gc);
            }
        }.start();

        stage.setTitle(GAME_TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        canvas.requestFocus();
    }

    private void initGame() {
        map       = new GameMap(getLayout(1));
        player    = new Player(map);
        // Shadow is the worked example — study it before implementing the others.
        // Add each ghost here after you finish its chooseTarget() in Phase 2:
        //   new Patrol(map), new Shy(map), new Ambush(map)
        ghosts    = new ArrayList<>(List.of(new Shadow(map)));
        scoreTree = new ScoreTree();
        scoreTree.loadFromFile(SCORES_FILE);
        state  = State.READY;
    }

    private void startLevel() {
        config = LevelConfig.forLevel(level);
        map    = new GameMap(getLayout(level));
        player = new Player(map);
        // Add each ghost here after you finish its chooseTarget() in Phase 2:
        //   new Patrol(map), new Shy(map), new Ambush(map)
        ghosts = new ArrayList<>(List.of(new Shadow(map)));

        // Resize the window only when the new layout has different pixel dimensions
        if (canvas != null &&
                ((int) canvas.getWidth() != map.width || (int) canvas.getHeight() != canvasH())) {
            canvas.setWidth(map.width);
            canvas.setHeight(canvasH());
            primaryStage.sizeToScene();
            primaryStage.centerOnScreen();
        }

        for (Ghost g : ghosts) {
            g.applySpeedMultiplier(config.ghostSpeedMultiplier);
        }
        // Stagger ghost spawns — ghosts 1-3 start inactive and are released one per spawnDelay
        nextToSpawn = 1;
        spawnTimer  = 0;
        if (config.spawnDelay > 0) {
            for (int i = 1; i < ghosts.size(); i++) ghosts.get(i).setActive(false);
        }
        dotsEaten = 0;
        bonusItems.clear();
        scoreFlashes.clear();
        ghostsEatenThisPellet = 0;
        paused            = false;
        frightenedSirenOn = false;
        pauseTimer = 2.0;
        state = State.GET_READY;
    }

    private void handleKey(KeyCode key) {
        if (key == KeyCode.SPACE && state == State.PLAYING) {
            paused = !paused;
            if (paused) audio.stopSiren(); else audio.startSiren();
        }
        if (key == KeyCode.ENTER) {
            if (state == State.READY || state == State.WIN) {
                level = 1; score = 0; lives = STARTING_LIVES; extraLifeAwarded = false; startLevel();
            } else if (state == State.GAME_OVER) {
                level = 1; score = 0; lives = STARTING_LIVES; extraLifeAwarded = false; initGame(); startLevel();
            }
        }
        if (state == State.PLAYING || state == State.GET_READY) player.handleKey(key);
    }

    private void update(double dt) {
        if (paused) return;
        if (hudMessageTimer > 0) hudMessageTimer -= dt;
        if (state == State.GET_READY)   { updateGetReady(dt);   return; }
        if (state == State.LEVEL_CLEAR) { updateLevelClear(dt); return; }
        if (state == State.DEAD_PAUSE)  { updateDeadPause(dt);  return; }
        if (state == State.PLAYING)       updatePlaying(dt);
    }

    private void updateGetReady(double dt) {
        pauseTimer -= dt;
        if (pauseTimer <= 0) {
            audio.startSiren();
            state = State.PLAYING;
        }
    }

    private void updateLevelClear(double dt) {
        pauseTimer -= dt;
        if (pauseTimer <= 0) {
            if (level >= MAX_LEVEL) {
                state = State.WIN;
            } else {
                level++;
                startLevel();
            }
        }
    }

    private void updateDeadPause(double dt) {
        pauseTimer -= dt;
        if (pauseTimer <= 0) {
            if (lives <= 0) {
                scoreTree.insert(score, level);
                scoreTree.saveToFile(SCORES_FILE);
                state = State.GAME_OVER;
            } else {
                killerGhost = null;
                player.resetPosition();
                for (Ghost g : ghosts) g.resetPosition();
                nextToSpawn = 1;
                spawnTimer  = 0;
                if (config.spawnDelay > 0) {
                    for (int i = 1; i < ghosts.size(); i++) ghosts.get(i).setActive(false);
                }
                scoreFlashes.clear();
                frightenedSirenOn = false;
                pauseTimer = 2.0;
                state = State.GET_READY;
            }
        }
    }

    private void updatePlaying(double dt) {
        // Release staggered ghosts
        if (nextToSpawn < ghosts.size()) {
            spawnTimer += dt;
            if (spawnTimer >= config.spawnDelay) {
                ghosts.get(nextToSpawn).setActive(true);
                nextToSpawn++;
                spawnTimer = 0;
            }
        }

        player.update(dt, map);

        // Eat dots
        int col = player.col(map), row = player.row(map);
        int earned = map.eatDot(col, row);
        if (earned > 0) {
            score += earned;
            dotsEaten++;
            if (earned == 50) { // power pellet
                audio.playPellet();
                for (Ghost g : ghosts) g.frighten(config.frightenDuration);
                ghostsEatenThisPellet = 0;
            } else {
                audio.playChomp();
            }
            audio.updateSirenRate((double) dotsEaten / map.getTotalDots());
            if (!extraLifeAwarded && score >= EXTRA_LIFE_THRESHOLD) {
                lives++;
                extraLifeAwarded = true;
                hudMessage     = "+1 UP!";
                hudMessageTimer = 2.0;
            }
            if (bonusItems.isEmpty() && dotsEaten >= config.bonusThreshold) {
                double bx = map.tileCenterX(map.spawnCol(GameMap.Tile.SPAWN_BONUS)) - GameMap.TILE / 2.0;
                double by = map.tileCenterY(map.spawnRow(GameMap.Tile.SPAWN_BONUS)) - GameMap.TILE / 2.0;
                bonusItems.add(new Cherry(bx, by));
            }
        }

        updateBonusItems(dt);

        // Update ghosts
        for (Ghost g : ghosts) g.update(dt, map, player);

        // Switch siren between normal and frightened mode as ghosts change state
        boolean anyFrightened = ghosts.stream().anyMatch(g -> g.isActive() && !g.isDead() && g.isFrightened());
        if (anyFrightened && !frightenedSirenOn) {
            frightenedSirenOn = true;
            audio.startFrightenedSiren();
        } else if (!anyFrightened && frightenedSirenOn) {
            frightenedSirenOn = false;
            audio.startSiren();
            audio.updateSirenRate((double) dotsEaten / map.getTotalDots());
        }

        // Ghost collisions
        for (Ghost g : ghosts) {
            if (!g.isActive() || g.isDead()) continue;
            if (g.collidesWith(player)) {
                if (g.isFrightened()) {
                    int pts = 200 * (1 << ghostsEatenThisPellet);
                    score += pts;
                    ghostsEatenThisPellet++;
                    audio.playGhostEaten();
                    scoreFlashes.add(new ScoreFlash(g.centerX(), g.centerY(), pts));
                    g.kill();
                } else {
                    lives--;
                    killerGhost = g;
                    audio.stopSiren();
                    audio.playDeath();
                    state      = State.DEAD_PAUSE;
                    pauseTimer = DEAD_PAUSE;
                    return;
                }
            }
        }

        scoreFlashes.forEach(f -> f.update(dt));
        scoreFlashes.removeIf(f -> f.timer <= 0);

        // Level clear — flash the maze then advance
        if (map.allDotsEaten()) {
            audio.stopSiren();
            scoreTree.insert(score, level);
            scoreTree.saveToFile(SCORES_FILE);
            pauseTimer = LEVEL_CLEAR_DURATION;
            state = State.LEVEL_CLEAR;
        }
    }

    private void updateBonusItems(double dt) {
        // TODO (Phase 3): Update every bonus item and handle collection and expiry.
        // Use two separate loops — see the Phase 3 guide for why a single loop
        // causes a ConcurrentModificationException, and how to structure them.
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, map.width, canvasH());

        // HUD
        drawHUD(gc);

        // Game area shifted down by HUD_HEIGHT
        gc.save();
        gc.translate(0, HUD_HEIGHT);
        if (state == State.LEVEL_CLEAR) {
            // Alternate between white and blue walls every 0.25 s
            Color wallColor = ((int)(pauseTimer / 0.25) % 2 == 0)
                ? Color.WHITE : Color.web("#1a1aff");
            map.draw(gc, wallColor);
        } else {
            map.draw(gc);
        }
        player.draw(gc);
        for (Ghost g : ghosts) g.draw(gc);
        for (BonusItem b : bonusItems) b.draw(gc);
        for (ScoreFlash f : scoreFlashes) f.draw(gc);
        gc.restore();

        // Overlays
        if (state == State.READY) {
            drawCenteredText(gc, GAME_TITLE, 40, Color.YELLOW, canvasH() / 2.0 - 40);
            drawCenteredText(gc, GAME_SUBTITLE, 16, Color.web("#aaaaaa"), canvasH() / 2.0 - 10);
            drawCenteredText(gc, "PRESS ENTER TO START", 20, HUD_TEXT, canvasH() / 2.0 + 20);
            drawCenteredText(gc, "SPACE TO PAUSE", 16, Color.web("#aaa"), canvasH() / 2.0 + 46);
        } else if (state == State.GET_READY) {
            drawCenteredText(gc, MSG_READY, 36, Color.YELLOW, canvasH() / 2.0);
        } else if (state == State.DEAD_PAUSE) {
            drawCenteredText(gc, MSG_DEAD, 36, Color.RED, canvasH() / 2.0 - 10);
            if (killerGhost != null)
                drawCenteredText(gc, "caught by " + killerGhost.getName(), 16, Color.web("#ff8888"), canvasH() / 2.0 + 20);
        } else if (state == State.GAME_OVER) {
            drawGameOver(gc);
        } else if (state == State.WIN) {
            drawCenteredText(gc, MSG_WIN, 40, Color.YELLOW, canvasH() / 2.0 - 40);
            drawCenteredText(gc, "PRESS ENTER TO PLAY AGAIN", 16, Color.web("#666"), canvasH() / 2.0 + 30);
        }

        if (paused) {
            gc.setFill(Color.color(0, 0, 0, 0.55));
            gc.fillRect(0, 0, map.width, canvasH());
            drawCenteredText(gc, "PAUSED", 40, Color.YELLOW, canvasH() / 2.0);
            drawCenteredText(gc, "PRESS SPACE TO RESUME", 18, Color.WHITE, canvasH() / 2.0 + 40);
        }
    }

    private void drawHUD(GraphicsContext gc) {
        gc.setFill(HUD_COLOR);
        gc.fillRect(0, 0, map.width, HUD_HEIGHT);

        gc.setFont(Font.font("Monospace", 18));
        gc.setFill(HUD_TEXT);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("SCORE  " + score, 10, 30);

        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("LEVEL " + level, map.width / 2.0, 30);

        // Lives as small pac-man icons
        for (int i = 0; i < lives; i++) {
            double lx = map.width - 30 - i * 24, ly = 14;
            gc.setFill(Color.YELLOW);
            gc.fillArc(lx, ly, 18, 18, 30, 300, javafx.scene.shape.ArcType.ROUND);
        }

        // Pause hint
        gc.setFont(Font.font("Monospace", 12));
        gc.setFill(Color.web("#555"));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("SPACE: PAUSE", map.width - 10, HUD_HEIGHT - 6);

        // Transient HUD notification (extra life, etc.)
        if (hudMessageTimer > 0) {
            gc.setFont(Font.font("Monospace", 14));
            gc.setFill(Color.YELLOW);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(hudMessage, map.width / 2.0, 44);
        }
    }

    private void drawGameOver(GraphicsContext gc) {
        gc.setFill(Color.color(0, 0, 0, 0.75));
        gc.fillRect(0, 0, map.width, canvasH());

        drawCenteredText(gc, "GAME OVER", 40, Color.RED, 160);
        drawCenteredText(gc, "SCORE: " + score, 26, Color.WHITE, 210);

        if (!scoreTree.isEmpty()) {
            drawCenteredText(gc, LEADERBOARD_TITLE, 18, Color.YELLOW, 260);
            List<ScoreNode> top = scoreTree.getTopScores(5);
            for (int i = 0; i < top.size(); i++) {
                ScoreNode n = top.get(i);
                String line = (i + 1) + ".  " + n.score + "  (level " + n.level + ")";
                drawCenteredText(gc, line, 16, Color.web("#aaa"), 290 + i * 24);
            }
        }

        drawCenteredText(gc, "PRESS ENTER TO PLAY AGAIN", 18, Color.WHITE, canvasH() - 60);
    }

    private void drawCenteredText(GraphicsContext gc, String text, int size, Color color, double y) {
        gc.setFont(Font.font("Monospace", size));
        gc.setFill(color);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, map.width / 2.0, y);
    }

    // -----------------------------------------------------------------------
    // Score flash — points that float upward and fade when a ghost is eaten
    // -----------------------------------------------------------------------

    private static final class ScoreFlash {
        double x, y;
        final int value;
        double timer = 1.0; // seconds until gone

        ScoreFlash(double x, double y, int value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }

        void update(double dt) {
            timer -= dt;
            y -= dt * 24; // float upward 24 px/s
        }

        void draw(GraphicsContext gc) {
            gc.save();
            gc.setGlobalAlpha(Math.max(0, timer)); // fades 1→0 over 1 second
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Monospace", 14));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(String.valueOf(value), x, y);
            gc.restore();
        }
    }

    public static void main(String[] args) { launch(args); }
}
