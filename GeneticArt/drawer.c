/*
* Made by Julia Connelly and Alex Walker
* 5/16/17
* clang -o drawer drawer.c 000pixel.o -lglfw -framework OpenGL
* run with ./drawer < FILE_NAME_TO_READ
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
int width;
int height;

void getColor(double index, double blankColor[3]) {
    for (int i = 0; i < 3; i++) {
        blankColor[i] = colorList[(int)index][i];
    }
}

void drawImage() {
    double color[3];
    char chr = fgetc(stdin);
    char num[30];
    for (int i = 0; i < 30; i++) {
        num[i] = '\0';
    }
    
    
    
    for (int y = 0; y < 12; y++) {
        for (int x = 0; x < 12; x++) {
            
            // go through file until next num
            while (chr == ' ' || chr == '\n') {
                chr = fgetc(stdin);
            }

            int i = 0;
            while (chr != ' ' && chr != '\n' && chr != EOF) {
                num[i] = chr;
                chr = fgetc(stdin);
                i++;
            }
            
            getColor(atof(num), color);
            pixSetRGB(x, y, color[0], color[1], color[2]);

            // reset num for next color value
            for (int i = 0; i < 30; i++) {
                num[i] = '\0';
            }
        }
    }
}

int main() {    
    // Makes a 512 x 512 window with the title 'Pixel Graphics'. 
	if (pixInitialize(512, 512, "Pixel Graphics") != 0)
		return 1;
	else {
		/* Clear the window to black. */
		pixClearRGB(0.0, 0.0, 0.0);
    }
    
     Sets up the file for window size
    FILE *window;
    if ((file = fopen("windowSize.txt", "r")) == NULL) {
        printf("No such file\n");
        exit(1);
    }
    fgets(window, );


    drawImage();
    pixSetRGB(5, 5, 1, 1, 1);

    pixRun();
    return 0;
}