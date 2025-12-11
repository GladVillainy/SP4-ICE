package SP3;

import SP3.utility_SP3.FileIO_SP3;
import SP3.utility_SP3.TextUI_SP3;

import java.util.ArrayList;
import java.util.List;


public class StreamingService {
    private ArrayList<Movie> movies = new ArrayList<>();
    private ArrayList<Series> series = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private User currentUser = null;
    private final List<Media> mediaLibrary = new ArrayList<>();


    TextUI_SP3 ui = new TextUI_SP3();
    FileIO_SP3 IO = new FileIO_SP3();

    public void start()
    {
        loadMedia();
        loadUsers();
        loadUserMedia();
        startMenu();
        mainMenu();
    }

    private void loadUsers() {
        List<String> lines = IO.readData("Data/userLogin.csv");
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
        List<String> movieLines = IO.readData("Data/movies.csv");
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
        List<String> seriesLines = IO.readData("Data/series.csv");
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
        List<String> seenLines = IO.readData("Data/userSeen.csv");
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
        List<String> savedLines = IO.readData("Data/userSaved.csv");
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
        IO.saveData(seenData, "Data/userSeen.csv", null);   // header already included
        IO.saveData(savedData, "Data/userSaved.csv", null);
    }



    private boolean validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
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
        String path = "Data/userLogin.csv";
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
                continueLoop = false;
            } else if (choice == 2) {
                User user = logIn();
                if (user != null) {// Successful login
                    if (user.getLocked() == false) {
                        currentUser = user;
                        continueLoop = false; // proceed to main menu
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


        while (true) {
            // Prompt user for input using TextUI
            int choice = ui.promptMenu("Main Menu", menuOptions);

            // Handle the menu choice
            switch (choice) {
                case 1:
                    searchByName();
                    break;
                case 2:
                    searchByCategory();
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

            if (validateUser(username, password)) {
                for (User u : users) {
                    if (u.getUsername().equals(username)) {
                        foundUser = u;
                        break;
                    }
                }

                ui.displayMsg("Logged in as " + username);
                loggedIn = true;
            } else {
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