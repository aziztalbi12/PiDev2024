package controllers;

import entities.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModifyCategoryController {

    CategoryService cs = new CategoryService();

    Category cat;
    @FXML
    private TextField descTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField quantiteTF;

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = quantiteTF.getScene();
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
    void Modifier(ActionEvent event) throws SQLException, IOException {
        String dsc = descTF.getText();
        String nom = nomTF.getText();
        String quantite = quantiteTF.getText();
        if (dsc.isEmpty() || nom.isEmpty() || quantite.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else if (!isValidString(dsc) || !isValidString(nom) || !isValidInt(quantite) ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid information.");
            alert.showAndWait();
        } else {
            cat.setDescription(dsc);
            cat.setNom(nom);
            cat.setQuantite(Integer.parseInt(quantite));
            cs.update(cat);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Modifié");
            alert.setHeaderText(null);
            alert.setContentText("Categorie modifié !");
            alert.getButtonTypes().clear(); // Remove existing button types
            alert.getButtonTypes().add(ButtonType.OK); // Add only OK button type
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
            Parent root = loader.load();
            Scene scene = quantiteTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }




    }



    public void initData(Category category){
        cat = category;
        descTF.setText(category.getDescription());
        nomTF.setText(category.getNom());
        quantiteTF.setText(String.valueOf(category.getQuantite()));
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
