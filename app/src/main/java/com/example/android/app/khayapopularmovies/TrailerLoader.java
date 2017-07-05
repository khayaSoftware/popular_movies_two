package com.example.android.app.khayapopularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by khaya on 05/07/2017.
 */

public class TrailerLoader extends AsyncTaskLoader<ArrayList<String>> {
    private final String TAG = TrailerLoader.class.getSimpleName();
    public static final int LOADER_ID = 21;
    private String id;
    private String jsonData;
    private Context context;
    private final String REVIEWS = "videos";

    public TrailerLoader(Context c, String movieID) {
        super(c);
        c = c;
        id = movieID;
    }

    @Override
    public ArrayList<String> loadInBackground() {
        URL movieRequest = NetworkUtils.buildUrl(REVIEWS, id);
        try {
            jsonData = NetworkUtils.getResponseFromHttpUrl(movieRequest);
            Log.d(TAG, "Json data = "+ jsonData);
            ArrayList reviews = OpenMovieJsonUtils.getSimpleTrailerStrings(context, jsonData);
            return reviews;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
