package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class HudThread {
    WorldGen worldGen;
    TETile[][] world;
    static final int FONT_SIZE = 14;
    final Double yPos = 29.3;

    public HudThread(TETile[][] world) {
        this.world = world;
    }


    public HudThread(WorldGen worldGen) {
        this.worldGen = worldGen;
        this.world = worldGen.getWorld();
    }

    public void checkType() {
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Monaco", Font.PLAIN, FONT_SIZE);
        StdDraw.setFont(font);
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        String tileType = "";
        try {
            if (this.world[x][y] == Tileset.STONE_FLOOR) {
                tileType = "floor";
            } else if (this.world[x][y] == Tileset.CRYSTAL_WALL) {
                tileType = "wall";
            } else if (this.world[x][y] == Tileset.PURPLE_AVATAR) {
                tileType = "avatar";
            } else {
                tileType = "nothing";
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
            tileType = "nothing";
        }
        StdDraw.text(4, yPos, tileType);
        StdDraw.show();
    }
}
