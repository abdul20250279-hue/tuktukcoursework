package datamodels;

import java.time.LocalDate;

public class Part {
    public static final int DEFAULT_LOW_STOCK_THRESHOLD = 10;

    private String code;
    private String name;
    private String supplier;
    private double price;
    private int quantity;
    private String category;
    private LocalDate dateAdded;
    private String imageFile;
    private int lowStockThreshold = DEFAULT_LOW_STOCK_THRESHOLD;
}

    public Part(String code, String name, String supplier, double price,
                int quantity, String category, LocalDate dateAdded, String imageFile) {
        this.code = code;
        this.name = name;
        this.supplier = supplier;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.imageFile = imageFile;
    }
    public boolean isLowStock() {
        return quantity < lowStockThreshold;
    }

    public double getTotalValue() {
        return price * quantity;
    }


    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDate getDateAdded() { return dateAdded; }
    public void setDateAdded(LocalDate dateAdded) { this.dateAdded = dateAdded; }
    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }
    public int getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(int lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public String toFileLine() {
        String supplierText = supplier == null ? "" : supplier;
        String dateText = dateAdded == null ? "" : dateAdded.toString();
        String imageText = imageFile == null ? "" : imageFile;
        return code + "|" + name + "|" + supplierText + "|" + price + "|" + quantity + "|"
                + category + "|" + dateText + "|" + imageText + "|" + lowStockThreshold;
}

    public String toString() {
    return code + " - " + name + " (" + category + ") qty=" + quantity + " price=" + price;
    }
}
