package image;

/**
 * Represents a 2d collection of sub-images.
 */
public class SubImages {
    private final SubImage[][] subImages;
    private final int verticalCount;
    private final int horizontalCount;

    /**
     * Instantiates a new Sub images.
     *
     * @param image      the parent image
     * @param resolution the wanted resolution
     */
    public SubImages(Image image, int resolution){
        int blockSize = image.getWidth() / resolution;

        verticalCount = image.getHeight() / blockSize;
        horizontalCount = image.getWidth() / blockSize;

        subImages = new SubImage[verticalCount][horizontalCount];

        for (int i = 0; i < verticalCount; i++) {
            for (int j = 0; j < horizontalCount; j++) {
                subImages[i][j] = new SubImage(image, blockSize, i, j);
            }
        }
    }

    /**
     * Calculate brightness 2d array of sub images.
     *
     * @return the brightnesses array
     */
    public double[][] getBrightnessArray() {
       double[][] brightnessArray = new double[verticalCount][horizontalCount];
       for (int i = 0; i < verticalCount; i++) {
           for (int j = 0; j < horizontalCount; j++) {
              brightnessArray[i][j] = subImages[i][j].getBrightness();
           }
       }
       return brightnessArray;
    }
}
