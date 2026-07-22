package businesslogic;

import datamodels.Part;
import java.util.ArrayList;
import java.util.List;

public class InventorySearchService {

    public List<Part> search(List<Part> parts, String category, Double minPrice,
                             Double maxPrice, String keyword) {
        List<Part> results = new ArrayList<>();

        for (Part part : parts) {
            if (category != null && !category.trim().isEmpty()) {
                if (!part.getCategory().equalsIgnoreCase(category.trim())) {
                    continue;
                }
            }
            if (minPrice != null && part.getPrice() < minPrice) continue;
            if (maxPrice != null && part.getPrice() > maxPrice) continue;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String search = keyword.trim().toLowerCase();
                boolean nameMatches = part.getName().toLowerCase().contains(search);
                boolean codeMatches = part.getCode().toLowerCase().contains(search);
                if (!nameMatches && !codeMatches) continue;
            }

            results.add(part);
        }
        return results;
    }
}
