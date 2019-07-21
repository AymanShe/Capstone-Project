package com.aymanshehri.whenimthere;

public class Item {
    private String title;
    private String details;
    private boolean isGot;

    public Item() {
    }

    Item(String title, String details, boolean isGot) {
        this.title = title;
        this.details = details;
        this.isGot = isGot;
    }

    String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    boolean isGot() {
        return isGot;
    }

    public void setGot(boolean got) {
        isGot = got;
    }
}
