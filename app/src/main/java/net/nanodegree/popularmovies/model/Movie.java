package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by antonio on 24/08/15.
 */
public class Movie  {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("adult")
    public Boolean adult;
    @JsonProperty("original_language")
    public String language;
    @JsonProperty("title")
    public String title;
    @JsonProperty("original_title")
    public String originalTitle;
    @JsonProperty("overview")
    public String overview;
    @JsonProperty("release_date")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    public Date release;
    @JsonProperty("backdrop_path")
    public String backdrop;
    @JsonProperty("poster_path")
    public String poster;
    @JsonProperty("popularity")
    public Float  popularity;
    @JsonProperty("video")
    public Boolean video;
    @JsonProperty("vote_average")
    public Float vote_average;
    @JsonProperty("vote_count")
    public Integer vote_count;
    @JsonProperty("genre_ids")
    public ArrayList<Integer> genre_ids;
}
