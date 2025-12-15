package SP4;

import SP3.User;
import SP4.utlity.TextUI;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Mikkel, Lucas & Jökull
 * Class that acts based on what SecuritySystem detacteds
 */
public class Rules {
    private SecuritySystem system;
    private ArrayList<User> lockedUsers = new ArrayList();
    private ArrayList<User> lockedOffHourUsers = new ArrayList();

    public Rules(SecuritySystem system) {
        this.system = system;
    }


    TextUI ui = new TextUI();


    /**
     * @author Lucas
     * Method that locks the user if bruteforce has been detected.
     * It adds the user and creates a moderate threat
     * @param user
     */
    public void bruteForceExecute(User user){
        user.setIsLocked(true);
        lockedUsers.add(user);
        ui.displayMsg("This user: "+ user.getUsername()+ " is locked");
        system.addThreat("Bruteforce", system.getLogEntries(), "Moderate", LocalDateTime.now(), "Bruteforce has been detected");
    }


    /**
     * @author Jökull
     * Method that locks the user if excessive deletion has been detected.
     * It adds the user and creates a severe threat
     * @param user
     */
    public void excessiveDeletionExecute(User user){
        user.setIsLocked(true);
        lockedUsers.add(user);
        ui.displayMsg("This user: "+ user.getUsername()+ " is locked");
        system.addThreat("Excessive deletion", system.getLogEntries(), "Severe", LocalDateTime.now(), "Excessive file deletion has been detected");

    }

    /**
     * @author Mikkel
     * Method locks the user in the off work hour.
     * It adds the user and creates a mild threat
     * @param user
     */
    public void offHoursLoginExecute(User user){
        user.setIsLocked(true);
        lockedOffHourUsers.add(user);
        ui.displayMsg("This user: "+ user.getUsername()+ " is on off hour lock");

        system.addThreat("Off hour login", system.getLogEntries(), "Mild", LocalDateTime.now(), "Off hour login has been detected");
    }
}