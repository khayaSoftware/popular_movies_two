package com.example.android.app.khayapopularmovies;

import java.io.Serializable;

/**
 * Created by noybs on 23/05/2017.
 */
//TODO SUGGESTION This noybs chap has written large portions of your submission - or so it seems :^)

public class Movie implements Serializable {

    public Movie(String posterPath, String overview,
                 String releaseDate, String id,
                 String title, String backdropPath,
                 String voteCount, String voteAverage) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.id = id;
        this.title = title;
        this.backdropPath = backdropPath;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
    }

    public int isFavourite = 0;
    public String posterPath;
    public String overview;
    public String releaseDate;
    public String id;
    public String title;
    public String backdropPath;
    public String voteCount;
    public String voteAverage;

    public int isFavourite(){
        return isFavourite;
    }

    public void setFavourite(int value){
        isFavourite = value;
    }
}
