package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

public class GenerateWorld {

    /* Given a seed and return a random TETile[][] with desired
       length and width
     */
    float[][] worldType;
    TETile[][] world;
    Random generator;
    final int width;
    final int height;
    int generated;
    static final double WEIGHT = 0.4;

    /*
    Firstly, The constructor generate a Tile world with Nothing filled
     */
    public GenerateWorld(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.generator = new Random(seed);
        this.world = new TETile[width][height];
        this.worldType = new float[width - 2][height - 2];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                world[i][j] = Tileset.NOTHING;
            }
        }


        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                if (worldType[i - 1][j - 1] == 1) {
                    world[i][j] = Tileset.FLOOR;
                }

            }
        }
    }

    public void generateRoom(int startX, int startY) {
        int now = generated;
        int desireWidth = 0;
        int desireHeight = 0;
        int direction = 0;
        int tryCount = 0;
        while (generated == now && tryCount < 10) {
            try {
                desireWidth = RandomUtils.uniform(generator, 1,
                        (int) Math.round(Math.sqrt(width)));
                desireHeight = RandomUtils.uniform(generator, 1,
                        (int) Math.round(Math.sqrt(height)));
                direction = RandomUtils.uniform(generator, 4);
//                for (int i = 0; i < desireWidth; i++) {
//                    for (int j = 0; j < desireHeight; j++) {
//                        switch (direction) {
//                            case 0:
//                                if (worldType[startX + i][startY + j] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + i][startY + j]  =  1;
//                                break;
//                            case 1:
//                                if (worldType[startX + j][startY + i] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + j][startY + i]  =  1;
//                                break;
//                            case 2:
//                                if (worldType[startX + j][startY - i] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + j][startY - i]  =  1;
//                                break;
//                            case 3:
//                                if (worldType[startX - i][startY + j] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX - i][startY + j]  =  1;
//                                break;
//                        }
//                    }
//                }
                generated += desireHeight * desireWidth;
            } catch (ArrayIndexOutOfBoundsException e) {
                tryCount += 1;
            }
        }
//        if ((float)generated / ((width - 2) * (height - 2)) < Weight) {
//            int nextStartY = RandomUtils.uniform(generator, desireWidth);
//            int nextStartX = RandomUtils.uniform(generator, desireHeight);
//            nextHallway(startX, startY, Direction, nextStartX, nextStartY);
//        }
    }

    public void dtmNextRoom() {
        int desireWidth = widthGen();
        int desireHeight = heightGen();
        int direction = directionGen();

    }

    public void generateHallway(int startX, int startY, int desireWidth,
                                int desireLength, int direction) {
        int now = generated;
        int tryCount = 0;
        while (generated == now && tryCount < 10) {
            try {
                int i = 0;
//                for (int i = 0; i < desireLength; i++) {
//                    for (int j = 0; j < desireWidth; j++) {
//                        switch (direction) {
//                            case 0:
//                                if (worldType[startX + i][startY + j] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + i][startY + j]  =  1;
//                                break;
//                            case 1:
//                                if (worldType[startX + j][startY + i] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + j][startY + i]  =  1;
//                                break;
//                            case 2:
//                                if (worldType[startX + j][startY - i] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX + j][startY - i]  =  1;
//                                break;
//                            case 3:
//                                if (worldType[startX - i][startY + j] == 0.0) {
//                                    generated += 1;
//                                }
//                                worldType[startX - i][startY + j]  =  1;
//                                break;
//                        }
//                    }
//                }
            } catch (ArrayIndexOutOfBoundsException e) {
                tryCount += 1;
            }
        }
//            int nextStartY = RandomUtils.uniform(generator, desireWidth);
//            int nextStartX = RandomUtils.uniform(generator, desireLength);
//            int TType = RandomUtils.uniform(generator, 2);

//            switch (Type) {
//                case 0:
//                    nextHallway(startX, startY, Direction, nextStartX, nextStartY);
//                    break;
//                case 1:
//                    nextRoom(startX, startY, Direction, nextStartX, nextStartY);
//                    break;
//            }


    }

//    public void nextHallway(int startX, int startY, int Direction, int nextX, int nextY) {
//        switch (Direction) {
//            case 0:
//                GenerateHallway(startX + nextX, startY + nextY);
//                break;
//            case 1:
//                GenerateHallway(startX + nextY, startY + nextX);
//                break;
//            case 2:
//                GenerateHallway(startX + nextY, startY - nextX);
//                break;
//            case 3:
//                GenerateHallway(startX - nextX, startY + nextY);
//        }
//    }

    public void nextRoom(int startX, int startY, int direction, int nextX, int nextY) {
//        switch (direction) {
//            case 0:
//                generateRoom(startX + nextX, startY + nextY);
//                break;
//            case 1:
//                generateRoom(startX + nextY, startY + nextX);
//                break;
//            case 2:
//                generateRoom(startX + nextY, startY - nextX);
//                break;
//            case 3:
//                generateRoom(startX - nextX, startY + nextY);
//        }
    }

    public int directionGen() {
        return RandomUtils.uniform(generator, 4);
    }

    public int widthGen() {
        return RandomUtils.uniform(generator, (int) Math.round(Math.sqrt(width)));
    }

    public int heightGen() {
        return RandomUtils.uniform(generator, (int) Math.round(Math.sqrt(height)));
    }


    public TETile[][] getWorld() {
        return this.world;
    }

    public float[][] getWorldType() {
        return this.worldType;
    }


}
