package game;

import java.util.EnumMap;
import java.util.EnumSet;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

// Pac-Man maze.
//
// To design your own maze, edit DEFAULT_LAYOUT below (or pass any Tile[][]
// to the GameMap(Tile[][]) constructor to support per-level layouts).
// Rules you must follow:
//   - Each spawn tile (PL, G0–G3, BN) must appear exactly once
//   - Any open tile on a left/right edge must have an open tile on the
//     opposite edge (horizontal tunnel); same rule for top/bottom edges
public class GameMap {

    // TILE is a rendering constant — always 28 px regardless of maze size
    public static final int TILE = 28;

    // Change these to customize your maze's color scheme
    public Color dotColor = Color.web("#ffeb99"); // dot and power-pellet color

    // Instance dimensions — derived from whichever layout was passed in
    public final int cols;
    public final int rows;
    public final int width;
    public final int height;

    // ---------------------------------------------------------------
    // Tile types
    // ---------------------------------------------------------------
    public enum Tile {
        W, // Wall
        D, // Dot
        P, // Power pellet
        E, // Empty
        SPAWN_PLAYER, // Player spawn position
        SPAWN_G0, // Ghost 0 (Shadow) spawn
        SPAWN_G1, // Ghost 1 (Patrol) spawn
        SPAWN_G2, // Ghost 2 (Shy) spawn
        SPAWN_G3, // Ghost 3 (Ambush) spawn
        SPAWN_BONUS // Bonus item spawn
    }

    // Short aliases so the maze array below stays readable
    private static final Tile W = Tile.W, D = Tile.D, P = Tile.P, E = Tile.E;
    private static final Tile PL = Tile.SPAWN_PLAYER;
    private static final Tile G0 = Tile.SPAWN_G0, G1 = Tile.SPAWN_G1;
    private static final Tile G2 = Tile.SPAWN_G2, G3 = Tile.SPAWN_G3;
    private static final Tile BN = Tile.SPAWN_BONUS;

    // Spawn tiles are walkable — treated as empty during gameplay
    private static final EnumSet<Tile> SPAWN_TILES = EnumSet.of(
            Tile.SPAWN_PLAYER, Tile.SPAWN_G0, Tile.SPAWN_G1,
            Tile.SPAWN_G2, Tile.SPAWN_G3, Tile.SPAWN_BONUS);

    public static boolean isSpawnTile(Tile t) {
        return SPAWN_TILES.contains(t);
    }

    // ---------------------------------------------------------------
    // Default maze layout (W=wall D=dot P=power pellet E=empty)
    // Spawn positions are marked with: PL (player), G0-G3 (ghosts), BN (bonus)
    // Move any spawn tile freely — just keep it on an open (non-W) tile.
    // Pass a different Tile[][] to GameMap(layout) for a custom maze.
    // ---------------------------------------------------------------
    /*
     * public static final Tile[][] DEFAULT_LAYOUT = {
     * { W, W, W, W, W, W, W, W, W, W, W, W, W, W, W },
     * { W, D, D, D, D, D, D, D, D, D, D, D, D, D, W },
     * { W, P, W, W, D, W, W, D, W, W, D, W, W, P, W },
     * { W, D, D, D, D, D, D, D, D, D, D, D, D, D, W },
     * { W, D, W, W, W, W, W, E, W, W, W, W, W, D, W },
     * { W, D, D, D, W, E, G1, G0, G2, E, W, D, D, D, W },
     * { W, D, W, D, W, E, G3, E, E, E, W, D, W, D, W },
     * { W, D, D, D, W, W, W, W, W, W, W, D, D, D, W },
     * { W, D, W, W, D, D, D, PL, D, D, D, W, W, D, W },
     * { W, D, D, D, D, W, D, BN, D, W, D, D, D, D, W },
     * { W, D, W, W, D, W, D, D, D, W, D, W, W, D, W },
     * { W, P, D, D, D, D, D, D, D, D, D, D, D, P, W },
     * { W, D, W, W, D, W, W, D, W, W, D, W, W, D, W },
     * { W, D, D, D, D, D, D, D, D, D, D, D, D, D, W },
     * { W, W, W, W, W, W, W, W, W, W, W, W, W, W, W },
     * };
     */

