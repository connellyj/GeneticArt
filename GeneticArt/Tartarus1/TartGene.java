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
    ColorVector evaluate(TartVariables cfg, TartGP gp, PixelInfo pixelInfo) {
        ColorVector c1, c2, c3;
        switch (node.value()) {

        case Grid.INVERT:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.invert();
            return c1;

        case Grid.ABS:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.abs();
            return c1;

        case Grid.ADD:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c1.addColor(c2);
            return c1;
//
        case Grid.SUBTR:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c1.subtrColor(c2);
            return c1;
//
        case Grid.MULT:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c1.multColor(c2);
            return c1;
//
        case Grid.DIV:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c1.divColor(c2);
            return c1;

        case Grid.MAX:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            return c1.greater(c1, c2);

        case Grid.AVG2:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c1.avgColor(c2);
            return c1;

        case Grid.AVG3:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c3 = ((TartGene)get(2)).evaluate(cfg, gp, pixelInfo);
            c1.avgColor(c2, c3);
            return c1;

        case Grid.IF:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c3 = ((TartGene)get(2)).evaluate(cfg, gp, pixelInfo);
            double avg = c1.avg();
            if(avg > 0.5) return c2;
            return c1;

        case Grid.X:
            double x = (double) pixelInfo.xCoord / (double) cfg.ImageDimension;
            return new ColorVector(x, x, x);

        case Grid.Y:
            double y = (double) pixelInfo.yCoord / (double) cfg.ImageDimension;
            return new ColorVector(y, y, y);

        case Grid.NORTH:
            return new ColorVector(1.0, 1.0, 1.0);

        case Grid.WEST:
            return new ColorVector(1.0, 1.0, 1.0);

        case Grid.NORTH_WEST:
            return new ColorVector(1.0, 1.0, 1.0);

        case Grid.RED:
            return new ColorVector(.5, 0.0, 0.0);

        case Grid.GREEN:
            return new ColorVector(.5, 0.0, 0.0);

        case Grid.BLUE:
            return new ColorVector(0.0, 0.0, 0.5);

        case Grid.RANDOM:
            Random r = new Random();
            return new ColorVector(r.nextDouble(), r.nextDouble(), r.nextDouble());

        case Grid.RAND_SCAL:
            Random s = new Random();
            double rand = s.nextDouble();
            return new ColorVector(rand, rand, rand);

        case Grid.SIN:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.sin();
            return c1;

        case Grid.COS:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.cos();
            return c1;

        case Grid.MAKE_RGB:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c2 = ((TartGene)get(1)).evaluate(cfg, gp, pixelInfo);
            c3 = ((TartGene)get(2)).evaluate(cfg, gp, pixelInfo);
            return new ColorVector(c1.getRed(), c2.getGreen(), c3.getBlue());

        case Grid.LOG:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.log();
            return c1;

        case Grid.ATAN:
            c1 = ((TartGene)get(0)).evaluate(cfg, gp, pixelInfo);
            c1.arcTan();
            return c1;

        default:
            throw new RuntimeException("Undefined function type "+node.value());
        }
    }

}
