
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


public class SecuritySystemTest {
    SecuritySystem system = new SecuritySystem();

    LocalDateTime timestamp1 = LocalDateTime.parse("2025-12-10T09:00:00");
    LocalDateTime timestamp2 = LocalDateTime.parse("2025-12-10T03:00:00");


        @Test
        void bruteForce () {
            assertTrue(system.bruteForce(5));
            assertFalse(system.bruteForce(2));

            // Positive path test – valid input got passed
            // Failure-case test – invalid input got rejected
        }

        @Test
        void excessiveDeletion () {

        }

    // AddLogEntry Test
    // Positive path test – Passed
    // Failure-case test – Passed

        @Test
        void offHoursLogin_Positive_path(){
            assertTrue(system.offHoursLogin(timestamp2));
            assertFalse(system.offHoursLogin(timestamp1));
        }


        @Test
        void offHoursLogin_Failure_case () {
            assertTrue(system.offHoursLogin(timestamp1));
            assertFalse(system.offHoursLogin(timestamp2));
        }

        // AddLogEntry Test
          // Positive path test – Passed
          // Failure-case test – Passed
        @Test
        void addLogEntry_Positive () {
            LocalDateTime timestamp = LocalDateTime.parse("2025-12-10T12:00:00");

            system.addLogEntry("Hej", timestamp);

            assertEquals(1, system.getLogEntries().size());
            LogEntry entry = system.getLogEntries().get(0);

            assertEquals("Hej", entry.getUser());
            assertEquals(LocalDateTime.parse("2025-12-10T12:00:00"), entry.getTimestamp());

        }

    @Test
    void addLogEntry_Failure_case () {
        LocalDateTime timestamp = LocalDateTime.parse("2025-12-10T11:00:00");

        system.addLogEntry("Gert", timestamp);

        assertEquals(1, system.getLogEntries().size());
        LogEntry entry = system.getLogEntries().get(0);

        assertEquals("Hej", entry.getUser());
        assertEquals(LocalDateTime.parse("2025-12-10T12:00:00"), entry.getTimestamp());

    }




    }