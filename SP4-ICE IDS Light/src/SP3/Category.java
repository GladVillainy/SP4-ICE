package SP3;

import java.util.ArrayList;
import java.util.Arrays;

public class Category {
    // Static list accessible from anywhere
    public static final ArrayList<String> All = new ArrayList<>(Arrays.asList(
            "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime",
            "Documentary", "Drama", "Family", "Fantasy", "Film-noir", "History",
            "Horror", "Music", "Musical", "Mystery", "Romance", "Sci-Fi",
            "Sport", "Talk-show", "Thriller", "War", "Western"
    ));
}
