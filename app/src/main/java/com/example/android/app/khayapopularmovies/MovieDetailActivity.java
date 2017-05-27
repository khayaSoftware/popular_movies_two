package com.example.android.app.khayapopularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewDescription;
    private TextView textViewStatus;
    private TextView textViewTitle;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imageView = (ImageView) findViewById(R.id.movie_poster);
        textViewDescription = (TextView) findViewById(R.id.tv_description);
        textViewStatus = (TextView) findViewById(R.id.tv_stats);
        textViewTitle = (TextView) findViewById(R.id.tv_title);
        Intent intentThatStarted = getIntent();

        if(intentThatStarted != null){
            Bundle extras = getIntent().getExtras();
            textViewStatus.setText(extras.getString("EXTRA_RELEASE_DATE"));
            textViewDescription.setText(extras.getString("EXTRA_DESCRIPTION"));
            textViewTitle.setText(extras.getString("EXTRA_TITLE"));
            String imagePath = "http://image.tmdb.org/t/p/w342/" + extras.getString("EXTRA_URL");
            Log.d(TAG, "image url " + imagePath);
            try {
                Picasso.with(this).load(imagePath).into(imageView);
            } catch (Exception e) {
                Log.d(TAG, "error");
                Log.v(TAG, "Image url - " + imagePath);
                e.printStackTrace();
                }
        }
        else{
            textViewDescription.setText("shit");
        }
    }

}
