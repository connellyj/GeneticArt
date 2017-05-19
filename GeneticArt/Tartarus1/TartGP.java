package Tartarus1;// gpjpp example program
// Copyright (c) 1997, Kim Kokkonen
//
// This program is free software; you can redistribute it and/or 
// modify it under the terms of version 2 of the GNU General Public 
// License as published by the Free Software Foundation.
// 
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// Send comments, suggestions, problems to kimk@turbopower.com

import java.awt.image.BufferedImage;
import java.io.*;
import gpjpp.*;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

//extend GP for lawn mowing
//class must be public for stream loading; otherwise non-public ok

public class TartGP extends GP {

    //public null constructor required during stream loading only
    public TartGP() {}

    //this constructor called when new GPs are created
    TartGP(int genes) { super(genes); }

    //this constructor called when GPs are cloned during reproduction
    TartGP(TartGP gpo) { super(gpo); }

    //called when GPs are cloned during reproduction
    protected Object clone() { return new TartGP(this); }

    //ID routine required for streams
    public byte isA() { return GPObject.USERGPID; }

    //must override GP.createGene to create LawnGene instances
    public GPGene createGene(GPNode gpo) { return new TartGene(gpo); }

    //must override GP.evaluate to return standard fitness
    public double evaluate(GPVariables cfg) {

        TartVariables tcfg = (TartVariables)cfg;

        double totFit = 0;
        // test GP on N random boards
        for (int k=0; k<tcfg.NumTestImages; k++) {
            //create new random grid
            tcfg.createGrid();

            // evaluate main tree for 80 steps of the dozer
            // Maybe I should use a grid thing but with color
            evaluateImage(tcfg, null);
            totFit += tcfg.dozerGrid.calcFitness(Grid.EvalTypes.values()[tcfg.EvalType]);
        }
        totFit = totFit/tcfg.NumTestImages;
        if (cfg.ComplexityAffectsFitness)
            //add length into fitness to promote small trees
            totFit += length()/1000.0;

        //return standard fitness
        return totFit;
    }

    //optionally override GP.printOn to print tartarus-specific data
    public void printOn(PrintStream os, GPVariables cfg) {
        super.printOn(os, cfg);
    }

    //optionally override GP.drawOn to draw tartarus-specific data
    public void drawOn(GPDrawing ods, String fnameBase, 
        GPVariables cfg) throws IOException {

        //store the result trees to gif files
        super.drawOn(ods, fnameBase, cfg);
    }

    //method called on best of population at each checkpoint
    public void testBest(GPVariables cfg){
	TartVariables tcfg = (TartVariables)cfg;
	BufferedWriter out = null;
        try{
            //sets it to append mode
            out = new BufferedWriter(new FileWriter("data/"+tcfg.baseName+"_simTime.txt", true));
        }catch(IOException exception){
            System.out.println("Error opening file");
        }

        try{
            //starting new generation
            out.write("New Generation  " + tcfg.CheckpointGens + "\n");
        }catch(IOException exception){
            System.out.println("Error writing to file");
        }

        int curGridFit;
        // run this genome on some number of test grids
        for (int j=0; j<tcfg.NumTestImages; j++) {
            //create new random grid but will be the same across different gens
            tcfg.createGrid();

            //evaluate main tree for 80 steps of the dozer, printing grid after each move
            evaluateImage(tcfg, out);
            curGridFit = tcfg.dozerGrid.calcFitness(Grid.EvalTypes.values()[tcfg.EvalType]);
            tcfg.dozerGrid.outputFitness(out, curGridFit);
        }
        try{
            out.flush();
            out.close();
        }catch(IOException exception){
            System.out.println("Error closing file");
        }
    }

    public void printTree(PrintStream os, GPVariables cfg) {
        //super.printTree(os, cfg);
	TartVariables tcfg = (TartVariables)cfg;

        BufferedWriter out = null;
        try{
            out = new BufferedWriter(new FileWriter("data/"+tcfg.baseName+"_simBest.txt"));
        }catch(IOException exception){
            System.out.println("Error writing to file");
        }
        
        // write grid at each step for this genome
        TartGene gene = (TartGene)get(0);
        int curGridFit = 0;
        double totFit = 0;
	
        // run this genome on some number of test grids, printing the resulting grid at each step
        for (int j=0; j<tcfg.NumTestImages; j++) {
            //create new random grid
            tcfg.createGrid();

            //evaluate main tree for max steps of the dozer (given in .ini file)
	        // writing grid after each move
            evaluateImage(tcfg, out);
            Platform.runLater(() -> {
                WritableImage image = new WritableImage(tcfg.ImageDimension, tcfg.ImageDimension);
                PixelWriter p = image.getPixelWriter();
                for(int x = 0; x < tcfg.ImageDimension; x++) {
                    for(int y = 0; y < tcfg.ImageDimension; y++) {
                        p.setColor(x, y, tcfg.dozerGrid.discreteColors.get(tcfg.dozerGrid.colors.get(tcfg.ImageDimension * y + x)));
                    }
                }
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(512);
                imageView.setSmooth(false);
                Main.rootPane.getChildren().clear();
                Main.rootPane.getChildren().add(imageView);

//                File outputFile = new File("genetic-art.png");
//                BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
//                try {
//                    ImageIO.write(bImage, "png", outputFile);
//                } catch (IOException e) {
//                    System.out.println("Error saving image");
//                    e.printStackTrace();
//                }

            });
            curGridFit = tcfg.dozerGrid.calcFitness(Grid.EvalTypes.values()[tcfg.EvalType]);
            tcfg.dozerGrid.outputFitness(out, curGridFit);
            totFit += curGridFit;
        }
        try{
            out.flush();
            out.close();
        }catch(IOException exception){
            System.out.println("Error closing file");
        }
        totFit = totFit/tcfg.NumTestImages;
        if (cfg.ComplexityAffectsFitness)
            //add length into fitness to promote small trees
            totFit += length()/1000.0;
        os.println("FINAL FITNESS = "+totFit);
    }

    private void evaluateImage(TartVariables tcfg, BufferedWriter out) {
        for (int i=0; i<tcfg.ImageDimension * tcfg.ImageDimension; i++) {
            int x = i % tcfg.ImageDimension;
            int y = i / tcfg.ImageDimension;
            int north = 0;
            int northIndex = i - tcfg.ImageDimension;
            if(northIndex >= 0) north = tcfg.dozerGrid.colors.get(northIndex);
            int west = 0;
            int westIndex = i - 1;
            if((westIndex + 1) % tcfg.ImageDimension != 0 & westIndex >= 0) west = tcfg.dozerGrid.colors.get(westIndex);
            int northWest = 0;
            int northWestIndex = northIndex - 1;
            if((northWestIndex + 1) % tcfg.ImageDimension != 0 && northWestIndex >= 0) northWest = tcfg.dozerGrid.colors.get(northWestIndex);
            PixelInfo pixelInfo = new PixelInfo(x, y, north, west, northWest);
            int result = ((TartGene)get(0)).evaluate(tcfg, this, pixelInfo);
            tcfg.dozerGrid.colorPixel(Math.abs(result) % Grid.numColors, out);
        }
    }

}
