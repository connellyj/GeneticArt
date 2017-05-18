package Tartarus1;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import java.util.*;
import java.io.*;

public class Grid {

    public ArrayList<Integer> colors;
    public ArrayList<Color> discreteColors;
    private int imageDimension;

    //functions and terminals
    public static final int INC = 0;
    public static final int ABS = 1;
    public static final int ADD = 2;
    public static final int MAX = 3;
    public static final int AVG2 = 4;
    public static final int AVG3 = 5;
    public static final int IF = 6;
    public static final int X = 7;
    public static final int Y = 8;
    public static final int NORTH = 9;
    public static final int WEST = 10;
    public static final int NORTH_WEST = 11;
    public static final int RANDOM = 12;

    public enum DiscreteColor {
        RED, ORANGE, YELLOW, LIME, GREEN, SEA_GREEN, LIGHT_BLUE, MEDIUM_BLUE, BLUE, PURPLE, MAGENTA, PINK
    }

    public enum EvalTypes {
        HUMAN, GRADIENT, STDEV, RAINBOW, SQUARES, X, INVERSE_X
    }

    public static final int numColors = DiscreteColor.values().length;

    public Grid(int imageDimension) {
        this.imageDimension = imageDimension;
        colors = new ArrayList<>();
        discreteColors = new ArrayList<>();
        discreteColors.add(Color.rgb(255, 0, 0));
        discreteColors.add(Color.rgb(255, 127, 0));
        discreteColors.add(Color.rgb(255, 255, 0));
        discreteColors.add(Color.rgb(127, 255, 0));
        discreteColors.add(Color.rgb(0, 255, 0));
        discreteColors.add(Color.rgb(0, 255, 127));
        discreteColors.add(Color.rgb(0, 255, 255));
        discreteColors.add(Color.rgb(0, 127, 255));
        discreteColors.add(Color.rgb(0, 0, 255));
        discreteColors.add(Color.rgb(127, 0, 255));
        discreteColors.add(Color.rgb(255, 0, 255));
        discreteColors.add(Color.rgb(255, 0, 127));
    }

    public void colorPixel(int color) {
        colorPixel(color, null);
    }

    public void colorPixel(int color, BufferedWriter out) {
        colors.add(color);
        if(out != null) updateFile(out, color);
    }

    private int humanEval() {
        Platform.runLater(() -> {
            WritableImage image = new WritableImage(imageDimension, imageDimension);
            PixelWriter p = image.getPixelWriter();
            for(int x = 0; x < imageDimension; x++) {
                for(int y = 0; y < imageDimension; y++) {
                    p.setColor(x, y, discreteColors.get(colors.get(x * y + x)));
                }
            }
            ImageView imageView = new ImageView(image);
            Main.rootPane.getChildren().clear();
            Main.rootPane.getChildren().add(imageView);
        });
        return getFitnessFromUser();
    }

    private int gradientEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            int north = 0;
            int northIndex = i - imageDimension;
            if(northIndex > 0) north = colors.get(northIndex);
            int west = 0;
            int westIndex = i - 1;
            if((westIndex + 1) % imageDimension != 0 && westIndex > 0) west = colors.get(westIndex);
            int northWest = 0;
            int northWestIndex = northIndex - 1;
            if((northWestIndex + 1) % imageDimension != 0 && northWestIndex > 0) northWest = colors.get(northWestIndex);
            int cur = colors.get(i);
            int ndif = Math.abs(north - cur);
            int wdif = Math.abs(west - cur);
            int nwdif = Math.abs(northWest - cur);
            if(ndif >= 1 && ndif < 4 && wdif >= 1 && wdif < 4 && nwdif >= 1 && nwdif < 4 && cur > 4) fitness -= 1;
        }
        return fitness;
    }

    private int stdevEval() {
        double mean = 0.0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            mean += colors.get(i);
        }
        if(mean != 0.0) mean /= (imageDimension * imageDimension);
        double stdev = 0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            stdev += Math.pow(colors.get(i) - mean, 2.0);
        }
        stdev /= numColors;
        stdev = Math.sqrt(stdev);
        return 100 - (int)Math.floor(stdev);
    }

    private int rainbowEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == i / imageDimension) fitness--;
        }
        return fitness;
    }

    private int squareEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 2; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == colors.get(i - 2) && colors.get(i) != colors.get(i - 1)) fitness--;
        }
        return fitness;
    }

    private int xEVal() {
        int fitness = imageDimension * imageDimension;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) == (i % imageDimension) % numColors) fitness--;
        }
        return fitness;
    }

    private int inverseXEval() {
        int fitness = imageDimension * imageDimension;
        for(int i = 1; i < imageDimension * imageDimension; i++) {
            if(colors.get(i) < colors.get(i - 1)) fitness--;
        }
        return fitness;
    }

    // determine the fitness of the current state of the grid. fitness is (maxScore+1) - score
    // where score is the number of sides of blocks that are touching a wall
    public int calcFitness(EvalTypes evalType) {
        switch (evalType) {
            case HUMAN:
                return humanEval();
            case GRADIENT:
                return gradientEval();
            case STDEV:
                return stdevEval();
            case RAINBOW:
                return rainbowEval();
            case SQUARES:
                return squareEval();
            case X:
                return xEVal();
            case INVERSE_X:
                return inverseXEval();
        }
        return 0;
    }

    private int getFitnessFromUser() {
        int fitness;
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println("Enter a score between 1 and 10 where 1 is the best");
        try {
            fitness = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("That's not an integer... Try again");
            return getFitnessFromUser();
        }
        if(fitness < 1 || fitness > 10) {
            System.out.println("That's not between 1 and 10... Try again");
            return getFitnessFromUser();
        }
        return fitness;
    }

    public void print() {
        print(System.out);
    }

    // print the current state of the grid, showing blocks and the dozer 
    // pointing in the correct direction
    public void print(PrintStream os) {
        for(int x = 0; x < imageDimension; x++) {
            for(int y = 0; y < imageDimension; y++) {
                os.print(colors.get(x * y + x) + " ");
            }
            os.println();
        }
    }

    private void updateFile(BufferedWriter out, int color) {
        try{
            out.write(color + " ");
        } catch (IOException e) {
            System.out.println("Error while writing to file in update method");
        }
    }

    public void outputFitness(BufferedWriter out, int gridFitness) {
        try{
            out.write("\n");
        } catch (IOException e) {
            System.out.println("Error while writing to file in fitness method");
        }
    }
}
