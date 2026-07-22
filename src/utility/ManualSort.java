package utility;

import datamodels.dealer;
import datamodels.Part;
import java.util.List;

public class ManualSort {

    private ManualSort() {
    }

    public static List<Part> sortByCategoryThenCode(List<Part> parts) {
        for (int i = 1; i < parts.size(); i++) {
            Part current = parts.get(i);
            int j = i - 1;
            while (j >= 0 && comparePartsByCategoryThenCode(parts.get(j), current) > 0) {
                parts.set(j + 1, parts.get(j));
                j--;
            }
            parts.set(j + 1, current);
        }
        return parts;
    }
    private static int comparePartsByCategoryThenCode(Part a, Part b) {
        int categoryResult = compareStrings(a.getCategory(), b.getCategory());
        if (categoryResult != 0) {
            return categoryResult;
        }
        return compareStrings(a.getCode(), b.getCode());
    }
    
    private static int compareStrings(String a, String b) {
        if (a == null) {
            a = "";
        }
        if (b == null) {
            b = "";
        }
        int shortestLength = Math.min(a.length(), b.length());
        for (int i = 0; i < shortestLength; i++) {
            char charA = Character.toLowerCase(a.charAt(i));
            char charB = Character.toLowerCase(b.charAt(i));
            if (charA != charB) {
                return charA - charB;
            }
        }
        return a.length() - b.length();
    }
}