/*
Book Cipher encrypter / decrypter. 
Takes a plain text file as input and encrypts it with Book Cipher 
using another text file (Book). Decrypts encrypted text files.
Supports custom encryption keys.
*/


import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.ArrayList;



class Main {
  public static void main(String[] args) {

    //decrypt("encrypted.txt", "book.txt");
    //encrypt("plaintext.txt", "book.txt");

    GUI.launch(GUI.class, args);

  }




  public static List<String> readTextFile(String filePath) throws IOException {
      Path path = Paths.get(filePath);
      return Files.readAllLines(path, StandardCharsets.UTF_8);
  }
  
  public static String decrypt(String encryptedFile, String bookFile) {
    StringBuilder textBuilder = new StringBuilder();

    try { 
      List<String> encryptedLines = readTextFile(encryptedFile); // read encrypted file and store by line
      List<String> bookLines = readTextFile(bookFile); // read book file and store by line


    String message = "";

    textBuilder.append("Original message:\n\n");
    for (String encryptedLine : encryptedLines){
      textBuilder.append(encryptedLine).append("\n");
    }
    textBuilder.append("------------------------\n");
      
    textBuilder.append("Decrypted message:\n\n");
      for (String encryptedLine : encryptedLines){

        if (encryptedLine.strip().equals("")){ // if line is empty, print message and clear message to prepare to read the next line
          textBuilder.append(message).append("\n"); 
          message = ""; 
          continue;
        }
        else if (encryptedLine.strip().equals("[word not in book]")){
          message += "xxxxx ";
          continue;
        }
        else{
          try{
          String[] encryptedLineArray = encryptedLine.split(" ");
          
          int line = -1; //index of line in book 
          int word = -1; //index of word in book

          if (encryptedLineArray.length >= 2 && !encryptedLineArray[1].isEmpty()) {
            line = Integer.parseInt(encryptedLineArray[1]); //line index = first number in encrypted line
          }

          if (encryptedLineArray.length >= 3 && !encryptedLineArray[2].isEmpty()) {
            word = Integer.parseInt(encryptedLineArray[2]); //line index = first number in encrypted line
          }

          String[] bookLinesArray = bookLines.toArray(new String[0]);

          if (line >= 1 && line <= bookLinesArray.length) { // Ensure line index is within bounds
              String bookLine = bookLinesArray[line - 1];

              
              String[] bookLineArray = bookLine.split(" "); // Split the book line into words

              
              if (word >= 1 && word <= bookLineArray.length) { // Ensure word index is within bounds
                  message += bookLineArray[word - 1] + " ";
                  message = message.replaceAll("\\p{Punct}", "").toLowerCase();
              } 
              else {
                  textBuilder.append("Invalid word index: " + word).append("\n");
              }
          } 
          else {
              textBuilder.append("Invalid line index: " + line).append("\n");
          }
          }
          catch (NumberFormatException e) {
              // Handle the case where encryptedLine is not an integer
            //System.out.println("catch");
              continue;
          }
              
        }
      } 
    textBuilder.append(message + "\n------------------------\n"); //prints final line of message
    }
      
    catch (IOException e) {
        e.printStackTrace();
    }
    catch (NumberFormatException e) {
    // Handle NumberFormatException
    e.printStackTrace();
    }
  return textBuilder.toString();
  
  }

  public static String encrypt(String textFile, String bookFile){
    StringBuilder textBuilder = new StringBuilder();

    try { 
      List<String> textLines = readTextFile(textFile); // read text file and store by line
      List<String> bookLines = readTextFile(bookFile); // read book file and store by line
    
    ArrayList<String> wordList = new ArrayList<String>();
    ArrayList<String> encryption = new ArrayList<String>();
    Dictionary<String, String> foundWords = new Hashtable<String, String>();

    int l = 0;
    int w = 0;
      
    for (String textLine : textLines){
      if (textLine.strip().equals("")){
        wordList.add("-emptyLine-");
      }
      else{
        String[] textLineArray = textLine.split(" ");
        for (String textWord : textLineArray){
          textWord= textWord.replaceAll("\\p{Punct}", "").toLowerCase();
          wordList.add(textWord);
        }
      }
    }
    for (String word : wordList){
      if (foundWords.get(word) != null){/////
        encryption.add(foundWords.get(word));
        continue;
          }
      l = 0;
      if (word.equals("-emptyLine-")){
        encryption.add("");
      }

      else{
        for (String bookLine : bookLines){
          if (foundWords.get(word) != null){
            break;
          }
          l = l + 1;
          w = 0;
          String[] bookLineArray = bookLine.split(" ");

          for (String bookWord : bookLineArray){
            w = w + 1;
            bookWord = bookWord.replaceAll("\\p{Punct}", "").toLowerCase();

            if (word .equals(bookWord)){
              
              encryption.add(Integer.toString(l) + " " + Integer.toString(w));
              foundWords.put(word, Integer.toString(l) + " " + Integer.toString(w));
              break;
            }
          }
        }
        if (foundWords.get(word) == null){
          encryption.add("[word not in book]");
        }
      }
    }  

    textBuilder.append("Original message:\n\n");
    for (String textLine : textLines){
      textBuilder.append(textLine).append("\n");
    }
    textBuilder.append("\n------------------------\n");
    
    textBuilder.append("Encrypted message:\n\n");
    for (String encrypted : encryption){
        textBuilder.append(encrypted).append("\n");
    }
    textBuilder.append("\n------------------------\n");

    
  }

    catch (IOException e) {
      e.printStackTrace();
    }
    catch (NumberFormatException e) {
      // Handle NumberFormatException
      e.printStackTrace();
    }

  
  return textBuilder.toString();
  }
}
