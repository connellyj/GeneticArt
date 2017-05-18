package Tartarus1;
// gpjpp example program
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

import java.io.*;
import java.nio.file.*;

import gpjpp.*;


//Lawnmower test
class Tartarus extends GPRun {

    //must override GPRun.createVariables to return lawn-specific variables
    protected GPVariables createVariables() { 
        return new TartVariables(); 
    }

    //must override GPRun.createNodeSet to return 
    //  initialized set of functions & terminals
    protected GPAdfNodeSet createNodeSet(GPVariables cfg) {
 
        GPNodeSet ns0 = new GPNodeSet(13);

        //MAIN TREE
        ns0.putNode(new GPNode(Grid.INC, "inc", 1));
        ns0.putNode(new GPNode(Grid.ABS, "abs", 1));
        ns0.putNode(new GPNode(Grid.ADD, "add", 2));
        ns0.putNode(new GPNode(Grid.MAX, "max", 2));
        ns0.putNode(new GPNode(Grid.AVG2, "avg2", 2));
        ns0.putNode(new GPNode(Grid.AVG3, "avg3", 3));
        ns0.putNode(new GPNode(Grid.IF, "if", 3));
        ns0.putNode(new GPNode(Grid.X, "x"));
        ns0.putNode(new GPNode(Grid.Y, "y"));
        ns0.putNode(new GPNode(Grid.NORTH, "N"));
        ns0.putNode(new GPNode(Grid.WEST, "W"));
        ns0.putNode(new GPNode(Grid.NORTH_WEST, "NW"));
        ns0.putNode(new GPNode(Grid.RANDOM, "rand"));

	// ADF - Do NOT change
	GPAdfNodeSet adfNs = new GPAdfNodeSet(1);
	adfNs.put(0, ns0);
        return adfNs;
    }

    //must override GPRun.createPopulation to return 
    //  lawn-specific population
    protected GPPopulation createPopulation(GPVariables cfg, 
        GPAdfNodeSet adfNs) {
        return new TartPopulation(cfg, adfNs);
    }

    //construct this test case
    Tartarus(String baseName) { super(baseName, true); }

    //main application function
    public static void startGA() {

        String baseName = "tartarus";

         //clear simulation files if exist
        try {
            Path path = Paths.get("data/"+baseName+"_simTime.txt");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("Error deleting simTime files");
            e.printStackTrace();
        }
        try {
            Path path = Paths.get("data/"+baseName+"_simBest.txt");
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println("Error deleting simTime files");
            e.printStackTrace();
        }


        //construct the test case
        Tartarus test = new Tartarus(baseName);

        //run the test
        test.run();

        //make sure all threads are killed
        //System.exit(0);
    }
}
