package game;

import javafx.scene.paint.Color;

// Ambush — tries to cut the player off by targeting ahead of where they are heading.
public class Ambush extends Ghost {

    // Number of tiles ahead of the player's current direction to target.
    // Try values between 2 and 8.
    private static final int LOOK_AHEAD = 4;

    public Ambush(GameMap map) {
        super(map, GameMap.Tile.SPAWN_G3, 1.7);
    }

    @Override
    public String getName() {
        return "Ambush";
    } // give your ghost a name

    @Override
    protected int[] chooseTarget(Player player, GameMap map) {
        // TODO (Base): Implement Ambush's personality.
        //
        // Ambush targets a point LOOK_AHEAD tiles ahead of the player's current
        // heading. Use player.getDx() and player.getDy() to find the heading,
        // then project that many tiles forward from the player's tile position.
        //
        // Make sure the target is always inside the maze — use Math.max and Math.min
        // to keep the column within [1, map.cols-2] and the row within [1, map.rows-2].
        //
        // When frightened, target a corner of the maze instead.
        //
        // How to verify: run the game and move in one direction — the orange ghost
        // should approach from in front of you rather than chasing from behind.

        // return new int[] { player.col(map), player.row(map) }; // placeholder —
        // replace this
        if (frightened) {
            // when scared, ambush runs to a corner
            return new int[] { 1, map.rows - 2 };
        }

        return new int[] {
                Math.max(1, Math.min(map.cols - 2, player.col(map) + player.getDx() * LOOK_AHEAD)),
                Math.max(1, Math.min(map.rows - 2, player.row(map) + player.getDy() * LOOK_AHEAD))
        };
    }

    // When chooseTarget() is working, add this ghost to the list in GameApp.java:
    // new Ambush(map)

    @Override
    protected Color getBodyColor() {
        return Color.web("#ffb852");
    }
}
