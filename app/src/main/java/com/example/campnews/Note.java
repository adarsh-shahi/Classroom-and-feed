package com.example.campnews;

public class Note {

    private String desc;
    private String imageUrl;
    private String name;

    private Note(){


    }

    public Note(String desc, String imageUrl,String name) {
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.name=name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
