package com.example.android.app.khayapopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_LIST_ITEMS = 3;

    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager gridLayout = new GridLayoutManager(this, 3);
        mMovieList.setLayoutManager(gridLayout);

        mMovieList.setHasFixedSize(true);

        mAdapter = new MovieAdapter(NUM_LIST_ITEMS,this);

        mMovieList.setAdapter(mAdapter);



    }



}
