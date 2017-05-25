package Tartarus1;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PixelInfo {

    public int xCoord;
    public int yCoord;
    public Color northColor;
    public Color westColor;
    public Color northWestColor;

    public PixelInfo(int i, int imageDimension, ArrayList<Color> colors) {
        int x = i % imageDimension;
        int y = i / imageDimension;
        Color north = Color.WHITE;
        int northIndex = i - imageDimension;
        if(northIndex >= 0) north = colors.get(northIndex);
        Color west = Color.WHITE;
        int westIndex = i - 1;
        if((westIndex + 1) % imageDimension != 0 & westIndex >= 0) west = colors.get(westIndex);
        Color northWest = Color.WHITE;
        int northWestIndex = northIndex - 1;
        if((northWestIndex + 1) % imageDimension != 0 && northWestIndex >= 0) northWest = colors.get(northWestIndex);
        this.xCoord = x;
        this.yCoord = y;
        this.northColor = north;
        this.westColor = west;
        this.northWestColor = northWest;
    }
}
