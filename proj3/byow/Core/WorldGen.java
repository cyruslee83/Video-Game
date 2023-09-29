package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WorldGen {
    Random random;
    int worldWidth;
    int worldHeight;
    TETile[][] world;
    TETile[][] worldLightOff;
    boolean light;
    int filledSize;
    int avatarX;
    int avatarY;
    // define an int array to represent TETile, 1 for floor and 0 for others
    int[][] worldType;
    static final double WEIGHT = 0.4;
    static final int TRY_COUNT = 50;
    static final int LIGHT_RANGE = 3;
    List<Room> rooms;

    public WorldGen(int worldWidth, int worldHeight, long seed, int[][] worldType,
                    boolean light, int avatarX, int avatarY) {
        random = new Random(seed);
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.worldType = worldType;
        this.world = new TETile[worldWidth][worldHeight];
        this.worldLightOff = new TETile[worldWidth][worldHeight];
        this.light = light;
        rooms = new ArrayList<Room>();
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                world[i][j] = Tileset.SHALLOW_WATER;
                worldLightOff[i][j] = Tileset.SHALLOW_WATER;
            }
        }
        this.avatarX = avatarX;
        this.avatarY = avatarY;
        transform();

    }

    public WorldGen(int worldWidth, int worldHeight, long seed) {
        random = new Random(seed);
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.worldType = new int[worldWidth][worldHeight];
        this.world = new TETile[worldWidth][worldHeight];
        this.worldLightOff = new TETile[worldWidth][worldHeight];
        this.filledSize = 0;
        this.light = true;
        rooms = new ArrayList<Room>();
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                world[i][j] = Tileset.SHALLOW_WATER;
                worldLightOff[i][j] = Tileset.SHALLOW_WATER;
            }
        }
    }

    //  Continuously setting random size Room at random coordinate(X, Y) until
    // the filled floor percentage exceed 0.4 or have tried over 70 times
    public void setRooms() {
        int count = 0;
        while ((float) filledSize / (worldWidth * worldHeight) < WEIGHT && count < TRY_COUNT) {
            int startX = startXGen();
            int startY = startYGen();
            Room roomDraw = new Room(startX, startY);
            count++;
            if (roomDraw.valid() && !roomDraw.overlapped()) {
                roomDraw.drawRoom();
                rooms.add(roomDraw);
                filledSize += roomDraw.height * roomDraw.width;
            }
        }
    }

    public void initAvatar() {
        Room init = rooms.get(RandomUtils.uniform(random, rooms.size()));
        setAvatar(init.startX, init.startY);
    }

    public void setAvatar(int x, int y) {
        worldType[x][y] = 2;
        this.avatarX = x;
        this.avatarY = y;
    }

    public void avatarA() {
        if (worldType[avatarX - 1][avatarY] != 0) {
            worldType[avatarX][avatarY] = 1;
            setAvatar(avatarX - 1, avatarY);
            transform();
        }
    }

    public void avatarW() {
        if (worldType[avatarX][avatarY + 1] != 0) {
            worldType[avatarX][avatarY] = 1;
            setAvatar(avatarX, avatarY + 1);
            transform();
        }
    }

    public void avatarS() {
        if (worldType[avatarX][avatarY - 1] != 0) {
            worldType[avatarX][avatarY] = 1;
            setAvatar(avatarX, avatarY - 1);
            transform();
        }
    }

    public void avatarD() {
        if (worldType[avatarX + 1][avatarY] != 0) {
            worldType[avatarX][avatarY] = 1;
            setAvatar(avatarX + 1, avatarY);
            transform();
        }
    }

    // connect all rooms with hallways, rooms are connected randomly
    public void drawHallways() {
        while (rooms.size() > 1) {
            int count = 0;
            int a = RandomUtils.uniform(random, rooms.size());
            int b = RandomUtils.uniform(random, rooms.size());
            while (a == b
                    || (Math.abs(rooms.get(a).startX - rooms.get(b).startX)) > worldWidth * WEIGHT
                    || ((Math.abs(rooms.get(a).startY - rooms.get(b).startY))
                    > worldHeight * WEIGHT)) {
                b = RandomUtils.uniform(random, rooms.size());
                count++;
                if (count > TRY_COUNT * WEIGHT) {
                    break;
                }
            }
            if (a != b) {
                getHallway(rooms.get(a), rooms.get(b));
                rooms.remove(a);
            }
        }
    }

    // Given two rooms, randomly choose one point inside each room and
    // connect them use a hallway.
    public void getHallway(Room a, Room b) {
        int startX = a.startX + RandomUtils.uniform(random, a.width);
        int startY = a.startY + RandomUtils.uniform(random, a.height);
        int endX = b.startX + RandomUtils.uniform(random, b.width);
        int endY = b.startY + RandomUtils.uniform(random, b.height);
        drawHallway(startX, startY, endX, endY);
    }


    // draw hallway between (startX, startY) and (endX, endY),
    // two different ways are randomly chosen
    public void drawHallway(int startX, int startY, int endX, int endY) {
        int way = RandomUtils.uniform(random, 2);
        if (way == 1) {
            drawHallY(startY, endY, startX);
            drawHallX(startX, endX, endY);
        } else {
            drawHallX(startX, endX, startY);
            drawHallY(startY, endY, endX);
        }
    }

    public void drawHallX(int startX, int endX, int startY) {
        for (int i = 0; i <= Math.abs(endX - startX); i++) {
            if (endX > startX) {
                worldType[startX + i][startY] = 1;
            } else {
                worldType[startX - i][startY] = 1;
            }
        }
    }

    public void drawHallY(int startY, int endY, int startX) {
        for (int i = 0; i <= Math.abs(endY - startY); i++) {
            if (endY > startY) {
                worldType[startX][startY + i] = 1;
            } else {
                worldType[startX][startY - i] = 1;
            }
        }
    }

    public int startXGen() {
        return RandomUtils.uniform(random, 1, worldWidth);
    }

    public int startYGen() {
        return RandomUtils.uniform(random, 1, worldHeight);
    }

    // Transform the worldType int array to a TETile array
    public void transform() {
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                if (worldType[i][j] == 1) {
                    world[i][j] = Tileset.STONE_FLOOR;
                }
                if (worldType[i][j] == 2) {
                    world[i][j] = Tileset.PURPLE_AVATAR;

                }
                if (worldType[i][j] == 0) {
                    if (isWall(i, j)) {
                        world[i][j] = Tileset.CRYSTAL_WALL;
                    }
                }
            }
        }
        setWorldLightOff(avatarX, avatarY);
    }

    public void setWorldLightOff(int x, int y) {
        for (int i = 0; i < worldWidth; i++) {
            for (int j = 0; j < worldHeight; j++) {
                worldLightOff[i][j] = Tileset.NOTHING;
            }
        }
        for (int i = x - LIGHT_RANGE; i < x + LIGHT_RANGE; i++) {
            for (int j = y - LIGHT_RANGE; j < y + LIGHT_RANGE; j++) {
                try {
                    worldLightOff[i][j] = world[i][j];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    continue;
                }
            }
        }

    }

    // determine whether int[x][y] is a wall
    public boolean isWall(int x, int y) {
        int sum = 0;
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                try {
                    sum += worldType[i][j];
                } catch (ArrayIndexOutOfBoundsException e) {
                    sum += 0;
                }
            }
        }
        return sum > 0;

    }

    public void setLight() {
        light = !light;
    }

    public void setWorldType(int[][] worldType) {
        this.worldType = worldType;
    }

    public boolean getLight() {
        return light;
    }

    public TETile[][] getWorld() {
        if (light) {
            return world;
        } else {
            return worldLightOff;
        }

    }

    public int[][] getWorldType() {
        return worldType;
    }

    public int getAvatarX() {
        return avatarX;
    }

    public int getAvatarY() {
        return avatarY;
    }

    // Room class, start point for every room is at its bottom left
    private class Room {
        int width;
        int height;
        int startX;
        int startY;

        Room(int startX, int startY) {
            width = RandomUtils.uniform(random, 2, (int) (Math.round(Math.sqrt(worldWidth))) + 2);
            height = RandomUtils.uniform(random, 2, (int) (Math.round(Math.sqrt(worldHeight))) + 2);
            this.startX = startX;
            this.startY = startY;
        }

        void drawRoom() {
            for (int i = startX; i < startX + width; i++) {
                for (int j = startY; j < startY + height; j++) {
                    worldType[i][j] = 1;
                }
            }
        }

        // determine whether a room to draw will be overlapped with
        // rooms already drawn
        boolean overlapped() {
            for (int i = startX - 1; i <= startX + width; i++) {
                for (int j = startY - 1; j <= startY + height; j++) {
                    if (worldType[i][j] == 1) {
                        return true;
                    }
                }
            }
            return false;
        }

        // determine whether a room can be drawn
        boolean valid() {
            return startX + width < worldWidth - 1 && startY + height < worldHeight - 1;
        }

    }

}
