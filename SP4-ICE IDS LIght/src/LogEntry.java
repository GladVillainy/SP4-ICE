import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String user;

    public LogEntry(String user, LocalDateTime timestamp) {
        this.user = user;
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", user='" + user + '\'' +
                '}';
    }
}