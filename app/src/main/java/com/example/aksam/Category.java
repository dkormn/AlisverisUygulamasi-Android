package com.example.aksam;

public class Category {
    private int id;
    private String categoryName;

    public Category(int id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public int getId() { return id; }
    public String getCategoryName() { return categoryName; }

    public void setId(int id) { this.id = id; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    @Override
    public String toString() {
        return categoryName;
    }
}
