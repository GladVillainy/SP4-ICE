package utlity;/*
*  Utility class to persist the Strings in an ArrayList
*  To use, each object in a list must first be serialized (field values of the object are added to the same String, separated by comma)
*
*  TODO: To avoid leakage, both scanner and FileWriter must be closed, suggestion to use try-with-ressources in both methods
*  TODO: catch exception if the file is found but empty
* */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
//JavaDoc is made by both ChatGPT and Lucas

/**
 * FileIO is a file handling utility
 * @author Peter
 */
public class FileIO {
/**
* Saves data to a file
 * If there is a problem writing to the file, this method prints
 * an error message and does not throw the exception.
 * @param list a list of strings to save
 * @param path The path to the file
 * @param header Title of the file
 */
    public void saveData(ArrayList<String> list, String path, String header){
        try {

            FileWriter writer = new FileWriter(path);
            writer.write(header+"\n");
            for (String s : list) {
                writer.write(s+"\n");
            }
            writer.close();

        }catch (IOException e) {
            System.out.println("problem: "+ e.getMessage());
        }
    }

    /**
     * Reads all data and puts it in an Array
     * @param path the path to the file
     * @throws FileNotFoundException If the method can't find the file
     * @return ArrayList of data
     */
    public ArrayList<String> readData(String path) {
        ArrayList<String> data = new ArrayList<>();
        File file = new File(path);
        try {
            Scanner scan = new Scanner(file);
            scan.nextLine();

            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                data.add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Filen findes ikke");
        }
        return data;
    }

    /**
     * Reads an amout of data
     * @param path The path to the file
     * @param length Lenght of the array of data you want
     * @return returns the arraylist with the suggested length
     * @throws FileNotFoundException if unable to find the file
     */

    public String[] readData(String path, int length){
        String[] data = new String [length];
        File file = new File(path);

        try {
            Scanner scan = new Scanner(file);
            scan.nextLine();

            for(int i = 0; i < data.length; i++){
                String line = scan.nextLine();
                data[i] = line;
            }

        } catch (FileNotFoundException e) {
            System.out.println("Filen findes ikke");
        }
        return data;
    }
}
