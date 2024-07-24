package com.example.aksam;



public class ProductItem {
    private int id;
    private String name;
    private double price;
    private String imagePath;
    private String category;
    private String size; // Yeni alan
    private int quantity; // Yeni alan

    public ProductItem(int id, String name, double price, String imagePath, String category, String size, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.category = category;
        this.size = size; // Yeni alan
        this.quantity = quantity; // Yeni alan
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
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSize() { return size; } // Yeni metod
    public void setSize(String size) { this.size = size; } // Yeni metod
    public int getQuantity() { return quantity; } // Yeni metod
    public void setQuantity(int quantity) { this.quantity = quantity; } // Yeni metod
}
