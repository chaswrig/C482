/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author chase
 */
public class ModifyProductScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public void SceneChange(String string) throws IOException{
        scene = FXMLLoader.load(getClass().getResource(string));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private GridPane modifyProductsGridPane;

    @FXML
    private TextField modifyProductIDTxt;

    @FXML
    private TextField modifyProductNameTxt;

    @FXML
    private TextField modifyProductInventoryTxt;

    @FXML
    private TextField modifyProductPriceTxt;

    @FXML
    private TextField modifyProductMaxTxt;

    @FXML
    private TextField modifyProductMinTxt;

    @FXML
    private TableView<Part> modifyProductAddTableView;

    @FXML
    private TableColumn<Part, Integer> modifyProductModIDCol;

    @FXML
    private TableColumn<Part, String> modifyProductModNameCol;

    @FXML
    private TableColumn<Part, Integer> modifyProductModInventoryCol;

    @FXML
    private TableColumn<Part, Double> modifyProductModPriceCol;

    @FXML
    private TextField modifyProductSearchTxt;

    @FXML
    private TableView<Part> modifyProductDeleteTableView;

    @FXML
    private TableColumn<Part, Integer> modifyProductDeleteIDCol;

    @FXML
    private TableColumn<Part, String> modifyProductDeleteNameCol;

    @FXML
    private TableColumn<Part, Integer> modifyProductDeleteInventoryCol;

    @FXML
    private TableColumn<Part, Double> modifyProductDeletePriceCol;
    
    @FXML
    void onActionModifyProductAdd(ActionEvent event) {
        //move item from add table to delete table
        Part addPart = modifyProductAddTableView.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;
        
        if(addPart == null){
            return;
        } else{
            int id = addPart.getId();
            for(int i = 0; i < addPartsList.size(); i++){
                if (addPartsList.get(i).getId() == id){
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Part is already included in this product.");
                    Optional<ButtonType> result = alert.showAndWait();
                    repeatedItem = true;
                }
            }
            
            if(!repeatedItem) {
                addPartsList.add(addPart);
            }
            modifyProductDeleteTableView.setItems(addPartsList);
        }
        modifyProductDeleteTableView.refresh();
    }

    @FXML
    void onActionModifyProductCancel(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will cancel any changes. Do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Inventory.getAllFilteredParts().clear();
            addPartsList.clear();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            SceneChange("/view/MainScreen.fxml");
        }
    }

    @FXML
    void onActionModifyProductDelete(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will remove the part from the product. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            addPartsList.remove(modifyProductDeleteTableView.getSelectionModel().getSelectedItem());
            modifyProductDeleteTableView.refresh();
        }
    }

    @FXML
    void onActionModifyProductSave(ActionEvent event) throws IOException {
        int id = Integer.parseInt(modifyProductIDTxt.getText());
        String name = modifyProductNameTxt.getText();
        double price = Double.parseDouble(modifyProductPriceTxt.getText());
        int stock = Integer.parseInt(modifyProductInventoryTxt.getText());
        int min = Integer.parseInt(modifyProductMinTxt.getText());
        int max = Integer.parseInt(modifyProductMaxTxt.getText());
        double totalPrice = 0.0;
        
        //Errorchecking
        outerloop:
        if(max >= min){
            if(stock >= min && stock <= max){
                Inventory.updateProduct(id, new Product(id, name, price, stock, min, max));
                //Logic to update associatedParts
                for(Product product : Inventory.getAllProducts()){
                    if(product.getId() == id){
                        for(Part part : addPartsList){
                            product.addAssociatedPart(part);
                            totalPrice += part.getPrice();
                        }                        
                    }
                }
                Inventory.getAllFilteredParts().clear();
                addPartsList.clear();
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                SceneChange("/view/MainScreen.fxml");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"Inventory must be within range.");
                Optional<ButtonType> result = alert.showAndWait();
                break outerloop;
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Maximum must be equal to or greater than minimum.");
            Optional<ButtonType> result = alert.showAndWait();
            break outerloop;
        }

    }

    @FXML
    void onActionModifyProductSearch(ActionEvent event) {
        try{
            modifyProductAddTableView.setItems(Inventory.lookupPartID(Integer.parseInt(modifyProductSearchTxt.getText())));;
        }
        catch(NumberFormatException e){
            modifyProductAddTableView.setItems(Inventory.lookupPartName(modifyProductSearchTxt.getText()));
        }
    }
    
    @FXML
    void partSearchTextClick(MouseEvent event) {
        modifyProductSearchTxt.setText("");
        modifyProductAddTableView.setItems(Inventory.getAllParts());
    }
    
    public void sendProduct(Product product){
        modifyProductIDTxt.setText(String.valueOf(product.getId()));
        modifyProductNameTxt.setText(product.getName());
        modifyProductInventoryTxt.setText(String.valueOf(product.getStock()));
        modifyProductPriceTxt.setText(String.valueOf(product.getPrice()));
        modifyProductMaxTxt.setText(String.valueOf(product.getMax()));
        modifyProductMinTxt.setText(String.valueOf(product.getMin()));
        
        //Logic to retrieve associated parts
        product.getAllAssociatedParts();
        addPartsList.addAll(product.getAllAssociatedParts());
        modifyProductDeleteTableView.setItems(addPartsList);
    }
    
    public static ObservableList<Part> addPartsList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modifyProductAddTableView.setItems(Inventory.getAllParts());
        modifyProductModIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductModNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductModInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductModPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        modifyProductDeleteTableView.setItems(addPartsList);
        modifyProductDeleteIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductDeleteNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductDeleteInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductDeletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        modifyProductIDTxt.setDisable(true);
    }    
}
