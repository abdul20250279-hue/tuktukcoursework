import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Insets;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.nio.file.Paths;

import businesslogic.Cart;
import businesslogic.InventorySearchService;
import businesslogic.RandomDealerSelector;
import datamodels.Part;
import datamodels.dealer;
import fileparsing.LegacyDataParser;
import utility.ManualSort;
import utility.AuditLogger;

public class MainController implements Initializable {

    @FXML private TableView<Part> inventoryTable;
    @FXML private TableView<Part> cartTable;
    @FXML private TableView<Part> searchResultsTable;
    @FXML private ListView<String> lowStockListView;
    @FXML private Label totalItemsLabel;
    @FXML private Label totalValueLabel;
    @FXML private Label cartTotalLabel;
    @FXML private TextField thresholdField;
    @FXML private ComboBox<String> categoryFilterCombo;
    @FXML private TextField minPriceField;
    @FXML private TextField maxPriceField;
    @FXML private TextField keywordField;
    @FXML private TextArea dealerDisplayArea;

    private ObservableList<Part> inventoryData = FXCollections.observableArrayList();
    private ObservableList<Part> filteredData = FXCollections.observableArrayList();
    private List<dealer> dealerData = new ArrayList<>();
    private int lowStockThreshold = 10;

    private Cart cart = new Cart();
    private LegacyDataParser parser = new LegacyDataParser();
    private InventorySearchService searchService = new InventorySearchService();
    private RandomDealerSelector dealerSelector = new RandomDealerSelector();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTables();
        setupCategoryFilter();
        loadInventoryData();
        loadDealerData();
        updateTotals();
        updateCartTotal();
        updateLowStockDisplay();
    }

    private void setupTables() {
        TableColumn<Part, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Part, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> supplierCol = new TableColumn<>("Supplier");
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplier"));
        TableColumn<Part, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Part, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        inventoryTable.getColumns().addAll(codeCol, nameCol, supplierCol, priceCol, qtyCol, categoryCol);
        inventoryTable.setItems(filteredData);

        TableColumn<Part, String> cartCode = new TableColumn<>("Code");
        cartCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Part, String> cartName = new TableColumn<>("Name");
        cartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, Double> cartPrice = new TableColumn<>("Price");
        cartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part, Integer> cartQty = new TableColumn<>("Quantity");
        cartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        cartTable.getColumns().addAll(cartCode, cartName, cartPrice, cartQty);
        cartTable.setItems(FXCollections.observableArrayList(cart.getItems()));

        TableColumn<Part, String> srchCode = new TableColumn<>("Code");
        srchCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Part, String> srchName = new TableColumn<>("Name");
        srchName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, String> srchCategory = new TableColumn<>("Category");
        srchCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<Part, Double> srchPrice = new TableColumn<>("Price");
        srchPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part, Integer> srchQty = new TableColumn<>("Quantity");
        srchQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        searchResultsTable.getColumns().addAll(srchCode, srchName, srchCategory, srchPrice, srchQty);
        searchResultsTable.setItems(filteredData);
    }

    private void setupCategoryFilter() {
        categoryFilterCombo.getItems().addAll("All", "Engine", "Electrical", "Brakes", "Bodywork", "Other");
        categoryFilterCombo.setValue("All");
    }

    private void loadInventoryData() {
        try {
            List<Part> parts = parser.loadInventory(Paths.get("inventory_legacy.txt"));
            inventoryData.addAll(parts);
            filteredData.addAll(parts);
            ManualSort.sortByCategoryThenCode(filteredData);
            if (!parser.getSkippedLines().isEmpty()) {
                showAlert("Parsing Warnings", "Some records were skipped:\n" + String.join("\n", parser.getSkippedLines()));
            }
        } catch (IOException e) {
            addSampleInventoryData();
        }
    }

    private void loadDealerData() {
        try {
            dealerData = parser.loadDealers(Paths.get("dealers_legacy.txt"));
        } catch (IOException e) {
            addSampleDealerData();
        }
    }

    private void addSampleInventoryData() {
        inventoryData.addAll(
                new Part("P001", "Bajaj Piston", "Bajaj", 4500.00, 15, "Engine", LocalDate.now(), ""),
                new Part("P002", "TVS Brake Pad", "TVS", 1250.00, 8, "Brakes", LocalDate.now(), ""),
                new Part("P003", "Tyre", "Local", 6500.00, 24, "Bodywork", LocalDate.now(), ""),
                new Part("P004", "Spark Plug", "NGK", 500.00, 20, "Electrical", LocalDate.now(), "")
        );
        filteredData.addAll(inventoryData);
        ManualSort.sortByCategoryThenCode(filteredData);
    }

    private void addSampleDealerData() {
        dealerData.add(new dealer("D101", "Sunil Motors", "0771234567", "Malabe"));
        dealerData.add(new dealer("D102", "Kaduwela Spares", "0719876543", "Kaduwela"));
        dealerData.add(new dealer("D103", "Ranatunga Auto", "N/A", "Pittugala"));
        dealerData.add(new dealer("D104", "Maharagama Tuk Parts", "0705556666", "Maharagama"));
    }

    private void updateTotals() {
        int totalItems = 0;
        double totalValue = 0.0;
        for (Part p : inventoryData) {
            totalItems += p.getQuantity();
            totalValue += p.getTotalValue();
        }
        totalItemsLabel.setText("Total Items: " + totalItems);
        totalValueLabel.setText(String.format("Total Value: Rs. %.2f", totalValue));
    }

    private void updateCartTotal() {
        double total = cart.calculateTotal();
        cartTotalLabel.setText(String.format("Cart Total: Rs. %.2f", total));
    }

    private void updateLowStockDisplay() {
        lowStockListView.getItems().clear();
        for (Part p : inventoryData) {
            if (p.getQuantity() < lowStockThreshold) {
                lowStockListView.getItems().add(p.getCode() + " - " + p.getName() + " (Qty: " + p.getQuantity() + ")");
            }
        }
    }

    @FXML
    private void refreshAll() {
        filteredData.clear();
        filteredData.addAll(inventoryData);
        ManualSort.sortByCategoryThenCode(filteredData);
        inventoryTable.refresh();
        updateTotals();
        updateLowStockDisplay();
        updateCartTotal();
    }

    @FXML
    private void showAddPartDialog() {
        Dialog<Part> dialog = new Dialog<>();
        dialog.setTitle("Add New Part");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField codeField = new TextField();
        TextField nameField = new TextField();
        TextField supplierField = new TextField();
        TextField priceField = new TextField();
        TextField quantityField = new TextField();
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Engine", "Electrical", "Brakes", "Bodywork", "Other");
        categoryCombo.setValue("Other");

        grid.add(new Label("Code:"), 0, 0);
        grid.add(codeField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Supplier:"), 0, 2);
        grid.add(supplierField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Quantity:"), 0, 4);
        grid.add(quantityField, 1, 4);
        grid.add(new Label("Category:"), 0, 5);
        grid.add(categoryCombo, 1, 5);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    String code = codeField.getText().trim();
                    String name = nameField.getText().trim();
                    String supplier = supplierField.getText().trim();
                    double price = Double.parseDouble(priceField.getText().trim());
                    int quantity = Integer.parseInt(quantityField.getText().trim());
                    String category = categoryCombo.getValue();

                    if (code.isEmpty() || name.isEmpty()) {
                        showAlert("Error", "Code and Name are required.");
                        return null;
                    }

                    for (Part p : inventoryData) {
                        if (p.getCode().equalsIgnoreCase(code)) {
                            showAlert("Error", "Part code already exists.");
                            return null;
                        }
                    }

                    Part newPart = new Part(code, name, supplier, price, quantity, category, LocalDate.now(), "");
                    inventoryData.add(newPart);
                    filteredData.add(newPart);
                    ManualSort.sortByCategoryThenCode(filteredData);
                    refreshAll();
                    AuditLogger.logAddPart(code, quantity);
                    showAlert("Success", "Part added successfully.");
                    return newPart;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter valid numbers.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void showEditPartDialog() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a part to edit.");
            return;
        }

        Dialog<Part> dialog = new Dialog<>();
        dialog.setTitle("Edit Part");
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField nameField = new TextField(selected.getName());
        TextField supplierField = new TextField(selected.getSupplier());
        TextField priceField = new TextField(String.valueOf(selected.getPrice()));
        TextField quantityField = new TextField(String.valueOf(selected.getQuantity()));
        ComboBox<String> categoryCombo = new ComboBox<>();
        categoryCombo.getItems().addAll("Engine", "Electrical", "Brakes", "Bodywork", "Other");
        categoryCombo.setValue(selected.getCategory());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Supplier:"), 0, 1);
        grid.add(supplierField, 1, 1);
        grid.add(new Label("Price:"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Quantity:"), 0, 3);
        grid.add(quantityField, 1, 3);
        grid.add(new Label("Category:"), 0, 4);
        grid.add(categoryCombo, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    selected.setName(nameField.getText().trim());
                    selected.setSupplier(supplierField.getText().trim());
                    selected.setPrice(Double.parseDouble(priceField.getText().trim()));
                    selected.setQuantity(Integer.parseInt(quantityField.getText().trim()));
                    selected.setCategory(categoryCombo.getValue());
                    ManualSort.sortByCategoryThenCode(filteredData);
                    refreshAll();
                    showAlert("Success", "Part updated successfully.");
                    return selected;
                } catch (NumberFormatException e) {
                    showAlert("Error", "Please enter valid numbers.");
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    @FXML
    private void deleteSelectedPart() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a part to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setContentText("Delete " + selected.getCode() + "?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            inventoryData.remove(selected);
            filteredData.remove(selected);
            refreshAll();
            AuditLogger.logDeletePart(selected.getCode(), selected.getQuantity());
            showAlert("Success", "Part deleted.");
        }
    }

    @FXML
    private void saveInventoryToFile() {
        try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter("inventory_saved.txt"))) {
            for (Part p : inventoryData) {
                writer.println(p.toFileLine());
            }
            showAlert("Success", "Inventory saved to inventory_saved.txt");
        } catch (IOException e) {
            showAlert("Error", "Failed to save: " + e.getMessage());
        }
    }

    @FXML
    private void updateLowStockThreshold() {
        try {
            int newThreshold = Integer.parseInt(thresholdField.getText().trim());
            if (newThreshold < 0) {
                showAlert("Error", "Threshold cannot be negative.");
                return;
            }
            lowStockThreshold = newThreshold;
            for (Part p : inventoryData) {
                p.setLowStockThreshold(newThreshold);
            }
            updateLowStockDisplay();
            showAlert("Success", "Threshold updated to " + newThreshold);
        } catch (NumberFormatException e) {
            showAlert("Error", "Enter a valid number.");
        }
    }

    @FXML
    private void performSearch() {
        String category = categoryFilterCombo.getValue();
        if ("All".equals(category)) category = null;

        Double minPrice = null;
        Double maxPrice = null;
        try {
            if (!minPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
            }
            if (!maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid price range.");
            return;
        }

        String keyword = keywordField.getText().trim().isEmpty() ? null : keywordField.getText().trim();

        List<Part> results = searchService.search(inventoryData, category, minPrice, maxPrice, keyword);
        filteredData.clear();
        filteredData.addAll(results);
        showAlert("Search Results", "Found " + results.size() + " parts.");
        inventoryTable.refresh();
    }

    @FXML
    private void clearSearch() {
        categoryFilterCombo.setValue("All");
        minPriceField.clear();
        maxPriceField.clear();
        keywordField.clear();
        filteredData.clear();
        filteredData.addAll(inventoryData);
        ManualSort.sortByCategoryThenCode(filteredData);
        inventoryTable.refresh();
    }

    @FXML
    private void addFromSearchToCart() {
        Part selected = searchResultsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                cart.addItem(selected, 1);
                updateCartTotal();
                showAlert("Success", "Added to cart.");
            } catch (IllegalArgumentException e) {
                showAlert("Error", e.getMessage());
            }
        } else {
            showAlert("Error", "Select a part first.");
        }
    }

    @FXML
    private void showAddToCartDialog() {
        Part selected = inventoryTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a part first.");
            return;
        }

        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Add to Cart");
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        Spinner<Integer> quantitySpinner = new Spinner<>(1, selected.getQuantity(), 1);
        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Quantity:"), quantitySpinner);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> dialogButton == addButtonType ? quantitySpinner.getValue() : null);
        dialog.showAndWait().ifPresent(qty -> {
            try {
                cart.addItem(selected, qty);
                refreshAll();
                updateCartTotal();
                showAlert("Success", "Added " + qty + " to cart.");
            } catch (IllegalArgumentException e) {
                showAlert("Error", e.getMessage());
            }
        });
    }

    @FXML
    private void removeFromCart() {
        Part selected = cartTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select an item to remove.");
            return;
        }

        for (Part p : inventoryData) {
            if (p.getCode().equals(selected.getCode())) {
                p.setQuantity(p.getQuantity() + selected.getQuantity());
                break;
            }
        }

        cart.getItems().remove(selected);
        refreshAll();
        updateCartTotal();
    }

    @FXML
    private void clearCart() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Clear entire cart?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            for (Part cartItem : cart.getItems()) {
                for (Part p : inventoryData) {
                    if (p.getCode().equals(cartItem.getCode())) {
                        p.setQuantity(p.getQuantity() + cartItem.getQuantity());
                        break;
                    }
                }
            }
            cart.getItems().clear();
            refreshAll();
            updateCartTotal();
        }
    }

    @FXML
    private void processCheckout() {
        if (cart.getItems().isEmpty()) {
            showAlert("Error", "Cart is empty.");
            return;
        }

        double total = cart.calculateTotal();
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("Total: Rs. " + String.format("%.2f", total) + "\nProceed?");
        if (confirm.showAndWait().get() == ButtonType.OK) {
            for (Part item : cart.getItems()) {
                AuditLogger.logCheckout(item.getCode(), item.getQuantity());
            }
            cart.getItems().clear();
            refreshAll();
            updateCartTotal();
            showAlert("Success", "Checkout complete! Total: Rs. " + String.format("%.2f", total));
        }
    }

    @FXML
    private void showAllDealers() {
        if (dealerData.isEmpty()) {
            dealerDisplayArea.setText("No dealer data available.");
            return;
        }

        StringBuilder sb = new StringBuilder("=== ALL DEALERS ===\n\n");
        for (dealer d : dealerData) {
            sb.append(d.toString()).append("\n");
        }
        dealerDisplayArea.setText(sb.toString());
    }

    @FXML
    private void selectRandomDealers() {
        if (dealerData.size() < 4) {
            dealerDisplayArea.setText("Need at least 4 dealers. Found: " + dealerData.size());
            return;
        }

        List<dealer> selected = dealerSelector.selectFourUniqueSortedByLocation(dealerData);
        StringBuilder sb = new StringBuilder("=== 4 RANDOM DEALERS (Sorted by Location) ===\n\n");
        for (dealer d : selected) {
            sb.append(d.toString()).append("\n");
        }
        dealerDisplayArea.setText(sb.toString());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}