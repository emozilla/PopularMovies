package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by antonio on 10/09/15.
 */
public class Cast {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("cast_id")
    public Integer cast_id;
    @JsonProperty("character")
    public String character;
    @JsonProperty("credit_id")
    public String creditId;
    @JsonProperty("name")
    public String name;
    @JsonProperty("order")
    public Integer order;
    @JsonProperty("profile_path")
    public String profileImage;
}
