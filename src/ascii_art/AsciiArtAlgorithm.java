package ascii_art;

import image.Image;
import image.ImagePreProcessing;
import image.SubImages;
import image_char_matching.SubImgCharMatcher;

/**
 * The class Ascii art algorithm.
 * Responsible for converting an Image to an asciiArt, using a SubImageCharMatcher
 */
public class AsciiArtAlgorithm {

    private final int resolution;
    private final SubImgCharMatcher subImgCharMatcher;
    private Image image;

    /**
     * Instantiates a new Ascii art algorithm.
     *
     * @param image             the image
     * @param resolution        the resolution
     * @param subImgCharMatcher the sub img char matcher
     */
    public AsciiArtAlgorithm(Image image, int resolution, SubImgCharMatcher subImgCharMatcher) {
        this.image = image;
        this.resolution = resolution;
        this.subImgCharMatcher = subImgCharMatcher;
    }

    /**
     * Run the ascii art algorithm.
     *
     * @return the ascii art
     */
    public char[][] run() {
        // Pad the image
        image = ImagePreProcessing.padImage(image);

        // Divide to sub images
        SubImages sub = new SubImages(image, resolution);
        // Calculate the sub image brightness
        double[][] imageBrightness = sub.getBrightnessArray();

        // Creating an array of corresponding chars
        char[][] asciiArt = new char[imageBrightness.length][imageBrightness[0].length];
        for (int i = 0; i < imageBrightness.length; i++) {
            for (int j = 0; j < imageBrightness[0].length; j++) {
                asciiArt[i][j] = subImgCharMatcher.getCharByImageBrightness(imageBrightness[i][j]);
            }
        }
        return asciiArt;
    }

}
