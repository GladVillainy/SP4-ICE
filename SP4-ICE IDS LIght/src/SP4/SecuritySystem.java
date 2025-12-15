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
 *
 * SecuritySystem is the main logic of the whole program.
 * It handles input from user logins and distributes it to rules class
 * And stores different login entry and treats in their respetive CSV file
 */

public class SecuritySystem {
   private ArrayList<LogEntry> logEntries = new ArrayList<>();
   private ArrayList<Threat> threats = new ArrayList<>();
   private ArrayList<String> fileLines = new ArrayList<>();

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
        String path = "SP4-ICE IDS LIght/LogData/LogEntry.csv";
        String header = "username;timestamp";
        IO.saveData(establish, path, header);
    }


    /**
     * @author Lucas & Mikkel
     * Method stores threads in a CSV-file
     * @param type
     * @param relatedEntries
     * @param severity
     * @param timestamp
     * @param description
     */
    public void addThreat(String type, ArrayList<LogEntry> relatedEntries, String severity, LocalDateTime timestamp, String description){
        Threat TT = new Threat(type, relatedEntries, severity, timestamp, description);
        threats.add(TT);

        // Prepares information
        ArrayList<String> establish = new ArrayList<>();
        for (Threat tt : threats) {
            establish.add(tt.getType() + ";" + tt.getRelatedEntries() + ";" +tt.getSeverity() + ";" +tt.getTimestamp() + ";" +tt.getDescription());
        }

        // Saves information to CSV
        String path = "SP4-ICE IDS LIght/LogData/Threat.csv";
        String header = "type;logEntryUsername;logEntryTimestamp;severity;timestamp;description";
        IO.saveData(establish, path, header);
    }


    /**
     * @author Lucas
     * This method detects if a bruteforce occurs and calls bruteforceExeute in rule class
     * Read more about execute methods in rule class
     * @param user
     * @param timestamp
     */
    public void bruteForce(User user, LocalDateTime timestamp) {
        user.setFailedAttempts(user.getFailedAttempts() + 1);
        int limit = 1;
        if (limit < user.getFailedAttempts()) {
            Threat BF = new Threat("Bruteforce", logEntries,"Moderate", timestamp, "Forcing entry has been detected");
            threats.add(BF);
            rule.bruteForceExecute(user);
        }
    }

    /**
     *  @author Lucas & Mikkel
     * Method that monitors if a chosen amount of data from a file has been deleted
     * If limit has been reached it calls excessiveDeletionExecute method in rule class
     * Read more about execute methods in rule class
     * @param user
     * @param timestamp
     * @param path_backup
     * @param path_dynamic
     */
    public void excessiveDeletion(User user, LocalDateTime timestamp, String path_backup, String path_dynamic) {
      //  String path_backup = "SP4/CSVDataTest/MoviesBackup";
        // String path_dynamic = "SP4/CSVDataTest/MoviesDynamic";

        ArrayList<String> backup = IO.readData(path_backup);


        ArrayList<String> dynamic = IO.readData(path_dynamic);
        Set<String> dynamicSet = new HashSet<>(dynamic);

        ArrayList<String> deletedLines = new ArrayList<>();
        for (String line : backup){
            if(!dynamicSet.contains(line)) {
                deletedLines.add(line);
            }
        }

        int limit = 2; //Can be edited to fit the need of the admin
        if (user.getDeletedLines() > limit){
            Threat ED = new Threat("Excessive deletion", logEntries,"Severe", timestamp, "Excessive file deletion has been detected");
            threats.add(ED);
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
     * This method detects if an of an off hour login occurs and calls offHoursLoginExecute in rule class
     * Read more about execute methods in rule class
     * @param user
     * @param timestamp
     */

    public void offHoursLogin(User user, LocalDateTime timestamp) {
        LocalTime earliestHour = LocalTime.parse("08:00");
        LocalTime latestHour = LocalTime.parse("16:00");
        if (timestamp.toLocalTime().isBefore(earliestHour) || timestamp.toLocalTime().isAfter(latestHour)) {
            Threat OHL = new Threat("Off hours login", logEntries,"Mild", timestamp, "Unusual login time has been detected");
            threats.add(OHL);
            rule.offHoursLoginExecute(user);
        }
    }

    public void deleteLineInFile(User user){
        String path = UI.promptText("Type the name of the file, or type 'back' to cancel");
        if (path.equalsIgnoreCase("back") == false) {
            fileLines = IO.readData(path);
            if (!fileLines.isEmpty()) {
                boolean continueLoop = true;
                while (continueLoop) {
                    int choice = UI.promptNumeric("Select line to delete");
                    fileLines.remove(choice);
                    user.setDeletedLines(user.getDeletedLines() + 1);
                    continueLoop = UI.promptBinary("Delete another line? Y/N");
                }
                IO.saveData(fileLines, path);
                excessiveDeletion(user, LocalDateTime.now(), "SP4-ICE IDS LIght/CSVDataTest/MoviesBackup.csv", path);
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
        List<String> logEntryLines = IO.readData("LogEntry.csv");
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
        List<String> threatLines = IO.readData("Threat.csv");
        ArrayList<LogEntry> relatedEntries = new ArrayList<>();

        for (String line : threatLines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            String type = parts[0].trim();
            String relatedEntryUsername = parts[1].trim();
            LocalDateTime relatedEntryTimeStamp = LocalDateTime.parse(parts[2].trim());
            String severity = parts[3].trim();
            LocalDateTime timestamp = LocalDateTime.parse(parts[4].trim());
            String description = parts[5].trim();

            LogEntry relatedEntry = new LogEntry(relatedEntryUsername, relatedEntryTimeStamp);
            relatedEntries.add(relatedEntry);

            threats.add(new Threat(type, relatedEntries, severity, timestamp, description));
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