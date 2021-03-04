package com.example.campnews;

public class Note {

    private String desc;
    private String imageUrl;
    private String name;
    private String time;

    private Note(){


    }

    public Note(String desc, String imageUrl,String name,String time) {
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.name=name;
        this.time=time;
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


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
