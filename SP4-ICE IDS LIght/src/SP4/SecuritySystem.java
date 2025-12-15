package SP4;


import SP3.User;
import SP4.utlity.FileIO;
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

    Rules rule = new Rules();
    FileIO IO = new FileIO();


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
        String path = "SP4/LogData/LogEntry.csv";
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
        String path = "SP4/LogData/Threat.csv";
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

        int limit = 10; //Can be edited to fit the need of the admin
        if (deletedLines.size() > limit){
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
}