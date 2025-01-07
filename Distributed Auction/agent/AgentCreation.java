package agent;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * This is the file for creation of an agent account
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class AgentCreation extends Application {
    public static String accountNamePublic;
    public static double amountOfMoney = 0;
    private static String[] args;
    public static Agent agent;
    private final int promptTextSize = 200;
    private static boolean started;

    /**
     * This the welcome screen for the agent and will call the main agent
     * screen once the user is created. If there are command line arguments
     * to start the agent or auto bidder this screen will be bypassed
     */
    @Override
    public void start(Stage primaryStage) {
        //Label for the Welcome Screen
        Label welcomeLabel =  new Label("Welcome!");
        welcomeLabel.setFont(new Font("Arial", 20));
        Label topLabel = new Label("Please set up an account with"
                +" the bank.");
        //Label for the TextField
        Label enterNameLabel = new Label("Enter your account name here:");
        enterNameLabel.setFont(new Font("Arial", 12));
        Label moneyAmountLabel = new Label("Enter your money amount here:");
        moneyAmountLabel.setFont(new Font("Arial", 12));
        Label hostNameLabel = new Label("Enter the bank name here:");
        hostNameLabel.setFont(new Font("Arial", 12));
        Label portNumberLabel = new Label("Enter the port number here:");
        portNumberLabel.setFont(new Font("Arial", 12));


        //Area where the TextFields are made
        TextField accountNameField = new TextField();
        accountNameField.setMaxWidth(promptTextSize);
        accountNameField.setPromptText("Enter account name");
        TextField numberField = new TextField();
        numberField.setMaxWidth(promptTextSize);
        numberField.setPromptText("Enter amount of money");
        TextField enterBankHostNameHere = new TextField();
        enterBankHostNameHere.setMaxWidth(promptTextSize);
        enterBankHostNameHere.setPromptText("Enter Bank Host Name here");
        TextField bankPortNumber = new TextField();
        bankPortNumber.setMaxWidth(promptTextSize);
        bankPortNumber.setPromptText("Enter Bank Port Number");

        //Vbox to combine the textField and the Labels
        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(enterNameLabel, accountNameField);
        nameBox.setAlignment(Pos.CENTER);

        VBox numberBox = new VBox();
        numberBox.getChildren().addAll(moneyAmountLabel, numberField);
        numberBox.setAlignment(Pos.CENTER);

        VBox bankBox = new VBox();
        bankBox.getChildren().addAll(hostNameLabel, enterBankHostNameHere);
        bankBox.setAlignment(Pos.CENTER);

        VBox portBox = new VBox();
        portBox.getChildren().addAll(portNumberLabel, bankPortNumber);
        portBox.setAlignment(Pos.CENTER);

        VBox comboBox = new VBox(10);
        comboBox.getChildren().addAll(nameBox, numberBox, bankBox, portBox);
        comboBox.setAlignment(Pos.CENTER);

        //Save button is made here
        Button saveButton = new Button("Create Account");
        //CheckBox created here
        CheckBox checkBox = new CheckBox("Add Auto Bidder");

        //Button action
        saveButton.setOnAction(e -> {
            try {
                accountNamePublic = accountNameField.getText();
                int portNumber = Integer.parseInt(numberField.getText());
                String hostName = enterBankHostNameHere.getText();
                int bankPort = Integer.parseInt(bankPortNumber.getText());
                agent = new Agent(portNumber, accountNamePublic, hostName, bankPort, false);
                amountOfMoney = agent.getBalance();
                if(checkBox.isSelected()) {
                    AutoBidderGUI.autoBidderGUI(primaryStage, hostName, bankPort);
                } else {
                    started = true;
                    AgentScreenMain.showAgentMainScreen(primaryStage);
                }
            } catch (NumberFormatException ex) {
                // Handle invalid number inputs
                showAlert("Invalid Input",
                        "Port numbers must be numeric. Please try again.");
            } catch (RuntimeException ex) {
                // Handle connection issues
                showAlert("Connection Error",
                        "Unable to connect to the bank server. " +
                                "Please check the host name and port.");
            }
        });

        HBox buttonHbox = new HBox(20, saveButton, checkBox);
        buttonHbox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(10);
        mainBox.getChildren().addAll(welcomeLabel,topLabel, comboBox, buttonHbox);
        mainBox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainBox,300,300);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveButton.fire();
            }
        });
        if (startAutoBidder(args)) {
            System.out.println("Starting AutoBidder");
        } else if (!commandLineStart(args)) {
            primaryStage.setScene(scene);
            primaryStage.setTitle("Agent Screen");
            primaryStage.show();
        } else {
            AgentScreenMain.showAgentMainScreen(primaryStage);
        }

    }

    /**
     * Method for when the user messes up.
     * @param title The title of the Alert
     * @param message The text of the Alert
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to check to see if the GUI has started
     *
     * @return if the GUI should/has started
     */
    public static boolean getStarted() {
        return started;
    }

    /**
     * Sets the status of the gui to started
     */
    public static void start() {
        started = true;
    }

    /**
     * Method to start normal Agent with the command line
     * @param args the command line arguments
     * @return if there was valid command line args to start the agent
     */
    private static boolean commandLineStart(String[] args) {
        if (args != null && args.length == 4) {
            accountNamePublic = args[0];
            amountOfMoney = Double.parseDouble(args[1]);
            agent = new Agent(amountOfMoney, accountNamePublic, args[2],
                    Integer.parseInt(args[3]), false);
            start();
            return true;
        }
        return false;
    }

    /**
     * Method to see if we can start and auto bidder from the command line
     * @param args the command line argument
     * @return if the auto agent was started from the command line
     */
    private static boolean startAutoBidder(String[] args) {
        if (args != null && args.length == 5) {
            accountNamePublic = args[0];
            amountOfMoney = Double.parseDouble(args[1]);
            agent = new Agent(amountOfMoney, accountNamePublic, args[2],
                    Integer.parseInt(args[3]), true);
            agent.autoBid();
            return true;
        }
        return false;
    }

    /**
     * This is the start of the GUI and the agent.
     * @param args is the command line arguments.
     */
    public static void main(String[] args){
        AgentCreation.args = args;
        launch(args);
    }
}