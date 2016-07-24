package gropoid.punter.retrofit.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
}
