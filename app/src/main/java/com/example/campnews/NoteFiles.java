package com.example.campnews;

public class NoteFiles {

    private String time;
    private String fileurl;

    public NoteFiles(String time, String fileurl) {
        this.time = time;
        this.fileurl = fileurl;
    }

    public NoteFiles() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }
}
