package Tartarus1;

import java.util.ArrayList;

public class PixelInfo {

    public int xCoord;
    public int yCoord;
    public int northColor;
    public int westColor;
    public int northWestColor;

    public PixelInfo(int i, int imageDimension, ArrayList<Integer> colors) {
        int x = i % imageDimension;
        int y = i / imageDimension;
        int north = 0;
        int northIndex = i - imageDimension;
        if(northIndex >= 0) north = colors.get(northIndex);
        int west = 0;
        int westIndex = i - 1;
        if((westIndex + 1) % imageDimension != 0 & westIndex >= 0) west = colors.get(westIndex);
        int northWest = 0;
        int northWestIndex = northIndex - 1;
        if((northWestIndex + 1) % imageDimension != 0 && northWestIndex >= 0) northWest = colors.get(northWestIndex);
        this.xCoord = x;
        this.yCoord = y;
        this.northColor = north;
        this.westColor = west;
        this.northWestColor = northWest;
    }
}
