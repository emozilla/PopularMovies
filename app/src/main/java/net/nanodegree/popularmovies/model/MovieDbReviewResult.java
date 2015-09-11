package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbReviewResult {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("page")
    public Integer page;
    @JsonProperty("results")
    public ArrayList<Review> results;
    @JsonProperty("total_pages")
    public Integer total_pages;
    @JsonProperty("total_results")
    public Integer total_results;
}
