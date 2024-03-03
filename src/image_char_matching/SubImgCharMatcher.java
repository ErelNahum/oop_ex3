package image_char_matching;

import java.util.*;


/**
 * Responsible for matching characters for given brightness.
 */
public class SubImgCharMatcher {
    private final TreeMap<Double, TreeSet<Character>> charBrightnessMap;
    private final SortedSet<Character> charSet;

    /**
     * Instantiates a new Sub img char matcher.
     *
     * @param charset the charset
     */
    public SubImgCharMatcher(char[] charset) {
        this.charBrightnessMap = new TreeMap<>();
        this.charSet = new TreeSet<>();
        for (char c : charset) {
            addChar(c);
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

        return charBrightnessMap.get(closestKey).first();
    }

    /**
     * Add char to charset.
     *
     * @param c the character to be added.
     */
    public void addChar(char c) {
        charSet.add(c);
        double brightness = calculateBrightnessForChar(c);
        // if character's brightness already exists in map, choose the lowest ascii value
        if(charBrightnessMap.containsKey(brightness)){
            charBrightnessMap.get(brightness).add(c);
        }
        else {
            TreeSet<Character> treeSet = new TreeSet<>();
            treeSet.add(c);
            charBrightnessMap.put(brightness, treeSet);
        }
    }

    /**
     * Remove char from charset.
     *
     * @param c the character to be removed
     */
    public void removeChar(char c) {
        if (charSet.contains(c)) {
            charSet.remove(c);
            double brightness = calculateBrightnessForChar(c);
            charBrightnessMap.get(brightness).remove(c);
            if (charBrightnessMap.get(brightness).isEmpty()) {
                charBrightnessMap.remove(brightness);
            }
        }
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
    public SortedSet<Character> getCharSet() {
        return charSet;
    }
}


