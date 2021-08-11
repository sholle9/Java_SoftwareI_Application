package controller;

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
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.lookupPart;

/** This is the controller for the Modify Product screen. */
public class ModifyProductFormController implements Initializable {

    @FXML
    private TextField ModifyProductIDTxt;

    @FXML
    private TextField ModifyProductNameTxt;

    @FXML
    private TextField ModifyProductInvTxt;

    @FXML
    private  TextField ModifyProductPriceTxt;

    @FXML
    private TextField ModifyProductMaxTxt;

    @FXML
    private TextField ModifyProductMinTxt;

    @FXML
    private TextField ModifyProductSearchTxt;

    @FXML
    private TableView <Part> ModifyProductTopTable;

    @FXML
    private TableColumn <Part, Integer> ModifyProductPartIDTopCol;

    @FXML
    private TableColumn<Part, String> ModifyProductPartNameTopCol;

    @FXML
    private TableColumn <Part, Integer> ModifyProductInventoryTopCol;

    @FXML
    private TableColumn <Part, Double> ModifyProductPriceTopCol;

    @FXML
    private Button ModifyProductTopAddBtn;

    @FXML
    private TableView<Part> ModifyProductBottomTable;

    @FXML
    private TableColumn<Part,Integer> ModifyProductPartIDBottomCol;

    @FXML
    private TableColumn<Part,String> ModifyProductPartNameBottomCol;

    @FXML
    private TableColumn<Part,Integer> ModifyProductInventoryBottomCol;

    @FXML
    private TableColumn<Part,Double> ModifyProductPriceBottomCol;

    @FXML
    private Button ModifyProductBottomRemoveBtn;

    @FXML
    private Button ModifyProductSaveBtn;

    @FXML
    private Button ModifyProductCancelBtn;

    //Global Variables
    Stage stage;
    Parent scene;

    private Product newProduct = null;

    public void sendProduct(Product newProduct) {
        ModifyProductIDTxt.setText(String.valueOf(newProduct.getId()));
        ModifyProductNameTxt.setText(newProduct.getName());
        ModifyProductInvTxt.setText(String.valueOf(newProduct.getStock()));
        ModifyProductPriceTxt.setText(String.valueOf(newProduct.getPrice()));
        ModifyProductMinTxt.setText(String.valueOf(newProduct.getMin()));
        ModifyProductMaxTxt.setText(String.valueOf(newProduct.getMax()));

    }

    /** This is the onAction method for the search text field on the Add Product screen.
     When the user enters a number or partial string that correlates to a part id or part name (respectively), the top table will display all the parts that equate to the search.
     In the if/else statement the program first looks for string or partial strings that match a part, if this doesn't return a value, it looks for an int that matches a part id.
     If there is not a match to any, there is a NumberFormatException which is solved by the else if(part == null) to display a Warning dialog box that the part was not found.
     The try/catch method is to resolve the NumberFormatException thrown when a string is not found because the lookup method is case-sensitive.
     In the future, resolve the case-sensitive lookup function.
     */
    public void onActionModifyProductSearch(){
        String nameSearch = ModifyProductSearchTxt.getText();

        ObservableList<Part> partialPartName = lookupPart(nameSearch);//Looks for partial, whole, and null of search

        try {
            if (partialPartName.size() == 0) { //if the lookupPart method with String parameter does not enter values into list 'partialPartName' do this loop

                int partID = Integer.parseInt(nameSearch); //partID is equal to the integer data type derived from the nameSearched string
                Part part = lookupPart(partID); //use the lookupPart Method with the now correct data type of int and assign the value to the Part type part

                if (part != null) {//if the search returns some value, add that value to the list partialPartName
                    partialPartName.add(part);
                } else if (part == null) {
                    //Displays a Warning Box that the searched part was not found
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("There were no parts found.");
                    alert.showAndWait();
                }

            }

        }
        catch (NumberFormatException e){
            //Displays a Warning Box that the searched part was not found
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("There were no parts found.");
            alert.showAndWait();
        }
        ModifyProductTopTable.setItems(partialPartName);//displays in table the results of search
        ModifyProductSearchTxt.setText("");//Clears search box
    }

    /** This is the onAction method for the Add button on the Modify Product screen.
     When the Add button is clicked, the selected part in the top table is added to the bottom table and added to the ObservableList associatedParts for the selected product.
     A NumberFormatException is thrown when the add button is selected, which is remedied with a if/else statement to display an Error dialog box to inform the user to select a part to add if there is not a part selected.
     For the future,since the product already exists, the add method permanently relates parts to that product without the need of the save button.
     */
    public void onActionModifyProductAdd(){

        Part relatedPart = ModifyProductTopTable.selectionModelProperty().get().getSelectedItem();

        if(relatedPart == null){
            //Displays the Error dialog box when a part is not selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part you wish to add to the product.");
            alert.showAndWait();
        }
        else {
            Inventory.getAllProducts().get(newProduct.getId() - 1).addAssociatedPart(relatedPart);

            ModifyProductBottomTable.setItems(Inventory.getAllProducts().get(newProduct.getId() - 1).getAllAssociatedParts());


        }
    }

