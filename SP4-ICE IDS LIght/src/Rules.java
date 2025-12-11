import SP3.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rules {
    ArrayList<User> lockedUsers = new ArrayList();
    ArrayList<User> lockedOffHourUsers = new ArrayList();



    public void bruteForceExecute(User user){
        user.setLocked(true);
        lockedUsers.add(user);
        System.out.println("This user:"+ user.getUsername()+ " is locked");
    }

    public void excessiveDeletionExecute(){

    }
    public void offHoursLoginExecute(User user){
        user.setLocked(true);
        lockedOffHourUsers.add(user);
        System.out.println("This user:"+ user.getUsername()+ " is on off hour lock");
    }

}