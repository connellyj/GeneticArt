An EA that uses a GP representation to evolve images.


To run:

1. clone the repository from https://github.com/connelly/GeneticArt.git
2. import the internal GeneticArt folder (not the top level repo folder, the one inside of that) as a project in IntelliJ
3. run the Main class

If you don't want to use IntelliJ, you can run things like you told us to in the Tartarus assignment, but you will have create a data folder in the Tartarus1 folder, as well as copy the tartarus.ini file into Tartarus1.


tartarus.ini:

The input file has some specifications specific to our GP:

1. NumTestImages is how many images will be drawn and then evaluated and then averaged to find the final fitness (we often just left it as 1)
2. ImageDimension is the height of the image (also the width because it's always a square), runs are fast with a size of 64, anything larger tends to take a long time
3. EvalType is the type of evaluation used (will explain which eval type is which later, because it differs in the different representations)


Different representations:

We created multiple different representations for our GP. Each representation is in a different branch:

1. master: this is the representation where we have a discrete number of colors, so the GP deals only in integers. The eval methods are as follows:
    0 - human evaluation: you will be prompted to rate each image in the population
    1 - gradient evaluation: the image is better if each pixel is similar (but not the same) to all its neighbors 
    2 - standard deviation: the image is better if its standard deviation is higher
    3 - rainbow evaluation: should produce a horizontal rainbow
    4 - square evaluation: should converge to vertical stripes
    5 - X evaluation: should evaluate to a vertical rainbow (a tree with one node: x)
    6 - inverse-X evaluation: should also just produce a vertical rainbow, but starting with a different color
    7 - similar-neighbor evaluation: like gradient, except only accounts for north, south, east, west neighbors
    8 - random evaluation: just an average of the evaluations from gradient, sim-neighbor, rainbow, and square eval
    9 - weighted evaluation: an image is good if some of the pixels evaluate well as a gradient, and some of them are the same as their neighboring pixels
    10 - blue-green evaluation: an image is good if each pixel is blue-green
    11 - symmetry evaluation: trys to make symmetrical images a better fitness
2. karl_sims: now the GP deals only with vectors of length 3, so more colors and possible terminals are available
    0 - 
3. rgb: the red, green, and blue values for each pixel are all evaluated with a different GP branch. The eval methods are as follows:
    0 - human evaluation: you will be prompted to select parents, only works with tournament selection with a tournament size of 5
    1 - gradient evaluation: attempting to be the same thing as gradient from the master branch, but also requires that the standard deviation be somewhat high
    2 - random evaluation: each image is assigned a random fitness
    3 - similar evaluation: an image is good if each pixel is similar to its north, south, east, west neighbors
    
    
NOTE: If the display windows freezes up at the end of a run, comment out these lines of code at the bottom of TartGP:

    //            File outputFile = new File("genetic-art.png");
    //            BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
    //            try {
    //                ImageIO.write(bImage, "png", outputFile);
    //            } catch (IOException e) {
    //                System.out.println("Error saving image");
    //                e.printStackTrace();
    //            }
