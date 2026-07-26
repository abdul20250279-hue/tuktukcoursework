import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TableView<Part> inventoryTable;
    @FXML private Label totalItemsLabel;
    @FXML private Label totalValueLabel;

    private ObservableList<Part> inventoryData = FXCollections.observableArrayList();
    private ObservableList<Part> filteredData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        addSampleData();
        updateTotals();
    }

    private void setupTable() {
        TableColumn<Part, String> codeCol = new TableColumn<>("Code");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        TableColumn<Part, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Part, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Part, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        TableColumn<Part, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        inventoryTable.getColumns().addAll(codeCol, nameCol, priceCol, qtyCol, categoryCol);
        inventoryTable.setItems(filteredData);
    }

    private void addSampleData() {
        inventoryData.add(new Part("P001", "Bajaj Piston", "Bajaj", 4500.00, 15, "Engine", LocalDate.now(), ""));
        inventoryData.add(new Part("P002", "TVS Brake Pad", "TVS", 1250.00, 8, "Brakes", LocalDate.now(), ""));
        inventoryData.add(new Part("P003", "Tyre", "Local", 6500.00, 24, "Bodywork", LocalDate.now(), ""));
        filteredData.addAll(inventoryData);
    }

    private void updateTotals() {
        int totalItems = 0;
        double totalValue = 0.0;
        for (Part p : inventoryData) {
            totalItems += p.getQuantity();
            totalValue += p.getPrice() * p.getQuantity();
        }
        totalItemsLabel.setText("Total Items: " + totalItems);
        totalValueLabel.setText(String.format("Total Value: Rs. %.2f", totalValue));
    }

    @FXML
    private void refreshAll() {
        filteredData.clear();
        filteredData.addAll(inventoryData);
        inventoryTable.refresh();
        updateTotals();
    }

    public static class Part {
        private String code, name, supplier, category, imageFile;
        private double price;
        private int quantity;
        private LocalDate dateAdded;

        public Part(String code, String name, String supplier, double price,
                    int quantity, String category, LocalDate dateAdded, String imageFile) {
            this.code = code;
            this.name = name;
            this.supplier = supplier;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
            this.dateAdded = dateAdded;
            this.imageFile = imageFile;
        }

        public String getCode() { return code; }
        public String getName() { return name; }
        public double getPrice() { return price; }
        public int getQuantity() { return quantity; }
        public String getCategory() { return category; }
    }
}