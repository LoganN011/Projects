package agent;

import auctionHouse.ItemForSale;
import auctionHouse.Listing;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The main screen of the Agent for the GUI
 * has both the screen for individual AHs and selecting the AH an agent wants
 * to bid on
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class AgentScreenMain {
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static VBox auctionVBox = new VBox(20);
    private static HBox auctionHBox;
    private static int auctionHouseCurrently = 0;
    public static AgentScreenMain agentScreenMain = new AgentScreenMain();
    private final int hBoxSpacing = 20;
    private static List<String> boughtList = new ArrayList<>();
    private static Label moneyAmount = new Label("");
    private static BorderPane mainScreen = new BorderPane();
    private static Boolean updateAH = false;
    private static ConcurrentHashMap.Entry<Pair<String, Integer>, Listing> currentAH;
    private static Scene mainScene;
    private static ArrayList<TextField> amountToBid = new ArrayList<>();

    /**
     * This is the main screen for the Agent.
     *
     * @param primaryStage passing in the primary stage so this becomes the main
     *                     screen.
     */
    public static void showAgentMainScreen(Stage primaryStage) {
        amountToBid.add(new TextField());
        amountToBid.add(new TextField());
        amountToBid.add(new TextField());
        if (!AgentCreation.agent.isAutoBidder()) {
            AgentScreenMain.screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
            AgentScreenMain.screenHeight = (int) Screen.getPrimary().getBounds().getHeight() - 100;
        }

        auctionVBox = new VBox(20);
        auctionHBox = new HBox(agentScreenMain.hBoxSpacing);
        auctionVBox.getChildren().add(auctionHBox);


        //Making Labels
        Label mainLabel = new Label(AgentCreation.accountNamePublic
                + "'s Lobby");
        moneyAmount.setText("Total In Bank: $"
                + AgentCreation.amountOfMoney);
        Label boughtLabel = new Label("List of Bought Items");

        //Editing the Labels
        mainLabel.setFont(new Font("Arial", 50));
        mainLabel.setAlignment(Pos.CENTER);
        moneyAmount.setFont(new Font("Arial", 20));
        moneyAmount.setAlignment(Pos.TOP_CENTER);
        boughtLabel.setFont(new Font("Arial", 20));
        boughtLabel.setAlignment(Pos.TOP_CENTER);


        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            Thread thread = new Thread(() -> {
                while (true) {
                    if (AgentCreation.agent.tryDisconnect()) {
                        System.exit(0);
                    } else {
                        try {

                            Thread.sleep(1000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            });
            thread.start();

        });


        Button addHouseButton = new Button();
        addHouseButton.setText("Add House");
        addHouseButton.setTextAlignment(TextAlignment.CENTER);
        addHouseButton.setFont(new Font("Arial", 20));
        addHouseButton.setMinWidth(50);
        addHouseButton.setMinHeight(50);
        addHouseButton.setBackground(new Background(new BackgroundFill(
                Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        addHouseButton.setAlignment(Pos.BOTTOM_CENTER);
        addHouseButton.setOnAction(event -> {
            updateAllScreen();
        });

        //HBox for the Top Labels
        HBox mainTitleBox = new HBox();
        mainTitleBox.getChildren().addAll(mainLabel);
        mainTitleBox.setAlignment(Pos.TOP_CENTER);


        HBox moneyAmountBox = new HBox();
        moneyAmountBox.getChildren().addAll(moneyAmount);
        moneyAmountBox.setAlignment(Pos.TOP_CENTER);

        HBox addHouseBox = new HBox();
        addHouseBox.getChildren().addAll(addHouseButton);
        addHouseBox.setAlignment(Pos.BOTTOM_CENTER);

        //Rectangle for the borders
        Canvas rightRect = new Canvas(200, screenHeight - 100);
        GraphicsContext gc = rightRect.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 200, screenHeight);

        //Right Stack Pane to put the border on it.
        StackPane rightPane = new StackPane();
        rightPane.setMaxHeight(screenHeight - 100);
        rightPane.getChildren().addAll(rightRect, moneyAmountBox, addHouseBox);

        rightPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(1)
        )));

        //Rectangle for the Left Box
        Canvas leftRect = new Canvas(200, screenHeight);
        GraphicsContext gcLeft = leftRect.getGraphicsContext2D();
        gcLeft.setFill(Color.WHITE);
        gcLeft.fillRect(0, 0, 200, screenHeight);

        //VBox for the left side
        VBox leftvbox = new VBox();
        leftvbox.getChildren().addAll(boughtLabel);
        leftvbox.setAlignment(Pos.TOP_CENTER);

        //Left Stack Pane to put the border on it.
        StackPane leftPane = new StackPane();
        leftPane.getChildren().addAll(leftRect, leftvbox);

        for (String temp : boughtList) {
            leftPane.getChildren().add(new Label(temp));
        }

        leftPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(1)
        )));

        //This is where the auction house are created.
        mainScreen.setBackground(Background.fill(Color.WHITE));
        mainScreen.setPrefSize(screenWidth, screenHeight);
        mainScreen.setTop(mainTitleBox);
        mainScreen.setRight(rightPane);
        mainScreen.setLeft(leftPane);

        mainScene = new Scene(mainScreen, screenWidth, screenHeight);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Main Screen");
        primaryStage.show();


        //This centers the window on the screen.
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    /**
     * A method to make the GUI elements to have the user select which AH
     * They would like to look at
     *
     * @param entry The map entry holding the pair representing the AH connection info
     *              mapped to the listing for that AH
     */
    public static void createAuctionHouse(ConcurrentHashMap.Entry<Pair<String, Integer>,
            Listing> entry) {
        Pair<String, Integer> key = entry.getKey();
        int buttonSize = 100;
        String auctionName = AgentCreation.agent.getAHName(key);
        Button auctionHouse = new Button(auctionName);
        auctionHouse.setMaxHeight(buttonSize);
        auctionHouse.setMinHeight(buttonSize);
        auctionHouse.setMaxHeight(buttonSize);
        auctionHouse.setMinHeight(buttonSize);
        auctionHouse.setWrapText(true);
        auctionHBox.getChildren().add(auctionHouse);
        auctionHouseCurrently++;

        if (auctionHouseCurrently >= 3) {
            auctionHBox = new HBox(agentScreenMain.hBoxSpacing);
            auctionVBox.getChildren().add(auctionHBox);
            auctionHouseCurrently = 0;
            auctionVBox.setAlignment(Pos.CENTER);
            mainScreen.setCenter(auctionVBox);
        } else {
            auctionHBox.setAlignment(Pos.CENTER);
            mainScreen.setCenter(auctionHBox);
        }

        auctionHouse.setOnAction(event -> {
            updateAH = true;
            currentAH = entry;
            createAuctionHouseScreen(entry);
        });
    }

    public static void disconnectUpdate() {
        Platform.runLater(() -> {
            updateAH = false;
            currentAH = null;
            auctionVBox.getChildren().clear();
            auctionHBox.getChildren().clear();
            updateAllScreen();
        });

    }

    /**
     * Creates the screen that allows the user to see all items for sale in a AH
     * Allows user to select and item and place a bid on any of the given items
     *
     * @param entry The map entry holding the pair representing the AH connection info
     *              mapped to the listing for that AH
     */
    private static void createAuctionHouseScreen(ConcurrentHashMap.Entry<Pair<String, Integer>, Listing> entry) {
        int fontSize = 30;

        VBox vBox = new VBox();

        Button escape = new Button("Go Back");
        escape.setAlignment(Pos.TOP_LEFT);
        escape.setFont(new Font("Arial", 20));
        escape.setBackground(new Background(new BackgroundFill(
                Color.RED,  // Background color
                null,       // Corner radii (null for no rounded corners)
                Insets.EMPTY // Insets (padding around the background)
        )));

        escape.setOnAction(event -> {
            updateAH = false;
            currentAH = null;
            auctionVBox.getChildren().clear();
            auctionHBox.getChildren().clear();
            updateAllScreen();
        });
        vBox.getChildren().add(escape);

        HBox hBox = new HBox(20);

        Listing items = entry.getValue();
        List<String> descriptions = new ArrayList<>();
        List<Double> minBids = new ArrayList<>();
        List<Double> currBids = new ArrayList<>();
        List<String> agents = new ArrayList<>();
        List<String> startTimes = new ArrayList<>();
        List<Integer> itemNumbers = new ArrayList<>();

        for (ItemForSale item : items.getListing()) {
            descriptions.add(item.getDescription());
            minBids.add(item.getMinBid());
            currBids.add(item.getCurrBid());
            agents.add(item.getAgent());
            itemNumbers.add(item.getNumber());
            if (item.getBidTime() == null) {
                startTimes.add("None");
            } else {
                startTimes.add(item.getBidTime().toString());
            }
        }

        for (int i = 0; i < descriptions.size(); i++) {
            VBox vbox = new VBox(20);
            Label descriptionLabel = new Label(descriptions.get(i));
            descriptionLabel.setMaxWidth(300);
            DecimalFormat format = new DecimalFormat("#.##");
            Label minBidLabel = new Label("Minimal Bid: " + format.format(minBids.get(i)));
            Label currBidLabel = new Label("Current Bid: " + format.format(currBids.get(i)));
            Label agentLabel = new Label();
            if (agents.get(i) == null) {
                agentLabel.setText("Agent: " + "None");
            } else {
                agentLabel.setText("Agent: " + agents.get(i));
            }
            Label timeLabel = new Label();
            if (startTimes.get(i) == null) {
                timeLabel.setText("Timer Hasn't Started");
            }

            if (!startTimes.get(i).equals("None")) {
                String timestamp = startTimes.get(i);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy");
                ZonedDateTime startTime = null;
                try {
                    startTime = ZonedDateTime.parse(timestamp, formatter);
                } catch (DateTimeParseException e) {
                    timeLabel.setText("Invalid timestamp format");
                }
                ZonedDateTime endTime = startTime.plusSeconds(30);

                // Create countdown logic
                AtomicLong remainingSeconds = new AtomicLong(30); // 30 seconds countdown
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    long secondsLeft = endTime.toEpochSecond() - ZonedDateTime.now().toEpochSecond();
                    if (secondsLeft > 0) {
                        timeLabel.setText(secondsLeft + " seconds remaining");
                    } else {
                        timeLabel.setText("Time's up!");
                    }
                }));
                timeline.setCycleCount((int) remainingSeconds.get() + 1);
                timeline.play();
            }


            descriptionLabel.setFont(new Font("Arial", fontSize));
            minBidLabel.setFont(new Font("Arial", fontSize));
            currBidLabel.setFont(new Font("Arial", fontSize));
            agentLabel.setFont(new Font("Arial", fontSize));
            timeLabel.setFont(new Font("Arial", fontSize));
            for (TextField textField : amountToBid) {
                textField.setPromptText("Place Bid Here");
                textField.setMaxWidth(150);
            }


            Button submitBid = new Button("Submit Bid");
            submitBid.setFont(new Font("Arial", fontSize));
            submitBid.setBackground(new Background(new BackgroundFill(
                    Color.GREEN,  // Background color
                    null,       // Corner radii (null for no rounded corners)
                    Insets.EMPTY // Insets (padding around the background)
            )));
            int index = i;
            submitBid.setOnAction(event -> {
                if (!amountToBid.get(index).getText().isEmpty() &&
                        amountToBid.get(index).getText().matches("\\d+(\\.\\d+)?")) {
                    double bid = Double.parseDouble(amountToBid.get(index).getText());
                    submitButtonAction(bid, minBids.get(index),
                            itemNumbers.get(index), entry);
                }
            });

            //CHECK For it not to be evenFilter
            submitBid.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (!amountToBid.get(index).getText().isEmpty() &&
                            amountToBid.get(index).getText().matches("\\d+(\\.\\d+)?")) {
                        float bid = Float.parseFloat(amountToBid.get(index).getText());
                        submitButtonAction(bid, minBids.get(index), itemNumbers.get(index), entry);
                    }
                }
            });

            vbox.getChildren().addAll(descriptionLabel, minBidLabel,
                    currBidLabel, agentLabel, timeLabel,
                    amountToBid.get(i), submitBid);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: black;" +
                    " -fx-border-width: 2; -fx-padding: 10;");

            vbox.setMaxHeight(200);
            vbox.setMinWidth(300);


            hBox.getChildren().add(vbox);
        }
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(hBox);
        mainScreen.setCenter(vBox);

    }

    /**
     * Button action to submit a bid
     *
     * @param bid        the amount for the bid
     * @param minBid     the minimum bid of the item
     * @param itemNumber the item Number of the item
     * @param entry      The map entry holding the pair representing the AH connection info
     *                   mapped to the listing for that AH
     */
    private static void submitButtonAction(double bid,
                                           double minBid,
                                           int itemNumber,
                                           ConcurrentHashMap.Entry<Pair<String, Integer>,
                                                   Listing> entry) {
        if (bid <= AgentCreation.agent.getBalance() && bid >= minBid) {
            Agent agent = AgentCreation.agent;
            agent.bidOnItem(entry.getKey(), itemNumber, bid);
        }
    }

    /**
     * A method to show a pop-up when receiving new info in the agent
     *
     * @param info      The text of the pop-up
     * @param alertType the type of alert shown
     */
    public static void bidInfoPopUp(String info, Alert.AlertType alertType, boolean isAuto) {
        if (!isAuto) {
            Platform.runLater(() -> {
                Alert popUp;
                popUp = new Alert(alertType);
                popUp.setHeaderText(null);
                popUp.setTitle("Bid Info");
                popUp.setContentText(info);
                popUp.showAndWait();
            });
        }
    }


    /**
     * A method to update all elements on the screen when a message is received with
     * new info from either the bank or the auction house
     *
     */
    public static void updateAllScreen() {
        if (!AgentCreation.agent.isAutoBidder() && AgentCreation.getStarted()) {
            Platform.runLater(() -> {
                Agent agent = AgentCreation.agent;
                moneyAmount.setText("Total In Bank: $"
                        + agent.getBalance());
                if (updateAH) {
                    createAuctionHouseScreen(agent.getEntry(currentAH.getKey()));
                } else {
                    ConcurrentHashMap<Pair<String, Integer>, Listing> agentAllListings
                            = agent.getAllListings();
                    for (ConcurrentHashMap.Entry<Pair<String, Integer>,
                            Listing> entry : agentAllListings.entrySet()) {
                        createAuctionHouse(entry);
                    }
                }
                boughtList.clear();
                for (ItemForSale cur : AgentCreation.agent.getInventory()) {
                    boughtList.add(cur.getDescription());
                }


            });
        }
    }
}

