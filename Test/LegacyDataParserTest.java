import fileparsing.LegacyDataParser;
import datamodels.dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import datamodels.Part;
import java.time.LocalDate;

@DisplayName("Legacy Data Parser Tests")
public class LegacyDataParserTest {

    private LegacyDataParser parser;

    @BeforeEach
    void setUp() {
        parser = new LegacyDataParser();
    }

    // dealer test
    @Test
    @DisplayName("Test 1: Parse dealer with comma")
    void testParseDealerWithComma() {
        dealer dealer = parser.parseDealerLine("D101, Sunil Motors, 0771234567, Malabe");

        assertEquals("D101", dealer.getCode());
        assertEquals("Sunil Motors", dealer.getName());
        assertEquals("0771234567", dealer.getPhone());
        assertEquals("Malabe", dealer.getLocation());
    }

    @Test
    @DisplayName("Test 2: Parse dealer with pipe")
    void testParseDealerWithPipe() {
        dealer dealer = parser.parseDealerLine("D102 |Kaduwela Spares Hub |0719876543 |Kaduwela");

        assertEquals("D102", dealer.getCode());
        assertEquals("Kaduwela Spares Hub", dealer.getName());
        assertEquals("0719876543", dealer.getPhone());
        assertEquals("Kaduwela", dealer.getLocation());
    }

    @Test
    @DisplayName("Test 3: Parse dealer with semicolon")
    void testParseDealerWithSemicolon() {
        dealer dealer = parser.parseDealerLine("D103; Ranatunga Auto; Pittugala");

        assertEquals("D103", dealer.getCode());
        assertEquals("Ranatunga Auto", dealer.getName());
        assertEquals("Pittugala", dealer.getLocation());
    }

    @Test
    @DisplayName("Test 4: Parse dealer with missing phone")
    void testParseDealerMissingPhone() {
        dealer dealer = parser.parseDealerLine("D107, Koswatta Three-Wheelers, Koswatta");

        assertEquals("D107", dealer.getCode());
        assertEquals("Koswatta Three-Wheelers", dealer.getName());
        assertEquals("Koswatta", dealer.getLocation());
    }
    // inventory test

    @Test
    @DisplayName("Test 5: Parse inventory with comma")
    void testParseInventoryWithComma() {
        Part part = parser.parseInventoryLine("P001, Bajaj Piston, Bajaj, Rs.4500.00, 15, Engine, 2023-10-12, piston.jpg");

        assertEquals("P001", part.getCode());
        assertEquals("Bajaj Piston", part.getName());
        assertEquals(4500.00, part.getPrice(), 0.01);
        assertEquals(15, part.getQuantity());
        assertEquals("Engine", part.getCategory());
        assertEquals(LocalDate.of(2023, 10, 12), part.getDateAdded());
    }

    @Test
    @DisplayName("Test 6: Parse inventory with pipe")
    void testParseInventoryWithPipe() {
        Part part = parser.parseInventoryLine("P002 |TVS King Brake Pad |TVS |1250 |8 |Brakes |12/05/2023 |brakepad.png");

        assertEquals("P002", part.getCode());
        assertEquals(1250.00, part.getPrice(), 0.01);
        assertEquals(8, part.getQuantity());
        assertEquals("Brakes", part.getCategory());
    }

    @Test
    @DisplayName("Test 7: Parse inventory with semicolon")
    void testParseInventoryWithSemicolon() {
        Part part = parser.parseInventoryLine("P003; 205/50-10 Tyre ; 6500.00; 24; Bodywork; Oct 15, 2023;");

        assertEquals("P003", part.getCode());
        assertEquals(6500.00, part.getPrice(), 0.01);
        assertEquals(24, part.getQuantity());
        assertEquals("Bodywork", part.getCategory());
        assertEquals(LocalDate.of(2023, 10, 15), part.getDateAdded());
    }

    @Test
    @DisplayName("Test 8: Parse price with Rs. format")
    void testParsePriceWithRsDot() {
        Part part = parser.parseInventoryLine("P006 ; Headlight Bulb 12V ; Rs. 450 | 30 ; ELECTRICAL ; 2023/11/20 ; hl_bulb.jpg");
        assertEquals(450.00, part.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Test 9: Parse price with Rs format")
    void testParsePriceWithRsNoDot() {
        Part part = parser.parseInventoryLine("P004 | Spark Plug NGK | NGK | Rs850 | 50 | Electrical | 2024-01-05 | spark.jpg");
        assertEquals(850.00, part.getPrice(), 0.01);
    }

    @Test
    @DisplayName("Test 10: Normalize category to proper case")
    void testNormalizeCategory() {
        Part part = parser.parseInventoryLine("P005, Test Part, Sup, 100, 10, ENGINE, 2023-01-01, test.jpg");
        assertEquals("Engine", part.getCategory());
    }

    @Test
    @DisplayName("Test 11: Parse different date formats")
    void testParseDifferentDateFormats() {
        Part part1 = parser.parseInventoryLine("P001, Test, Sup, 100, 10, Engine, 2023-10-12, test.jpg");
        assertEquals(LocalDate.of(2023, 10, 12), part1.getDateAdded());

        Part part2 = parser.parseInventoryLine("P002, Test, Sup, 100, 10, Engine, 12/05/2023, test.jpg");
        assertEquals(LocalDate.of(2023, 5, 12), part2.getDateAdded());

        Part part3 = parser.parseInventoryLine("P003, Test, Sup, 100, 10, Engine, Oct 15, 2023, test.jpg");
        assertEquals(LocalDate.of(2023, 10, 15), part3.getDateAdded());
    }

    @Test
    @DisplayName("Test 12: Handle missing date")
    void testHandleMissingDate() {
        Part part = parser.parseInventoryLine("P001, Test, Sup, 100, 10, Engine, , test.jpg");
        assertNull(part.getDateAdded());
    }

    @Test
    @DisplayName("Test 13: Handle missing image")
    void testHandleMissingImage() {
        Part part = parser.parseInventoryLine("P001, Test, Sup, 100, 10, Engine, 2023-10-12,");
        assertEquals("", part.getImageFile());
    }

    @Test
    @DisplayName("Test 14: Throw exception on invalid record")
    void testThrowExceptionOnInvalidRecord() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseInventoryLine("INVALID, Not enough fields");
        });
    }

    @Test
    @DisplayName("Test 15: Throw exception on empty code")
    void testThrowExceptionOnEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parseInventoryLine(", Empty Name, Sup, 100, 10, Engine, 2023-10-12, test.jpg");
        });
    }
}