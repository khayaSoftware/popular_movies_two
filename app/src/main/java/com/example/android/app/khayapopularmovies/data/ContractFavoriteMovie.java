package com.example.android.app.khayapopularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by khaya on 28/06/2017.
 */

public class ContractFavoriteMovie {

    public static final String AUTHORITY = "com.example.android.app.khayapopularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "sypnosis";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_BACKDROP_URL = "backdrop";
        public static final String COLUMN_POSTER_URL = "poster";
        public static final String COLUMN_RELEASE_DATE = "release";

    }
}
