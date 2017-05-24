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
    private TextView textView;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        imageView = (ImageView) findViewById(R.id.movie_poster);
        textView = (TextView) findViewById(R.id.tv_description);

        Intent intentThatStarted = getIntent();

        if(intentThatStarted != null){

                Bundle extras = getIntent().getExtras();

                textView.setText(extras.getString("EXTRA_DESCRIPTION"));
                String imagePath = "http://image.tmdb.org/t/p/w342/" + extras.getString("EXTRA_URL");
                Log.d(TAG, "image url " + imagePath);
                try{
                    Picasso.with(this).load(imagePath).into(imageView);
                }catch (Exception e){
                    Log.d(TAG, "error");
                    Log.v(TAG, "Image url - " + imagePath);
                    e.printStackTrace();
                }


        }
        else{
            textView.setText("shit");
        }
    }

}
