package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.Inventory;
import model.Part;
import model.Product;
//import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.*;

/** This is the controller for the MainForm. */
public class MainFormController implements Initializable  {
    @FXML
    private TableView <Part> MainPartTable;

    @FXML
    private TableColumn<Part,Integer> MainPartIDCol;

    @FXML
    private TableColumn<Part,String> MainPartNameCol;


    @FXML
    private TableColumn<Part,Integer> MainPartInventoryCol;

    @FXML
    private TableColumn<Part,Double> MainPartPriceCol;

    @FXML
    private Button MainPartAddBtn;

    @FXML
    private Button MainPartModifyBtn;

    @FXML
    private Button MainPartDeleteBtn;

    @FXML
    private TableView<Product> MainProductTable;

    @FXML
    private TableColumn<Product,Integer> MainProductIDCol;

    @FXML
    private TableColumn<Product, String> MainProductNameCol;

    @FXML
    private TableColumn<Product,Integer> MainProductInventoryCol;

    @FXML
    private TableColumn<Product,Double> MainProductPriceCol;

   @FXML
   private TextField MainPartSearchTxt;

   @FXML
   private TextField MainProductSearchTxt;

    //Global variables
    Stage stage;
    Parent scene;


    private static Product newProduct = null;

    /** This is the getNewProduct method.
     This method retrieve a defined product.
     @return Returns a product.
     */
    public static Product getNewProduct(){
        return newProduct;
    }


    /** This is the onAction method for the search text field for the parts on the Main Form screen.
     When the user enters a number or partial string that correlates to a part id or part name (respectively), the table will display all the parts that equate to the search.
     In the if/else statement the program first looks for string or partial strings that match a part, if this doesn't return a value, it looks for an int that matches a part id.
     If there is not a match to any, there is a NumberFormatException which is solved by the else if(part == null) to display a Warning dialog box that the part was not found.
     The try/catch method is to resolve the NumberFormatException thrown when a string is not found because the lookup method is case-sensitive.
     In the future, resolve the case-sensitive lookup function.
     */
    @FXML
    public void onActionMainPartSearch (ActionEvent actionEvent){
        String nameSearch = MainPartSearchTxt.getText();

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

        MainPartTable.setItems(partialPartName);//displays in table the results of search
        MainPartSearchTxt.setText("");//Clears search box

    }


    /** This is the onAction method for the Add button for the Parts on the Main Form.
     This will direct the user to the Add Part screen.
     */
   @FXML
    public void onActionMainAddPart(ActionEvent actionEvent) throws IOException {

       stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
       scene = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
       stage.setScene(new Scene(scene));
       stage.show();

    }

    /** This is the onAction for the Modify button for the Part table on the MainForm.
     Redirects the user to the Modify Part screen and pre-populates the text fields with the selected part's information.
     A NullPointerException is thrown when there is no part selected; thus, the try/catch is used to display an Error dialog box informing the user to select a part they wish to modify.
     */
    @FXML
    public void onActionMainModifyPart(ActionEvent actionEvent) throws IOException{
       try {
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
           loader.load();

           ModifyPartFormController MPartFController = loader.getController();
           MPartFController.sendPart(MainPartTable.getSelectionModel().getSelectedItem());

           stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
           //scene = FXMLLoader.load(getClass().getResource("/view/ModifyProductForm.fxml"));
           Parent scene = loader.getRoot();
           stage.setScene(new Scene(scene));
           stage.show();
       }
       catch (NullPointerException e){
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error Dialog");
           alert.setContentText("Please select a part you wish to modify.");

           alert.showAndWait(); //Displays the Error dialog box
       }
    }

    /** This is the onAction method for the Delete button for the parts on the Main Form screen.
     This will delete a selected part from the parts table on the Main screen and will remove the part from the ObservableList allParts.
     A Conformation dialog box will display first to ensure the user wishes to delete the selected part.
     The part will be permanently deleted from the ObservableList allParts, when the 'OK' button is clicked on the dialog box; otherwise, no action is taken.
     */
    @FXML
    public void onActionMainPartDelete(ActionEvent actionEvent) throws IOException{

        //Creates the Dialog Confirmation Box for deleting the part selected on the Main Form
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part? Click OK to delete.");
        Optional<ButtonType> result = alert.showAndWait();

        //When the OK button on the dialog box is pressed, the part will be deleted. Otherwise, nothing will happen
        if(result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(MainPartTable.getSelectionModel().getSelectedItem());
        }
    }

