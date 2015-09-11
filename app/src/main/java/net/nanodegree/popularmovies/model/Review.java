package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by antonio on 10/09/15.
 */
public class Review {
    @JsonProperty("id")
    public String id;
    @JsonProperty("author")
    public String author;
    @JsonProperty("content")
    public String content;
    @JsonProperty("url")
    public String url;
}
