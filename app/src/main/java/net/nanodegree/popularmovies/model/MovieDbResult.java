package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by antonio on 24/08/15.
 */
public class MovieDbResult {

    @JsonProperty("page")
    public Integer page;

    @JsonProperty("results")
    public ArrayList<Movie> results;

    @JsonProperty("total_pages")
    public Integer pages;

    @JsonProperty("total_results")
    private Integer count;
}
