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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CategoryService;
import services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductListController implements Initializable {

    private static ProductListController instance;

    ProductService ps = new ProductService();

    @FXML
    private TableColumn<?, ?> categoryIdCol;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> nomCol;

    @FXML
    private TableColumn<?, ?> prixCol;

    @FXML
    private TableView<Product> tabProd;

    private List<Product> ProductList;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        try {
            ListeProducts();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ListeProducts() throws SQLException {

        ProductService ps = new ProductService();

        // Initialize table columns
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        boolean deleteColumnExists = false;
        boolean ModifyColumnExists = false;

        for (TableColumn column : tabProd.getColumns()) {
            if (column.getText().equals("Action")) {
                deleteColumnExists = true;
                break;
            }
        }

        if (!deleteColumnExists) {
            TableColumn<Product, Void> deleteColumn = new TableColumn<>("Action");
            deleteColumn.setCellFactory(column -> {
                return new TableCell<Product, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Product p = getTableView().getItems().get(getIndex());
                            ProductService ps = new ProductService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Product");
                            alert.setHeaderText("Are you sure you want to delete this Product?");
                            alert.setContentText("This action cannot be undone.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    System.out.println(p);
                                    ps.delete(p);

                                    refreshTable();
                                } catch (SQLException ex) {
                                    Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {

                                alert.close();
                            }

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            });

            tabProd.getColumns().add(deleteColumn);
        }

        if (!ModifyColumnExists) {
            TableColumn<Product, Void> modifyColumn = new TableColumn<>("Update");
            modifyColumn.setCellFactory(column -> {
                return new TableCell<Product, Void>() {
                    private final Button modifyButton = new Button("Modify");

                    {
                        modifyButton.setOnAction(event -> {
                            Product selectedProd = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyProduct.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                                ModifyProductController controller = loader.getController();
                                controller.initData(selectedProd);
                                Scene scene = modifyButton.getScene();
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
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            });

            tabProd.getColumns().add(modifyColumn);
        }

        // Load voyages from the database
        List<Product> list = ps.read();
        System.out.println(list);
        ObservableList<Product> observableList = FXCollections.observableArrayList(list);
        tabProd.setItems(observableList);

    }

    public void refreshTable() {
        try {
            ProductList = new ProductService().read();
            tabProd.setItems(FXCollections.observableArrayList(ProductList));
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void Create(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createProduct.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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
    void goToStat(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stat.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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
    void Go(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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