    public static final Tile[][] DEFAULT_LAYOUT = {
            { W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W },
            { W, P, D, D, D, D, D, D, D, D, W, D, D, D, D, D, D, D, D, P, W },
            { W, D, W, W, W, D, W, W, W, D, W, D, W, W, W, D, W, W, W, D, W },
            { W, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, W },
            { W, D, W, W, D, D, W, D, W, W, W, W, W, D, W, D, D, W, W, D, W },
            { W, D, D, D, D, D, W, D, D, D, D, D, W, D, D, D, D, D, D, D, W },
            { W, W, W, W, D, D, W, W, W, D, W, D, W, W, W, D, D, W, W, W, W },
            { W, D, D, D, D, D, D, D, W, E, G0, E, W, D, D, D, D, D, D, D, W },
            { W, D, W, W, D, D, W, D, W, G2, G1, G3, W, D, W, D, D, W, W, D, W },
            { W, D, D, D, D, D, W, D, W, W, E, W, W, D, W, D, D, D, D, D, W },
            { W, W, W, W, D, D, W, D, D, D, BN, D, D, D, W, D, D, W, W, W, W },
            { W, D, D, D, D, D, W, D, W, W, W, W, W, D, W, D, D, D, D, D, W },
            { W, D, W, W, D, D, D, D, D, D, D, D, D, D, D, D, D, W, W, D, W },
            { W, D, D, W, D, D, W, W, W, D, W, D, W, W, W, D, D, W, D, D, W },
            { W, D, D, W, D, D, D, D, D, D, W, D, D, D, D, D, D, W, D, D, W },
            { W, W, D, W, D, D, W, D, W, W, W, W, W, D, W, D, D, W, D, W, W },
            { W, D, D, D, D, D, W, D, D, D, PL, D, D, D, W, D, D, D, D, D, W },
            { W, D, W, W, W, W, W, W, W, D, W, D, W, W, W, W, W, W, W, D, W },
            { W, D, D, D, D, D, D, D, W, D, W, D, W, D, D, D, D, D, D, D, W },
            { W, D, W, W, W, W, W, D, W, D, W, D, W, D, W, W, W, W, W, D, W },
            { W, P, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, P, W },
            { W, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, D, W },
            { W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W }
    };

    // Original layout (never modified) and working copy (dots consumed as eaten)
    private final Tile[][] layout;
    private final Tile[][] state;

    // Spawn positions discovered by scanning the layout
    private final EnumMap<Tile, int[]> spawnPositions = new EnumMap<>(Tile.class);

    private int dotsRemaining;
    private int totalDots;

    public GameMap() {
        this(DEFAULT_LAYOUT);
    }

    public GameMap(Tile[][] layout) {
        this.rows = layout.length;
        this.cols = layout[0].length;
        this.width = cols * TILE;
        this.height = rows * TILE;
        this.layout = layout;
        this.state = new Tile[rows][cols];
        scanSpawns();
        validateLayout();
        reset();
    }

    // ---------------------------------------------------------------
    // Spawn position lookup — call these to find where each spawn is
    // ---------------------------------------------------------------
    public int spawnCol(Tile spawnTile) {
        return spawnPositions.get(spawnTile)[0];
    }

    public int spawnRow(Tile spawnTile) {
        return spawnPositions.get(spawnTile)[1];
    }

