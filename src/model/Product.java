package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This is the Product Class that has dependency on the static class Part.*/
public class Product {
    private ObservableList<Part> associatedParts;
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** This is the Product class constructor. This will assign default values to the Product values.
     @param id The product id
     @param name The product name
     @param price The product price
     @param stock The product Inventory on-hand
     @param min The minimum amount for inventory
     @param max The maximum amount for inventory
     @param associatedParts The list of parts associated with the product
     */
    public Product(int id, String name, double price, int stock, int min, int max, ObservableList<Part> associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        if(associatedParts == null){
            this.associatedParts = FXCollections.observableArrayList();
        }
        else {
            this.associatedParts = associatedParts;
        }
    }



    /** This is the getId method.
     This will return an id assigned to a specific product.
     @return Returns the id of a product.
     */
    public int getId() {
        return id;
    }

    /** This is the setId method that passes an int id parameter.
     This will reassign the id value for a specified product.
     @param id the new id value for the product.
     */
    public void setId(int id) {
        this.id = id;
    }

    /** This is the getName method.
     This will return a name assigned to a specific product.
     @return Returns product name value.
     */
    public String getName() {
        return name;
    }

    /** This is the setName method.
     This will reassign the name value for a specified product.
     @param name The new name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /** This is the getPrice method.
     This will return a price assigned to a specific product.
     @return Returns the price of a product.
     */
    public double getPrice() {
        return price;
    }

    /** This is the setPrice method.
     This will reassign the price value for a specified product
     @param price Returns the price of a product.
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /** This is the getStock method.
     This will return a int value representing the amount of inventory assigned to a specific product.
     @return Returns the stock value for a product.
     */
    public int getStock() {
        return stock;
    }

    /** This is the setStock method.
     This will reassign the stock value for a specified product.
     @param stock the new stock value for the product.
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /** This is the getMin method.
     This will return the minimum allowed int of inventory assigned to a specific product.
     @return Returns the minimum inventory value of a product.
     */
    public int getMin() {
        return min;
    }

    /** This is the setMin method.
     This will reassign the minimum inventory value for a specified product
     @param min The new minimum inventory value of a product.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /** This is the getMax method.
     This will return the maximum allowed int of inventory assigned to a specific product.
     @return Returns the maximum inventory value of a product.
     */
    public int getMax() {
        return max;
    }
    /** This is the setMax method.
     This will reassign the maximum inventory value for a specified product.
     @param max The new maximum inventory value of a product.
     */
    public void setMax(int max) {

        this.max = max;
    }

    /** This is the addAssociatedPart method.
     This will add a part to the ObservableList associatedParts.
     @param part The part that is to be added to the ObservableList associatedParts.
     */
    public void addAssociatedPart(Part part){

        associatedParts.add(part);
    }

    /** This is the deleteAssociatedPart method.
     This will remove a part from the ObservableList associatedParts.
     @param selectedAssociatedPart The part to be removed from the ObservableList associatedParts.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        for(Part part : getAllAssociatedParts()){
            if(part == selectedAssociatedPart){
                return associatedParts.remove(selectedAssociatedPart);
            }
        }
        return false;
    }

    /** This is the getAllAssociatedParts method.
     This will return the ObservableList of parts that are associated with a specific product.
     @return Returns the parts in the ObservableList associatedParts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return this.associatedParts;
    }
}
