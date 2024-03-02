package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class Shell.
 * Responsible for Command Line Interface.
 */
public class Shell {
    private int resolution;
    private final int DEFAULT_RESOLUTION = 128;
    private int minResolution;
    private int maxResolution;
    private Image image;
    private String imageName;
    private HtmlAsciiOutput htmlAsciiOutput;
    private ConsoleAsciiOutput consoleAsciiOutput;
    private AsciiOutput asciiOutput;
    private SubImgCharMatcher subImgCharMatcher;
    private HashMap<String, HashMap<Integer, char[][]>> asciiCache;

    /**
     * the main method of shell.
     */
    public void run(){
        // Set the image
        imageName = "cat.jpeg";
        try {
            image = setImage(imageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // ??
        char[] charset = "7890123456".toCharArray();
        subImgCharMatcher = new SubImgCharMatcher(charset);

        // Initialize the outputs
        htmlAsciiOutput = new HtmlAsciiOutput("output.html", "Courier New");
        consoleAsciiOutput = new ConsoleAsciiOutput();
        asciiOutput = consoleAsciiOutput;

        asciiCache = new HashMap<>();

        // Receive input from the user
        while (true) {
            String userInput = getInput();
            if (userInput.equals("exit")) {
                System.out.println("Exiting program.");
                break;
            } else {
                handleUserInput(userInput);
            }
        }
    }
    private Image setImage(String fileName) throws IOException{
        imageName = fileName;
        Image newImage;
        newImage = new Image(fileName);

        // Set resolution and charset
        resolution = DEFAULT_RESOLUTION;
        minResolution = Math.max(1, newImage.getWidth()/ newImage.getHeight());
        maxResolution = newImage.getWidth();
        if(resolution > maxResolution){
            resolution = maxResolution;
        }else if(resolution < minResolution){
            resolution = minResolution;
        }
        return newImage;
    }
    private String getInput(){
        System.out.print(">>> ");
        return KeyboardInput.readLine();
    }
    private void handleUserInput(String userInput){
        if (userInput.equals("chars")){
            printCharSet();
        }else if(userInput.startsWith("add ") || userInput.equals("add")){
            handleAdd(userInput);
        }else if(userInput.startsWith("remove ") || userInput.equals("remove")){
            handleRemove(userInput);
        }else if(userInput.startsWith("res ") || userInput.equals("res")){
            handleRes(userInput);
        }else if(userInput.startsWith("image ") || userInput.equals("image")){
            handleImage(userInput);
        }else if(userInput.startsWith("output ") || userInput.equals("output")){
            handleOutput(userInput);
        }else if(userInput.equals("asciiArt")){
            handleAsciiArt();
        }else{
            System.out.println("Did not execute due to incorrect command.");
        }
    }
    private void handleAsciiArt(){
        if(subImgCharMatcher.isEmpty()){
            System.out.println("Did not execute. Charset is empty.");
            return;
        }
        char[][] asciiArtResult;
        if (asciiCache.containsKey(imageName) &&
                asciiCache.get(imageName).containsKey(resolution)
        ) {
            asciiArtResult = asciiCache.get(imageName).get(resolution);
        }
        else {
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, subImgCharMatcher);
            asciiArtResult = asciiArtAlgorithm.run();
            if (!asciiCache.containsKey(imageName)) {
                asciiCache.put(imageName, new HashMap<>());
            }
            asciiCache.get(imageName).put(resolution, asciiArtResult);

        }
        asciiOutput.out(asciiArtResult);
    }
    private void handleOutput(String userInput){
        if(userInput.equals("output html")){
            asciiOutput = htmlAsciiOutput;
        }else if(userInput.equals("output console")){
            asciiOutput = consoleAsciiOutput;
        }else{
            System.out.println("Did not change output method due to incorrect format.");
        }
    }
    private void handleImage(String userInput){
        if(userInput.length() <= "image ".length()){
            return;
        }
        String fileName = userInput.substring("image ".length());
        try{
            image = setImage(fileName);
        }catch (IOException e){
            System.out.println("Did not execute due to problem with image file.");
        }
    }
    private void handleRes(String userInput){
        if(userInput.equals("res up")){
            if(resolution * 2 <= maxResolution){
                resolution *= 2;
                System.out.println(String.format("Resolution set to %s.", resolution));
            }
            else{
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        }else if(userInput.equals("res down")){
            if(resolution / 2 >= minResolution){
                resolution /= 2;
                System.out.println(String.format("Resolution set to %s.", resolution));
            }
            else{
                System.out.println("Did not change resolution due to exceeding boundaries.");
            }
        }else{
            System.out.println("Did not change resolution due to incorrect format.");
        }
    }
    private void handleAdd(String userInput){
        if((userInput.substring("add".length())).isEmpty()){
            System.out.println("Did not add due to incorrect format.");
            return;
        }
        asciiCache = new HashMap<>();
        userInput = userInput.substring("add ".length());
        if(userInput.equals("all")){
            addAllChars();
        }else if(userInput.equals("space")){
            subImgCharMatcher.addChar(' ');
        } else if(userInput.length() == 1){
            subImgCharMatcher.addChar(userInput.charAt(0));
        }else if(userInput.length() == 3 && userInput.charAt(1) == '-'){
            int first = userInput.charAt(0);
            int second = userInput.charAt(2);
            if(first < second){
                for (int i = first; i <= second; i++) {
                    subImgCharMatcher.addChar((char)i);
                }
            }else{
                for (int i = second; i <= first; i++) {
                    subImgCharMatcher.addChar((char)i);
                }
            }
        }else{
            System.out.println("Did not add due to incorrect format.");
        }
    }
    private void handleRemove(String userInput){
        if((userInput.substring("remove".length())).isEmpty()){
            System.out.println("Did not remove due to incorrect format.");
            return;
        }
        asciiCache = new HashMap<>();
        userInput = userInput.substring("remove ".length());
        if(userInput.equals("all")){
            removeAllChars();
        }else if(userInput.equals("space")){
            subImgCharMatcher.removeChar(' ');
        } else if(userInput.length() == 1){
            subImgCharMatcher.removeChar(userInput.charAt(0));
        }else if(userInput.length() == 3 && userInput.charAt(1) == '-'){
            int first = userInput.charAt(0);
            int second = userInput.charAt(2);
            if(first < second){
                for (int i = first; i <= second; i++) {
                    subImgCharMatcher.removeChar((char)i);
                }
            }else{
                for (int i = second; i <= first; i++) {
                    subImgCharMatcher.removeChar((char)i);
                }
            }
        }else{
            System.out.println("Did not remove due to incorrect format.");
        }
    }
    private void printCharSet(){
        // Printing the sorted char array with spaces
        for (char c : subImgCharMatcher.getCharSet()) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private void addAllChars(){
        for (int i = 32; i <= 126; i++) {
            subImgCharMatcher.addChar((char)i);
        }
    }
    private void removeAllChars(){
        for (int i = 32; i <= 126; i++) {
            subImgCharMatcher.removeChar((char)i);
        }
    }
}
