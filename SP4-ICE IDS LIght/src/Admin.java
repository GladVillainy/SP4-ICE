import java.util.logging.LogManager;

public class Admin {
    private String password;
    private String username;
   // private ThreatMonitor threatMonitor;
    private LogManager logManager;
 //   private UserManager userManager;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    //    this.threatMonitor = new ThreatMonitor();
      //  this.logManager = new LogManager();
      //  this.userManager = new UserManager();
    }
    public void showThreat() {
    System.out.println("--- Active threats---");


    }


    public void showLogEntry() {
        System.out.println("---Security logs---");
    }

    public void usersConfig() {
    }
}