    /** This is the onAction method for the 'Remove Associated Part' button on the Modify Product screen.
     This removes selected parts from the bottom table and the ObservableList associatedParts that relate to the selected product.
     The user is presented with a Confirmation dialog box to ensure they wish to delete the part.
     The part will be removed from the list when the 'OK' button is selected on the dialog box, otherwise no action will occur.
     For the future, since the Remove button removes the part from the ObservableList associatedParts for the selected product, it removes permanently without the save button.
     */
    public void onActionModifyRemovePart(){

        //This will display a Confirmation window to ensure that deletion is wanted
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently remove the part associated with this product. If you wish to continue, click OK.");
        Optional<ButtonType> result = alert.showAndWait();

        //If the OK button is pressed the program will delete the related part from the associatedParts list
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Part relatedPart = ModifyProductBottomTable.selectionModelProperty().get().getSelectedItem();


            Inventory.getAllProducts().get(newProduct.getId() - 1).deleteAssociatedPart(relatedPart);
            ModifyProductBottomTable.setItems(Inventory.getAllProducts().get(newProduct.getId() - 1).getAllAssociatedParts());
        }
    }

    /** This is the onAction for the Save button on the Modify Product screen.
     When the button is clicked, it will modify the selected product (except the id) and assign associated parts to the ObservableList associatedParts from the bottom table.
     When there is no or incorrect information in the text fields, a NumberFormatException is throw.
     The exception is remedied with a try/catch which will display an Error dialog box to input correct values.
     If/else statements are used to make sure that the max value is greater than the min value and stock falls somewhere between the max and min values.
     */
    public void onActionModifyProductSave(ActionEvent actionEvent) throws IOException{

        try {
            int id = Integer.parseInt(ModifyProductIDTxt.getText());
            String name = ModifyProductNameTxt.getText();
            int stock = Integer.parseInt(ModifyProductInvTxt.getText());
            double price = Double.parseDouble(ModifyProductPriceTxt.getText());
            int min = Integer.parseInt(ModifyProductMinTxt.getText());
            int max = Integer.parseInt(ModifyProductMaxTxt.getText());
            ObservableList<Part> associatedParts = ModifyProductBottomTable.getItems();

            if(min > max){
                //Displays the Error dialog box when the min is greater than the max
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please make sure you minimum inventory value is less than the maximum inventory value.");
                alert.showAndWait();
            }
            else if (max > min && (stock > max || stock < min)){
                //Displays the Error dialog box when the stock value is not min <= stock <= max
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please make sure the inventory value is between the minimum and maximum inventory values, or equal to either.");
                alert.showAndWait();
            }
            else {
                Inventory.updateProduct(id, new Product(id, name, price, stock, min, max, associatedParts));

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e){

            //Displays the Error dialog box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field!");
            alert.showAndWait();
        }
    }

    /** This is the onAction method for the Cancel button on the Modify Product Form.
     This button redirects the user to the Main Form screen without saving the modify product from the text fields to the ObservableList allProducts.
     To confirm that the user wishes to leave the page without add a product, a Confirmation dialog box is displayed that will continue the redirection to the Main screen when the OK button is clicked.
     */
    public void onActionModifyProductCancel(ActionEvent actionEvent) throws IOException {

        //This will create a Dialog box that will confirm if you want to continue back to the main screen without saving the text field information
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all edited text field values. If you wish to continue, click OK.");
        Optional<ButtonType> result = alert.showAndWait();

        //If the OK button is pressed the program will direct back to the main Form without adding to the product table
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This is the initialize method for the Add Product Form.
     This will pre-populate the top table with all of the available parts in the system and the bottom table with the parts in the associatedParts ObservableList for the selected product.
     If there is not a product selected a NullPointerException is thrown which is caught by the try/catch.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newProduct = MainFormController.getNewProduct();

        ModifyProductTopTable.setItems(Inventory.getAllParts());
        ModifyProductPartIDTopCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductPartNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProductInventoryTopCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductPriceTopCol.setCellValueFactory(new PropertyValueFactory<>("price"));


        try {
            ModifyProductBottomTable.setItems(Inventory.getAllProducts().get(newProduct.getId() - 1).getAllAssociatedParts());
        }
        catch (NullPointerException e){

        }

        ModifyProductPartIDBottomCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ModifyProductPartNameBottomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ModifyProductInventoryBottomCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ModifyProductPriceBottomCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

}
