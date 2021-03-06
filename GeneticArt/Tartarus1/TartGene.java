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
    int evaluate(TartVariables cfg, TartGP gp, PixelInfo pixelInfo) {

        int arg1, arg2, arg3, result;
        switch (node.value()) {

        case Grid.INC:
            return ( ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo) + 1);

        case Grid.ABS:
            return Math.abs(((TartGene)get(0)).evaluate(cfg, gp, pixelInfo));

        case Grid.ADD:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo) + ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            return result;

        case Grid.SUBTR:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo) - ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            return result;

        case Grid.MULT:
            result = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo) * ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            return result;

        case Grid.DIV:
            int temp = ((TartGene) get(1)).evaluate(cfg, gp, pixelInfo);
            if (temp == 0) {
                temp = 1;
            }
            result = ((TartGene) get(0)).evaluate(cfg, gp, pixelInfo) / temp;
            return result;

        case Grid.MAX:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            if (arg1 > arg2) return arg1;
            else return arg2;

        case Grid.AVG2:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            return (arg1 + arg2) / 2;

        case Grid.AVG3:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            arg3 = ( (TartGene)get(2) ).evaluate(cfg, gp, pixelInfo);
            return (arg1 + arg2 + arg3) / 3;

        case Grid.IF:
            arg1 = ( (TartGene)get(0) ).evaluate(cfg, gp, pixelInfo);
            arg2 = ( (TartGene)get(1) ).evaluate(cfg, gp, pixelInfo);
            arg3 = ( (TartGene)get(2) ).evaluate(cfg, gp, pixelInfo);
            if (Math.floor(arg1) % 2 == 0) {
                return arg2;
            }
            else {
                return arg3;
            }

        case Grid.X:
            return pixelInfo.xCoord;

        case Grid.Y:
            return pixelInfo.yCoord;

        case Grid.NORTH:
            return pixelInfo.northColor;

        case Grid.WEST:
            return pixelInfo.westColor;

        case Grid.NORTH_WEST:
            return pixelInfo.northWestColor;

        case Grid.RANDOM:
            Random r = new Random();
            return r.nextInt(Grid.numColors);

        case Grid.ZERO:
            return 0;

        case Grid.ONE:
            return 1;

        case Grid.TWO:
            return 2;

        case Grid.THREE:
            return 3;

        case Grid.FOUR:
            return 4;

        case Grid.FIVE:
            return 5;

        case Grid.SIX:
            return 6;

        case Grid.SEVEN:
            return 7;

        case Grid.EIGHT:
            return 8;

        case Grid.NINE:
            return 9;

        case Grid.TEN:
            return 10;

        case Grid.ELEVEN:
            return 11;

        case Grid.SIN:
            return (int)Math.floor(Math.abs(Math.sin(((TartGene)get(0)).evaluate(cfg, gp, pixelInfo))) % Grid.numColors);

        case Grid.COS:
            return (int)Math.floor(Math.abs(Math.cos(((TartGene)get(0)).evaluate(cfg, gp, pixelInfo))) % Grid.numColors);

        default:
            throw new RuntimeException("Undefined function type "+node.value());
        }
    }

}
