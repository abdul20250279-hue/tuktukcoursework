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
    public double calculateTotal() {
        double total = 0.0;
        boolean hasEngine = false, hasElectrical = false;

        for (Part item : items) {
            double subtotal = item.getPrice() * item.getQuantity();
            if (item.getQuantity() >= 3) {
                subtotal *= 0.95; // bulk discount
            }
            if (item.getCategory().equalsIgnoreCase("Engine")) hasEngine = true;
            if (item.getCategory().equalsIgnoreCase("Electrical")) hasElectrical = true;
            total += subtotal;
        }

        if (hasEngine && hasElectrical) {
            total *= 0.90; // synergy discount
        }

        return total;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
