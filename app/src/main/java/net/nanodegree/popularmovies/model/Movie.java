package net.nanodegree.popularmovies.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentMimeTypeVnd;
import com.tojc.ormlite.android.annotation.AdditionalAnnotation.DefaultContentUri;

import net.nanodegree.popularmovies.misc.Utils;
import net.nanodegree.popularmovies.provider.MovieContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by antonio on 24/08/15.
 */

@DatabaseTable(tableName = MovieContract.TABLE_NAME)
@DefaultContentUri(authority = MovieContract.AUTHORITY, path = MovieContract.CONTENT_URI_PATH)
@DefaultContentMimeTypeVnd(name = MovieContract.MIMETYPE_NAME, type = MovieContract.MIMETYPE_TYPE)
public class Movie  {

    @DatabaseField(id = true, columnName = BaseColumns._ID, dataType = DataType.INTEGER_OBJ)
    @JsonProperty("id")
    public Integer id;

    @DatabaseField(dataType = DataType.BOOLEAN_OBJ)
    @JsonProperty("adult")
    public Boolean adult;

    @DatabaseField
    @JsonProperty("original_language")
    public String language;

    @DatabaseField
    @JsonProperty("title")
    public String title;

    @DatabaseField
    @JsonProperty("original_title")
    public String originalTitle;

    @DatabaseField
    @JsonProperty("overview")
    public String overview;

    @DatabaseField(dataType = DataType.DATE_STRING)
    @JsonProperty("release_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public Date release;

    @DatabaseField
    @JsonProperty("backdrop_path")
    public String backdrop;

    @DatabaseField
    @JsonProperty("poster_path")
    public String poster;

    @DatabaseField(dataType = DataType.FLOAT_OBJ)
    @JsonProperty("popularity")
    public Float  popularity;

    @DatabaseField(dataType = DataType.BOOLEAN_OBJ)
    @JsonProperty("video")
    public Boolean video;

    @DatabaseField(dataType = DataType.FLOAT_OBJ)
    @JsonProperty("vote_average")
    public Float vote_average;

    @DatabaseField(dataType = DataType.INTEGER_OBJ)
    @JsonProperty("vote_count")
    public Integer vote_count;

    /* FIX
    @DatabaseField
    @JsonProperty("genre_ids")
    public ArrayList<Integer> genre_ids; */


    public ContentValues getContentValues() {

        ContentValues values =  new ContentValues();

        values.put(MovieContract.ID, this.id);
        values.put(MovieContract.ADULT, this.adult);
        values.put(MovieContract.LANGUAGE, this.language);
        values.put(MovieContract.TITLE, this.title);
        values.put(MovieContract.ORIGINAL_TITLE, this.originalTitle);
        values.put(MovieContract.OVERVIEW, this.overview);
        values.put(MovieContract.RELEASE, new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(this.release));
        values.put(MovieContract.BACKDROP, this.backdrop);
        values.put(MovieContract.POSTER, this.poster);
        values.put(MovieContract.POPULARITY, this.popularity);
        values.put(MovieContract.VIDEO, this.video);
        values.put(MovieContract.VOTE_AVERAGE, this.vote_average);
        values.put(MovieContract.VOTE_COUNT, this.vote_count);

        return values;
    }

    public static Movie fromCursor(Cursor c) {

        Movie movie = new Movie();

        movie.id = c.getInt(c.getColumnIndexOrThrow(MovieContract.ID));
        movie.adult = new Boolean(  (c.getShort(c.getColumnIndexOrThrow(MovieContract.ADULT))== 0)? false : true);
        movie.language = c.getString(c.getColumnIndexOrThrow(MovieContract.LANGUAGE));
        movie.title = c.getString(c.getColumnIndexOrThrow(MovieContract.TITLE));
        movie.originalTitle = c.getString(c.getColumnIndexOrThrow(MovieContract.ORIGINAL_TITLE));
        movie.overview = c.getString(c.getColumnIndexOrThrow(MovieContract.OVERVIEW));
        try {
            movie.release = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(c.getString(c.getColumnIndexOrThrow(MovieContract.RELEASE)));
        }catch(Exception e) {}
        movie.backdrop = c.getString(c.getColumnIndexOrThrow(MovieContract.BACKDROP));
        movie.poster = c.getString(c.getColumnIndexOrThrow(MovieContract.POSTER));
        movie.popularity = c.getFloat(c.getColumnIndexOrThrow(MovieContract.POPULARITY));
        movie.video = new Boolean(  (c.getShort(c.getColumnIndexOrThrow(MovieContract.VIDEO))== 0)? false : true);
        movie.vote_average = c.getFloat(c.getColumnIndexOrThrow(MovieContract.VOTE_AVERAGE));
        movie.vote_count = c.getInt(c.getColumnIndexOrThrow(MovieContract.VOTE_COUNT));

        return movie;
    }
}
