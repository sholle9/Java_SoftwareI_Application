package controller;

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

/** This is the controller for the Modify Part screen*/
public class ModifyPartFormController{

    @FXML
    private RadioButton ModifyPartInHouseRtBtn;

    @FXML
    private RadioButton ModifyPartOutsourcedRtBtn;

    @FXML
    private TextField ModifyPartIDTxt;

    @FXML
    private TextField ModifyPartNameTxt;

    @FXML
    private TextField ModifyPartInvTxt;

    @FXML
    private TextField ModifyPartPriceTxt;

    @FXML
    private TextField ModifyPartMaxTxt;

    @FXML
    private TextField ModifyPartMachineIDTxt;

    @FXML
    private TextField ModifyPartMinTxt;

    @FXML
    private Button ModifyPartSaveBtn;

    @FXML
    private Button ModifyPartCancelBtn;

    @FXML
    private Label ModifyPartMachineIdLbl;

    //Global Variables
    Stage stage;
    Parent scene;

    /** This is the onAction method for when the In-House Radio Button on the Modify Part screen.
     When the Radio Button In-House is selected, the AddPartMachineIdLbl label displays the text 'Machine ID'.
     */
    public void onActionModifyInHouse(){
        ModifyPartMachineIdLbl.setText("Machine ID");
    }

    /** This is the onAction method for the Outsourced Radio Button on the Modify Part screen.
     When the radio button Outsource is selected, the AddPartMachineIdLbl label displays the text 'Company Name'.
     */
    public void onActionModifyOutsourced(){
        ModifyPartMachineIdLbl.setText("Company Name");
    }

    /** This is the sendPart method.
     This method is used to set the text for the text field to the information of the selected part from the Main Form screen.
     If/else statement is used to deteremine the class as either InHouse or Outsourced and set the final text field to either machineId or companyName, respectively.
     @param newPart The part selected to display the information in the text fields.
     */
    public void sendPart(Part newPart) {
        ModifyPartIDTxt.setText(String.valueOf(newPart.getId()));
        ModifyPartNameTxt.setText(newPart.getName());
        ModifyPartInvTxt.setText(String.valueOf(newPart.getStock()));
        ModifyPartPriceTxt.setText(String.valueOf(newPart.getPrice()));
        ModifyPartMinTxt.setText(String.valueOf(newPart.getMin()));
        ModifyPartMaxTxt.setText(String.valueOf(newPart.getMax()));



        if(newPart instanceof InHouse){
            ModifyPartMachineIDTxt.setText(String.valueOf(((InHouse) newPart).getMachineId()));
            ModifyPartInHouseRtBtn.setSelected(true);
        }
        else if(newPart instanceof OutSourced){
            ModifyPartMachineIDTxt.setText(((OutSourced) newPart).getCompanyName());
            ModifyPartOutsourcedRtBtn.setSelected(true);
        }

    }

    /** This is the onAction method for the ModifyPartForm Save button.
     When the Save button is clicked, the information entered in the text fields (except the id field) are modified to the part selected part from ObservableList allParts.
     A NumberFormatException is encountered if no values or incorrect values are entered. The try/catch method is used to create an error message asking for correct values.
     If/else statements are used to make sure that the max value is greater than the min value and stock falls somewhere between the max and min values.
     */
    public void onActionModifyPartSave(ActionEvent actionEvent) throws IOException{

        try {
            int id = Integer.parseInt(ModifyPartIDTxt.getText());
            String name = ModifyPartNameTxt.getText();
            int stock = Integer.parseInt(ModifyPartInvTxt.getText());
            double price = Double.parseDouble(ModifyPartPriceTxt.getText());
            int min = Integer.parseInt(ModifyPartMinTxt.getText());
            int max = Integer.parseInt(ModifyPartMaxTxt.getText());


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
                if (ModifyPartInHouseRtBtn.isSelected()) {
                    int machineId = Integer.parseInt(ModifyPartMachineIDTxt.getText());
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineId));
                } else if (ModifyPartOutsourcedRtBtn.isSelected()) {
                    String companyName = ModifyPartMachineIDTxt.getText();
                    Inventory.updatePart(id, new OutSourced(id, name, price, stock, min, max, companyName));
                }

                stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/View/MainForm.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e){
            //Displays the Error dialog box that the modified information will not be saved
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field!");
            alert.showAndWait();
        }
    }

    /** This is the onAction for the ModifyPartForm Cancel Button.
     When the cancel button is clicked, the user is redirected to the Main Form.
     This will not save information imputed, so there is a Confirmation box presented to ensure the user wishes to continue.
     If the user clicks 'OK' on the Confirmation box, it will redirect to the Main Form without saving; and if not, the user remains on the AddPartForm screen.
     */
    public void onActionModifyPartCancel(ActionEvent actionEvent) throws IOException {

        //This will create a Dialog box that will confirm if you want to continue back to the main screen without saving information
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values. If you wish to continue, click OK.");
        Optional<ButtonType> result = alert.showAndWait();

        //If the OK button is pressed the program will direct back to the main Form without adding to the part table
        if(result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/View/MainForm.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }


}
