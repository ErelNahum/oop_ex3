package image;

import java.awt.Color;

public class ImagePreProcessing {
    private static final Color MARGIN_COLOR = Color.WHITE;
    public static Image padImage(Image sourceImage) {
        Color[][] paddedImage;
        int newWidth = ImagePreProcessing.closestPowerOfTwo(sourceImage.getWidth());
        int newHeight = ImagePreProcessing.closestPowerOfTwo(sourceImage.getHeight());

        paddedImage = new Color[newHeight][newWidth];

        int widthMargin = (newWidth - sourceImage.getWidth()) / 2;
        int heightMargin = (newHeight - sourceImage.getHeight()) / 2;

        for (int i = 0; i < newHeight; i++) {
            for (int j = 0; j < newWidth; j++) {
                if (i <= heightMargin || // top margin
                        newHeight - heightMargin - 1 < i || // bottom margin
                        j <= widthMargin  || // left margin
                        newWidth - widthMargin - 1 < j // right margin
                ) {
                    paddedImage[i][j] = MARGIN_COLOR;
                } else {

                    paddedImage[i][j] = sourceImage.getPixel(Math.max(i - heightMargin-1,0), Math.max(j - widthMargin-1,0));
                }
            }
        }
        return new Image(paddedImage, newWidth, newHeight);
    }

    private static int closestPowerOfTwo(int number) {
        int result = 1;
        while (result < number) {
            result *= 2;
        }
        return result;
    }
}
