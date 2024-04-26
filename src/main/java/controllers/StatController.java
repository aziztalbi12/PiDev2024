package controllers;

import entities.Category;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import services.CategoryService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatController implements Initializable {
    @FXML
    private PieChart pieChart;

    private List<Category> categoryList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fetch();
        populatePieChart();
    }

    private void fetch() {
        try {
            categoryList = new CategoryService().read();
        } catch (SQLException ex) {
            Logger.getLogger(StatController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void populatePieChart() {
        if (categoryList != null) {
            int totalQuantity = calculateTotalQuantity();
            pieChart.setData(FXCollections.observableArrayList());

            for (Category category : categoryList) {
                double percentage = (double) category.getQuantite() / totalQuantity * 100;
                PieChart.Data slice = new PieChart.Data(category.getNom(), percentage);
                pieChart.getData().add(slice);
            }
        }
    }

    private int calculateTotalQuantity() {
        int totalQuantity = 0;
        for (Category category : categoryList) {
            totalQuantity += category.getQuantite();
        }
        return totalQuantity;
    }

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = pieChart.getScene();
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
}
