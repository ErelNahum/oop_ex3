package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class Shell {
    private int resolution;
    private char[] charset;
    private final int DEFUALT_RESOLUTION = 128;
    private int minResolution;
    private int maxResolution;
    private Image image;
    private boolean outputToConsole = true;
    private HtmlAsciiOutput htmlAsciiOutput;
    private ConsoleAsciiOutput consoleAsciiOutput;
    public void run(){
        // Set the image
        try {
            image = setImage("cat.jpeg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Change this to your desired resolution
        charset = "7890123456".toCharArray();

        // Initialize the outputs
        htmlAsciiOutput = new HtmlAsciiOutput("output.html", "Courier New");
        consoleAsciiOutput = new ConsoleAsciiOutput();

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
        Image newImage;
        newImage = new Image(fileName);

        // Set resolution and charset
        resolution = DEFUALT_RESOLUTION;
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
        KeyboardInput keyboardInput = KeyboardInput.getObject();
        System.out.print(">>> ");
        String userInput = KeyboardInput.readLine();
        return userInput;
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
        if(charset.length == 0){
            System.out.println("Did not execute. Charset is empty.");
            return;
        }
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, charset);

        // Run the algorithm
        char[][] asciiArtResult = asciiArtAlgorithm.run();

        if(outputToConsole){
            consoleAsciiOutput.out(asciiArtResult);
        }else{
            htmlAsciiOutput.out(asciiArtResult);
        }
    }
    private void handleOutput(String userInput){
        if(userInput.equals("output html")){
            outputToConsole = false;
        }else if(userInput.equals("output console")){
            outputToConsole = true;
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
            Image newImage = setImage(fileName);
            image = newImage;
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
        userInput = userInput.substring("add ".length());
        if(userInput.equals("all")){
            addAllChars();
        }else if(userInput.equals("space")){
            charset = addCharToCharset(charset, ' ');
        } else if(userInput.length() == 1){
            charset = addCharToCharset(charset,userInput.charAt(0));
        }else if(userInput.length() == 3 && userInput.charAt(1) == '-'){
            int first = (int)userInput.charAt(0);
            int second = (int)userInput.charAt(2);
            if(first < second){
                for (int i = first; i <= second; i++) {
                    charset = addCharToCharset(charset, (char)i);
                }
            }else{
                for (int i = second; i <= first; i++) {
                    charset = addCharToCharset(charset, (char)i);
                }
            }
        }else{
            System.out.println("Did not add due to incorrect format.");
        }

        charset = removeDuplicates(charset);
    }
    private void handleRemove(String userInput){
        if((userInput.substring("remove".length())).isEmpty()){
            System.out.println("Did not remove due to incorrect format.");
            return;
        }
        userInput = userInput.substring("remove ".length());
        if(userInput.equals("all")){
            removeAllChars();
        }else if(userInput.equals("space")){
            charset = removeCharFromCharset(charset, ' ');
        } else if(userInput.length() == 1){
            charset = removeCharFromCharset(charset,userInput.charAt(0));
        }else if(userInput.length() == 3 && userInput.charAt(1) == '-'){
            int first = (int)userInput.charAt(0);
            int second = (int)userInput.charAt(2);
            if(first < second){
                for (int i = first; i <= second; i++) {
                    charset = removeCharFromCharset(charset, (char)i);
                }
            }else{
                for (int i = second; i <= first; i++) {
                    charset = removeCharFromCharset(charset, (char)i);
                }
            }
        }else{
            System.out.println("Did not remove due to incorrect format.");
        }
    }
    private void printCharSet(){
        // Sorting the char array
        Arrays.sort(charset);

        // Printing the sorted char array with spaces
        for (char c : charset) {
            System.out.print(c + " ");
        }
        System.out.println();
    }

    private char[] addCharToCharset(char[] charset, char newChar) {
        // Create a new array with increased size
        char[] newCharset = Arrays.copyOf(charset, charset.length + 1);

        // Add the new char to the end of the array
        newCharset[newCharset.length - 1] = newChar;

        return newCharset;
    }

    private void addAllChars(){
        for (int i = 32; i <= 126; i++) {
            char newChar = (char) i;
            charset = addCharToCharset(charset, newChar);
        }
    }
    private void removeAllChars(){
        for (int i = 32; i <= 126; i++) {
            char newChar = (char) i;
            charset = removeCharFromCharset(charset, newChar);
        }
    }
    private char[] removeDuplicates(char[] charArray) {
        // Convert char array to Set to automatically remove duplicates
        LinkedHashSet<Character> charSet = new LinkedHashSet<>();
        for (char c : charArray) {
            charSet.add(c);
        }

        // Convert Set back to char array
        char[] uniqueChars = new char[charSet.size()];
        int index = 0;
        for (char c : charSet) {
            uniqueChars[index++] = c;
        }

        return uniqueChars;
    }

    private char[] removeCharFromCharset(char[] charset, char charToRemove) {
        if(charset.length == 0){
            return new char[0];
        }
        boolean appeared = false;
        for (int i = 0; i < charset.length; i++) {
            if(charset[i] == charToRemove){
                appeared = true;
            }
        }
        if(!appeared){
            return charset;
        }
        int j = 0;
        char[] newCharSet = new char[charset.length-1];
        for (int i = 0; i < charset.length; i++) {
            if(charset[i] == charToRemove){
                continue;
            }
            newCharSet[j] = charset[i];
            j++;
        }
        return newCharSet;
    }

}
