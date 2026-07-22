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
