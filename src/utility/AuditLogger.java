package utility;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditLogger {
    private static final String LOG_FILE = "audit_log.txt";

    public static void log(String action, String itemCode, int quantity) {
        try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
            writer.write(LocalDateTime.now() + " | " + action + " | " + itemCode + " | qty=" + quantity + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
    public static void logAddPart(String itemCode, int quantity) {
        log("Add Part", itemCode, quantity);
    }

    public static void logDeletePart(String itemCode, int quantity) {
        log("Delete Part", itemCode, quantity);
    }

    public static void logCheckout(String itemCode, int quantity) {
        log("Checkout Transaction", itemCode, quantity);
    }
}
