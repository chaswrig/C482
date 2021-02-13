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
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 *
 * @author chase
 */
public class MainScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public void SceneChange(String string) throws IOException{
        scene = FXMLLoader.load(getClass().getResource(string));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    private TableView<Part> mainPartsTableView;

    @FXML
    private TableColumn<Part, Integer> mainPartsIDCol;

    @FXML
    private TableColumn<Part, String> mainPartsNameCol;

    @FXML
    private TableColumn<Part, Integer> mainPartsInventoryCol;

    @FXML
    private TableColumn<Part, Double> mainPartsPriceCol;
    
    @FXML
    private TextField mainPartsTxt;

    @FXML
    private TableView<Product> mainProductsTableView;

    @FXML
    private TableColumn<Product, Integer> mainProductsIDCol;

    @FXML
    private TableColumn<Product, String> mainProductsNameCol;

    @FXML
    private TableColumn<Product, Integer> mainProductsInventoryCol;

    @FXML
    private TableColumn<Product, Double> mainProductsPriceCol;

    @FXML
    private TextField mainProductsTxt;
    
    @FXML
    void onActionMainExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onActionMainPartsAdd(ActionEvent event) throws IOException {
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        SceneChange("/view/AddPartScreen.fxml");
    }

    @FXML
    void onActionMainPartsDelete(ActionEvent event) {
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete the part. Do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Inventory.deletePart(mainPartsTableView.getSelectionModel().getSelectedItem());
            mainPartsTableView.refresh();
        }  
    }

    @FXML
    void onActionMainPartsModify(ActionEvent event) throws IOException {
        
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPartScreen.fxml"));
        loader.load();
        ModifyPartScreenController ModPartScrnController = loader.getController();
        ModPartScrnController.sendPart(mainPartsTableView.getSelectionModel().getSelectedItem());
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionMainPartsSearch(ActionEvent event) {
        try{
            mainPartsTableView.setItems(Inventory.lookupPartID(Integer.parseInt(mainPartsTxt.getText())));
            mainPartsTableView.getSelectionModel().select(selectFilterPart(1));///////////////////////////////////////////////////////
        }
        catch(NumberFormatException e){
            mainPartsTableView.setItems(Inventory.lookupPartName(mainPartsTxt.getText()));
            mainPartsTableView.getSelectionModel().select(selectFilterPart(1));///////////////////////////////////////////////////////
        }
    }

    @FXML
    void onActionMainProductsAdd(ActionEvent event) throws IOException {
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        SceneChange("/view/AddProductScreen.fxml");
    }

    @FXML
    void onActionMainProductsDelete(ActionEvent event) {
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete the product. Do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Inventory.deleteProduct(mainProductsTableView.getSelectionModel().getSelectedItem());
            mainProductsTableView.refresh();
        }
    }

    @FXML
    void onActionMainProductsModify(ActionEvent event) throws IOException {
        Inventory.getAllFilteredParts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PART FILTER
        Inventory.getAllFilteredProducts().clear(); //HEY LISTEN!! THIS SHOULD CLEAR THE PRODUCT FILTER
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProductScreen.fxml"));
        loader.load();
        ModifyProductScreenController ModProdScrnController = loader.getController();
        ModProdScrnController.sendProduct(mainProductsTableView.getSelectionModel().getSelectedItem());
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionMainProdutcsSearch(ActionEvent event) {
        try{
            mainProductsTableView.setItems(Inventory.lookupProductID(Integer.parseInt(mainProductsTxt.getText())));
            mainProductsTableView.getSelectionModel().select(selectFilterProduct(1));///////////////////////////////////////////////////////
        }
        catch (NumberFormatException e){
            mainProductsTableView.setItems(Inventory.lookupProductName(mainProductsTxt.getText()));
            mainProductsTableView.getSelectionModel().select(selectFilterProduct(1));///////////////////////////////////////////////////////
        }
        
    }
    
    public Part selectPart(int id){
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == id){
                return part;
            }
        }
        return null;
    }
    
    public Part selectFilterPart(int id){
        for(Part part : Inventory.getAllFilteredParts()){
            if(part.getId() == id){
                return part;
            }
        }
        return null;
    }
    
    public Product selectProduct(int id){
        for(Product product : Inventory.getAllProducts()){
            if(product.getId() == id){
                return product;
            }
        }
        return null;
    }
    
    public Product selectFilterProduct(int id){
        for(Product product : Inventory.getAllFilteredProducts()){
            if(product.getId() == id){
                return product;
            }
        }
        return null;
    }
    
    @FXML
    void partSearchTextClick(MouseEvent event) {
        mainPartsTxt.setText("");
        mainPartsTableView.setItems(Inventory.getAllParts());
    }

    @FXML
    void productSearchTextClick(MouseEvent event) {
        mainProductsTxt.setText("");
        mainProductsTableView.setItems(Inventory.getAllProducts());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Add Parts to table
        mainPartsTableView.setItems(Inventory.getAllParts());
        
        mainPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainPartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //Default Part Selector
        mainPartsTableView.getSelectionModel().select(selectPart(1));
        
        //Add Products to table
        mainProductsTableView.setItems(Inventory.getAllProducts());
        
        mainProductsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainProductsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainProductsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainProductsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //Default Product Selector
        mainProductsTableView.getSelectionModel().select(selectProduct(1));
    }   
    
}
