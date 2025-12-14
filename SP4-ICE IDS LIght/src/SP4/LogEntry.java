package SP4;
import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String username;

    public LogEntry(String username, LocalDateTime timestamp) {
        this.username = username;
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}