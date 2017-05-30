package Tartarus1;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;

public class Grid {

    public ArrayList<Color> colors;
    public ArrayList<Color> discreteColors;
    private int imageDimension;

    //functions and terminals
    public static final int INC = 0;
    public static final int ABS = 1;
    public static final int ADD = 2;
    public static final int SUBTR = 3;
    public static final int MULT = 4;
    public static final int DIV = 5;
    public static final int MAX = 6;
    public static final int AVG2 = 7;
    public static final int AVG3 = 8;
    public static final int IF = 9;
    public static final int X = 10;
    public static final int Y = 11;
    public static final int NORTH = 12;
    public static final int WEST = 13;
    public static final int NORTH_WEST = 14;
    public static final int RANDOM = 15;
    public static final int ZERO = 16;
    public static final int ONE = 17;
    public static final int TWO = 18;
    public static final int THREE = 19;
    public static final int FOUR = 20;
    public static final int FIVE = 21;
    public static final int SIX = 22;
    public static final int SEVEN = 23;
    public static final int EIGHT = 24;
    public static final int NINE = 25;
    public static final int TEN = 26;
    public static final int ELEVEN = 27;
    public static final int SIN = 28;
    public static final int COS = 29;
    public static final int RGB = 30;

    public enum DiscreteColor {
        RED, ORANGE, YELLOW, LIME, GREEN, SEA_GREEN, LIGHT_BLUE, MEDIUM_BLUE, BLUE, PURPLE, MAGENTA, PINK
    }

    public enum EvalTypes {
        HUMAN, GRADIENT, RANDOM, SIMILAR
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

    public void colorPixel(int[] rgb) {
        colorPixel(rgb, null);
    }

    public void colorPixel(int[] rgb, BufferedWriter out) {
        Color color = new Color(rgb[0] / 12.0, rgb[1] / 12.0, rgb[2] / 12.0, 1.0);
        colors.add(color);
        if(out != null) updateFile(out, color);
    }

    public static int getIntFromColor(Color c) {
        java.awt.Color color = new java.awt.Color((float)c.getRed(), (float)c.getGreen(), (float)c.getBlue());
        return color.getRGB();
    }

    private int humanEval() {
        Platform.runLater(() -> {
            WritableImage image = new WritableImage(imageDimension, imageDimension);
            PixelWriter p = image.getPixelWriter();
            for(int x = 0; x < imageDimension; x++) {
                for(int y = 0; y < imageDimension; y++) {
                    p.setColor(x, y, colors.get(imageDimension * y + x));
                }
            }
            ImageView imageView = new ImageView(image);
            Main.rootPane.getChildren().clear();
            Main.rootPane.getChildren().add(imageView);
        });
        return getFitnessFromUser();
    }

    private boolean isGradient(int i) {
        int low = 500;
        int high = 20000;
        int northIndex = -1;
        int northEastIndex = -1;
        int eastIndex = -1;
        int southEastIndex = -1;
        int southIndex = -1;
        int southWestIndex = -1;
        int westIndex = -1;
        int northWestIndex = -1;
        if(i >= imageDimension) {
            northIndex = i - imageDimension;
        }
        if(i <= imageDimension * imageDimension - imageDimension - 1) {
            southIndex = i + imageDimension;
        }
        if((i + 1) % imageDimension != 0) {
            eastIndex = i + 1;
        }
        if(i % imageDimension != 0) {
            westIndex = i - 1;
        }
        if(northIndex != -1 && eastIndex != -1) {
            northEastIndex = northIndex + 1;
        }
        if(northIndex != -1 && westIndex != -1) {
            northWestIndex = northIndex - 1;
        }
        if(southIndex != -1 && eastIndex != -1) {
            southEastIndex = southIndex + 1;
        }
        if(southIndex != -1 && westIndex != -1) {
            southWestIndex = southIndex - 1;
        }
        int cur = getIntFromColor(colors.get(i));
        int north = 1;
        if(northIndex != -1) north = Math.abs(getIntFromColor(colors.get(northIndex)) - cur);
        int northEast = 1;
        if(northEastIndex != -1) northEast = Math.abs(getIntFromColor(colors.get(northEastIndex)) - cur);
        int east = 1;
        if(eastIndex != -1) east = Math.abs(getIntFromColor(colors.get(eastIndex)) - cur);
        int southEast = 1;
        if(southEastIndex != -1) southEast = Math.abs(getIntFromColor(colors.get(southEastIndex)) - cur);
        int south = 1;
        if(southIndex != -1) south = Math.abs(getIntFromColor(colors.get(southIndex)) - cur);
        int southWest = 1;
        if(southWestIndex != -1) southWest = Math.abs(getIntFromColor(colors.get(southWestIndex)) - cur);
        int west = 1;
        if(westIndex != -1) west = Math.abs(getIntFromColor(colors.get(westIndex)) - cur);
        int northWest = 1;
        if(northWestIndex != -1) northWest = Math.abs(getIntFromColor(colors.get(northWestIndex)) - cur);
        return north >= low && north < high && northEast >= low && northEast < high && east >= low && east < high &&
                southEast >= low && southEast < high && south >= low && south < high && southWest >= low && southWest < high &&
                west >= low && west < high && northWest >= low && northWest < high;
    }

    private int gradientEval() {
        int fitness = imageDimension * imageDimension;
        if(calcStdDev() < 50000000) return fitness;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            if(isGradient(i)) fitness--;
        }
        return fitness;
    }

    public static boolean areSimilar(Color c1, Color c2) {
        double r = Math.abs(c1.getRed() - c2.getRed());
        double g = Math.abs(c1.getGreen() - c2.getGreen());
        double b = Math.abs(c1.getBlue() - c2.getBlue());
        return r < 0.2 && r > 0.05 && g < 0.2 && g > 0.05 && b < 0.2 && b > 0.05;
    }

    private double calcStdDev() {
        double mean = 0.0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            mean += getIntFromColor(colors.get(i));
        }
        if(mean != 0.0) mean /= (imageDimension * imageDimension);
        double stdev = 0;
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            stdev += Math.pow(getIntFromColor(colors.get(i)) - mean, 2.0);
        }
        stdev /= numColors;
        stdev = Math.sqrt(stdev);
        return stdev;
    }

    private int similarEval() {
        int fitness = imageDimension * imageDimension;
        Random r = new Random();
        for(int i = 0; i < imageDimension * imageDimension; i++) {
            Color cur = colors.get(i);
            Color north = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
            Color south = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
            Color east = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
            Color west = new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1.0);
            if(i >= imageDimension) north = colors.get(i - imageDimension);
            if(i < imageDimension * imageDimension - imageDimension) south = colors.get(i + imageDimension);
            if(i > 0) west = colors.get(i - 1);
            if(i < imageDimension * imageDimension - 1) east = colors.get(i + 1);
            if(areSimilar(cur, north) && areSimilar(cur, south) && areSimilar(cur, east) && areSimilar(cur, west)) {
                fitness--;
            }
        }
        return fitness;
    }

    // determine the fitness of the current state of the grid. fitness is (maxScore+1) - score
    // where score is the number of sides of blocks that are touching a wall
    public int calcFitness(EvalTypes evalType) {
        switch (evalType) {
            case HUMAN:
                return 0;
            case GRADIENT:
                return gradientEval();
            case RANDOM:
                Random r = new Random();
                return r.nextInt(imageDimension);
            case SIMILAR:
                return similarEval();
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

    private void updateFile(BufferedWriter out, Color color) {
        try{
            out.write(color.toString() + " ");
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
