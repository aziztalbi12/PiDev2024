package controllers;

import entities.Category;
import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.CategoryService;
import services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

public class ModifyProductController implements Initializable {
    ProductService ps = new ProductService();

    @FXML
    private ComboBox<Category> categoryCB;

    @FXML
    private TextField descTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prixTF;
    private final CategoryService categoryService = new CategoryService();

    Product prodd;

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/productList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = prixTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void Modify(ActionEvent event) throws SQLException, IOException {
        String dsc = descTF.getText();
        String nom = nomTF.getText();
        String prix = prixTF.getText();
        String selectedCategoryName = String.valueOf(categoryCB.getValue()); // Get selected category name


        if (dsc.isEmpty() || nom.isEmpty() || prix.isEmpty() ||  selectedCategoryName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else if (!isValidString(dsc) || !isValidString(nom) || !isValidInt(prix)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid information.");
            alert.showAndWait();
        } else {
            int quantiteInt = parseInt(prix);
            Product prod = new Product(prodd.getId(),quantiteInt,nom,dsc,(categoryCB.getValue()).getId());
            ps.update(prod);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié");
            alert.setHeaderText(null);
            alert.setContentText("Product Modifié !");
            alert.getButtonTypes().clear(); // Remove existing button types
            alert.getButtonTypes().add(ButtonType.OK); // Add only OK button type
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/productList.fxml"));
            Parent root = loader.load();
            Scene scene = nomTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void initData(Product p){
        prodd = p;
        //categoryCB.setValue(p.get);
        descTF.setText(p.getDescription());
        nomTF.setText(p.getNom());
        prixTF.setText(String.valueOf(p.getPrix()));

    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Category> categories = categoryService.read();
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);

            // Set custom cell factory to render only category names
            categoryCB.setCellFactory(param -> new ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom()); // Render only category name
                    }
                }
            });

            // Set converter to display only category names in the combo box
            categoryCB.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category == null ? null : category.getNom();
                }

                @Override
                public Category fromString(String string) {
                    return null; // Not needed in this case
                }
            });

            categoryCB.setItems(categoryList); // Set category objects in the combo box
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
    private boolean isValidInt(String value) {
        // Check if the value is a valid integer
        return value.matches("-?\\d+");
    }

    private boolean isValidString(String name) {
        // Check if the name contains only letters and has length between 2 and 50
        return name.matches("^[a-zA-Z0-9_ ]*$");
    }
}
