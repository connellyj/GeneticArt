package Tartarus1;
// Tartarus implementation
// Copyright (c) 2013, Sherri Goings
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

import java.io.*;
import java.util.Properties;
import java.util.Random;
import gpjpp.*;

//extension of GPVariables for Tartarus-specific stuff

public class TartVariables extends GPVariables {

    int ImageDimension = 10;
    int NumTestImages = 10;
    int EvalType = 1;

    // simulation grid, not read from file but created during run
    // included here for easy access in any class/function
    public Grid dozerGrid;

    //public null constructor required for stream loading
    public TartVariables() {}

    //ID routine required for streams
    public byte isA() { return GPObject.USERVARIABLESID; }

    public void createGrid() {
        dozerGrid = new Grid(ImageDimension);
    }

    //get values from properties
    public void load(Properties props) {
        if (props == null)
            return;
        super.load(props);
        NumTestImages = getInt(props, "NumTestImages", NumTestImages);
        ImageDimension = getInt(props, "ImageDimension", ImageDimension);
        EvalType = getInt(props, "EvalType", ImageDimension);
    }

    //get values from a stream
    protected void load(DataInputStream is)
        throws ClassNotFoundException, IOException,
            InstantiationException, IllegalAccessException {
        super.load(is);
        NumTestImages = is.readInt();
        ImageDimension = is.readInt();
        EvalType = is.readInt();
    }

    //save values to a stream
    protected void save(DataOutputStream os) throws IOException {
        super.save(os);
        os.writeInt(NumTestImages);
        os.writeInt(ImageDimension);
        os.writeInt(EvalType);
    }

    //write values to a text file
    public void printOn(PrintStream os, GPVariables cfg) {
        super.printOn(os, cfg);
        os.println("NumTestImages             = "+NumTestImages);
        os.println("ImageDimension            = "+ImageDimension);
        os.println("EvalType                  = "+EvalType);
    }
}
