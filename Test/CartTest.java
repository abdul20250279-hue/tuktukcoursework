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
