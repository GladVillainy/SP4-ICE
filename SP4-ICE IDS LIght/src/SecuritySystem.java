import java.time.LocalDateTime;
import java.util.List;

public class SecuritySystem {
    private List<LogEntry> logEntries;
    private List<Threat> threats;

    Rules RS = new Rules();

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
        int limit = 15;
        if (limit < filesDeleted) {
            RS.ex
            return true;
        } else {
            return false;
        }
        return true;
    }

    public boolean offHoursLogin() {
        LogEntry logEntry = new LogEntry();
        LocalTime earliestHour = LocalTime.parse("08:00");
        LocalTime latestHour = LocalTime.parse("16:00");
        if (logEntry.getTimestamp().toLocalTime().isBefore(earliestHour) || logEntry.getTimestamp().toLocalTime.isAfter(latestHour)) {
            return true;
        } else {
            return false;
        }
    }
}