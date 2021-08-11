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
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.lookupPart;
/** This is the controller for the Add Product screen.*/
public class AddProductFormController implements Initializable {

    @FXML
    private TextField AddProductIDTxt;

    @FXML
    private TextField AddProductNameTxt;

    @FXML
    private TextField AddProductInvTxt;

    @FXML
    private  TextField AddProductPriceTxt;

    @FXML
    private TextField AddProductMaxTxt;

    @FXML
    private TextField AddProductMinTxt;

    @FXML
    private TextField AddProductSearchTxt;

    @FXML
    private TableView <Part> AddProductTopTable;

    @FXML
    private TableColumn <Part, Integer> AddProductPartIDTopCol;

    @FXML
    private TableColumn<Part, String> AddProductPartNameTopCol;

    @FXML
    private TableColumn <Part, Integer> AddProductInventoryTopCol;

    @FXML
    private TableColumn <Part, Double> AddProductPriceTopCol;

    @FXML
    private Button AddProductTopAddBtn;

    @FXML
    private TableView <Part> AddProductBottomTable;

    @FXML
    private TableColumn <Part,Integer>AddProductPartIDBottomCol;

    @FXML
    private TableColumn <Part,String> AddProductPartNameBottomCol;

    @FXML
    private TableColumn <Part,Integer> AddProductInventoryBottomCol;

    @FXML
    private TableColumn <Part,Double> AddProductPriceBottomCol;

    @FXML
    private Button AddProductBottomRemoveBtn;

    @FXML
    private Button AddProductSaveBtn;

    @FXML
    private Button AddProductCancelBtn;

    //Global Variables
    Stage stage;
    Parent scene;



    ObservableList<Part> newRelatedParts = FXCollections.observableArrayList();//This sets the array for the new parts to be temporarily placed in till permanently save with onActionAddProductSave

    /** This is the onAction method for the search text field on the Add Product screen.
     When the user enters a number or partial string that correlates to a part id or part name (respectively), the top table will display all the parts that equate to the search.
     In the if/else statement the program first looks for string or partial strings that match a part, if this doesn't return a value, it looks for an int that matches a part id.
     If there is not a match to any, there is a NumberFormatException which is solved by the else if(part == null) to display a Warning dialog box that the part was not found.
     The try/catch method is to resolve the NumberFormatException thrown when a string is not found because the lookup method is case-sensitive.
     In the future, resolve the case-sensitive lookup function.
     */
    @FXML
    public void onActionAddProductSearch() {

       String nameSearch = AddProductSearchTxt.getText();

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

       AddProductTopTable.setItems(partialPartName);//displays in table the results of search
       AddProductSearchTxt.setText("");//Clears search box
    }

    /** This is the onAction method for the Add button on the Add Product screen.
     When the Add button is clicked, the selected part in the top table is added to the bottom table.
     A NumberFormatException is thrown when the add button is selected, which is remedied with a if/else statement to display an Error dialog box to inform the user to select a part to add if there is not a part selected.
     */
    public void onActionAddProductTopTableAdd(){


       Part relatedPart = AddProductTopTable.selectionModelProperty().get().getSelectedItem();
       if(relatedPart == null){
           //Displays the Error dialog box when a part is not selected
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Error Dialog");
           alert.setContentText("Please select a part you wish to add to the product.");
           alert.showAndWait();
       }
       else {
           newRelatedParts.add(relatedPart);
           AddProductBottomTable.setItems(newRelatedParts);
           AddProductPartIDBottomCol.setCellValueFactory(new PropertyValueFactory<>("id"));
           AddProductPartNameBottomCol.setCellValueFactory(new PropertyValueFactory<>("name"));
           AddProductInventoryBottomCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
           AddProductPriceBottomCol.setCellValueFactory(new PropertyValueFactory<>("price"));
       }


    }

    /** This is the onAction method for the 'Remove Associated Part' button on the Add Product screen.
     This removes selected parts from the bottom table.
     The user is presented with a Confirmation dialog box to ensure they wish to delete the part.
     The part will be removed from the list when the 'OK' button is selected on the dialog box, otherwise no action will occur.
     */
    public void onActionAddProductRemovePart(){
       //This will display a Confirmation window to ensure that deletion is wanted
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will permanently remove the part associated with this product. If you wish to continue, click OK.");
       Optional<ButtonType> result = alert.showAndWait();

       //If the OK button is pressed the program will delete the related part from the associatedParts list
       if(result.isPresent() && result.get() == ButtonType.OK) {
           Part relatedPart = AddProductBottomTable.selectionModelProperty().get().getSelectedItem();
           newRelatedParts.remove(relatedPart);
           AddProductBottomTable.setItems(newRelatedParts);
       }
    }

    /** This is the onAction for the Save button on the Add Product screen.
     When the button is clicked, it will create a new product with unique id and assign associated parts from the bottom table.
     When there is no or incorrect information in the text fields, a NumberFormatException is throw.
     The exception is remedied with a try/catch which will display an Error dialog box to input correct values.
     If/else statements are used to make sure that the max value is greater than the min value and stock falls somewhere between the max and min values.
     */
    public void onActionAddProductSave(ActionEvent actionEvent) throws IOException{

        //Local Variables
        try {

            ObservableList<Product> listID = Inventory.getAllProducts();
            int maxValue = listID.get(listID.size() - 1).getId();//this looks at the list 'listID' and gets the last item in the list 'get(listID.size() - 1)' and returns the id

            int id = ++maxValue;
            String name = AddProductNameTxt.getText();
            double price = Double.parseDouble(AddProductPriceTxt.getText());
            int stock = Integer.parseInt(AddProductInvTxt.getText());
            int min = Integer.parseInt(AddProductMinTxt.getText());
            int max = Integer.parseInt(AddProductMaxTxt.getText());
            ObservableList<Part> assosicatedParts = AddProductBottomTable.getItems();

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

                Inventory.addProduct(new Product(id, name, price, stock, min, max, assosicatedParts));

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

    /** This is the onAction method for the Cancel button on the Add Product Form.
     This button redirects the user to the Main Form screen without saving the product to the ObservableList allProducts.
     To confirm that the user wishes to leave the page without add a product, a Confirmation dialog box is displayed that will continue the redirection to the Main screen when the OK button is clicked.
     */
    public void onActionAddProductCancel(ActionEvent actionEvent) throws IOException {

       //This will create a Dialog box that will confirm if you want to continue back to the main screen without saving information
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values. If you wish to continue, click OK.");
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
     This will pre-populate the top table with all of the available parts in the system.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        AddProductTopTable.setItems(Inventory.getAllParts());
        AddProductPartIDTopCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AddProductPartNameTopCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AddProductInventoryTopCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AddProductPriceTopCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }
}
