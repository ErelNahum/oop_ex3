package image;

import java.awt.Color;

public class SubImage{
    private final Image image;
    private final int resolution;
    public SubImage(Image image, int resolution){
        this.image = image;
        this.resolution = resolution;
    }
    public Color[][][][] divideImage(int resolution) {
        int height = image.getHeight();
        int width = image.getWidth();

        int subImageSize = Math.max(1,width / resolution);
        int resolutionY = height / subImageSize;
        Color[][][][] dividedImages = new Color[resolutionY][resolution][][];

        for (int i = 0; i < resolutionY; i++) {
            for (int j = 0; j < resolution; j++) {
                int startRow = i * subImageSize;
                int startCol = j * subImageSize;

                Color[][] subImage = new Color[subImageSize][subImageSize];

                for (int row = 0; row < subImageSize; row++) {
                    for (int col = 0; col < subImageSize; col++) {
                        subImage[row][col] = image.getPixel(startRow + row,startCol + col);
                    }
                }

                dividedImages[i][j] = subImage;
            }
        }
        return dividedImages;
    }

    public double rgbToGreySubImage(Color[][] image){
        double brightness = 0;
        for(int row = 0; row < image.length; row++){
            for(int col = 0; col < image[0].length; col++){
                Color color = image[row][col];
                double greyPixel = color.getRed() * 0.2126 + color.getGreen() * 0.7152 + color.getBlue() * 0.0722;
                brightness += greyPixel;
            }
        }
        return brightness / (image.length * image[0].length * 255);
    }

    public double[][] imageBrightness(){
        Color[][][][] dividedImages = divideImage(resolution);
        double[][] brightness = new double[dividedImages.length][dividedImages[0].length];
        for (int i = 0; i < dividedImages.length; i++) {
            for (int j = 0; j < dividedImages[0].length; j++) {
                brightness[i][j] = rgbToGreySubImage(dividedImages[i][j]);
            }
        }
        return brightness;
    }
}
