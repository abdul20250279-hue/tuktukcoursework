import businesslogic.Cart;
import datamodels.Part;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Shopping Cart Tests")
public class CartTest {

    private Cart cart;
    private Part enginePart;
    private Part electricalPart;
    private Part brakePart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        enginePart = new Part("E001", "Engine Piston", "Bajaj", 1000.0, 10, "Engine", LocalDate.now(), "");
        electricalPart = new Part("EL001", "Spark Plug", "NGK", 500.0, 20, "Electrical", LocalDate.now(), "");
        brakePart = new Part("B001", "Brake Pad", "TVS", 300.0, 15, "Brakes", LocalDate.now(), "");
    }

    //add item test

    @Test
    @DisplayName("Test 1: Add valid item to cart")
    void testAddValidItem() {
        cart.addItem(enginePart, 2);

        // Check stock was reduced
        assertEquals(8, enginePart.getQuantity());
    }

    @Test
    @DisplayName("Test 2: Add item with zero quantity - throws exception")
    void testAddZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem(enginePart, 0);
        });
    }

    @Test
    @DisplayName("Test 3: Add item with negative quantity - throws exception")
    void testAddNegativeQuantity() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem(enginePart, -5);
        });
    }

    @Test
    @DisplayName("Test 4: Add item with insufficient stock - throws exception")
    void testAddInsufficientStock() {
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem(enginePart, 20);
        });
    }
//discount test

    @Test
    @DisplayName("Test 5: No discount for less than 3 items")
    void testNoDiscount() {
        cart.addItem(brakePart, 2);
        double total = cart.calculateTotal();
        assertEquals(600.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 6: Bulk discount - 5% for 3+ units")
    void testBulkDiscount() {
        cart.addItem(brakePart, 3);
        double total = cart.calculateTotal();
        assertEquals(855.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 7: Bulk discount with 4 units")
    void testBulkDiscount4Units() {
        cart.addItem(brakePart, 4);
        double total = cart.calculateTotal();
        assertEquals(1140.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 8: Synergy discount - 10% for Engine + Electrical")
    void testSynergyDiscount() {
        cart.addItem(enginePart, 1);
        cart.addItem(electricalPart, 1);

        double total = cart.calculateTotal();
        assertEquals(1350.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 9: Both discounts - bulk + synergy")
    void testBothDiscounts() {
        cart.addItem(enginePart, 3);
        cart.addItem(electricalPart, 1);

        double total = cart.calculateTotal();
        assertEquals(3015.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 10: Empty cart total is 0")
    void testEmptyCartTotal() {
        double total = cart.calculateTotal();
        assertEquals(0.00, total, 0.01);
    }

    @Test
    @DisplayName("Test 11: Check if cart is empty")
    void testCartIsEmpty() {
        assertTrue(cart.isEmpty());

        cart.addItem(enginePart, 1);
        assertFalse(cart.isEmpty());
    }
}