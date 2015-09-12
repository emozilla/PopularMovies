package net.nanodegree.popularmovies.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by antonio on 12/09/15.
 */
public class MovieContract implements BaseColumns {

    private MovieContract() {}

    public static final String TABLE_NAME = "Movie";
    public static final String AUTHORITY = "net.nanodegree.popularmovies.moviedb";
    public static final String CONTENT_URI_PATH = "movies";
    public static final String MIMETYPE_TYPE = "movies";
    public static final String MIMETYPE_NAME = "net.nanodegree.popularmovies.moviedb.provider";
    public static final int CONTENT_URI_PATTERN_MANY = 1;
    public static final int CONTENT_URI_PATTERN_ONE = 2;
    public static final Uri CONTENT_URI = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).appendPath(CONTENT_URI_PATH).build();

    public static final String ID = "_id";
    public static final String ADULT = "adult";
    public static final String LANGUAGE = "language";
    public static final String TITLE = "title";
    public static final String ORIGINAL_TITLE = "originalTitle";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE = "release";
    public static final String BACKDROP = "backdrop";
    public static final String POSTER = "poster";
    public static final String POPULARITY = "popularity";
    public static final String VIDEO = "video";
    public static final String VOTE_AVERAGE = "vote_average";
    public static final String VOTE_COUNT = "vote_count";
}