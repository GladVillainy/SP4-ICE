
import SP4.SecuritySystem;
import SP3.User;
import SP4.LogEntry;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


public class SecuritySystemTest {
    SecuritySystem system = new SecuritySystem();

    LocalDateTime timestamp1 = LocalDateTime.parse("2025-12-10T09:00:00");
    LocalDateTime timestamp2 = LocalDateTime.parse("2025-12-10T03:00:00");



    // BruteForce detekt Test
    // Positive path test
    // Failure-case test
        @Test
        void bruteForce_Positive_path () {
           // assertTrue(system.bruteForce(5));
          //  assertFalse(system.bruteForce(2));
        }
    @Test
    void bruteForce_Failure_case () {
      //  assertTrue(system.bruteForce(2));
      //  assertFalse(system.bruteForce(5));
    }


        @Test
        void excessiveDeletion () {

        }

    // AddLogEntry Test
    // Positive path test – Passed
    // Failure-case test – Passed

        @Test
        void offHoursLogin_Positive_path(){
            //User user = new User("name", "pass");
            //assertTrue(system.offHoursLogin(timestamp2, user));
            //assertFalse(system.offHoursLogin(timestamp1, user));
        }


        @Test
        void offHoursLogin_Failure_case () {
            //User user = new User("name", "pass");
            //assertTrue(system.offHoursLogin(timestamp1, user));
            //assertFalse(system.offHoursLogin(timestamp2, user));
        }

        // AddLogEntry Test
          // Positive path test – Passed
          // Failure-case test – Passed
        @Test
        void addLogEntry_Positive () {
            LocalDateTime timestamp = LocalDateTime.parse("2025-12-10T12:00:00");

            User user1 = new User("Hej", "pass");
            User user2 = new User("Gert", "pass");

            // system.addLogEntry(user1, timestamp);

            assertEquals(1, system.getLogEntries().size());
            LogEntry entry = system.getLogEntries().get(0);

           // assertEquals(user1, entry.getUser());
            assertEquals(LocalDateTime.parse("2025-12-10T12:00:00"), entry.getTimestamp());

        }

    @Test
    void addLogEntry_Failure_case () {
        LocalDateTime timestamp = LocalDateTime.parse("2025-12-10T11:00:00");

        User user1 = new User("Hej", "pass");
        User user2 = new User("Gert", "pass");

        system.addLogEntry(user2, timestamp);

        assertEquals(1, system.getLogEntries().size());
        LogEntry entry = system.getLogEntries().get(0);

       // assertEquals(user1, entry.getUser());
        assertEquals(LocalDateTime.parse("2025-12-10T12:00:00"), entry.getTimestamp());

    }




    }