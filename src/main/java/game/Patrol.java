package game;

import javafx.scene.paint.Color;

// Patrol — patrols a fixed corner until the player gets close, then chases.
public class Patrol extends Ghost {

    // How close (in tiles) the player must be before Patrol switches from
    // patrolling to chasing.  Try values between 5 and 12.
    private static final double CHASE_RADIUS = 8.0;

    // The row of Patrol's home corner (top edge of the maze).
    // Adjust this (and the target col below) to change which corner it patrols.
    private static final int CORNER_ROW = 1;

    public Patrol(GameMap map) {
        super(map, GameMap.Tile.SPAWN_G1, 1.6);
    }

    @Override
    public String getName() { return "Patrol"; } // give your ghost a name

    @Override
    protected int[] chooseTarget(Player player, GameMap map) {
        // TODO (Base): Implement Patrol's personality.
        //
        // Patrol has two modes:
        //   1. SCATTER — head toward the top-right corner (map.cols-2, CORNER_ROW)
        //   2. CHASE   — target the player's exact tile (player.col(map), player.row(map))
        //
        // Switch from SCATTER to CHASE when the player is within CHASE_RADIUS tiles.
        // Hint: use Math.hypot(deltaCol, deltaRow) for the tile distance.
        //
        // When frightened, move AWAY from the player instead.
        // Target a point on the opposite side of Patrol's position from the player,
        // then keep the result inside the maze using Math.max and Math.min.
        //
        // How to verify: run the game and stay far from the pink ghost — it should
        // move toward the top-right area of the maze. Walk close and it should
        // switch to chasing you directly.

        return new int[]{ player.col(map), player.row(map) }; // placeholder — replace this
    }

    // When chooseTarget() is working, add this ghost to the list in GameApp.java:
    //   new Patrol(map)

    @Override
    protected Color getBodyColor() { return Color.web("#ffb8ff"); }
}
