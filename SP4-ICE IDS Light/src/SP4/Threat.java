package SP4;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas & Mikkel
 * Class made to describe threats
 */
public class Threat {
    private String type;
    private String description;
    private LocalDateTime timestamp;
    private String severity;
    private String username;

    /**
     * Constructor to create treats
     * @param type
     * @param username
     * @param severity
     * @param timestamp
     * @param description
     */
    public Threat(String type, String username, String severity, LocalDateTime timestamp, String description) {
        this.type = type;
        this.username = username;
        this.severity = severity;
        this.timestamp = timestamp;
        this.description = description;
    }



    //Getters & Setters
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSeverity() {
        return severity;
    }
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Threat {" +
                "\n  Type        : " + type +
                "\n  Severity    : " + severity +
                "\n  Timestamp   : " + timestamp +
                "\n  User        : " + username +
                "\n  Description : " + description +
                "\n}";
    }
}