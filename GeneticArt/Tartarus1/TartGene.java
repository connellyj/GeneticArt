package Tartarus1;
// Implementation of Tartarus Problem
// Author: Sherri Goings
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

import gpjpp.*;
import javafx.scene.paint.Color;

import java.util.Random;

//extend GPGene to evaluate Tartarus
public class TartGene extends GPGene {

    //public null constructor required during stream loading only
    public TartGene() {}

    //this constructor called when new genes are created
    TartGene(GPNode gpo) { super(gpo); }

    //this constructor called when genes are cloned during reproduction
    TartGene(TartGene gpo) { super(gpo); }

    //called when genes are cloned during reproduction
    protected Object clone() { return new TartGene(this); }

    //ID routine required for streams
    public byte isA() { return GPObject.USERGENEID; }

    //must override GPGene.createChild to create TartGene instances
    public GPGene createChild(GPNode gpo) { return new TartGene(gpo); }

    //called by TartGP.evaluate() for main branch of each GP
    Color evaluate(TartVariables cfg, TartGP gp, PixelInfo pixelInfo) {
        Color c1, c2, c3;
        switch (node.value()) {

        case Grid.MULT:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            return new Color(c1.getRed() * c2.getRed(), c1.getGreen() * c2.getGreen(),
                    c1.getBlue() * c2.getBlue(), 1.0);

        case Grid.MAX:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            if(c1.getSaturation() > c2.getSaturation()) return c1;
            return c2;

        case Grid.AVG2:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            return c1.interpolate(c2, 0.5);

        case Grid.AVG3:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c3 = ((TartGene)get(2)).evaluate(cfg, gp, pixelInfo);
            return c1.interpolate(c2, 0.5).interpolate(c3, 0.5);

        case Grid.X:
            double x = pixelInfo.xCoord / cfg.ImageDimension;
            return new Color(x, x, x, 1.0);

        case Grid.Y:
            double y = pixelInfo.yCoord / cfg.ImageDimension;
            return new Color(y, y, y, 1.0);

        case Grid.NORTH:
            return pixelInfo.northColor;

        case Grid.WEST:
            return pixelInfo.westColor;

        case Grid.NORTH_WEST:
            return pixelInfo.northWestColor;

        case Grid.RANDOM:
            Random r = new Random();
            return new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(),1.0);

        default:
            throw new RuntimeException("Undefined function type "+node.value());
        }
    }

}
