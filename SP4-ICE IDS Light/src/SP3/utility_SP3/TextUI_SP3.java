package SP3.utility_SP3;

import java.util.ArrayList;
import java.util.Scanner;

public class TextUI_SP3 {


    private static Scanner sc = new Scanner(System.in);

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

    public void displayList(ArrayList<String>list, String msg) {
        for (int i = 0; i < list.size();i++) {
            System.out.println(i+1+". "+list.get(i));
        }
    }

    public void displayMsg(String msg){
        System.out.println(msg);

    }

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

    // Displays a numbered menu and prompts the user to select one of the options.
    // Menu options are always numbered starting from 1 and up.
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
