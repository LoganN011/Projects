package auctionHouse;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Creates the JavaFX GUI for the Auction House
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.2, 12/6/24
 */

public class AuctionHouseCreation extends Application {
    public static String accountName;
    public static String ahPortNumber;
    private int promptTextSize = 200;
    private static String[] args;

    /**
     * Method to start the creation screen of the AH
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    public void start(Stage primaryStage) {
        //Label for the Welcome Screen
        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setFont(new Font("Arial", 20));
        Label topLabel = new Label("Please set up an account with"
                +" the bank.");
        //Labels to help guide the AH creation process
        Label AHPortLabel = new Label("Please enter the port number for" +
                " Auction House here");
        AHPortLabel.setFont(new Font("Arial", 12));
        Label hostNameLabel = new Label("Please enter the bank name here");
        hostNameLabel.setFont(new Font("Arial", 12));
        Label bankPortLabel = new Label("Please enter the banks port number here");
        bankPortLabel.setFont(new Font("Arial", 12));

        //Area where the TextFields are made
        TextField accountPortNumber = new TextField();
        accountPortNumber.setMaxWidth(promptTextSize);
        accountPortNumber.setPromptText("Enter account port number");

        TextField enterBankHostNameHere = new TextField();
        enterBankHostNameHere.setMaxWidth(promptTextSize);
        enterBankHostNameHere.setPromptText("Enter Bank Host Name here");

        TextField bankPortNumber = new TextField();
        bankPortNumber.setMaxWidth(promptTextSize);
        bankPortNumber.setPromptText("Enter Bank Port Number");

        //Vbox to combine the labels and TextFields
        VBox AHPortVBox = new VBox();
        AHPortVBox.getChildren().addAll(AHPortLabel, accountPortNumber);
        AHPortVBox.setAlignment(Pos.CENTER);

        VBox hostNameVBox = new VBox();
        hostNameVBox.getChildren().addAll(hostNameLabel, enterBankHostNameHere);
        hostNameVBox.setAlignment(Pos.CENTER);

        VBox bankPortVBox = new VBox();
        bankPortVBox.getChildren().addAll(bankPortLabel, bankPortNumber);
        bankPortVBox.setAlignment(Pos.CENTER);

        VBox combinedVBox = new VBox();
        combinedVBox.getChildren().addAll(AHPortVBox, hostNameVBox, bankPortVBox);
        combinedVBox.setAlignment(Pos.CENTER);


        //Button made here
        Button saveButton = new Button("Create Account");

        //Button action
        saveButton.setOnAction(e -> {
            ahPortNumber = accountPortNumber.getText();
            String[] passMe = {ahPortNumber, enterBankHostNameHere.getText(),
                    bankPortNumber.getText(), accountName};

            try {
                // Validate that ahPortNumber and bankPortNumber are numeric
                Integer.parseInt(ahPortNumber); // Throws NumberFormatException if not a number
                Integer.parseInt(bankPortNumber.getText()); // Throws NumberFormatException if not a number

                AuctionHouse.start(passMe);
                AuctionHouseMain.showAuctionHouse(primaryStage);
            } catch (NumberFormatException ex) {
                // Handle invalid numeric inputs
                showAlert("Invalid Input", "Both the Auction House" +
                        " port number and the Bank port number must be numeric. " +
                        "Please try again.");
            } catch (IOException ex) {
                // Handle potential issues with starting the AuctionHouse
                showAlert("Startup Error", "There was an issue " +
                        "starting the Auction House. Please check your inputs " +
                        "and try again.");
            } catch (RuntimeException ex) {
                // Handle other unexpected runtime exceptions
                showAlert("Unexpected Error", "An unexpected error" +
                        " occurred. Please check your inputs and try again.");
            }
        });

        VBox mainBox = new VBox(10);
        mainBox.getChildren().addAll(welcomeLabel,topLabel, combinedVBox, saveButton);
        mainBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainBox,300,300);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveButton.fire();
            }
        });

        if (!commandLineStart(args)) {
            primaryStage.setScene(scene);
            primaryStage.setTitle("Auction House Screen");
            primaryStage.show();
        } else {
            try {
                AuctionHouse.start(args);
                AuctionHouseMain.showAuctionHouse(primaryStage);
            } catch (Exception ex) {
                showAlert("Startup Error", "INVALID COMMAND LINE ARGUMENTS!!!");
            }

        }
    }

    /**
     * Method to show a pop-up window when something is entered in wrong during
     * the creation of the Auction House
     * @param title The error's title passed in as a string
     * @param message The error passed in as a string
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method will read in command line arguments to skip over the GUI
     * @param args String Array passed in from the command line
     * @return if there was a command line start
     */
    private static boolean commandLineStart(String[] args) {
        if (args != null && args.length == 3) {
            ahPortNumber = args[0];
            return true;
        }
        return false;
    }

    /**
     * This method starts the Auction House
     * @param args String Array passed in from the command line
     */
    public static void main(String[] args) {
        if (args.length == 3) {
            AuctionHouseCreation.args = args;
        }
        launch(args);
    }
}