package SP4;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lucas & Mikkel
 * Class made to create threats
 */
public class Threat {
    private String type;
    private String description;
    private LocalDateTime timestamp;
    private String severity;
    private ArrayList<LogEntry> relatedEntries;

    /**
     * Constructor to create treats
     * @param type
     * @param relatedEntries
     * @param severity
     * @param timestamp
     * @param description
     */
    public Threat(String type, ArrayList<LogEntry> relatedEntries, String severity, LocalDateTime timestamp, String description) {
        this.type = type;
        this.relatedEntries = relatedEntries;
        this.severity = severity;
        this.timestamp = timestamp;
        this.description = description;
    }

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

    public List<LogEntry> getRelatedEntries() {
        return relatedEntries;
    }

    public void setRelatedEntries(ArrayList<LogEntry> relatedEntries) {
        this.relatedEntries = relatedEntries;
    }
}