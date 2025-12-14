package SP4;

import SP3.User;
import SP4.utlity.TextUI;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rules {
    private ArrayList<User> lockedUsers = new ArrayList();
    private ArrayList<User> lockedOffHourUsers = new ArrayList();

    SecuritySystem system = new SecuritySystem();


    TextUI ui = new TextUI();


    public void bruteForceExecute(User user){
        user.setIsLocked(true);
        lockedUsers.add(user);
        ui.displayMsg("This user: "+ user.getUsername()+ " is locked");
        system.addThreat("Bruteforce", system.getLogEntries(), "Moderate", LocalDateTime.now(), "Bruteforce has been detected");
    }

    public void excessiveDeletionExecute(){

    }
    public void offHoursLoginExecute(User user){
        user.setIsLocked(true);
        lockedOffHourUsers.add(user);
        ui.displayMsg("This user: "+ user.getUsername()+ " is on off hour lock");

        system.addThreat("Off hour login", system.getLogEntries(), "Mild", LocalDateTime.now(), "Off hour login has been detected");
    }
}