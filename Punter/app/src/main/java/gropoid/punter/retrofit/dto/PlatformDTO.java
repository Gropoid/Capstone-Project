package gropoid.punter.retrofit.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import gropoid.punter.domain.Platform;

public class PlatformDTO {
    @Expose
    @SerializedName("id")
    int id;
    @Expose
    @SerializedName("name")
    String name;
    @Expose
    @SerializedName("abbreviation")
    String abbreviation;

    public Platform toPlatform() {
        Platform platform = new Platform();
        platform.setId(id);
        platform.setName(name);
        platform.setAbbreviation(abbreviation);
        return platform;
    }
}
