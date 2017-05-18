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

    for (int y = 0; y < width; y++) {
        for (int x = 0; x < height; x++) {
            
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
    // Sets up the file for window size
    FILE *window;
    if ((window = fopen("windowSize.txt", "r")) == NULL) {
        printf("No such file\n");
        exit(1);
    }
    // Reads window size ands stores in global variables
    char buff1[10];
    char buff2[10];
    fscanf(window, "%s", buff1);
    fscanf(window, "%s", buff2);
    width = atof(buff1);
    height = atof(buff2);
    fclose(window);
    
    /* makes sure the window size is a power of 2 (or else
     * it will break), then creates window dims so that the image
     * will fit */
    int i = 2;
    while (i < width) {
        i = 2 * i;
    }
    int j = 2;
    while (j < height) {
        j = 2 * j;
    }
    
    
    // Makes a i x j window with the title 'Pixel Graphics'. 
	if (pixInitialize(i, j, "Pixel Graphics") != 0)
		return 1;
	else {
		/* Clear the window to black. */
		pixClearRGB(0.0, 0.0, 0.0);
    }


    drawImage();
    pixSetRGB(5, 5, 1, 1, 1);

    pixRun();
    return 0;
}