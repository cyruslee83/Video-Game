package byow.Core;

import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Engine {
    long seed;
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    public static final int POW = 10;
    WorldGen worldGen;
    TETile[][] worldFrame;
    final int fontBigSize = 30;
    final int fontMidSize = 22;
    final int fontSmallSize = 15;
    Font fontBig = new Font("Monaco", Font.BOLD, fontBigSize);
    Font fontMid = new Font("Monaco", Font.BOLD, fontMidSize);
    Font fontSmall = new Font("Monaco", Font.BOLD, fontSmallSize);
    final Double xHalf = 0.5;
    final Double x7 = 0.7;
    final Double y62 = 0.62;
    final Double y48 = 0.48;
    final Double halfRectangleHeight = 0.03;
    final Double y55 = 0.55;
    final Double y475 = 0.475;
    final Double y6 = 0.6;
    final Double y8 = 0.8;
    final Double yDiff = 0.05;
    final int pauseTime = 1000;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInputSource input = new KeyboardInputSource();
        while (true) {
            char c = input.getNextKey();
            if (c == 'N') {
                StringBuilder inputSeed = new StringBuilder("N");
                StringBuilder inputMinusN = new StringBuilder("");
                drawFrameN(inputMinusN.toString());
                while (true) {
                    char next = input.getNextKey();
                    if (Character.isDigit(next)) {
                        inputSeed.append(next);
                        inputMinusN.append(next);
                        drawFrameS(inputMinusN.toString());
                    } else if (next == 'S') {
                        inputSeed.append(next);
                        controlFrame();
                        break;
                    }
                }
                interactWithInputString(inputSeed.toString());
                continue;
            } else if (c == 'L') {
                loadWorld();
            } else if (c == ':') {
                if (input.getNextKey() == 'Q') {
                    saveFile();
                    System.exit(0);
                }
            }
            if (worldFrame != null) {
                interactWithInputChar(c);
            }
        }
    }

    public void loadWorld() {
        try {
            // create a new FileReader object with the file name
            Scanner scanner = new Scanner(new File("historyWorld.txt"));
            seed = Long.parseLong(scanner.next());
            int width = Integer.parseInt(scanner.next());
            int height = Integer.parseInt(scanner.next());
            int[][] historyWorldType = new int[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    historyWorldType[i][j] = Integer.parseInt(scanner.next());
                }
            }
            boolean light = Boolean.parseBoolean(scanner.next());
            int avatarX = scanner.nextInt();
            int avatarY = scanner.nextInt();
            worldGen = new WorldGen(width, height, seed, historyWorldType, light, avatarX, avatarY);
            worldFrame = worldGen.getWorld();
            ter = new TERenderer();
            ter.initialize(WIDTH, HEIGHT);
            ter.renderFrame(worldFrame);
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }


    public void saveFile() {
        try {
            FileWriter writer = new FileWriter("historyWorld.txt");
            writer.write(worldToString(worldGen.getWorldType()));
            writer.close();
        } catch (IOException | NullPointerException e) {
        }
    }

    public String worldToString(int[][] worldType) {
        StringBuilder sb = new StringBuilder();
        sb.append(seed);
        sb.append(" " + WIDTH);
        sb.append(" " + HEIGHT);
        for (int i = 0; i < worldType.length; i++) {
            for (int j = 0; j < worldType[i].length; j++) {
                sb.append(" " + worldType[i][j]);
            }
        }
        sb.append(" ").append(worldGen.getLight());
        sb.append(" ").append(worldGen.getAvatarX());
        sb.append(" ").append(worldGen.getAvatarY());
        return sb.toString();
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     * <p>
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     * <p>
     * In other words, running both of these:
     * - interactWithInputString("n123sss:q")
     * - interactWithInputString("lww")
     * <p>
     * should yield the exact same world state as:
     * - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        StringInputDevice inputDevice = new StringInputDevice(input);
        while (inputDevice.possibleNextInput()) {
            char c = inputDevice.getNextKey();
            if (c == 'N') {
                StringBuilder inputSeed = new StringBuilder("N");
                while (true) {
                    char next = inputDevice.getNextKey();
                    if (Character.isDigit(next)) {
                        inputSeed.append(next);
                    } else if (next == 'S') {
                        inputSeed.append(next);
                        break;
                    }
                }
                initializeWorld(inputSeed.toString());
                continue;
            } else if (c == 'L') {
                loadWorld();
            }
            if (worldFrame != null) {
                if (c == ':') {
                    if (inputDevice.getNextKey() == 'Q') {
                        saveFile();
                        return worldGen.getWorld();
                    }
                }
                interactWithInputChar(c);
            }
        }
        return worldGen.getWorld();
    }

    public void initializeWorld(String input) {
        seed = getSeedFromInput(input);
        this.worldGen = new WorldGen(WIDTH, HEIGHT, seed);
        worldGen.setRooms();
        worldGen.drawHallways();
        worldGen.initAvatar();
        worldGen.transform();
        worldFrame = worldGen.getWorld();
        ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        ter.renderFrame(worldFrame);
    }

    public void interactWithInputChar(Character input) {
        switch (input) {
            case 'W':
                worldGen.avatarW();
                ter.renderFrame(worldGen.getWorld());
                break;
            case 'S':
                worldGen.avatarS();
                ter.renderFrame(worldGen.getWorld());
                break;
            case 'A':
                worldGen.avatarA();
                ter.renderFrame(worldGen.getWorld());
                break;
            case 'D':
                worldGen.avatarD();
                ter.renderFrame(worldGen.getWorld());
                break;
            case 'B':
                worldGen.setLight();
                ter.renderFrame((worldGen.getWorld()));
                break;
            default:
                break;
        }
    }

    // Given input string as N****S, calculate the seed from that
    public long getSeedFromInput(String input) {
        long seeds = 0;
        List<Character> inputChar = new ArrayList<Character>();
        StringInputDevice inputDevice = new StringInputDevice(input);
        while (inputDevice.possibleNextInput()) {
            inputChar.add(inputDevice.getNextKey());
        }
        for (int i = 1; i < inputChar.size() - 1; i++) {
            seeds += Character.getNumericValue(inputChar.get(i))
                    * (long) Math.pow(POW, inputChar.size() - i - 1);
        }
        return seeds;
    }

    public void drawFrameN(String s) {
        /* Take the input string S and display it on the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.WHITE);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(xHalf, x7, "MomCraft");
        StdDraw.setFont(fontSmall);
        StdDraw.text(xHalf, y62, "please input seed number");
        StdDraw.setFont(fontMid);
        StdDraw.filledRectangle(xHalf, y48, 0.25, halfRectangleHeight);
        StdDraw.text(xHalf, y55, "Seed:");
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMid);
        StdDraw.text(xHalf, y475, s);
    }

    public void drawFrameS(String s) {
        /* Take the input string S and display it on the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.WHITE);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontBig);
        StdDraw.text(xHalf, x7, "MomCraft");
        StdDraw.setFont(fontSmall);
        StdDraw.text(xHalf, y62, "input S to start world");
        StdDraw.setFont(fontMid);
        StdDraw.filledRectangle(xHalf, y48, 0.25, halfRectangleHeight);
        StdDraw.text(xHalf, y55, "Seed:");
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(fontMid);
        StdDraw.text(xHalf, y475, s);
    }

    public void controlFrame() {
        /* Take the input string S and display it on the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.WHITE);
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.setFont(fontMid);
        String s = "";
        int timer = 5;
        while (timer > 0) {
            StdDraw.clear(Color.WHITE);
            StdDraw.setFont(fontMid);
            StdDraw.text(xHalf, y6, "Controls:");
            StdDraw.text(xHalf, y6 - yDiff, "w - move up");
            StdDraw.text(xHalf, y6 - yDiff * 2, "s - move down");
            StdDraw.text(xHalf, y6 - yDiff * 3, "a - move left");
            StdDraw.text(xHalf, y6 - yDiff * 4, "d - move right");
            StdDraw.text(xHalf, y6 - yDiff * 5, "b - toggle lights");
            StdDraw.setFont(fontBig);
            StdDraw.text(xHalf, y8, s + timer);
            timer--;
            StdDraw.pause(pauseTime);
        }
    }
}
