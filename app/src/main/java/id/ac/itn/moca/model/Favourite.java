package id.ac.itn.moca.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_favourite")
public class Favourite {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int MovieId;
    private boolean video;
    private String posterPath;
    private boolean adult;
    private String backdropPath;
    private String originalLanguage;
    private String originalTitle;
    private String title;
    private Double voteAverage;
    private int voteCount;
    private String overview;
    private String releaseDate;

    public Favourite() {
    }

    public Favourite(int movieId, boolean video, String posterPath, boolean adult, String backdropPath, String originalLanguage, String originalTitle, String title, Double voteAverage, int voteCount, String overview, String releaseDate) {
        MovieId = movieId;
        this.video = video;
        this.posterPath = posterPath;
        this.adult = adult;
        this.backdropPath = backdropPath;
        this.originalLanguage = originalLanguage;
        this.originalTitle = originalTitle;
        this.title = title;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieId(int movieId) {
        MovieId = movieId;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return MovieId;
    }

    public boolean isVideo() {
        return video;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

}
