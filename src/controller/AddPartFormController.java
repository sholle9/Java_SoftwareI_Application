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
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller for the Add Part screen. */
public class AddPartFormController implements Initializable {

    @FXML
    private RadioButton AddPartInHouseRtBtn;

    @FXML
    private RadioButton AddPartOutsourcedRtBtn;

    @FXML
    private TextField AddPartIDTxt;

    @FXML
    private TextField AddPartNameTxt;

    @FXML
    private TextField AddPartInvTxt;

    @FXML
    private TextField AddPartPriceTxt;

    @FXML
    private TextField AddPartMaxTxt;

    @FXML
    private TextField AddPartMachineIDTxt;

    @FXML
    private TextField AddPartMinTxt;

    @FXML
    private Button AddPartSaveBtn;

    @FXML
    private Button AddPartCancelBtn;

    @FXML
    private Label AddPartMachineIdLbl;


    //Global Variables
    Stage stage;
    Parent scene;

    /** This is the onAction method for when the In-House Radio Button on the Add Part screen.
     When the Radio Button In-House is selected, the AddPartMachineIdLbl label displays the text 'Machine ID'.
     */
    public void onActionAddPartInHouse(){

        AddPartMachineIdLbl.setText("Machine ID");
    }

    /** This is the onAction method for the Outsourced Radio Button.
     When the radio button Outsource is selected, the AddPartMachineIdLbl label displays the text 'Company Name'.
     */
    public void onActionAddPartOutsourced(){

        AddPartMachineIdLbl.setText("Company Name");
    }

    /** This is the onAction method for the Save button.
     When the Save button is clicked, the information entered in the text fields (except the id field) are added to the ObservableList allParts as a new part.
     A NumberFormatException is encountered if no values or incorrect values are entered. The try/catch method is used to create an error message asking for correct values.
     If/else statements are used to make sure that the max value is greater than the min value and stock falls somewhere between the max and min values.
     */
    @FXML
    public void onActionAddPartSave(ActionEvent actionEvent) throws IOException{
        //Local Variables
        try {
            ObservableList<Part> listID = Inventory.getAllParts();
            int maxValue = listID.get(listID.size() - 1).getId();//this looks at the list 'listID' and gets the last item in the list 'get(listID.size() - 1)' and returns the id


            int id = ++maxValue;
            String name = AddPartNameTxt.getText();
            int stock = Integer.parseInt(AddPartInvTxt.getText());
            double price = Double.parseDouble(AddPartPriceTxt.getText());
            int min = Integer.parseInt(AddPartMinTxt.getText());
            int max = Integer.parseInt(AddPartMaxTxt.getText());


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
                //when user selects In-House RadioButton vs Outsourced
                if (AddPartInHouseRtBtn.isSelected()) {
                    int machineId = Integer.parseInt(AddPartMachineIDTxt.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                } else {
                    String companyName = AddPartMachineIDTxt.getText();
                    Inventory.addPart(new OutSourced(id, name, price, stock, min, max, companyName));
                }


                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field!");

            alert.showAndWait(); //Displays the Error dialog box
        }
    }

    /** This is the onAction for the AddPartForm Cancel Button.
     When the cancel button is clicked, the user is redirected to the Main Form.
     This will not save information imputed, so there is a Confirmation box presented to ensure the user wishes to continue.
     If the user clicks 'OK' on the Confirmation box, it will redirect to the Main Form without saving; and if not, the user remains on the AddPartForm screen.
     */
    @FXML
    public void onActionAddPartCancel(ActionEvent actionEvent) throws IOException {

        //This will create a Dialog box that will confirm if you want to continue back to the main screen without saving information
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values. If you wish to continue, click OK.");
        Optional<ButtonType> result = alert.showAndWait();

        //If the OK button is pressed the program will direct back to the main Form without adding to the part table
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /** This is the AddPartFormController initialize method.
     This method is created when the application is launched.
     This method ensures that the In-House radio button is selected when the form is opened to ensure that the new part has either an in-house or outsourced method called.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        AddPartInHouseRtBtn.setSelected(true);
    }

}
