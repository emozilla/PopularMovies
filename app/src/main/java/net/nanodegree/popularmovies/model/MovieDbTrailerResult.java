package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbTrailerResult {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("results")
    public ArrayList<Trailer> results;

}
