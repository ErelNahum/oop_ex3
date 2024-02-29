package image;

import java.awt.*;

/**
 * Represents a sub-image.
 */
public class SubImage extends Image{

    /**
     * Instantiates a new Sub image.
     *
     * @param image         the image
     * @param blockSize     the block size
     * @param verticalPos   the vertical pos
     * @param horizontalPos the horizontal pos
     */
    public SubImage(Image image, int blockSize, int verticalPos, int horizontalPos) {
        super(new Color[blockSize][blockSize], blockSize, blockSize);

        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                this.pixelArray[i][j] = image.getPixel(
                        blockSize * verticalPos + i,
                        blockSize * horizontalPos + j
                );
            }
        }
    }

    /**
     * Calculates brightness of entire sub-image.
     *
     * @return the brightness
     */
    public double getBrightness() {
        double brightness = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = this.getPixel(i, j);
                brightness += color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
            }
        }
        return brightness / (height * width * 255);
    }
}
