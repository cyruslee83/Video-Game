package byow.InputDemo;

/**
 * Created by hug.
 */

import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;

public class KeyboardInputSource implements InputSource {
    private static final boolean PRINT_TYPED_KEYS = false;
    Font fontBig = new Font("Monaco", Font.BOLD, 30);
    Font fontMid = new Font("Monaco", Font.BOLD, 22);
    Font fontSmall = new Font("Monaco", Font.BOLD, 15);

    public KeyboardInputSource() {
        StdDraw.setFont(fontBig);
        StdDraw.text(0.5, 0.7, "MomCraft");
        StdDraw.setFont(fontSmall);
        StdDraw.text(0.5, 0.62, "begin typing to choose seed");
        StdDraw.setFont(fontMid);
        StdDraw.filledRectangle(0.5, 0.48, 0.25, 0.03);
        StdDraw.text(0.5, 0.55, "Seed:");
        StdDraw.setFont(fontSmall);
        StdDraw.text(0.5, 0.40, "Guide:");
        StdDraw.text(0.5, 0.35, "n - new world");
        StdDraw.text(0.5, 0.31, "s - start world");
        StdDraw.text(0.5, 0.27, "l - load world");
        StdDraw.text(0.5, 0.23, ":q - quit world");

    }

    public char getNextKey() {
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
//                if (PRINT_TYPED_KEYS) {
//                    System.out.print(c);
//                }
                return c;
            }
        }
    }

    public boolean possibleNextInput() {
        return StdDraw.hasNextKeyTyped();
    }
}
