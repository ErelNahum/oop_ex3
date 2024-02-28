package ascii_art;

import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImagePreProcessing;
import image.SubImage;
import image_char_matching.SubImgCharMatcher;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

public class AsciiArtAlgorithm {

    private int resolution;
    private char[] charset;
    private Image image;

    public AsciiArtAlgorithm(Image image, int resolution, char[] charset) {
        this.image = image;
        this.resolution = resolution;
        this.charset = charset;
    }

    public char[][] run() {
        // Pad the image
        image = ImagePreProcessing.padImage(image);

        // Divide to sub images
        SubImage sub = new SubImage(image, resolution);
        // Calculate the sub image brightness
        double[][] imageBrightness = sub.imageBrightness();


        // יצירת מחלקה להתאמת תווים לבהירות
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(charset);

        // יצירת מערך דו-מימדי של תווים ASCII בהתאם לבהירות התמונה
        char[][] asciiArt = new char[imageBrightness.length][imageBrightness[0].length];
        for (int i = 0; i < imageBrightness.length; i++) {
            for (int j = 0; j < imageBrightness[0].length; j++) {
                asciiArt[i][j] = charMatcher.getCharByImageBrightness(imageBrightness[i][j]);
            }
        }

        return asciiArt;
    }




    public static void main(String[] args) {
        // Load your original image (replace "path/to/your/image.jpg" with the actual path)
//        Image image = loadImage("path/to/your/image.jpg");
        Shell shell = new Shell();
        shell.run();

        Image image;
        try {
            image = new Image("cat.jpeg");
        }catch(IOException e){
            image = null;
        }
        // Set resolution and charset
        int resolution = 128; // Change this to your desired resolution
        char[] charset = "0123456789".toCharArray();//"⠀⠁⠂⠃⠄⠅⠆⠇⠈⠉⠊⠋⠌⠍⠎⠏⠐⠑⠒⠓⠔⠕⠖⠗⠘⠙⠚⠛⠜⠝⠞⠟⠠⠡⠢⠣⠤⠥⠦⠧⠨⠩⠪⠫⠬⠭⠮⠯⠰⠱⠲⠳⠴⠵⠶⠷⠸⠹⠺⠻⠼⠽⠾⠿".toCharArray();
//= {'@', '#', '8', '&', 'o', ':', '*', '.', ' ',';','-','!'}; // Example charset

        // Create an instance of AsciiArtAlgorithm
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, charset);

        // Run the algorithm
        char[][] asciiArtResult = asciiArtAlgorithm.run();

        // You can print the result or perform any other action with it
        printAsciiArt(asciiArtResult);

        HtmlAsciiOutput html = new HtmlAsciiOutput("test.html", "Courier");
        html.out(asciiArtResult);
    }

    private static void printAsciiArt(char[][] asciiArt) {
        for (char[] row : asciiArt) {
            for (char pixel : row) {
                System.out.print(pixel); // Print each pixel
            }
            System.out.println(); // Move to the next line
        }
    }
}
