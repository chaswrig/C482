/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventorymanagementcw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 *
 * @author chase
 */
public class InventoryManagementCW extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //Sample Data -- Parts
        InHouse part1 = new InHouse(1, "inpartone", 1.99, 1, 1, 10, 100);
        InHouse part2 = new InHouse(2, "inpart2", 2.99, 2, 2, 20, 200);
        InHouse part3 = new InHouse(3, "inpart3", 3.99, 3, 3, 30, 300);
        InHouse part4 = new InHouse(4, "inpart4", 4.99, 4, 4, 40, 400);
        InHouse part5 = new InHouse(5, "inpart5", 5.99, 5, 5, 50, 500);
        Outsourced part6 = new Outsourced(6, "outpart6", 6.99, 6, 6, 60, "Company6");
        Outsourced part7 = new Outsourced(7, "outpart7", 7.99, 7, 7, 70, "Company7");
        Outsourced part8 = new Outsourced(8, "outpart8", 8.99, 8, 8, 80, "Company8");
        Outsourced part9 = new Outsourced(9, "outpart9", 9.99, 9, 9, 90, "Company9");
        Outsourced part10 = new Outsourced(10, "outpartten", 10.99, 10, 10, 100, "Company10");
        
        
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);
        Inventory.addPart(part4);
        Inventory.addPart(part5);
        Inventory.addPart(part6);
        Inventory.addPart(part7);
        Inventory.addPart(part8);
        Inventory.addPart(part9);
        Inventory.addPart(part10);
        
        //Sample Data -- Products
        Product product1 = new Product(1, "productone", 1.99, 1, 1, 10);
        product1.addAssociatedPart(part1);
        Product product2 = new Product(2, "product2", 2.99, 2, 2, 20);
        product2.addAssociatedPart(part2);
        Product product3 = new Product(3, "product3", 3.99, 3, 3, 30);
        product3.addAssociatedPart(part3);
        Product product4 = new Product(4, "product4", 4.99, 4, 4, 40);
        product4.addAssociatedPart(part4);
        Product product5 = new Product(5, "product5", 5.99, 5, 5, 50);
        product5.addAssociatedPart(part5);
        Product product6 = new Product(6, "porduct6", 6.99, 6, 6, 60);
        product6.addAssociatedPart(part6);
        Product product7 = new Product(7, "porduct7", 7.99, 7, 7, 70);
        product7.addAssociatedPart(part7);
        Product product8 = new Product(8, "porduct8", 8.99, 8, 8, 80);
        product8.addAssociatedPart(part8);
        
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);
        Inventory.addProduct(product5);
        Inventory.addProduct(product6);
        Inventory.addProduct(product7);
        Inventory.addProduct(product8);
        
        launch(args);
    }
    
}
