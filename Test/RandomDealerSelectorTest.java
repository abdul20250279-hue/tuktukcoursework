import businesslogic.RandomDealerSelector;
import datamodels.dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Random Dealer Selector Tests")
public class RandomDealerSelectorTest {

    private RandomDealerSelector selector;
    private List<dealer> dealers;

    @BeforeEach
    void setUp() {
        selector = new RandomDealerSelector();
        dealers = new ArrayList<>();
        dealers.add(new dealer("D101", "Sunil Motors", "0771234567", "Malabe"));
        dealers.add(new dealer("D102", "Kaduwela Spares", "0719876543", "Kaduwela"));
        dealers.add(new dealer("D103", "Ranatunga", "N/A", "Pittugala"));
        dealers.add(new dealer("D104", "Maharagama", "0705556666", "Maharagama"));
        dealers.add(new dealer("D105", "Nimal & Sons", "0778889999", "Malabe"));
        dealers.add(new dealer("D106", "Athurugiriya", "0721112222", "Athurugiriya"));
    }

    @Test
    @DisplayName("Test 1: Select exactly 4 dealers")
    void testSelectFourDealers() {
        List<dealer> selected = selector.selectFourUniqueSortedByLocation(dealers);
        assertEquals(4, selected.size());
    }

    @Test
    @DisplayName("Test 2: Selected dealers are unique")
    void testSelectedDealersAreUnique() {
        List<dealer> selected = selector.selectFourUniqueSortedByLocation(dealers);

        for (int i = 0; i < selected.size(); i++) {
            for (int j = i + 1; j < selected.size(); j++) {
                assertNotEquals(selected.get(i).getCode(), selected.get(j).getCode());
            }
        }
    }

    @Test
    @DisplayName("Test 3: Selected dealers are sorted by location")
    void testSelectedDealersSortedByLocation() {
        List<dealer> selected = selector.selectFourUniqueSortedByLocation(dealers);

        for (int i = 0; i < selected.size() - 1; i++) {
            String current = selected.get(i).getLocation();
            String next = selected.get(i + 1).getLocation();
            assertTrue(current.compareToIgnoreCase(next) <= 0);
        }
    }

    @Test
    @DisplayName("Test 4: Throw exception when less than 4 dealers")
    void testThrowExceptionWhenLessThan4Dealers() {
        List<dealer> smallList = new ArrayList<>();
        smallList.add(new dealer("D001", "Dealer1", "123", "Loc1"));
        smallList.add(new dealer("D002", "Dealer2", "456", "Loc2"));
        smallList.add(new dealer("D003", "Dealer3", "789", "Loc3"));

        assertThrows(IllegalArgumentException.class, () -> {
            selector.selectFourUniqueSortedByLocation(smallList);
        });
    }

    @Test
    @DisplayName("Test 5: Throw exception on null list")
    void testThrowExceptionOnNullList() {
        assertThrows(IllegalArgumentException.class, () -> {
            selector.selectFourUniqueSortedByLocation(null);
        });
    }
}