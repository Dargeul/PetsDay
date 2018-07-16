package com.example.gypc.petsday.model;

public class ImageUploadResponse {
    private String path;
    private boolean succ;

    public ImageUploadResponse(String path, boolean succ) {
        this.path = path;
        this.succ = succ;
    }


    public String getPath() {
        return path;
    }

    public boolean isSucc() {
        return succ;
    }
}
