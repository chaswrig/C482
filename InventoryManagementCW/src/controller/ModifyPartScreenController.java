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
public class ModifyPartScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public void SceneChange(String string) throws IOException{
        scene = FXMLLoader.load(getClass().getResource(string));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private RadioButton modifyPartInHouseRdBtn;

    @FXML
    private RadioButton modifyPartOutsourceRdBtn;

    @FXML
    private GridPane modifyPartGridPane;
    
    @FXML
    private Label machineIDLabel;

    @FXML
    private TextField modifyPartIDTxt;

    @FXML
    private TextField modifyPartNameTxt;

    @FXML
    private TextField modifyPartInventoryTxt;

    @FXML
    private TextField modifyPartPriceTxt;

    @FXML
    private TextField modifyPartMaxTxt;

    @FXML
    private TextField modifyPartCompanyTxt;

    @FXML
    private TextField modifyPartMinTxt;
    
    @FXML
    void onActionModifyPartCancel(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"This will cancel any changes. Do you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            SceneChange("/view/MainScreen.fxml");
        }
    }

    @FXML
    void onActionModifyPartInHouseRdBtn(ActionEvent event) {
        machineIDLabel.setText("Machine ID");
    }

    @FXML
    void onActionModifyPartOutsourceRdBtn(ActionEvent event) {
        machineIDLabel.setText("Company Name");
    }

    @FXML
    void onActionModifyPartSave(ActionEvent event) throws IOException {
        int id = Integer.parseInt(modifyPartIDTxt.getText());
        String name = modifyPartNameTxt.getText();
        double price = Double.parseDouble(modifyPartPriceTxt.getText());
        int stock = Integer.parseInt(modifyPartInventoryTxt.getText());
        int min = Integer.parseInt(modifyPartMinTxt.getText());
        int max = Integer.parseInt(modifyPartMaxTxt.getText());
        
        //Errorchecking
        outerloop:
        if(max >= min){ //Max greater than min
            if(stock >= min && stock <= max){ //Inventory within range
                if(modifyPartInHouseRdBtn.isSelected()){
                    
                    try{
                    int machineID = Integer.parseInt(modifyPartCompanyTxt.getText());
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineID));
                    }
                    catch(NumberFormatException e){
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Machine ID must be an integer.");
                        Optional<ButtonType> result = alert.showAndWait();
                        break outerloop;
                    }
                }
                else if(modifyPartOutsourceRdBtn.isSelected()){
                    String companyName = modifyPartCompanyTxt.getText();
                    Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
                }
                
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                SceneChange("/view/MainScreen.fxml");
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR,"Inventory must be within range.");
                Optional<ButtonType> result = alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"Maximum must be equal to or greater than minimum.");
            Optional<ButtonType> result = alert.showAndWait();
        }
    }
    
    public void sendPart(Part part){
    
        if(part instanceof InHouse){
            modifyPartInHouseRdBtn.setSelected(true);
            modifyPartIDTxt.setText(String.valueOf(part.getId()));
            modifyPartNameTxt.setText(part.getName());
            modifyPartInventoryTxt.setText(String.valueOf(part.getStock()));
            modifyPartPriceTxt.setText(String.valueOf(part.getPrice()));
            modifyPartMaxTxt.setText(String.valueOf(part.getMax()));
            modifyPartMinTxt.setText(String.valueOf(part.getMin()));
            modifyPartCompanyTxt.setText(String.valueOf(((InHouse) part).getMachineID()));
        }
        else if(part instanceof Outsourced){
            modifyPartOutsourceRdBtn.setSelected(true);
            modifyPartIDTxt.setText(String.valueOf(part.getId()));
            modifyPartNameTxt.setText(part.getName());
            modifyPartInventoryTxt.setText(String.valueOf(part.getStock()));
            modifyPartPriceTxt.setText(String.valueOf(part.getPrice()));
            modifyPartMaxTxt.setText(String.valueOf(part.getMax()));
            modifyPartMinTxt.setText(String.valueOf(part.getMin()));
            modifyPartCompanyTxt.setText(((Outsourced) part).getCompanyName());
            machineIDLabel.setText("Company Name");
        }
    }
    
     /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        modifyPartIDTxt.setDisable(true);
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
