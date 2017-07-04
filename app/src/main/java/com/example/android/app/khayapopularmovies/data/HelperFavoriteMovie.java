package com.example.android.app.khayapopularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by noybs on 28/06/2017.
 */


public class HelperFavoriteMovie extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";


    private static final int DATABASE_VERSION = 1;

    public HelperFavoriteMovie(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + ContractFavoriteMovie.FavoriteMovieEntry.TABLE_NAME + " (" +
                ContractFavoriteMovie.FavoriteMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NOT NULL," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_VOTE_COUNT + " TEXT NOT NULL," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_BACKDROP_URL + " TEXT NOT NULL," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                ContractFavoriteMovie.FavoriteMovieEntry.COLUMN_IS_FAVORITE + " INT NOT NULL" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ContractFavoriteMovie.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public static void deleteDatabase(Context mContext) {
        mContext.deleteDatabase(DATABASE_NAME);
    }
}

