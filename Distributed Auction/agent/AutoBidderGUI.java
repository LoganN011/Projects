package agent;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * A GUI element that is used when A Auto Bidder is starting
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class AutoBidderGUI {
    private static final int promptTextSize = 200;

    /**
     * The method that will make the screen elements of the GUI to request info
     * from the user about the new auto bidder being added
     * @param primaryStage The main stage of the JavaFX screen
     * @param hostName the bank address
     * @param port the port number of the bank
     */
    public static void autoBidderGUI(Stage primaryStage, String hostName, int port) {
        //Label Set Up
        Label title = new Label("Create an Auto Bidder");
        title.setFont(new Font("Arial", 20));

        Label enterNameLabel = new Label("Enter the auto bidder account " +
                "name here:");
        enterNameLabel.setFont(new Font("Arial", 12));
        Label moneyAmountLabel = new Label("Enter the amount of money for" +
                " the auto bidder here:");
        moneyAmountLabel.setFont(new Font("Arial", 12));

        //TextField Set Up Here
        TextField name = new TextField();
        name.setMaxWidth(promptTextSize);
        name.setPromptText("Enter Auto Bidder Name Here");

        TextField moneyAmount = new TextField();
        moneyAmount.setMaxWidth(promptTextSize);
        moneyAmount.setPromptText("Enter Auto Bidder Money Here");

        VBox nameBox = new VBox(enterNameLabel, name);
        nameBox.setAlignment(Pos.CENTER);

        VBox moneyBox = new VBox(moneyAmountLabel, moneyAmount);
        moneyBox.setAlignment(Pos.CENTER);

        VBox comboBox = new VBox(10, nameBox, moneyBox);
        comboBox.setAlignment(Pos.CENTER);

        //Button Set Up
        Button saveButton = new Button("Save Auto Bidder");
        saveButton.setOnAction(event -> {
            startAutoBidder(name.getText(), Integer.parseInt(moneyAmount.getText()), hostName, port);
            AgentCreation.start();
            AgentScreenMain.showAgentMainScreen(primaryStage);
        });

        VBox mainBox = new VBox(20, title, comboBox, saveButton);
        mainBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainBox, 300, 300);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveButton.fire();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setTitle("Auto Bidder Screen");
        primaryStage.show();
    }

    /**
     * Function to call the Auto Bidder from the GUI
     * @param name        the name of the AutoBidder
     * @param moneyAmount the amount of money for the bidder
     * @param hostName    the name of the Bank Server
     * @param port        the port number of the Bank Server
     */
    private static void startAutoBidder(String name, int moneyAmount, String hostName, int port) {
        Agent agent = new Agent(moneyAmount, name, hostName, port, true);
        agent.autoBid();
    }
}
