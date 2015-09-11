package net.nanodegree.popularmovies.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by antonio on 10/09/15.
 */
public class Crew {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("credit_id")
    public String creditId;
    @JsonProperty("department")
    public String department;
    @JsonProperty("job")
    public String job;
    @JsonProperty("name")
    public String name;
    @JsonProperty("profile_path")
    public String profileImage;
}
