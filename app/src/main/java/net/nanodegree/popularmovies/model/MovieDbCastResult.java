package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by antonio on 10/09/15.
 */
public class MovieDbCastResult {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("cast")
    public ArrayList<Cast> cast;
    @JsonProperty("crew")
    public ArrayList<Crew> crew;
}
