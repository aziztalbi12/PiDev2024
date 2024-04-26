package controllers;

import entities.Category;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryListController implements Initializable {

    private static CategoryListController instance;

    CategoryService cs = new CategoryService();
    @FXML
    private TableView<Category> tabCat;
    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> nomCol;

    @FXML
    private TableColumn<?, ?> quantityCol;

    private List<Category> categoryList;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;

        try {
            ListeCategories();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


    public void ListeCategories() throws SQLException {

        CategoryService cs = new CategoryService();

        // Initialize table columns
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        boolean deleteColumnExists = false;
        boolean ModifyColumnExists = false;

        for (TableColumn column : tabCat.getColumns()) {
            if (column.getText().equals("Action")) {
                deleteColumnExists = true;
                break;
            }
        }

        if (!deleteColumnExists) {
            TableColumn<Category, Void> deleteColumn = new TableColumn<>("Action");
            deleteColumn.setCellFactory(column -> {
                return new TableCell<Category, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Category c = getTableView().getItems().get(getIndex());
                            CategoryService cs = new CategoryService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Category");
                            alert.setHeaderText("Are you sure you want to delete this Category?");
                            alert.setContentText("This action cannot be undone.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    System.out.println(c);
                                    cs.delete(c);

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

            tabCat.getColumns().add(deleteColumn);
        }

        if (!ModifyColumnExists) {
            TableColumn<Category, Void> modifyColumn = new TableColumn<>("Update");
            modifyColumn.setCellFactory(column -> {
                return new TableCell<Category, Void>() {
                    private final Button modifyButton = new Button("Modify");

                    {
                        modifyButton.setOnAction(event -> {
                            Category selectedCategory = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyCategory.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                                ModifyCategoryController controller = loader.getController();
                                controller.initData(selectedCategory);
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

            tabCat.getColumns().add(modifyColumn);
        }

        // Load voyages from the database
        List<Category> list = cs.read();
        System.out.println(list);
        ObservableList<Category> observableList = FXCollections.observableArrayList(list);
        tabCat.setItems(observableList);

    }

    public void refreshTable() {
        try {
            categoryList = new CategoryService().read();
            tabCat.setItems(FXCollections.observableArrayList(categoryList));
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }



    @FXML
    void Create(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createCategorie.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabCat.getScene();
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
            Scene scene = tabCat.getScene();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/productList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabCat.getScene();
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
