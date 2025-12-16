package SP3;

import SP3.utility_SP3.FileIO_SP3;
import SP3.utility_SP3.TextUI_SP3;
import SP4.Rules;
import SP4.SecuritySystem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class StreamingService {
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Series> series = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;
    private final List<Media> mediaLibrary = new ArrayList<>();


    TextUI_SP3 ui = new TextUI_SP3();
    FileIO_SP3 IO = new FileIO_SP3();

    //Added - Can use SecuritySystem methods in StreamingService
    private SecuritySystem securitySystem;

    public StreamingService(SecuritySystem securitySystem) {
        this.securitySystem = securitySystem;
    }


    public void start()
    {
        //Added - to be able to use our methods
        securitySystem.loadThreat();
        securitySystem.loadLogEntry();

        loadMedia();
        loadUsers();
        loadUserMedia();
        startMenu();
        mainMenu();

    }

    private void loadUsers() {
        List<String> lines = IO.readData("SP4-ICE IDS Light/data_SP3/userLogin.csv");
        for (String line : lines) {
            if (line.startsWith("username")) continue; // skip header
            String[] parts = line.split(";");
            if (parts.length == 2) {
                users.add(new User(parts[0], parts[1]));
            }
        }
    }

    private void loadMedia() {
        movies.clear();
        series.clear();
        mediaLibrary.clear();

        // Load Movies
        List<String> movieLines = IO.readData("SP4-ICE IDS Light/data_SP3/movies.csv");
        for (String line : movieLines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 4) {
                ui.displayMsg("Skipping malformed movie line: " + line);
                continue;
            }
            try {
                String name = parts[0].trim();
                int year = Integer.parseInt(parts[1].trim());
                String category = parts[2].trim();
                double rating = Double.parseDouble(parts[3].trim().replace(',', '.'));
                double length = 0;
                if (parts.length >= 5 && !parts[4].trim().isEmpty()) {
                    length = Double.parseDouble(parts[4].trim().replace(',', '.'));
                }

                movies.add(new Movie(name, year, rating, category, length));
            } catch (NumberFormatException e) {
                ui.displayMsg("Skipping invalid movie line: " + line);
            }
        }

        // Load Series
        List<String> seriesLines = IO.readData("SP4-ICE IDS Light/data_SP3/series.csv");
        for (String line : seriesLines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(";");
            if (parts.length < 5) {
                ui.displayMsg("Skipping malformed series line: " + line);
                continue;
            }

            try {
                String name = parts[0].trim();
                String yearRange = parts[1].trim();
                String category = parts[2].trim();
                double rating = Double.parseDouble(parts[3].trim().replace(',', '.'));

                String seasonsRaw = parts[4].trim();
                String[] seasonsSplit = seasonsRaw.split("[,;]");
                List<String> seasons = new ArrayList<>();
                for (String s : seasonsSplit) {
                    if (!s.trim().isEmpty()) seasons.add(s.trim());
                }

                series.add(new Series(name, yearRange, rating, category, seasons));
            } catch (NumberFormatException e) {
                ui.displayMsg("Skipping invalid series line: " + line);
            }
        }

        // Combine into mediaLibrary
        mediaLibrary.addAll(movies);
        mediaLibrary.addAll(series);

        ui.displayMsg("Loaded " + movies.size() + " movies and " + series.size() + " series into library.");
    }



    private void loadUserMedia() {
        // Load seen media
        List<String> seenLines = IO.readData("SP4-ICE IDS Light/data_SP3/userSeen.csv");
        for (String line : seenLines) {
            if (line.startsWith("username")) continue; // skip header
            String[] parts = line.split(";", 2); // username ; media1,media2,...
            if (parts.length < 2) continue;

            String username = parts[0].trim();
            String[] mediaNames = parts[1].split(",");

            // Find user
            User user = null;
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    user = u;
                    break;
                }
            }
            if (user == null) continue;

            // Add media to seen list
            for (String name : mediaNames) {
                for (Media m : mediaLibrary) {
                    if (m.getName().equals(name.trim())) {
                        user.getSeenMedia().add(m);
                        break;
                    }
                }
            }
        }

        // Load saved media
        List<String> savedLines = IO.readData("SP4-ICE IDS Light/data_SP3/userSaved.csv");
        for (String line : savedLines) {
            if (line.startsWith("username")) continue; // skip header
            String[] parts = line.split(";", 2);
            if (parts.length < 2) continue;

            String username = parts[0].trim();
            String[] mediaNames = parts[1].split(",");

            // Find user
            User user = null;
            for (User u : users) {
                if (u.getUsername().equals(username)) {
                    user = u;
                    break;
                }
            }
            if (user == null) continue;

            // Add media to wantsToSee list
            for (String name : mediaNames) {
                for (Media m : mediaLibrary) {
                    if (m.getName().equals(name.trim())) {
                        user.getWantsToSee().add(m);
                        break;
                    }
                }
            }
        }
    }



    private void saveUserMedia() {
        ArrayList<String> seenData = new ArrayList<>();
        ArrayList<String> savedData = new ArrayList<>();

        // Add CSV headers
        seenData.add("username;seenMedia");
        savedData.add("username;wantsToSee");

        for (User u : users) {
            // Seen media
            String seenLine = u.getUsername() + ";";
            for (Media m : u.getSeenMedia()) {
                seenLine += m.getName() + ",";
            }
            if (seenLine.endsWith(",")) {
                seenLine = seenLine.substring(0, seenLine.length() - 1); // remove trailing comma
            }
            seenData.add(seenLine);

            // Saved media
            String savedLine = u.getUsername() + ";";
            for (Media m : u.getWantsToSee()) {
                savedLine += m.getName() + ",";
            }
            if (savedLine.endsWith(",")) {
                savedLine = savedLine.substring(0, savedLine.length() - 1);
            }
            savedData.add(savedLine);
        }

        // Save files using FileIO
        IO.saveData(seenData, "SP4-ICE IDS Light/data_SP3/userSeen.csv", null);   // header already included
        IO.saveData(savedData, "SP4-ICE IDS Light/data_SP3/userSaved.csv", null);
    }



    private boolean validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    //Added - Used in userConfig() to find a user with a certain username
    private User usernameToUser (String username) {
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                return u;
            }
        }
        return null;
    }
    //Added - Allows the admin to remove a lock from a user
    private void userConfig() {
        String username = ui.promptText("Type the username of the user you want to config");
        User user = usernameToUser(username);
        try {
            user.setIsLocked(false);
            ui.displayMsg("User config successful");
        } catch (NullPointerException e) {
            ui.displayMsg("No user found");
        }
    }

    private void createNewUser() {
        // Prompt for username and password
        String username = ui.promptText("Create a Netflix login. \nPlease Type your Username");

        // Check if username already exists
        for (User u : users) {
            if (u.getUsername().equals(username)) {
                ui.displayMsg("Username already exists. Try another one.");
                return; // exits the method, user must try again
            }
        }

        String password = ui.promptText("Type your Password");

        // Create new User instance
        User newUser = new User(username, password);

        // Add new user to the list
        users.add(newUser);

        // Prepare data to save all users
        ArrayList<String> establish = new ArrayList<>();
        for (User u : users) {
            establish.add(u.getUsername() + ";" + u.getPassword());
        }

        // Save all users to CSV
        String path = "SP4-ICE IDS Light/data_SP3/userLogin.csv";
        String header = "username;password";
        IO.saveData(establish, path, header);

        ui.displayMsg("User created successfully!");

    }

    private void startMenu() {
        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add("Create new user");
        menuOptions.add("Log in");

        boolean continueLoop = true;
        while (continueLoop) {
            int choice = ui.promptMenu("Start menu", menuOptions);

            if (choice == 1) {
                createNewUser();
            } else if (choice == 2) {
                User user = logIn();
                if (user != null) {// Successful login

                    //Added - Blocks user if they log in with a locked account
                    if (user.getIsLocked() == false) {
                        currentUser = user;
                        continueLoop = false; // proceed to main menu
                        ui.displayMsg("Logged in as " + currentUser.getUsername());
                        securitySystem.addLogEntry(currentUser, LocalDateTime.now());
                    } else {
                        ui.displayMsg("User is locked, returning to start menu...");
                    }
                } else {
                    // User chose to go back
                    ui.displayMsg("Returning to start menu...");
                }
            } else {
                ui.displayMsg("Type a valid number");
            }
        }
    }

    private void mainMenu() {
        // Define menu options
        ArrayList<String> menuOptions = new ArrayList<>();
        menuOptions.add("Search for media by name");
        menuOptions.add("Search for media by category");
        menuOptions.add("Get list of your saved movies");
        menuOptions.add("Get list of movies you have already seen");
        menuOptions.add("Exit streaming service");

        // Added - Admin console
        if(currentUser.getIsAdmin() == true) {
            menuOptions.add("ADMIN: Show all threats");
            menuOptions.add("ADMIN: Show all log entries");
            menuOptions.add("ADMIN: Config user");
        }
        menuOptions.add("Delete line in a file");


        while (true) {
            // Prompt user for input using TextUI
            int choice = ui.promptMenu("Main Menu", menuOptions);
            //Added - Changed switch case to an if statement (below)
            // Handle the menu choice
            /*
            switch (choice) {
                case 1:
                    AN.showThreat();
                    // searchByName();
                    break;
                case 2:

                    //  searchByCategory();
                    break;

                case 3:
                    getListOfSaved();
                    break;
                case 4:
                    getListOfWatched();
                    break;
                case 5:
                    saveUserMedia();
                    // Exit the program safely
                    ui.displayMsg("Exiting streaming service.");
                    System.exit(0);
                default:
                    // If the input doesn't match a valid option
                    ui.displayMsg("Invalid choice, try again.");
            }
            */

            // Added - If statement as it was easier to work with in this case.
             // See above for the orginal design
            if (choice == 1) {
                searchByName();
            } else if (choice == 2) {
                searchByCategory();
            } else if (choice == 3) {
                getListOfSaved();
            } else if (choice == 4) {
                getListOfWatched();
            } else if (choice == 5) {
                saveUserMedia();
                // Exit the program safely
                ui.displayMsg("Exiting streaming service.");
                System.exit(0);

                //Added - if Admin you are able to have additional menu options
            } else if (choice == 6 && currentUser.getIsAdmin() == true) {
                currentUser.showThreat(securitySystem);
            } else if (choice == 7 && currentUser.getIsAdmin() == true) {
                currentUser.showLogEntry(securitySystem);
            } else if (choice == 8 && currentUser.getIsAdmin() == true) {
                userConfig();
            } else if ((choice == 6 && currentUser.getIsAdmin() == false) || (choice == 9 && currentUser.getIsAdmin() == true)) {
                securitySystem.deleteLineInFile(currentUser);
                if(currentUser.getIsLocked()) {
                    ui.displayMsg("Excessive deletion detected, returning to start menu...");
                    currentUser = null;
                    startMenu();
                }
                // System.out.println(currentUser.getIsLocked());
                // System.out.println(currentUser.getDeletedLines());
            } else {
                // If the input doesn't match a valid option
                ui.displayMsg("Invalid choice, try again.");
            }


        }
    }

    private void searchByName() {
        String searchFor = ui.promptText("Enter the name of the media to search for: ").toLowerCase();

        ArrayList<Media> foundMedia = new ArrayList<>();

        for (Movie m : movies) {
            if (m.getName().toLowerCase().contains(searchFor)) foundMedia.add(m);
        }

        for (Series s : series) {
            if (s.getName().toLowerCase().contains(searchFor)) foundMedia.add(s);
        }

        if (foundMedia.isEmpty()) {
            ui.displayMsg("No media found with that name.");
            return;
        }

        ArrayList<String> mediaNames = new ArrayList<>();
        for (Media media : foundMedia) mediaNames.add(media.getName());

        int choice = ui.promptMenu("Select a media from the results:", mediaNames);
        Media selected = foundMedia.get(choice - 1);

        ui.displayMsg("You selected: " + selected.getName());


        selected.playMedia(currentUser);
    }



    private void searchByCategory() {
        String categoryInput = ui.promptText("Enter a category to search for: ").toLowerCase();
        ArrayList<Media> results = new ArrayList<>();

        for (Media m : mediaLibrary) {
            if (m.getCategory().toLowerCase().contains(categoryInput)) {
                results.add(m);
            }
        }

        if (results.isEmpty()) {
            ui.displayMsg("No media found in that category.");
            return;
        }

        ArrayList<String> mediaNames = new ArrayList<>();
        for (Media m : results) mediaNames.add(m.getName());

        int choice = ui.promptMenu("Select a media from the results:", mediaNames);
        Media selected = results.get(choice - 1);

        ui.displayMsg("You selected: " + selected.getName());

        selected.playMedia(currentUser);
    }




    private void getListOfSaved() {

        ArrayList<Media> list = currentUser.getWantsToSee();
        if (list == null || list.isEmpty()) {
            ui.displayMsg("You have no saved media.");
            return;
        }

        ArrayList<String> mediaNames = new ArrayList<>();
        for(Media media : list) {
            mediaNames.add(media.getName());
        }
        int choice = ui.promptMenu("Select media", mediaNames);
        list.get(choice -1).playMedia(currentUser);
    }

    private void getListOfWatched() {
        ArrayList<Media> list = currentUser.getSeenMedia();
        if (list == null || list.isEmpty()) {
            ui.displayMsg("You have no watched media.");
            return;
        }

        ArrayList<String> mediaNames = new ArrayList<>();
        for (Media media : list) {
            mediaNames.add(media.getName());
        }
        int choice = ui.promptMenu("Select media", mediaNames);
        list.get(choice - 1).playMedia(currentUser);
    }


    private User logIn() {
        boolean loggedIn = false;
        User foundUser = null;

        while (!loggedIn) {
            String username = ui.promptText("Type your username (or type 'back' to return):");
            if (username.equalsIgnoreCase("back")) {
                return null; // Return null to indicate the user canceled
            }

            String password = ui.promptText("Type your password:");
            if (password.equalsIgnoreCase("back")) {
                return null;
            }

            //Added - To be able to get login date information
            if (validateUser(username, password)) {
                for (User u : users) {
                    if (u.getUsername().equals(username)) {
                        foundUser = u;

                        //Able to change time
                        securitySystem.offHoursLogin(u, LocalDateTime.now());
                        //For at teste off hour lås
                        // securitySystem.offHoursLogin(u, LocalDateTime.parse("2025-12-12T12:00:00"));
                        break;
                    }
                }
                loggedIn = true;
                //Added - calls bruteforce if username matches but password dosnt
            } else {
                for(User u : users) {
                    if (u.getUsername().equals(username)){
                        securitySystem.bruteForce(u);
                    }
                }
                ui.displayMsg("Invalid username or password. Try again, or type 'back' to return.");
            }
        }

        currentUser = foundUser;
        return currentUser;
    }



    // Getters and setters
    public List<Movie> getMovies() {
        return movies;
    }

    public List<Series> getSeries() {
        return series;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}