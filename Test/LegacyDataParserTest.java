import fileparsing.LegacyDataParser;
import datamodels.dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.testng.AssertJUnit.assertEquals;

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
