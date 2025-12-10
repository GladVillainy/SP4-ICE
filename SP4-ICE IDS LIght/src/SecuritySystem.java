import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SecuritySystem {
   private ArrayList<LogEntry> logEntries = new ArrayList<>();
   private  ArrayList<Threat> threats;

    Rules RS = new Rules();


    public void addLogEntry(String name, LocalDateTime timestamp){
        LogEntry LE = new LogEntry(name, timestamp);
        logEntries.add(LE);
    }

    public ArrayList<LogEntry> getLogEntries() {
        return logEntries;
    }

    public ArrayList<Threat> getThreats() {
        return threats;
    }

    public boolean bruteForce(int attempts) {
        int limit = 3;
        if (limit < attempts) {
            RS.bruteForceExecute();
            return true;
        } else {
            return false;
        }
    }

    public boolean excessiveDeletion() {
     /*   final int limit = 15;
        if (limit < filesDeleted) {
            RS.excessiveDeletionExecute();
            return true;
        } else {
            return false;
        }


      */

        return true;
    }

    public boolean offHoursLogin(LocalDateTime timestamp) {
        LocalTime earliestHour = LocalTime.parse("08:00");
        LocalTime latestHour = LocalTime.parse("16:00");
        if (timestamp.toLocalTime().isBefore(earliestHour) || timestamp.toLocalTime().isAfter(latestHour)) {
            return true;
        } else {
            return false;
        }
    }
}