package SP4;
import java.time.LocalDateTime;

/**
 * Class to be able to describe logins
 */
public class LogEntry {
    private LocalDateTime timestamp;
    private String username;

    /**
     * Constructor to create login entries
     * @param username
     * @param timestamp
     */
    public LogEntry(String username, LocalDateTime timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    @Override
    public String toString() {
        return "LogEntry {" +
                "\n  Username  : " + username +
                "\n  Timestamp : " + timestamp +
                "\n}";
    }
}