
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class SecuritySystemTest {
    SecuritySystem system = new SecuritySystem();

    @org.junit.jupiter.api.Test
    void bruteForce() {
        assertTrue(system.bruteForce(5));
        assertFalse(system.bruteForce(2));
    }

    @org.junit.jupiter.api.Test
    void excessiveDeletion() {

    }

    @org.junit.jupiter.api.Test
    void offHoursLogin() {

    }
}