package SP3;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        StreamingService streaming = new StreamingService();



        //Ændret her
        //vigtigt, disse er midterlige, og er kun for at kunne teste systemets forskellige scenarie
        User testLocked = new User("TestLocked", "TestLocked");
        testLocked.setIsLocked(true);
        streaming.getUsers().add(testLocked);

        User test= new User("Test", "Test");
        streaming.getUsers().add(test);


      //Disse users' logintidspunkter kan blive testet
        // Ved at hardcode tidspunktet på linje 457 i StreamingService

        User testOffLoginPostiv = new User("TestPostiv", "TestPostiv");
        streaming.getUsers().add(testOffLoginPostiv);

        User testOffLoginNegativ = new User("TestNegativ", "TestNegativ");
        streaming.getUsers().add(testOffLoginNegativ);



        //Admin test
        User Admin = new User("admin", "admin");
        Admin.setIsAdmin(true);
        streaming.getUsers().add(Admin);


        streaming.start();

    }
}
