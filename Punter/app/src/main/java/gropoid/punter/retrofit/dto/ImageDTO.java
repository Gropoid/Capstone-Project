package gropoid.punter.retrofit.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageDTO {

    @Expose
    @SerializedName("icon_url")
    String iconUrl;
    @Expose
    @SerializedName("medium_url")
    String mediumUrl;
    @Expose
    @SerializedName("screen_url")
    String screenUrl;
    @Expose
    @SerializedName("small_url")
    String smallUrl;
    @Expose
    @SerializedName("super_url")
    String superUrl;
    @Expose
    @SerializedName("thumb_url")
    String thumbUrl;
    @Expose
    @SerializedName("tiny_url")
    String tinyUrl;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMediumUrl() {
        return mediumUrl;
    }

    public void setMediumUrl(String mediumUrl) {
        this.mediumUrl = mediumUrl;
    }

    public String getScreenUrl() {
        return screenUrl;
    }

    public void setScreenUrl(String screenUrl) {
        this.screenUrl = screenUrl;
    }

    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    public String getSuperUrl() {
        return superUrl;
    }

    public void setSuperUrl(String superUrl) {
        this.superUrl = superUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getTinyUrl() {
        return tinyUrl;
    }

    public void setTinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }
}
