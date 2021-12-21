package com.daybook.db.model;

public class ColorItem {
    private int id;
    private String haxString;
    private boolean isSelected;

    public ColorItem(int id, String haxString, boolean isSelected) {
        this.id = id;
        this.haxString = haxString;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHaxString() {
        return haxString;
    }

    public void setHaxString(String haxString) {
        this.haxString = haxString;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