    // ---------------------------------------------------------------
    // Scan the layout once to record each spawn tile's position
    // ---------------------------------------------------------------
    private void scanSpawns() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile t = layout[r][c];
                if (isSpawnTile(t))
                    spawnPositions.put(t, new int[] { c, r });
            }
        }
        for (Tile t : SPAWN_TILES) {
            if (!spawnPositions.containsKey(t))
                throw new IllegalStateException(
                        "Maze design error: required spawn tile " + t + " not found in layout.");
        }
    }

    // ---------------------------------------------------------------
    // Validate tunnel symmetry
    // ---------------------------------------------------------------
    private void validateLayout() {
        for (int c = 0; c < cols; c++) {
            if ((layout[0][c] != Tile.W) != (layout[rows - 1][c] != Tile.W))
                throw new IllegalStateException(
                        "Maze design error: col " + c + " has an open top edge but not bottom (or vice versa). " +
                                "Vertical tunnels must be open on both sides.");
        }
        for (int r = 0; r < rows; r++) {
            if ((layout[r][0] != Tile.W) != (layout[r][cols - 1] != Tile.W))
                throw new IllegalStateException(
                        "Maze design error: row " + r + " has an open left edge but not right (or vice versa). " +
                                "Horizontal tunnels must be open on both sides.");
        }
    }

    /** True when row {@code r} has open tiles on both the left and right edges. */
    public boolean isHorizontalTunnel(int r) {
        return r >= 0 && r < rows && layout[r][0] != Tile.W;
    }

    /** True when col {@code c} has open tiles on both the top and bottom edges. */
    public boolean isVerticalTunnel(int c) {
        return c >= 0 && c < cols && layout[0][c] != Tile.W;
    }

    public final void reset() {
        dotsRemaining = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                // Spawn tiles are walkable empty floor during gameplay
                state[r][c] = isSpawnTile(layout[r][c]) ? Tile.E : layout[r][c];
                if (layout[r][c] == Tile.D || layout[r][c] == Tile.P)
                    dotsRemaining++;
            }
        }
        totalDots = dotsRemaining;
    }

    public Tile getTile(int col, int row) {
        // if (row < 0 || row >= rows || col < 0 || col >= cols)
        // return Tile.W; // TODO: replace with isOutOfGrid()
        if (isOutOfGrid(col, row))
            return Tile.W;
        return state[row][col];
    }

    public boolean isOutOfGrid(int col, int row) {
        // TODO (Phase 1): Return true if (col, row) is outside the grid. COMPLETE
        return col < 0 || col >= cols || row < 0 || row >= rows;
        // The grid has 'cols' columns (0 to cols-1) and 'rows' rows (0 to rows-1).
    }

    public boolean isWall(int col, int row) {
        // TODO (Phase 1): Return true if the tile at (col, row) is a wall. COMPLETE
        // Use getTile(col, row) — one line is enough.
        return getTile(col, row) == Tile.W;
    }

    // Returns 0 if nothing eaten, 10 for dot, 50 for power pellet
    public int eatDot(int col, int row) { // step6 IS FINISHED NOW
        // TODO (Phase 1): If there is a dot or power pellet at (col, row), remove it
        if (isOutOfGrid(col, row)) {
            return 0;
        }
        // and return its point value. If the tile is empty, return 0.
        Tile t = state[row][col];
        if (t == Tile.D) {
            state[row][col] = Tile.E;
            dotsRemaining--;
            return 10;
        } else if (t == Tile.P) {
            state[row][col] = Tile.E;
            dotsRemaining--;
            return 50;
        }
        // Removing a dot means replacing it with Tile.E and updating dotsRemaining.
        // Note: state is indexed [row][col], not [col][row].
        return 0; // placeholder — replace this
    }

    public boolean isPowerPellet(int col, int row) {
        return getTile(col, row) == Tile.P;
    }

    public boolean allDotsEaten() {
        return dotsRemaining == 0;
    }

    public int getDotsRemaining() {
        return dotsRemaining;
    }

    public int getTotalDots() {
        return totalDots;
    }

    public int pixelToCol(double px) {
        return (int) (px / TILE);
    }

    public int pixelToRow(double py) {
        return (int) (py / TILE);
    }

    public double tileCenterX(int col) {
        return col * TILE + TILE / 2.0;
    }

    public double tileCenterY(int row) {
        return row * TILE + TILE / 2.0;
    }

    public void draw(GraphicsContext gc) {
        draw(gc, Color.web("#550baaff")); // i changed the hex color already to PURPLE
    }

    public void draw(GraphicsContext gc, Color wallColor) {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, width, height);
        // TODO (Phase 1): Loop over every row r (0..rows-1) and every col c
        // (0..cols-1).
        // Inside the loop:
        // double px = c * TILE; // pixel x of this tile's top-left corner
        // double py = r * TILE; // pixel y of this tile's top-left corner
        // Tile t = state[r][c]; // what tile is here?
        // COMPLETE
        // Then draw based on t using a switch statement:
        // Tile.W — wall: a rounded rectangle inset 1 px on each side
        // gc.setFill(wallColor);
        // gc.fillRoundRect(px + 1, py + 1, TILE - 2, TILE - 2, 6, 6);
        //
        // Tile.D — dot: a small filled circle centered in the tile
        // double cx = px + TILE / 2.0, cy = py + TILE / 2.0;
        // gc.setFill(dotColor);
        // gc.fillOval(cx - 3, cy - 3, 6, 6);
        //
        // Tile.P — power pellet: same idea but radius 7 instead of 3
        // gc.fillOval(cx - 7, cy - 7, 14, 14);
        //
        // Tile.E — empty: nothing to draw, the black background shows through
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                double px = c * TILE;
                double py = r * TILE;
                Tile t = state[r][c];
                switch (t) {
                    case W -> {
                        gc.setFill(wallColor);
                        gc.fillRoundRect(px + 1, py + 1, TILE - 2, TILE - 2, 6, 6);
                    }
                    case D -> {
                        gc.setFill(dotColor);
                        gc.fillOval(px + TILE / 2.0 - 3, py + TILE / 2.0 - 3, 6, 6);
                    }
                    case P -> {
                        gc.setFill(dotColor);
                        gc.fillOval(px + TILE / 2.0 - 7, py + TILE / 2.0 - 7, 14, 14);
                    }
                    case E -> {
                    }
                    default -> {
                    }
                }
            }
        }
    }
}
