package SP4.utlity;

import java.util.ArrayList;
import java.util.Scanner;

//JavaDoc is made by both ChatGPT and Lucas

/**
 * TextUI is made to be able to prompt, ask etc the user.
 * @author Peter
 */
public class TextUI {
    private static Scanner sc = new Scanner(System.in);

    /**
     * Shows a list of options and lets the user choose several of them.
     * The user types numbers (1, 2, 3, ...) to pick options.
     * The method keeps asking until the user has chosen the given number of items.
     * @param options a list of options to choose from (each item is shown with a number)
     * @param limit   how many choices the user must make (for example: 2 or 3)
     * @param msg     a message shown to the user before asking for input
     * @return a list with the options that the user selected
     */
    public ArrayList<String> promptChoice( ArrayList<String> options, int limit, String msg){
        displayMsg(msg);
        displayList(options, "");
        ArrayList<String> choices = new ArrayList<>();  //Lave en beholder til at gemme brugerens valg

        while(choices.size() < limit){             //tjekke om brugeren skal vælge igen

            int choice = promptNumeric(msg);
            choices.add(options.get(choice-1));
        }
        return choices;
    }

    /**
     * Displays a numbered list of text lines in the console.
     * Each item is shown with a number starting from 1.
     * @param list the list of strings to show
     * @param msg  (not used right now) could be a message shown before the list
     */
    public void displayList(ArrayList<String>list, String msg) {
        for (int i = 0; i < list.size();i++) {
            System.out.println(i+1+". "+list.get(i));
        }
    }

    /**
     * Printes out a message
     * @param msg the message you want to print
     */
    public void displayMsg(String msg){
        System.out.println(msg);
    }

    /**
     * Asks the user to type a number in the console.
     * The method keeps asking until the user types a valid, non-negative integer (0 or higher)
     * If the user types something that is not a number (for example letters),
     * the method catches the error, shows an error message, and asks again.
     * @param msg the message shown to the user before they type a number
     * @return the number typed by the user (0 or higher)
     */

    public int promptNumeric(String msg){
        int num = -1;

        while (num < 0)
        {
            System.out.print(msg + " ");
            String input = sc.nextLine();

            try
            {
                // Try converting the input to an integer
                num = Integer.parseInt(input);

                // Check if the number is non-negative
                if (num < 0)
                {
                    System.err.println("Please enter a non-negative number.");
                }
            }
            catch (NumberFormatException e)
            {
                // If input wasn't a valid number
                System.err.println("Invalid input, enter a valid number.");
            }
        }

        return num; // Return the valid number
    }

    public String promptText(String msg){
        displayMsg(msg);         //Stille brugeren et spørgsmål
        String input = sc.nextLine();          //Give brugere et sted at placere sit svar og vente på svaret

        return input;
    }

    public boolean promptBinary(String msg){
        displayMsg(msg);
        String input = sc.nextLine();
        if(input.equalsIgnoreCase("Y")){
            return true;
        }
        else if(input.equalsIgnoreCase("N")){
            return false;
        }
        else{
            return promptBinary(msg);
        }
    }
    /**
     * Shows a simple numbered menu in the console and lets the user choose one option.
     * The menu options are printed with numbers starting from 1.
     * The method keeps asking until the user enters a valid choice.
     * If the user types something that is not a number (for example letters),
     * the method catches the error, shows an error message, and asks again.
     * @param menuOptions a list of menu options to show to the user
     * @return the number of the chosen option (1 = first option, 2 = second option, ...)
     */
    public int promptMenuOptions(ArrayList<String> menuOptions)
    {
        int choice = -1; // Initialize with an invalid value to ensure the loop runs at least once

        // Repeat until the user enters a valid choice
        while (choice == -1)
        {
            // Print the menu options
            for (int i = 0; i < menuOptions.size(); i++)
            {
                System.out.println((i + 1) + ") " + menuOptions.get(i));
            }

            // Ask the user for input
            System.out.print("Enter choice: ");
            String input = sc.nextLine(); // Read input as string to safely handle invalid inputs

            try
            {
                // Try converting the input to an integer
                choice = Integer.parseInt(input);

                // Validate the input
                if (choice >= 1 && choice <= menuOptions.size())
                {
                    return choice; // Valid choice; return it
                }

                // If we reach here, the number was outside the valid range
                System.err.println("Invalid choice, try again.");
                choice = -1; // Reset choice to continue looping
            }
            catch (NumberFormatException e)
            {
                // Handle the case where input wasn't a valid number (e.g. letters)
                System.err.println("Invalid input, enter a number.");
            }
        }

        return choice; // Technically unreachable, but required for compilation
    }

    // Displays a menu with a heading and lets the user make a selection.
    // Uses promptMenuOptions to handle user input.
    public int promptMenu(String menuHeading, ArrayList<String> menuOptions)
    {
        // Show the menu header
        System.out.println(menuHeading +
                """
                \n
                ------------
                """);

        return promptMenuOptions(menuOptions);
    }
}
