package com.barmej.apod;

import com.ortiz.touchview.TouchImageView;

public class Data {
    private String title;
    private String description;
    private String image;

    public Data(String title, String description, String imageOrVideo) {
        this.title = title;
        this.description = description;
        this.image = imageOrVideo;
    }

    public String getImageOrVideo() {
        return image;
    }

    public void setImageOrVideo(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
