package gropoid.punter.retrofit.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class GameDTO {
    @Expose
    @SerializedName("id")
    long id;
    @Expose
    @SerializedName("api_detail_url")
    String apiDetailUrl;
    @Expose
    @SerializedName("deck")
    String deck;
    @Expose
    @SerializedName("image")
    ImageDTO image;
    @Expose
    @SerializedName("name")
    String name;
    @Expose
    @SerializedName("original_releaseDate")
    Date originalReleaseDate;
    @Expose
    @SerializedName("platforms")
    List<PlatformDTO> platforms;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiDetailUrl() {
        return apiDetailUrl;
    }

    public void setApiDetailUrl(String apiDetailUrl) {
        this.apiDetailUrl = apiDetailUrl;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public ImageDTO getImage() {
        return image;
    }

    public void setImage(ImageDTO image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getOriginalReleaseDate() {
        return originalReleaseDate;
    }

    public void setOriginalReleaseDate(Date originalReleaseDate) {
        this.originalReleaseDate = originalReleaseDate;
    }

    public List<PlatformDTO> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<PlatformDTO> platforms) {
        this.platforms = platforms;
    }
}
