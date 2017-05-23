package com.example.android.app.khayapopularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private String url;
    private static final int NUM_LIST_ITEMS = 100;
    private TextView textView;
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);

        textView = (TextView) findViewById(R.id.tv_url);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 3);
        mMovieList.setLayoutManager(gridLayout);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS,this);

        mMovieList.setAdapter(mAdapter);

        new FetchMoviesTask().execute("top_rated");


    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if(params.length == 0){
                return null;
            }

            URL movieRequest = NetworkUtils.buildUrl(params[0]);
            try{
                url = NetworkUtils.getResponseFromHttpUrl(movieRequest);
                ArrayList movies = OpenMovieJsonUtils.getSimpleMovieStrings(MainActivity.this, url);

                return movies;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies != null){
                for(Movie movie : movies){

                }
            }
        }
    }



}
