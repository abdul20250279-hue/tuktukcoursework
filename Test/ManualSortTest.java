import datamodels.Part;
import datamodels.dealer;
import utility.ManualSort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Manual Sorting Tests")
public class ManualSortTest {

    //part sort test
    @Test
    @DisplayName("Test 1: Sort parts by category then code")
    void testSortPartsByCategoryThenCode() {
        List<Part> parts = new ArrayList<>();
        parts.add(new Part("P003", "Tyre", "Local", 400, 8, "Bodywork", LocalDate.now(), ""));
        parts.add(new Part("P001", "Engine Piston", "Bajaj", 1000, 10, "Engine", LocalDate.now(), ""));
        parts.add(new Part("P002", "Brake Pad", "TVS", 300, 15, "Brakes", LocalDate.now(), ""));
        parts.add(new Part("P004", "Spark Plug", "NGK", 500, 20, "Electrical", LocalDate.now(), ""));

        ManualSort.sortByCategoryThenCode(parts);

        assertEquals("Bodywork", parts.get(0).getCategory());
        assertEquals("Brakes", parts.get(1).getCategory());
        assertEquals("Electrical", parts.get(2).getCategory());
        assertEquals("Engine", parts.get(3).getCategory());
    }

    @Test
    @DisplayName("Test 2: Sort parts with same category by code")
    void testSortPartsSameCategoryByCode() {
        List<Part> parts = new ArrayList<>();
        parts.add(new Part("P002", "Part2", "Sup", 100, 5, "Engine", LocalDate.now(), ""));
        parts.add(new Part("P001", "Part1", "Sup", 100, 5, "Engine", LocalDate.now(), ""));
        parts.add(new Part("P003", "Part3", "Sup", 100, 5, "Engine", LocalDate.now(), ""));

        ManualSort.sortByCategoryThenCode(parts);

        assertEquals("P001", parts.get(0).getCode());
        assertEquals("P002", parts.get(1).getCode());
        assertEquals("P003", parts.get(2).getCode());
    }

    @Test
    @DisplayName("Test 3: Sort empty parts list")
    void testSortEmptyPartsList() {
        List<Part> parts = new ArrayList<>();
        ManualSort.sortByCategoryThenCode(parts);
        assertTrue(parts.isEmpty());
    }

    //dealer sort test
    @Test
    @DisplayName("Test 4: Sort dealers by location")
    void testSortDealersByLocation() {
        List<dealer> dealers = new ArrayList<>();
        dealers.add(new dealer("D104", "Maharagama", "0705556666", "Maharagama"));
        dealers.add(new dealer("D101", "Sunil Motors", "0771234567", "Malabe"));
        dealers.add(new dealer("D102", "Kaduwela Spares", "0719876543", "Kaduwela"));
        dealers.add(new dealer("D103", "Ranatunga", "N/A", "Pittugala"));

        ManualSort.sortByLocation(dealers);

        assertEquals("Kaduwela", dealers.get(0).getLocation());
        assertEquals("Maharagama", dealers.get(1).getLocation());
        assertEquals("Malabe", dealers.get(2).getLocation());
        assertEquals("Pittugala", dealers.get(3).getLocation());
    }
    @Test
    @DisplayName("Test 5: Sort empty dealers list")
    void testSortEmptyDealersList() {
        List<dealer> dealers = new ArrayList<>();
        ManualSort.sortByLocation(dealers);
        assertTrue(dealers.isEmpty());
    }
}