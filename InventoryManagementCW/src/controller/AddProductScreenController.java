/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
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
import javafx.scene.control.Label;
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
public class AddProductScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public void SceneChange(String string) throws IOException{
        scene = FXMLLoader.load(getClass().getResource(string));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private GridPane addProductsGridPane;

    @FXML
    private TextField addProductIDTxt;

    @FXML
    private TextField addProductNameTxt;

    @FXML
    private TextField addProductInventoryTxt;

    @FXML
    private TextField addProductPriceTxt;

    @FXML
    private TextField addProductMaxTxt;

    @FXML
    private TextField addProductMinTxt;

    @FXML
    private TableView<Part> addProductAddTableView;

    @FXML
    private TableColumn<Part, Integer> addProductAddIDCol;

    @FXML
    private TableColumn<Part, String> addProductAddNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductAddInventoryCol;

    @FXML
    private TableColumn<Part, Double> addProductAddPriceCol;

    @FXML
    private TextField addProductSearchTxt;

    @FXML
    private TableView<Part> addProductDeleteTableView;

    @FXML
    private TableColumn<Part, Integer> addProductDeleteIDCol;

    @FXML
    private TableColumn<Part, String> addProductDeleteNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductDeleteInventoryCol;

    @FXML
    private TableColumn<Part, Double> addProductDeletePriceCol;
    
    @FXML
    void onActionAddProductAdd(ActionEvent event){
        //move item from add table to delete table
        Part addPart = addProductAddTableView.getSelectionModel().getSelectedItem();
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
            addProductDeleteTableView.setItems(addPartsList);
        }
        addProductDeleteTableView.refresh();
    }

    @FXML
    void onActionAddProductCancel(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete entered information. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            Inventory.getAllFilteredParts().clear();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            SceneChange("/view/MainScreen.fxml");
        }
    }

    @FXML
    void onActionAddProductDelete(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will remove the part from the product. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            //Inventory.getAllFilteredParts().remove(addProductDeleteTableView.getSelectionModel().getSelectedItem());
            addPartsList.remove(addProductDeleteTableView.getSelectionModel().getSelectedItem());
            addProductDeleteTableView.refresh();
        }
    }

    @FXML
    void onActionAddProductSave(ActionEvent event) throws IOException {
        
        //AddProduct Logic
        int id = Integer.parseInt(addProductIDTxt.getText());
        String name = addProductNameTxt.getText();
        double price = Double.parseDouble(addProductPriceTxt.getText());
        int stock = Integer.parseInt(addProductInventoryTxt.getText());
        int min = Integer.parseInt(addProductMinTxt.getText());
        int max = Integer.parseInt(addProductMaxTxt.getText());  
        double totalPrice = 0.0;
        
        //Error Checking -- Max greater than Min
        outerloop:
        if(max >= min){
            //Error Checking -- Inventory within Range
            if(stock >= min && stock <= max){
                Inventory.addProduct(new Product(id, name, price, stock, min, max));
                //Logic to add parts to newly created product using enhanced for loop
                for(Product product : Inventory.getAllProducts()){
                    if(product.getId() == id){
                        //for(Part part : Inventory.getAllFilteredParts()){
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
                Alert alert = new Alert(Alert.AlertType.ERROR,"Inventory must be within minimum and maximum range.");
                Optional<ButtonType> result = alert.showAndWait();
                break outerloop;
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Maximum must be equal to or greater than minimum.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    void onActionAddProductSearch(ActionEvent event) {
        try{
            addProductAddTableView.setItems(Inventory.lookupPartID(Integer.parseInt(addProductSearchTxt.getText())));;
        }
        catch(NumberFormatException e){
            addProductAddTableView.setItems(Inventory.lookupPartName(addProductSearchTxt.getText()));
        }
    }
    
    @FXML
    void partSearchTextClick(MouseEvent event) {
        addProductSearchTxt.setText("");
        addProductAddTableView.setItems(Inventory.getAllParts());
    }

    //ProductID Generator
    public int idGenerator(){
        int testID;
        Random rand = new Random();
        int randProductID = rand.nextInt(1000) + 1;
        randLoop:
        for(Product product : Inventory.getAllProducts()){
            if(product.getId() == randProductID)
                randProductID++;
        }
        return randProductID;
    }
    
    //class used for AddTableView data
    public static ObservableList<Part> addPartsList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addProductAddTableView.setItems(Inventory.getAllParts());
        addProductAddIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAddNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAddInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAddPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        addProductDeleteTableView.setItems(addPartsList);
        addProductDeleteIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductDeleteNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductDeleteInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductDeletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        addProductIDTxt.setDisable(true);
        addProductIDTxt.setText(String.valueOf(idGenerator()));    
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
