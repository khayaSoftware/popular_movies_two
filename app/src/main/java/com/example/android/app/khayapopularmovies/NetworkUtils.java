package com.example.android.app.khayapopularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by User on 5/23/2017.
 */

public class NetworkUtils {
    private static final String  BASE_URL = "http://api.themoviedb.org/3/movie/";
    //TODO SUGGESTION Follow the standard Java Naming Conventions i.e. BASE_URL in this instance

    private static final String API_QUERY = "api_key";

    private static final String API_KEY = BuildConfig.MY_MOVIEDB_API_KEY;
    //TODO AWESOME You're keeping your API key out of source code!

    public static URL buildUrl(String sortBy){
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter(API_QUERY, API_KEY)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }else{
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }
}
