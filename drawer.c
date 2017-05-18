/*
* Made by Julia Connelly and Alex Walker
* 5/16/17
* clang drawer.c 000pixel.o -lglfw -framework OpenGL
*/
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "000pixel.h"

enum {
    RED,
    ORANGE,
    YELLOW,
    LIME,
    GREEN,
    SEA_GREEN,
    LIGHT_BLUE,
    MED_BLUE,
    BLUE,
    PURPLE,
    MAGENTA,
    PINK
};

double colorList[12][3] = {{1.0, 0.0, 0.0},
                           {1.0, 0.5, 0.0},
                           {1.0, 1.0, 0.0},
                           {0.5, 1.0, 0.0},
                           {0.0, 1.0, 0.0},
                           {0.0, 1.0, 0.5},
                           {0.0, 1.0, 1.0},
                           {0.0, 0.5, 1.0},
                           {0.0, 0.0, 1.0},
                           {0.5, 0.0, 1.0},
                           {1.0, 0.0, 1.0},
                           {1.0, 0.0, 0.5}};

void getColor(double index, double blankColor[3]) {
    for (int i = 0; i < 3; i++) {
        blankColor[i] = colorList[(int)index][i];
    }
}

void drawImage(FILE *file) {
    double color[3];
    char chr = fgetc(file);
    char num[30];
    for (int i = 0; i < 30; i++) {
        num[i] = '\0';
    }
    
    for (int x = 0; x < 512; x++) {
        for (int y = 0; x <512; x++) {
            
            int i = 0;
            while (chr != ' ' || chr != '\n') {
                num[i] = chr;
                chr = fgetc(file);
                i++;
            }
            
            getColor(atof(num), color);
            pixSetRGB(x, y, color[0], color[1], color[2]);
            
            // go through file until next num
            while (chr == ' ' || chr == '\n') {
                chr = fgetc(file);
            }
            // reset num for next color value
            for (int i = 0; i < 30; i++) {
                num[i] = '\0';
            }
        }
    }
}

int main() {    
    // Makes a 512 x 512 window with the title 'Pixel Graphics'. 
	if (pixInitialize(12, 12, "Pixel Graphics") != 0)
		return 1;
	else {
		/* Clear the window to black. */
		pixClearRGB(0.0, 0.0, 0.0);
    }

    // Sets up the file
    FILE *file;
    if ((file = fopen("output.txt", "r")) == NULL) {
        printf("No such file\n");
        exit(1);
    }  

    drawImage(file);
    fclose(file);

    pixRun();
    return 0;
}