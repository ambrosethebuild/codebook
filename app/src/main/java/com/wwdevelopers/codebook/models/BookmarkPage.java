package com.wwdevelopers.codebook.models;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class BookmarkPage extends LitePalSupport implements Serializable {

    private String title;
    private String url;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
