package SP3;

import SP3.utility_SP3.TextUI_SP3;

public class Movie extends Media implements Playable {

    private double lengthInMinutes;

    Movie(String name, int releaseYear, double rating, String category, double lengthInMinutes) {
        super(name, releaseYear, rating, category);
        this.lengthInMinutes = lengthInMinutes;
    }
    public void playMedia(User u) {
        TextUI_SP3 ui = new TextUI_SP3();
        ui.displayMsg("1. Play " + getName());
        ui.displayMsg("2. Save " + getName() + " to your list");

        int choice = ui.promptNumeric("Type 1 or 2");

        if (choice == 1) {
            ui.displayMsg("Now playing: " + getName());
            if (!u.getSeenMedia().contains(this)) {
                u.getSeenMedia().add(this);
            }
        } else if (choice == 2) {
            ui.displayMsg(getName() + " has been added to your list");
            if (!u.getWantsToSee().contains(this)) {
                u.getWantsToSee().add(this);
            }
        } else {
            ui.displayMsg("Invalid number");
        }
    }
}