    /** This is the onAction method for the search text field for the Product table on the Main Form screen.
     When the user enters a number or partial string that correlates to a part id or part name (respectively), the table will display all the products that equate to the search.
     In the if/else statement the program first looks for string or partial strings that match a product, if this doesn't return a value, it looks for an int that matches a product id.
     If there is not a match to any, there is a NumberFormatException which is solved by the else if(prod == null) to display a Warning dialog box that the product was not found.
     The try/catch method is to resolve the NumberFormatException thrown when a string is not found because the lookup method is case-sensitive.
     In the future, resolve the case-sensitive lookup function.
     */
    @FXML
    public void onActionMainProductSearch(ActionEvent actionEvent){
        String searchName = MainProductSearchTxt.getText();

        ObservableList<Product> partialProductName = lookupProduct(searchName);

        try {
            if (partialProductName.size() == 0) {//if the lookupProduct with String method doesn't add to the partialProductName list then use the lookupProduct with int parameter

                int productID = Integer.parseInt(searchName);
                Product prod = lookupProduct(productID);
                if (prod != null) {
                    partialProductName.add(prod);
                } else if (prod == null) {
                    //Displays a Warning Box that the searched product was not found
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setContentText("There were no products found.");
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

        MainProductTable.setItems(partialProductName);
        MainProductSearchTxt.setText("");
    }

    /** This is the onAction method for the Add button for the Products on the Main Form.
     This will direct the user to the Add Product screen.
     */
    @FXML
    public void onActionMainAddProduct(ActionEvent actionEvent) throws IOException{

        //Passes selected product to AddProductFormController called as newProduct
        newProduct = MainProductTable.getSelectionModel().getSelectedItem();

        stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProductForm.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /** This is the onAction for the Modify button for the Product table on the MainForm.
     Redirects the user to the Modify Product screen and pre-populates the text fields with the selected product's information and the bottom table with the product's associated parts.
     A NullPointerException is thrown when there is no product selected; thus, the try/catch is used to display an Error dialog box informing the user to select a product they wish to modify.
     */
    @FXML
    public void onActionMainModifyProduct(ActionEvent actionEvent) throws IOException{

        //Passes selected product to ModifyProductFormController called as newProduct
        newProduct = MainProductTable.getSelectionModel().getSelectedItem();

        try {


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
            loader.load();



            ModifyProductFormController MPFController = loader.getController();
            MPFController.sendProduct(MainProductTable.getSelectionModel().getSelectedItem());



            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NullPointerException e){
            //Displays the Error dialog box when a product is not selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a product you wish to modify.");
            alert.showAndWait();
        }
    }

    /** This is the onAction method for the Delete button for the Product table on the MainForm.
     This will delete a product so long as it does not have associated parts.
     If the product's ObservableList associatedParts size not 0 then there is an Error dialog box displayed to inform the user that they cannot delete the product with associated parts.
     If the product's Observable associatedParts is empty (size 0) then the product is removed from the table and the ObservableList allProducts.
     */
    @FXML
    public void onActionMainProductDelete(ActionEvent actionEvent){

        Product prod = MainProductTable.getSelectionModel().getSelectedItem();
        ObservableList<Part> partCheck = Inventory.getAllProducts().get(prod.getId()-1).getAllAssociatedParts();

        if(partCheck.size() != 0){
            //Displays the Error dialog box when a product is selected that has associated parts
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("This product cannot be deleted while parts are associated with it.");
            alert.showAndWait();
        }
        else {
            //Creates the Dialog Confirmation Box for deleting the product selected on the Main Form
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product? Click OK to delete.");
            Optional<ButtonType> result = alert.showAndWait();

            //When the OK button on the dialog box is pressed, the product will be deleted. Otherwise, nothing will happen
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deleteProduct(prod);
            }
        }
    }

    /** This is the onAction method for the exit button on the MainForm screen.
     This will end the program.
     */
    @FXML
    void onActionMainExit(ActionEvent actionEvent){
        System.exit(0);

    }

    /** this is the initialize method for the MainForm.
     This pre-populates the left table with the parts in the ObservableList allParts and the right table with the products in the ObservableList allProducts. */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        MainPartTable.setItems(Inventory.getAllParts());
        MainPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        MainProductTable.setItems(Inventory.getAllProducts());
        MainProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MainProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        MainProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        MainProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
}
