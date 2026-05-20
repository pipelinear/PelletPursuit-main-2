package game;

import javafx.scene.paint.Color;

// Shadow — the chaser. Always targets the player's exact tile.
// This ghost is provided as a WORKED EXAMPLE for students to study.
// chooseTarget() returns a tile coordinate; the engine automatically
// moves the ghost toward it through the maze.
public class Shadow extends Ghost {

    public Shadow(GameMap map) {
        super(map, GameMap.Tile.SPAWN_G0, 1.8);
    }

    @Override
    public String getName() { return "Shadow"; }

    @Override
    protected int[] chooseTarget(Player player, GameMap map) {
        if (frightened) {
            // Run away: target the corner farthest from the player
            int pc = player.col(map), pr = player.row(map);
            int targetCol, targetRow;
            if (pc < map.cols / 2) {
                targetCol = map.cols - 2;  // player on left  → flee right
            } else {
                targetCol = 1;             // player on right → flee left
            }
            if (pr < map.rows / 2) {
                targetRow = map.rows - 2;  // player on top    → flee bottom
            } else {
                targetRow = 1;             // player on bottom → flee top
            }
            return new int[]{ targetCol, targetRow };
        }
        // Chase: target the player's current tile.
        // The engine finds the shortest path there through the maze walls.
        return new int[]{ player.col(map), player.row(map) };
    }

    @Override
    protected Color getBodyColor() { return Color.web("#ff0000"); }
}
