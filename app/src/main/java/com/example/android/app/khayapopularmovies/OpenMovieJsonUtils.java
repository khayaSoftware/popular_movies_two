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
//TODO SUGGESTION This noybs chap has written large portions of your submission - or so it seems :^)

public class OpenMovieJsonUtils {
    private final static String TAG = OpenMovieJsonUtils.class.getSimpleName();
    private final static String POSTER_PATH = "poster_path";
    private final static String OVERVIEW = "overview";
    private final static String RELEASE_DATE = "release_date";
    private final static String ID = "id";
    private final static String TITLE = "title";
    private final static String BACKDROP_PATH = "backdrop_path";
    private final static String VOTE_COUNT = "vote_count";
    private final static String VOTE_AVE = "vote_average";
    private final static String RESULTS = "results";
    private final static String CONTENT = "content";
    private final static String AUTHOR = "author";
    private final static String YOUTUBE = "youtube";
    private final static String SOURCE = "source";

    public static ArrayList getSimpleMovieStrings(Context context, String movieJsonStr) throws JSONException {

        ArrayList movies = new ArrayList();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        for (int i = 0; i < movieArray.length(); ++i) {
            Movie movie = new Movie(
                    movieArray.getJSONObject(i).getString(POSTER_PATH),
                    movieArray.getJSONObject(i).getString(OVERVIEW),
                    movieArray.getJSONObject(i).getString(RELEASE_DATE),
                    movieArray.getJSONObject(i).getString(ID),
                    movieArray.getJSONObject(i).getString(TITLE),
                    movieArray.getJSONObject(i).getString(BACKDROP_PATH),
                    movieArray.getJSONObject(i).getString(VOTE_COUNT),
                    movieArray.getJSONObject(i).getString(VOTE_AVE)
            );
            Log.d(TAG, movie.posterPath);
            movies.add(movie);
        }
        return movies;
    }

    public static ArrayList<String> getSimpleReviewStrings(Context context, String movieJsonStr) throws JSONException {

        ArrayList<String> reviews = new ArrayList<String>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(RESULTS);

        for (int i = 0; i < movieArray.length(); ++i) {
            String review = movieArray.getJSONObject(i).getString(AUTHOR) + "\n" + movieArray.getJSONObject(i).getString(CONTENT);
            reviews.add(review);
        }
        return reviews;
    }

    public static ArrayList<String> getSimpleTrailerStrings(Context context, String movieJsonStr) throws JSONException {

        ArrayList<String> reviews = new ArrayList<String>();

        JSONObject movieJson = new JSONObject(movieJsonStr);

        JSONArray movieArray = movieJson.getJSONArray(YOUTUBE);

        for (int i = 0; i < movieArray.length(); ++i) {
            String review = movieArray.getJSONObject(i).getString(SOURCE);
            reviews.add(review);
        }
        return reviews;
    }

}

