import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String user;

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