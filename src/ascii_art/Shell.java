package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import exceptions.*;
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
        char[] charset = "0123456789".toCharArray();
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
                try{
                    handleUserInput(userInput);
                }catch(IOException e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    private Image setImage(String fileName) throws IOException, ImageIOException {
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
    private void handleUserInput(String userInput) throws IOException {
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
            throw new NameIOException();
        }
    }
    private void handleAsciiArt() throws EmptyCharSetIOException {
        if(subImgCharMatcher.isEmpty()){
            throw new EmptyCharSetIOException();
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
    private void handleOutput(String userInput) throws OutputIOException {
        if(userInput.equals("output html")){
            asciiOutput = htmlAsciiOutput;
        }else if(userInput.equals("output console")){
            asciiOutput = consoleAsciiOutput;
        }else{
            throw new OutputIOException();
        }
    }
    private void handleImage(String userInput) throws ImageIOException{
        if(userInput.length() <= "image ".length()){
            return;
        }
        String fileName = userInput.substring("image ".length());
        try{
            image = setImage(fileName);
        }catch (IOException e){
            throw new ImageIOException();
        }
    }
    private void handleRes(String userInput) throws ResolutionIOException, ResolutionBoundariesIOException {
        if(userInput.equals("res up")){
            if(resolution * 2 <= maxResolution){
                resolution *= 2;
                System.out.println(String.format("Resolution set to %s.", resolution));
            }
            else{
                throw new ResolutionBoundariesIOException();
            }
        }else if(userInput.equals("res down")){
            if(resolution / 2 >= minResolution){
                resolution /= 2;
                System.out.println(String.format("Resolution set to %s.", resolution));
            }
            else{
                throw new ResolutionBoundariesIOException();
            }
        }else{
            throw new ResolutionIOException();
        }
    }
    private void handleAdd(String userInput) throws AddIOException{
        if((userInput.substring("add".length())).isEmpty()){
            throw new AddIOException();
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
            throw new AddIOException();
        }
    }
    private void handleRemove(String userInput) throws RemoveIOException{
        if((userInput.substring("remove".length())).isEmpty()){
            throw new RemoveIOException();
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
            throw new RemoveIOException();
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
