package SP3;

import SP3.utility_SP3.FileIO_SP3;
import SP3.utility_SP3.TextUI_SP3;
import SP4.SecuritySystem;

import java.util.ArrayList;

public class User {




// ser authentication
    private String username;
    private String password;

    //Added - New variables
    private boolean isLocked;
    private int failedAttempts;
   // private int filesDeleted;
    private boolean isAdmin;

// User media information
  private ArrayList<Media> seenMedia = new ArrayList<Media>();
    private ArrayList<Media> wantsToSee = new ArrayList<Media>();


//Constructor
    public User(String username, String password) {
        this.username = username;
        this.password = password;

        // Added - New variables
        this.failedAttempts = 0;
       // this.filesDeleted = 0;
        this.isLocked = false;
        this.isAdmin = false;
    }




    // Getter and setter on
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
   public ArrayList<Media> getWantsToSee() {return wantsToSee;}
    public void setWantsToSee(ArrayList<Media> wantsToSee) {this.wantsToSee = wantsToSee;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
   public ArrayList<Media> getSeenMedia() {return seenMedia;}
   public void setSeenMedia(ArrayList<Media> seenMedie) {this.seenMedia = seenMedie;}




    //Added - everything under

    /**
     * @author Mikkel
     * Shows the CSV threat file
     * @param system
     */
    public void showThreat(SecuritySystem system) {
            System.out.println("--- Active threats---");
            System.out.println(system.getThreats());
    }
    /**
     * @author Mikkel
     * Shows the CSV login file
     * @param system
     */
    public void showLogEntry(SecuritySystem system) {
        System.out.println("---Security logs---");
        System.out.println(system.getLogEntries());
    }



    //Getter og setter
    public boolean getIsLocked() {
        return isLocked;
    }
    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }
    public int getFailedAttempts() {
        return failedAttempts;
    }
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }
    public boolean getIsAdmin() {
        return isAdmin;
    }
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
