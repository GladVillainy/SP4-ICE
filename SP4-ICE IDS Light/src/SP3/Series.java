package SP3;

import SP3.utility_SP3.TextUI_SP3;

import java.util.List;

public class Series extends Media implements Playable {
    private int season;
    private int episode;
    private int endYear;
    private List<String> seasons;   // for summary style
    private String yearRange;       // for summary style

    // Detailed constructor
    Series(String name, int releaseYear, double rating, String category, int season, int episode, int endYear) {
        super(name, releaseYear, rating, category);
        this.season = season;
        this.episode = episode;
        this.endYear = endYear;
    }

    // Summary constructor for CSV loader
    Series(String name, String yearRange, double rating, String category, List<String> seasons) {
        super(name, 0, rating, category); // 0 placeholder for releaseYear
        this.yearRange = yearRange;
        this.seasons = seasons;
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
