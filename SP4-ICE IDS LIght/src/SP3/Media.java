package SP3;

public abstract class Media {
    protected String name;
    protected int releaseYear;
    protected double rating;
    protected String category;

    public Media(String name, int releaseYear, double rating, String category) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void playMedia(User currentUser) {
        // Implementation here
    }
}
