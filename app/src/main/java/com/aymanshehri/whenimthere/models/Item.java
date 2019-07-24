package com.aymanshehri.whenimthere.models;

public class Item {
    private String title;
    private String details;
    private boolean isGot;

    public Item() {
    }

    public Item(String title, String details, boolean isGot) {
        this.title = title;
        this.details = details;
        this.isGot = isGot;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isGot() {
        return isGot;
    }

    public void setGot(boolean got) {
        isGot = got;
    }
}
