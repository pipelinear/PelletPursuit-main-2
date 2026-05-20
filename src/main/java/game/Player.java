package game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Player extends Sprite {

    // Requested direction from keyboard; actual direction changes only at tile
    // centers
    private int dx = 0, dy = 0;
    private int nextDx = 0, nextDy = 0;

    // Mouth animation
    private double mouthAngle = 45;
    private double mouthDir = -1;

    // Change this to any Color to customize your player's appearance
    protected Color bodyColor = Color.YELLOW;

    private final int startCol;
    private final int startRow;

    public Player(GameMap map) {
        super(
                map.spawnCol(GameMap.Tile.SPAWN_PLAYER) * GameMap.TILE,
                map.spawnRow(GameMap.Tile.SPAWN_PLAYER) * GameMap.TILE,
                GameMap.TILE,
                2.5);
        this.startCol = map.spawnCol(GameMap.Tile.SPAWN_PLAYER);
        this.startRow = map.spawnRow(GameMap.Tile.SPAWN_PLAYER);
    }

    public void handleKey(KeyCode key) {
        // COMPLETLE - (Phase 1): Queue the player's next movement direction.
        //
        // Each arrow key (and WASD equivalent) maps to a (nextDx, nextDy) pair:
        switch (key) {
            case RIGHT, D -> {
                nextDx = 1;
                nextDy = 0;
            }
            case LEFT, A -> {
                nextDx = -1;
                nextDy = 0;
            }
            case DOWN, S -> {
                nextDx = 0;
                nextDy = 1;
            }
            case UP, W -> {
                nextDx = 0;
                nextDy = -1;
            }
            default -> {
            }
            //
            // Use a switch statement on `key` with KeyCode.RIGHT, LEFT, DOWN, UP
            // (and D, A, S, W). The movement engine reads nextDx/nextDy every frame
            // and applies the turn when the player reaches the next tile center.
            //
            // Without this the player sits still and ignores all input.
        }
    }

    @Override
    public void update(double dt, GameMap map) {
        double pixels = speed * dt * 60;

        // Try to honor queued turn at tile center
        if (isAligned(map)) {
            int col = col(map), row = row(map);
            if (!map.isWall(col + nextDx, row + nextDy)) {
                if (nextDx != dx || nextDy != dy) {
                    // Snap exactly to tile center before turning so the new
                    // corridor is entered perfectly centerd
                    x = map.tileCenterX(col) - size / 2.0;
                    y = map.tileCenterY(row) - size / 2.0;
                }
                dx = nextDx;
                dy = nextDy;
            }
        }

        // Move if the next tile in current direction is open.
        // Allow out-of-bounds columns so the player can exit the tunnel.
        double nx = x + dx * pixels;
        double ny = y + dy * pixels;
        int nc = map.pixelToCol(nx + size / 2 + dx * (size / 2 - 2));
        int nr = map.pixelToRow(ny + size / 2 + dy * (size / 2 - 2));

        // TODO (Phase 1): Decide whether the player can enter tile (nc, nr).
        // Use map.isWall(nc, nr) and map.isOutOfGrid(nc, nr).
        // Think about what should happen when (nc, nr) is outside the grid entirely —
        // should that block the player or allow movement? Add a comment explaining your
        // reasoning.
        boolean canMove = true; // placeholder — replace this line
        if (canMove) {
            x = nx;
            y = ny;
        }

        // Continuously correct the perpendicular axis so straight movement
        // never drifts off-center in a corridor
        if (dx != 0)
            y = map.tileCenterY(row(map)) - size / 2.0;
        if (dy != 0)
            x = map.tileCenterX(col(map)) - size / 2.0;

        // Wrap tunnels — any open edge row/col wraps to the opposite side
        if (x + size < 0 && map.isHorizontalTunnel(row(map)))
            x = map.width - size;
        if (x > map.width && map.isHorizontalTunnel(row(map)))
            x = 0;
        if (y + size < 0 && map.isVerticalTunnel(col(map)))
            y = map.height - size;
        if (y > map.height && map.isVerticalTunnel(col(map)))
            y = 0;

        // Mouth animates only while moving; freezes when blocked
        if (canMove) {
            mouthAngle += mouthDir * 4;
            if (mouthAngle <= 5) {
                mouthAngle = 5;
                mouthDir = 1;
            }
            if (mouthAngle >= 45) {
                mouthAngle = 45;
                mouthDir = -1;
            }
        }
    }

    // True when the sprite is close enough to a tile center to allow turning
    private boolean isAligned(GameMap map) {
        double cx = centerX(), cy = centerY();
        double tx = map.tileCenterX(col(map));
        double ty = map.tileCenterY(row(map));
        return Math.abs(cx - tx) < speed + 1 && Math.abs(cy - ty) < speed + 1;
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Rotation based on movement direction
        double angle = 0;
        if (dx == 1)
            angle = 0;
        else if (dx == -1)
            angle = 180;
        else if (dy == -1)
            angle = 270;
        else if (dy == 1)
            angle = 90;

        gc.save();
        gc.translate(centerX(), centerY());
        gc.rotate(angle);

        if (spriteImage != null) {
            gc.drawImage(spriteImage, -size / 2, -size / 2, size, size);
        } else {
            gc.translate(-size / 2, -size / 2);
            gc.setFill(bodyColor);
            gc.fillArc(0, 0, size, size, mouthAngle / 2, 360 - mouthAngle, javafx.scene.shape.ArcType.ROUND);
        }

        gc.restore();
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public void resetPosition() {
        x = startCol * GameMap.TILE;
        y = startRow * GameMap.TILE;
        dx = 0;
        dy = 0;
        nextDx = 0;
        nextDy = 0;
    }
}
