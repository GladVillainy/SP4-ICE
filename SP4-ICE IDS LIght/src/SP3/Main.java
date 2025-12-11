package SP3;

public class Main {
    public static void main(String[] args) {
        StreamingService streaming = new StreamingService();

        User testLocked = new User("TestLocked", "TestLocked");
        testLocked.setLocked(true);
        streaming.getUsers().add(testLocked);

        streaming.start();

    }
}
