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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author chase
 */
public class AddPartScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public void SceneChange(String string) throws IOException{
        scene = FXMLLoader.load(getClass().getResource(string));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private RadioButton addPartInHouseRdBtn;

    @FXML
    private RadioButton addPartOutsourcedRdBtn;

    @FXML
    private GridPane addPartGridPane;
    
    @FXML
    private Label machineIDLable;

    @FXML
    private TextField addPartIDTxt;

    @FXML
    private TextField addPartNameTxt;

    @FXML
    private TextField addPartInventoryTxt;

    @FXML
    private TextField addPartPriceTxt;

    @FXML
    private TextField addPartMaxTxt;

    @FXML
    private TextField addPartMachineIDTxt;

    @FXML
    private TextField addPartMinTxt;
    
    @FXML
    void onActionAddPartCancel(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will delete entered information. Do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            SceneChange("/view/MainScreen.fxml");
        }        
    }

    @FXML
    void onActionAddPartSave(ActionEvent event) throws IOException {
        
        //AddPartLogic Here
        int id = idGenerator();
        String name = addPartNameTxt.getText();
        double price = Double.parseDouble(addPartPriceTxt.getText());
        int stock = Integer.parseInt(addPartInventoryTxt.getText());
        int min = Integer.parseInt(addPartMinTxt.getText());
        int max = Integer.parseInt(addPartMaxTxt.getText());
                
        //Error checking -- Maximum greater than minimum
        outerloop:
        if(max>=min){
            //Error checking -- Inventory within range
            if(stock >= min && stock<= max){

                if(addPartInHouseRdBtn.isSelected()){
                    try{
                    int machineID = Integer.parseInt(addPartMachineIDTxt.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                    }
                    catch(NumberFormatException e){
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Machine ID must be an integer.");
                        Optional<ButtonType> result = alert.showAndWait();
                        break outerloop;
                    }
                }
                else if(addPartOutsourcedRdBtn.isSelected()){
                    String companyName = addPartMachineIDTxt.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }

                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                SceneChange("/view/MainScreen.fxml");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"Inventory must be within minimum and maximum range.");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Maximum must be equal to or greater than minimum.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }

    @FXML
    void onActionInHouseRdBtn(ActionEvent event) {
        machineIDLable.setText("Machine ID");
        addPartMachineIDTxt.setPromptText("Machine ID");
    }

    @FXML
    void onActionOutsourceRdBtn(ActionEvent event) {
        machineIDLable.setText("Company Name");
        addPartMachineIDTxt.setPromptText("Company Name");
    }
    
    //PartID Generator
    public int idGenerator(){
        int testID;
        Random rand = new Random();
        int randPartID = rand.nextInt(1000) + 1;
        randLoop:
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == randPartID)
                randPartID++;
        }
        return randPartID;
    }
    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        addPartInHouseRdBtn.setSelected(true);
        addPartIDTxt.setDisable(true);
        addPartIDTxt.setText(String.valueOf(idGenerator()));
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
