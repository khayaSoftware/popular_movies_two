package com.example.android.app.khayapopularmovies;

/**
 * Created by noybs on 23/05/2017.
 */

public class Movie {

    public Movie(String posterPath, String overview,
                 String releaseDate, String id,
                 String title, String backdropPath,
                 String voteCount, String voteAcerage ){
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.voteAcerage = voteAcerage;
    }
    public String posterPath;
    public String overview;
    public String releaseDate;
    public String id;
    public String title;
    public String backdropPath;
    public String voteCount;
    public String voteAcerage;
}
