package auctionHouse;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The main screen that shows the main info about the AH.
 * It will have all available items for sale and the amount of money made
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class AuctionHouseMain {
    private final static int screenWidth =
            (int) Screen.getPrimary().getBounds().getWidth();
    private final static int screenHeight =
            (int) Screen.getPrimary().getBounds().getHeight() - 100;
    private static Label amountEarned;
    private static BorderPane mainBorderPane = new BorderPane();

    public static void showAuctionHouse(Stage primaryStage) {
        //Label Creation
        Label mainLabel = new Label(AuctionHouse.auctionHouse.getName() + " " +
                AuctionHouseCreation.ahPortNumber);
        amountEarned = new Label("Made a profit of $" +
                AuctionHouse.auctionHouse.getBalance());

        //Editing the Labels
        mainLabel.setFont(new Font("Arial", 50));
        mainLabel.setAlignment(Pos.CENTER);

        amountEarned.setFont(new Font("Arial", 25));

        //HBox for the Main Label to Center it.
        HBox mainLabelHbox = new HBox();
        mainLabelHbox.getChildren().add(mainLabel);
        mainLabelHbox.setAlignment(Pos.TOP_CENTER);

        //Creating the Box's as to what things are being sold
        HBox sellingHBox = createSellingItems();
        sellingHBox.setAlignment(Pos.CENTER);

        //HBox made to center the amountEarned Label
        HBox moneyMadeBox = new HBox(amountEarned);
        moneyMadeBox.setAlignment(Pos.TOP_CENTER);

        //BorderPane being made
        mainBorderPane.setBackground(Background.fill(Color.WHITE));
        mainBorderPane.setTop(mainLabelHbox);
        mainBorderPane.setCenter(sellingHBox);
        mainBorderPane.setRight(moneyMadeBox);

        Scene mainScene = new Scene(mainBorderPane, screenWidth, screenHeight);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Auction House Screen");
        primaryStage.show();

        //Exit Everything Here
        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // put this here first to stop GUI from disappearing
            Thread thread = new Thread(() -> {
                AuctionHouse.auctionHouse.emptyRemainingList();
                while(AuctionHouse.auctionHouse.getActiveItems().bidsActive()) {
                    try {
                        Thread.sleep(1000); //keep checking every sec
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                update();
                AuctionHouse.auctionHouse.disconnectMe();
                System.exit(0);
             });
            thread.start();
        });


        //This centers the window on the screen.
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth()
                - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight()
                - primaryStage.getHeight()) / 2);
    }

    /**
     * This Method creates the HBox of items being sold.
     * @return The H box of the items for sale
     */
    private static HBox createSellingItems(){
        int fontSize = 25;
        HBox hBox = new HBox(20);
        Listing items = AuctionHouse.auctionHouse.getActiveItems();
        List<String> descriptions = new ArrayList<>();
        List<Double> minBids = new ArrayList<>();
        List<Double> currBids = new ArrayList<>();
        List<String> agents = new ArrayList<>();
        List<String> startTimes = new ArrayList<>();

        for (ItemForSale item : items.getListing()) {
            descriptions.add(item.getDescription());
            minBids.add(item.getMinBid());
            currBids.add(item.getCurrBid());
            agents.add(item.getAgent());
            if (item.getBidTime() == null) {
                startTimes.add("None");
            }else {
                startTimes.add(item.getBidTime().toString());
            }
        }

        for(int i = 0; i < descriptions.size(); i++) {

            VBox vbox = new VBox(20);
            Label descriptionLabel = new Label(descriptions.get(i));
            DecimalFormat format = new DecimalFormat("#.##");
            Label minBidLabel = new Label("Minimal Bid: " + format.format(minBids.get(i)));
            Label currBidLabel = new Label("Current Bid: " + format.format(currBids.get(i)));
            Label agentLabel = new Label();
            if(agents.get(i) == null){
                agentLabel.setText("Agent: " + "None");
            } else {
                agentLabel.setText("Agent: " + agents.get(i));
            }
            Label timeLabel = new Label();
            if(startTimes.get(i).equals("None")) {
                timeLabel.setText("Timer Hasn't Started");
            }

            descriptionLabel.setFont(new Font("Arial", fontSize));
            minBidLabel.setFont(new Font("Arial", fontSize));
            currBidLabel.setFont(new Font("Arial", fontSize));
            agentLabel.setFont(new Font("Arial", fontSize));
            timeLabel.setFont(new Font("Arial", fontSize));

            if(!startTimes.get(i).equals("None")) {
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
                Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                    long secondsLeft = endTime.toEpochSecond() - ZonedDateTime.now().toEpochSecond();
                    if (secondsLeft > 0) {
                        timeLabel.setText(secondsLeft + " seconds remaining");
                    } else {
                        timeLabel.setText("Time's up!");
                    }
                }));
                timeLine.setCycleCount((int) remainingSeconds.get() + 1);
                timeLine.play();
            }
            vbox.getChildren().addAll(descriptionLabel, minBidLabel, currBidLabel, agentLabel, timeLabel);
            vbox.setAlignment(Pos.CENTER);
            vbox.setStyle("-fx-border-color: black; -fx-border-width: 2; -fx-padding: 10;");

            vbox.setMaxHeight(200);
            vbox.setMinWidth(300);

            hBox.getChildren().add(vbox);
        }
        hBox.setAlignment(Pos.CENTER);
        mainBorderPane.setCenter(hBox);
        return hBox;
    }

    /**
     * This method will update the screen everytime it is called.
     */
    public static void update(){
        Platform.runLater(()->{
            createSellingItems();
            amountEarned.setText("Made a profit of $" + AuctionHouse.auctionHouse.getBalance());
        });
    }
}
