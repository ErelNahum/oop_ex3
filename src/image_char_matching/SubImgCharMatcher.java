package image_char_matching;

import java.util.*;


/**
 * Responsible for matching characters for given brightness.
 */
public class SubImgCharMatcher {
    private final TreeMap<Double, Character> charBrightnessMap;

    /**
     * Instantiates a new Sub img char matcher.
     *
     * @param charset the charset
     */
    public SubImgCharMatcher(char[] charset) {
        this.charBrightnessMap = new TreeMap<>();
        for (char c : charset) {
            double brightness = calculateBrightnessForChar(c);
            // if character's brightness already exists in map, choose the lowest ascii value
            if(charBrightnessMap.containsKey(brightness)){
                if (c < charBrightnessMap.get(brightness)) {
                    charBrightnessMap.replace(brightness, c);
                }
            }
            else {
                charBrightnessMap.put(brightness, c);
            }
        }
    }

    /**
     * Gets char by image brightness.
     *
     * @param brightness the brightness
     * @return the corresponding character
     */
    public char getCharByImageBrightness(double brightness) {

        double minBrightness = charBrightnessMap.firstKey();
        double maxBrightness = charBrightnessMap.lastKey();
        double updatedBrightness = (maxBrightness - minBrightness) * brightness + minBrightness;
        double closestFromTheRight = charBrightnessMap.ceilingKey(updatedBrightness);
        double closestFromTheLeft = charBrightnessMap.floorKey(updatedBrightness);
        double closestKey;

        if (Math.abs(updatedBrightness - closestFromTheLeft) <=
                Math.abs(updatedBrightness - closestFromTheRight)) {
            closestKey = closestFromTheLeft;
        }
        else {
            closestKey = closestFromTheRight;
        }

        return charBrightnessMap.get(closestKey);
    }

    /**
     * Add char to charset.
     *
     * @param c the character to be added.
     */
    public void addChar(char c) {
        double brightness = calculateBrightnessForChar(c);
        charBrightnessMap.put(brightness, c);
    }

    /**
     * Remove char from charset.
     *
     * @param c the character to be removed
     */
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

    /**
     * Checks if charset is empty.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return charBrightnessMap.isEmpty();
    }

    /**
     * Gets sorted list of charset.
     *
     * @return the list
     */
    public List<Character> getCharSet() {
        ArrayList<Character> charset = new ArrayList<>(charBrightnessMap.values());
        java.util.Collections.sort(charset);
        return charset;
    }
}


