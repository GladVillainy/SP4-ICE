package SP4;
import java.time.LocalDateTime;

/**
 * Class to be able to create logins
 */
public class LogEntry {
    private LocalDateTime timestamp;
    private String username;

    /**
     * Constructor to create login entrys
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
}