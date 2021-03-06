package gropoid.punter.retrofit.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Page<T> {
    @Expose
    @SerializedName("error")
    String error;
    @Expose
    @SerializedName("limit")
    int limit;
    @Expose
    @SerializedName("offset")
    int offset;
    @Expose
    @SerializedName("number_of_page_results")
    int number_of_page_results;
    @Expose
    @SerializedName("number_of_total_results")
    int number_of_total_results;
    @Expose
    @SerializedName("status_code")
    int status_code;
    @Expose
    @SerializedName("results")
    List<T> results;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber_of_page_results() {
        return number_of_page_results;
    }

    public void setNumber_of_page_results(int number_of_page_results) {
        this.number_of_page_results = number_of_page_results;
    }

    public int getNumber_of_total_results() {
        return number_of_total_results;
    }

    public void setNumber_of_total_results(int number_of_total_results) {
        this.number_of_total_results = number_of_total_results;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
