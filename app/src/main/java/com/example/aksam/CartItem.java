package com.example.aksam;



public class CartItem {
    private int id;
    private String name;
    private double price;
    private String imagePath;
    private String size;
    private String category; // Yeni alan
    private int quantity;

    public CartItem(String name, double price, String imagePath, String size, String category, int quantity) {
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.size = size;
        this.category = category;
        this.quantity = quantity;
    }

    public CartItem(int id, String name, double price, String imagePath, String size, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.size = size;
        this.category = category;
        this.quantity = quantity;
    }

    // Getter ve Setter metodları
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
