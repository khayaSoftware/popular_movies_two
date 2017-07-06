package com.example.android.app.khayapopularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.app.khayapopularmovies.data.ContractFavoriteMovie.FavoriteMovieEntry.TABLE_NAME;

/**
 * Created by noybs on 29/06/2017.
 */

public class FavoriteContentProvider extends ContentProvider {
    final static String TAG = FavoriteContentProvider.class.getSimpleName();
    public static final int FAVORITE_MOVIE_WITH_ID = 101;
    public static final int FAVORITES = 100;

    private static final UriMatcher sUriMatcher = builUriMatcher();

    public static UriMatcher builUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContractFavoriteMovie.AUTHORITY, ContractFavoriteMovie.PATH_FAVORITES + "/#", FAVORITE_MOVIE_WITH_ID);
        uriMatcher.addURI(ContractFavoriteMovie.AUTHORITY, ContractFavoriteMovie.PATH_FAVORITES, FAVORITES);
        return uriMatcher;
    }

    private HelperFavoriteMovie mFavoriteDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavoriteDbHelper = new HelperFavoriteMovie(context);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        Uri returnUri;
        Log.d(TAG, "" + sUriMatcher.match(uri) + FAVORITES);
        switch (sUriMatcher.match(uri)){

            case FAVORITES:
                long id = db.insert(TABLE_NAME, null, values);
                if(id > 0){
                    returnUri = ContentUris.withAppendedId(ContractFavoriteMovie.FavoriteMovieEntry.CONTENT_URI, id);
                }else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri:"+uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor returnCursor;

        switch (match){
            case FAVORITES:
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getLastPathSegment();
                String[] selctions = new String[]{id};
                returnCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selctions,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int tasksDelted;

        switch (match){
            case FAVORITE_MOVIE_WITH_ID:
                String id = uri.getPathSegments().get(1);

                tasksDelted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if(tasksDelted != 0){
            getContext().getContentResolver().notifyChange(uri, null);;
        }

        return tasksDelted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int ifUpdated = db.update(ContractFavoriteMovie.FavoriteMovieEntry.TABLE_NAME, null, null, null);

        if (ifUpdated != 0){
            return ifUpdated;
        }else {
            return 0;
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
