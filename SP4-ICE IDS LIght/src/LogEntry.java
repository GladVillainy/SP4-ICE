import SP3.User;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private User user;

    public LogEntry(User user, LocalDateTime timestamp) {
        this.user = user;
        this.timestamp = timestamp;
    }

    public User getUser() {
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