package SP4;


import SP3.User;
import SP4.utlity.FileIO;
import SP4.utlity.TextUI;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Lucas & Mikkel
 * SecuritySystem is the main logic of the whole program.
 * It handles input from user logins and distributes it to rules class
 * And stores different login entry and treats in their respetive CSV file
 */

public class SecuritySystem {

    //variables
    private int deleteLimit = 10; // used to make the limit for how many lines a user is allowed to delete in csv file
    private int loginLimit = 3; // used to make a limit for how many times a user is allowed to try to login
   private ArrayList<LogEntry> logEntries = new ArrayList<>();
   private ArrayList<Threat> threats = new ArrayList<>();
   private ArrayList<String> fileLines = new ArrayList<>();

   //Method call's from other classes
    FileIO IO = new FileIO();
    TextUI UI = new TextUI();
    private Rules rule;
    public SecuritySystem() {
        this.rule = new Rules(this);
    }

    /**
     * @author Lucas & Mikkel
     * Method adds Logins to a CSV-file to monitor which user has locked in and when
     * @param user
     * @param timestamp
     */
    public void addLogEntry(User user, LocalDateTime timestamp){
        LogEntry LE = new LogEntry(user.getUsername(), timestamp);
        logEntries.add(LE);

        // Prepares information
        ArrayList<String> establish = new ArrayList<>();
        for (LogEntry le : logEntries) {
            establish.add(le.getUsername() + ";" + le.getTimestamp());
        }

        // Saves information to CSV
        String path = "SP4-ICE IDS Light/LogData/LogEntry.csv";
        String header = "username;timestamp";
        IO.saveData(establish, path, header);
    }


    /**
     * @author Lucas & Mikkel
     * Method stores threads in a CSV-file
     * @param type
     * @param username
     * @param severity
     * @param timestamp
     * @param description
     */
    public void addThreat(String type, String username, String severity, LocalDateTime timestamp, String description){
        Threat TT = new Threat(type, username, severity, timestamp, description);
        threats.add(TT);

        // Prepares information
        ArrayList<String> establish = new ArrayList<>();
        for (Threat tt : threats) {
            establish.add(tt.getType() + ";" + tt.getUsername() + ";" +tt.getSeverity() + ";" +tt.getTimestamp() + ";" +tt.getDescription());
        }

        // Saves information to CSV
        String path = "SP4-ICE IDS Light/LogData/Threat.csv";
        String header = "type;username;severity;timestamp;description";
        IO.saveData(establish, path, header);
    }


    /**
     * @author Lucas
     * This method detects if a bruteforce occurs, if a user tries to login too many times and fails
     * it calls bruteforceExeute() in rule class
     * Read more about execute methods in rule class
     * @param user
     */
    public void bruteForce(User user) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        if (loginLimit < user.getFailedAttempts()) {
            rule.bruteForceExecute(user);
        }
    }


    /**
     *  @author Lucas & Mikkel
     * Method that monitors if a chosen amount of lines from a file has been deleted
     * If delete limit has been passed it calls excessiveDeletionExecute() method in rule class
     * Read more about execute methods in rule class
     * @param user
     * @param path_backup
     * @param path_dynamic
     */
    public void excessiveDeletion(User user, String path_backup, String path_dynamic) {

        ArrayList<String> backup = IO.readData(path_backup);


        ArrayList<String> dynamic = IO.readData(path_dynamic);
        Set<String> dynamicSet = new HashSet<>(dynamic);

        ArrayList<String> deletedLines = new ArrayList<>();
        for (String line : backup){
            if(!dynamicSet.contains(line)) {
                deletedLines.add(line);
            }
        }

        if (user.getDeletedLines() >= deleteLimit){
            rule.excessiveDeletionExecute(user);
        }

        /*
        //Jökull
         user.setFilesDeleted(user.getFilesDeleted() + 1);
         final int limit = 15;
         if (limit < user.getFilesDeleted()) {
            Threat FD = new Threat("Files deleted", logEntries,"Severe", timestamp, "Excessive file deletion has been detected");
            threats.add(FD);
            rule.excessiveDeletionExecute(user);
        }
         */
    }

    /**
     * @author Mikkel
     * This method detects if an of an off hour login occurs and calls offHoursLoginExecute() in rule class
     * Read more about execute methods in rule class
     * @param user
     * @param timestamp
     */

    public void offHoursLogin(User user, LocalDateTime timestamp) {
        LocalTime earliestHour = LocalTime.parse("08:00");
        LocalTime latestHour = LocalTime.parse("16:00");
        if (timestamp.toLocalTime().isBefore(earliestHour) || timestamp.toLocalTime().isAfter(latestHour)) {
            rule.offHoursLoginExecute(user);
        }
    }

    /**
     * @author Mikkel
     * Method prompts user if they want to delete a line in a csv file.
     * After deleting a line, the user can type y to delete another, if the delete limit hasn't been passed
     * If user types n instead, they will return to main menu in SP3 StreamingService
     * excessiveDeletion() is called at the end
     * Read more about execute methods in rule class
     * @param user
     */
    public void deleteLineInFile(User user){
        String path = UI.promptText("Type the name of the file, or type 'back' to cancel");
        if (path.equalsIgnoreCase("back") == false) {
            fileLines = IO.readData(path);
            if (!fileLines.isEmpty()) {
                boolean continueLoop = true;
                while (continueLoop && user.getDeletedLines() < deleteLimit) {
                    int choice = UI.promptNumeric("Select line to delete");
                    fileLines.remove(choice);
                    user.setDeletedLines(user.getDeletedLines() + 1);
                    continueLoop = UI.promptBinary("Delete another line? Y/N");
                }
                IO.saveData(fileLines, path);
                excessiveDeletion(user, "SP4-ICE IDS Light/CSVDataTest/MoviesBackup.csv", path);
            }
        }
    }

    /**
     * @author Lucas & Mikkel
     * This method loads CSV entry data
     */
    public void loadLogEntry() {
        logEntries.clear();

        // Load LogEntry
        List<String> logEntryLines = IO.readData("SP4-ICE IDS Light/LogData/LogEntry.csv");
        for (String line : logEntryLines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            String username = parts[0].trim();
            LocalDateTime timestamp = LocalDateTime.parse(parts[1].trim());
            logEntries.add(new LogEntry(username, timestamp));
        }
    }


    /**
     * @author Lucas & Mikkel
     * This method loads CSV threat data
     */
    public void loadThreat() {
        threats.clear();
        // Load Threat
        List<String> threatLines = IO.readData("SP4-ICE IDS Light/LogData/Threat.csv");

        for (String line : threatLines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            String type = parts[0].trim();
            String username = parts[1].trim();
            String severity = parts[2].trim();
            LocalDateTime timestamp = LocalDateTime.parse(parts[3].trim());
            String description = parts[4].trim();

            threats.add(new Threat(type, username, severity, timestamp, description));
        }
    }

//Getter
    public ArrayList<LogEntry> getLogEntries() {
        return logEntries;
    }
    public ArrayList<Threat> getThreats() {
        return threats;
    }
}