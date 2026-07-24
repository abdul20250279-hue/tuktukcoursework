package businesslogic;

import datamodels.Part;
import utility.AuditLogger;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<Part> items = new ArrayList<>();

    public void addItem(Part part, int qty) {
        if (qty <= 0 || qty > part.getQuantity()) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        part.setQuantity(part.getQuantity() - qty);
        AuditLogger.log("Checkout Add", part.getCode(), qty);
        items.add(new Part(part.getCode(), part.getName(), part.getSupplier(),
                part.getPrice(), qty, part.getCategory(), part.getDateAdded(), part.getImageFile()));
    }
