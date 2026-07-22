package businesslogic;

import datamodels.Part;
import java.util.ArrayList;
import java.util.List;

public class InventorySearchService {

    public List<Part> search(List<Part> parts, String category, Double minPrice, Double maxPrice) {
        List<Part> results = new ArrayList<>();

        for (Part part : parts) {
            if (category != null && !category.trim().isEmpty()) {
                if (!part.getCategory().equalsIgnoreCase(category.trim())) {
                    continue;
                }
            }
            if (minPrice != null && part.getPrice() < minPrice) continue;
            if (maxPrice != null && part.getPrice() > maxPrice) continue;

            results.add(part);
        }
        return results;
    }
}
