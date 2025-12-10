import java.time.LocalDateTime;
import java.util.List;

public class Threat {
    private String type;
    private String description;
    private LocalDateTime timestamp;
    private String severity;
    private List<LogEntry> relatedEntries;
}