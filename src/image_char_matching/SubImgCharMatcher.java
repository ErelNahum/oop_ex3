package image_char_matching;

import java.util.Map;
import java.util.TreeMap;


public class SubImgCharMatcher {
    private TreeMap<Double, Character> charBrightnessMap;

    public SubImgCharMatcher(char[] charset) {
        this.charBrightnessMap = new TreeMap<>();
        for (char c : charset) {
            double brightness = calculateBrightnessForChar(c);
            if(charBrightnessMap.get(brightness) != null){
                if((int)c > (int)charBrightnessMap.get(brightness)){
                    continue;
                }
            }
            charBrightnessMap.put(brightness, c);
        }
        normalizeBrightnessMap();
    }

    public char getCharByImageBrightness(double brightness) {

        double closestFromTheRight = charBrightnessMap.ceilingKey(brightness);
        double closestFromTheLeft = charBrightnessMap.floorKey(brightness);
        double closestKey;

        if (Math.abs(brightness - closestFromTheLeft) <= Math.abs(brightness - closestFromTheRight)) {
            closestKey = closestFromTheLeft;
        }
        else {
            closestKey = closestFromTheRight;
        }

        return charBrightnessMap.get(closestKey);
    }

    public void addChar(char c) {
        double brightness = calculateBrightnessForChar(c);
        charBrightnessMap.put(brightness, c);
    }

    public void removeChar(char c) {
        charBrightnessMap.values().remove(c);
    }


    private double calculateBrightnessForChar(char character) {
        boolean[][] boolArray = CharConverter.convertToBoolArray(character);

        int whitePixelCount = 0;
        int totalPixelCount = boolArray.length * boolArray[0].length;

        for (boolean[] row : boolArray) {
            for (boolean pixel : row) {
                if (pixel) {
                    whitePixelCount++;
                }
            }
        }

        return (double) whitePixelCount / totalPixelCount;
    }

    private void normalizeBrightnessMap() {
        double minBrightness = charBrightnessMap.firstKey();
        double maxBrightness = charBrightnessMap.lastKey();

        TreeMap<Double, Character> tempMap = new TreeMap<>();

        for (Map.Entry<Double, Character> entry : charBrightnessMap.entrySet()) {
            tempMap.put(
                    (entry.getKey() - minBrightness) / (maxBrightness - minBrightness),
                    entry.getValue()
            );
        }
        charBrightnessMap = tempMap;
    }
}


