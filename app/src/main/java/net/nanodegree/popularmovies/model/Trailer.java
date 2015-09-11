package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by antonio on 10/09/15.
 */
public class Trailer {
    @JsonProperty("id")
    public String id;
    @JsonProperty("iso_639_1")
    public String iso;
    @JsonProperty("key")
    public String key;
    @JsonProperty("name")
    public String name;
    @JsonProperty("site")
    public String site;
    @JsonProperty("size")
    public Integer size;
    @JsonProperty("type")
    public String type;
}
