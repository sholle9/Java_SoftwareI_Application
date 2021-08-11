package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the Inventory class which has dependency for the Product and Part classes*/
public class Inventory {

    /** This creates an ObservableList of the Part class called allParts.*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /** This creates an ObservableList of the Products class called allProducts.*/
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    //public static ReturnType MethodName(variableType variableName){observableListName.method(variableName);}

    /** This is the addPart method.
     This is called when a part is added to the allParts ObservableList.
     @param newPart The part to be added to the ObservableList allParts.
     */
    public static void addPart(Part newPart) {

        allParts.add(newPart);
    }
    /** This is the addProduct method.
     This is called when a product is added to the allProduct ObservableList.
     @param newProduct The product to be added to the ObservableList allProducts.
     */
    public static void addProduct(Product newProduct) {

        allProducts.add(newProduct);
    }


    /** This is the lookupPart method.
     This will return a part when looking for specific id. This will only return exact id matches. For future, make this return partial id matches.
     @param partId This is the part id being searched.
     */
    public static Part lookupPart(int partId){

        ObservableList<Part> allPartSearched = Inventory.getAllParts();//creates a list for the matching id to be put into

        for(int i = 0; i < allPartSearched.size(); i++){
            Part partSearched = allPartSearched.get(i);//declares that partSearched is equal to the part at index i in the list

            if(partSearched.getId() == partId){//if that partSearched is the same as the partID then return the value of the partSearched
                return partSearched;
            }
        }
        return null;
    }

    /** This is the lookupProduct method.
     This will return a product when looking for specific id. This will only return exact id matches. For future, make this return partial id matches.
     @param productId The product id being searched.
     */
    public static Product lookupProduct(int productId){

        ObservableList<Product> allProductSearched = Inventory.getAllProducts();

        for (int i = 0; i < allProductSearched.size(); i++){
            Product productSearched = allProductSearched.get(i);

            if(productSearched.getId() == productId){//This will only return exact id matches - input '1' you will not get '13' you will only get id = 1
                return productSearched;
            }
        }
        return null;
    }


    /** This is the lookupPart method.
     This will return any part that contains the searched string. This is specific to capitalized or not capitalized letters/phrases. For future, make it non-case sensitive.
     @param partName The part name or partial name string being searched.
     */
    public static ObservableList<Part> lookupPart(String partName){
       ObservableList<Part> partSearched = FXCollections.observableArrayList(); //Creates a new list for part to be added to
       ObservableList<Part> allPartSearched = Inventory.getAllParts(); //gets the entire list of parts

       for (Part part : allPartSearched){ //the 'part' is of the class 'Part' and go through the list that is 'allPartSearch'
           if(part.getName().contains(partName)){//contains() will search for specifically capital or not capitalized and empty strings - typing nothing returns whole list
               partSearched.add(part);
           }
       }
       return partSearched;
    }

    /** This is the lookupProduct method.
     This will return any product that contains the searched string. This is specific to capitalized or not capitalized letters/phrases. For future, make it non-case sensitive.
     @param productName The product name or partial name string being searched.
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> productSearched = FXCollections.observableArrayList();
        ObservableList<Product> allProductSearched = Inventory.getAllProducts();

        for(Product prod : allProductSearched){
            if(prod.getName().contains(productName)){
                productSearched.add(prod);
            }
        }

        return productSearched;
    }

    /** This is the updatePart method.
     This will modify the selected part by replacing the previous values with new values.
     @param index The position of the part from 0 to end of list in the ObservableList allParts.
     @param selectedPart The selected part to be updated.
     */
    public static void updatePart(int index, Part selectedPart){
        index--;
        if(selectedPart instanceof InHouse)
            Inventory.getAllParts().set(index, selectedPart);
        else if(selectedPart instanceof OutSourced)
            Inventory.getAllParts().set(index, selectedPart);

    }

    /** This is the updateProduct method.
     This will modify the selected product by replacing the previous values with new values.
     @param index The position of the product from 0 to end of list in the ObservableList allProducts.
     @param newProduct The selected product to be updated.
     */
    public static void updateProduct (int index, Product newProduct){
                index--;
                Inventory.getAllProducts().set(index, newProduct);


    }

    /** This is the deletePart method.
     This will remove the selectedPart from the observableList allParts.
     @param selectedPart This is the part to be removed from the ObservableList allParts.
     */
    public static boolean deletePart(Part selectedPart){
        for (Part part : Inventory.getAllParts()){
            if(part == selectedPart){

                return Inventory.getAllParts().remove(part);
            }
        }
        return false;
    }

    /** This is the deleteProduct Method.
     This will remove the selectedProduct from the observableList allProducts.
     @param selectedProduct This is the product to be removed from the ObservableList allProducts.
     */
    public static boolean deleteProduct(Product selectedProduct){
        for(Product product: Inventory.getAllProducts()){
            if(product == selectedProduct)
                return Inventory.getAllProducts().remove(product);
        }
        return false;
    }

    /** This is the getAllParts method.
     This method will return the ObservableList allParts.
     @return Returns the list of parts from the ObservableList allParts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** This is the getAllProducts method.
     This method will return the ObservableList allProducts.
     @return Returns the list of products in the ObservableList allProducts.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
