package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.util.Objects;

/** This is the Main class that houses the Main method for the Application. */
public class Main extends Application {

    /** This is the Start method.
     This method will mark the location of the view for the users to interact with first.
     @param primaryStage The location you want to call for the stage.
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/MainForm.fxml"));
        primaryStage.setTitle("SKatuzienskiTask1");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }


    /** This is the Main method.
     This method presets three products and three parts, and then launches the Main Form screen.
     */
    public static void main(String[] args) {



        Part part1 = new InHouse(1,"Brakes",12.99,15,1,20,4);
        Part part2 = new OutSourced(2,"Tire",14.99,15,1,20, "Katuzienski, Inc");
        Part part3 = new InHouse(3,"Rim",56.99,15,1,20,6);

        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        ObservableList<Part> related1 = FXCollections.observableArrayList();
        ObservableList<Part> related2 = FXCollections.observableArrayList();


        Product product1 = new Product(1,"Giant Bicycle",299.99,15,2,3, related1);
        Product product2 = new Product(2,"Scott Bicycle",199.99,15,3,4, related2);
        Product product3 = new Product(3,"GT Bike",99.99,15,4,5, related1);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);

        related1.add(part1);
        related1.add(part3);
        related2.add(part2);





        launch(args);

    }
}
