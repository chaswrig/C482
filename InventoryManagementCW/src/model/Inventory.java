/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author chase
 */
public class Inventory {
    
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    private static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    
    public static ObservableList<Part> getAllFilteredParts(){
        return filteredParts;
    }
    
    public static ObservableList<Product> getAllFilteredProducts(){
        return filteredProducts;
    }
    
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    //Lookup Part by ID
    public static ObservableList<Part> lookupPartID(int partID){
        
        if(!(Inventory.getAllFilteredParts().isEmpty()))
            Inventory.getAllFilteredParts().clear();
        
        for(Part part : Inventory.getAllParts()){
            if(part.getId() == partID)
                Inventory.getAllFilteredParts().add(part);
        }
        
        if(Inventory.getAllFilteredParts().isEmpty())
            return Inventory.getAllParts();
        else
            return Inventory.getAllFilteredParts();
    }
    
    //Lookup Product by ID
    public static ObservableList<Product> lookupProductID(int productID){
        if(!(Inventory.getAllFilteredProducts().isEmpty()))
            Inventory.getAllFilteredProducts().clear();
        
        for(Product product : Inventory.getAllProducts()){
            if(product.getId() == productID)
                Inventory.getAllFilteredProducts().add(product);
        }
        
        if(Inventory.getAllFilteredProducts().isEmpty())
            return Inventory.getAllProducts();
        else
            return Inventory.getAllFilteredProducts();
    }
        
    //Lookup Part by Name
    public static ObservableList<Part> lookupPartName(String name){ //HEY LISTEN LOOK HERE THIS IS WHAT WE'RE WORKING WITH FOR SEARCH ERROR CHECKING!!
            
        if(!(Inventory.getAllFilteredParts().isEmpty()))
            Inventory.getAllFilteredParts().clear();
        
        for(Part part : Inventory.getAllParts()){
            if(part.getName().contains(name))
                Inventory.getAllFilteredParts().add(part);
        }
        
        if(Inventory.getAllFilteredParts().isEmpty())
            return Inventory.getAllParts();
        else
            return Inventory.getAllFilteredParts();
    }
    
    //Lookup Product by Name
    public static ObservableList<Product> lookupProductName(String name){
        if(!(Inventory.getAllFilteredProducts().isEmpty()))
            Inventory.getAllFilteredProducts().clear();
        
        for(Product product : Inventory.getAllProducts()){
            if(product.getName().contains(name))
                Inventory.getAllFilteredProducts().add(product);
        }
        
        if(Inventory.getAllFilteredProducts().isEmpty())
            return Inventory.getAllProducts();
        else
            return Inventory.getAllFilteredProducts();
    }
    
    //Update Part
    public static void updatePart(int id, Part selectedPart){
        int index = -1;
        
        for(Part part : Inventory.getAllParts()){
            index++;
            
            if(part.getId() == id){
                Inventory.getAllParts().set(index, selectedPart);
            }
        }       
    }
    
    //Update Product
    public static void updateProduct(int id, Product selectedProduct){
        int index = -1;
        
        for(Product product : Inventory.getAllProducts()){
            index++;
            
            if(product.getId() == id){
                Inventory.getAllProducts().set(index, selectedProduct);
            }
        }
    }
    
    //Delete Part
    public static void deletePart(Part selectedPart){
        Inventory.getAllParts().remove(selectedPart);
    }
    
    //Delete Product
    public static void deleteProduct(Product selectedProduct){
        Inventory.getAllProducts().remove(selectedProduct);
    }
    
    //Get allParts
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    //Get allProducts
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
