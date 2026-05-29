//THIS IS THE CYAN GHOST
package game;

import javafx.scene.paint.Color;

// Shy — runs away unless cornered (few open neighbors), then attacks.
public class Shy extends Ghost {

    public Shy(GameMap map) {
        super(map, GameMap.Tile.SPAWN_G2, 1.5);
    }

    @Override
    public String getName() {
        return "Shy";
    } // give your ghost a name

    @Override
    protected int[] chooseTarget(Player player, GameMap map) {
        // TODO (Base): Implement Shy's personality.
        //
        // Shy has two modes:
        // 1. FLEE — target the maze corner FARTHEST from the player
        // 2. ATTACK — target the player's exact tile (like Shadow)
        //
        // Shy switches from FLEE to ATTACK when it is "cornered":
        // Count how many of Shy's four neighbors (up/down/left/right) are open
        // (not walls). If only ONE neighbor is open, Shy is cornered.
        // Use col(map) and row(map) for Shy's position, and map.isWall() to test each
        // neighbor.
        //
        // To find the farthest corner from the player:
        // Check each of the four near-corner tiles and pick the one with the
        // greatest Math.hypot distance from the player's tile.
        //
        // When frightened, CHASE the player instead of fleeing.
        // Note: this is intentionally the OPPOSITE of every other ghost's frightened
        // behavior — Shy is bold when cornered and bold when scared.
        //
        // How to verify: run the game and walk toward Shy — it should move away
        // from you. Trap it in a dead-end corridor and it should turn and chase.

        // return new int[]{ player.col(map), player.row(map) }; // placeholder —
        // replace this YES SEE BELOW
        // if the ghost is 'frightened' or cornered (only 1 open neighbor), the ghost
        // goes to to the pacman
        if (frightened || ((map.isWall(col(map) - 1, row(map)) ? 0 : 1) +
                (map.isWall(col(map) + 1, row(map)) ? 0 : 1) +
                (map.isWall(col(map), row(map) - 1) ? 0 : 1) +
                (map.isWall(col(map), row(map) + 1) ? 0 : 1) == 1)) {
            // attack mode
            return new int[] { player.col(map), player.row(map) };
        }

        // flee mode: the ghost targets the maze corner farthest from the player
        return new int[] {
                (player.col(map) < map.cols / 2) ? map.cols - 2 : 1,
                (player.row(map) < map.rows / 2) ? map.rows - 2 : 1
        };

    }

    // When chooseTarget() is working, add this ghost to the list in GameApp.java:
    // new Shy(map)

    @Override
    protected Color getBodyColor() {
        return Color.web("#00ffff");
    }
}
