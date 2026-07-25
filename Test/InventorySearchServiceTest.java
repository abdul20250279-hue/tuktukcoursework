import businesslogic.InventorySearchService;
import datamodels.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Inventory Search Tests")
public class InventorySearchServiceTest {

    private InventorySearchService service;
    private List<Part> parts;

    @BeforeEach
    void setUp() {
        service = new InventorySearchService();
        parts = new ArrayList<>();
        parts.add(new Part("P001", "Engine Piston", "Bajaj", 1000.0, 10, "Engine", LocalDate.now(), ""));
        parts.add(new Part("P002", "Brake Pad", "TVS", 300.0, 15, "Brakes", LocalDate.now(), ""));
        parts.add(new Part("P003", "Spark Plug", "NGK", 500.0, 20, "Electrical", LocalDate.now(), ""));
        parts.add(new Part("P004", "Air Filter", "Piaggio", 150.0, 5, "Engine", LocalDate.now(), ""));
        parts.add(new Part("P005", "Tyre", "Local", 400.0, 8, "Bodywork", LocalDate.now(), ""));
    }

    //single criteria test

    @Test
    @DisplayName("Test 1: Search by category")
    void testSearchByCategory() {
        List<Part> results = service.search(parts, "Engine", null, null, null);
        assertEquals(2, results.size());
        assertEquals("Engine", results.get(0).getCategory());
        assertEquals("Engine", results.get(1).getCategory());
    }

    @Test
    @DisplayName("Test 2: Search by price range")
    void testSearchByPriceRange() {
        List<Part> results = service.search(parts, null, 200.0, 600.0, null);
        assertEquals(3, results.size()); // Brake Pad (300), Spark Plug (500), Tyre (400)
    }

    @Test
    @DisplayName("Test 3: Search by keyword")
    void testSearchByKeyword() {
        List<Part> results = service.search(parts, null, null, null, "Brake");
        assertEquals(1, results.size());
        assertEquals("P002", results.get(0).getCode());
    }

    @Test
    @DisplayName("Test 4: Keyword search is case insensitive")
    void testKeywordSearchCaseInsensitive() {
        List<Part> results = service.search(parts, null, null, null, "brake");
        assertEquals(1, results.size());
        assertEquals("P002", results.get(0).getCode());
    }

    @Test
    @DisplayName("Test 5: Search with no filters returns all")
    void testSearchNoFilters() {
        List<Part> results = service.search(parts, null, null, null, null);
        assertEquals(5, results.size());
    }

    @Test
    @DisplayName("Test 6: Search with no matches returns empty")
    void testSearchNoMatches() {
        List<Part> results = service.search(parts, null, null, null, "NonExistent");
        assertEquals(0, results.size());
    }
