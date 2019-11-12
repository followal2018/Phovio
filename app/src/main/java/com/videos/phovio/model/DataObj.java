package com.videos.phovio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataObj {

    @SerializedName("categories")
    @Expose
    private List<Category> categories = null;
    @SerializedName("slides")
    @Expose
    private List<Slide> slides = null;
    @SerializedName("fullscreen")
    @Expose
    private List<Status> fullscreen = null;
    @SerializedName("status")
    @Expose
    private List<Status> status = null;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Slide> getSlides() {
        return slides;
    }

    public void setSlides(List<Slide> slides) {
        this.slides = slides;
    }

    public List<Status> getFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(List<Status> fullscreen) {
        this.fullscreen = fullscreen;
    }

    public List<Status> getStatus() {
        return status;
    }

    public void setStatus(List<Status> status) {
        this.status = status;
    }
}
