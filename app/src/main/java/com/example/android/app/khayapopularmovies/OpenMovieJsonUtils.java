package com.example.android.app.khayapopularmovies;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by noybs on 23/05/2017.
 */

public class OpenMovieJsonUtils{
    private final static String TAG = OpenMovieJsonUtils.class.getSimpleName();

    public static ArrayList getSimpleMovieStrings(Context context, String movieJsonStr)throws JSONException{

        ArrayList movies = new ArrayList();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray("results");

        for(int i = 0; i < movieArray.length(); ++i){
            Movie movie = new Movie(
                    movieArray.getJSONObject(i).getString("poster_path"),
                    movieArray.getJSONObject(i).getString("overview"),
                    movieArray.getJSONObject(i).getString("release_date"),
                    movieArray.getJSONObject(i).getString("id"),
                    movieArray.getJSONObject(i).getString("title"),
                    movieArray.getJSONObject(i).getString("backdrop_path"),
                    movieArray.getJSONObject(i).getString("vote_count"),
                    movieArray.getJSONObject(i).getString("vote_average")
            );
            Log.d(TAG, movie.posterPath);
            movies.add(movie);
        }
        return movies;
    }

}

