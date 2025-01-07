package smart.rails.create.board;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import smart.rails.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller sets up all the user interactions.
 * It creates the ComboBox that is used to select stations
 * It also updates the Trains, and the locks on the screen.
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Controller{
    private final List<String> trainsToMove = new ArrayList<>();
    private final List<String> endingStation = new ArrayList<>();
    private final List<Direction> directionEnum = new ArrayList<>();
    private AnimationTimer timer;
    private long lastUpdateTime = 0;
    private Board myBoard;
    private char stationChar;

    private final List<Label> trainNames =  new ArrayList<>();

    /**
     * setUp, prepares the buttons, and the ComboBox.
     * @param board is in ported to have the correct Stations and trains in use.
     * @return returns a group to be placed on the Screen.
     */
    public Group setUp(Board board){
        myBoard = board;
        Group group = new Group();

        //Buttons
        Button addTrain = new Button("Add Train");

        Button startSim = new Button("Start");

        VBox verticalMovingStation = new VBox(10);

        //Button on action
        addTrain.setOnAction(event -> {
            for(Station station : board.getStations()){
                if(station.getLabel() == stationChar&&!station.hasTrain()){
                    Train train = new Train(board.getComponents(), stationChar);
                    Thread trainThread = new Thread(train);
                    trainThread.start();
                    boardSetUp.trainList.add(train);
                    createTrain(train);
                    verticalMovingStation.getChildren().clear();
                    trainsToMove.clear();
                    endingStation.clear();
                    directionEnum.clear();


                    for (Train t : boardSetUp.trainList){
                        //This area is to create the correct amount of spots in the array.
                        trainsToMove.add(String.valueOf(t.getTrainNumber()));
                        endingStation.add(null);
                        directionEnum.add(null);
                        // Train Names
                        Label label = new Label(String.valueOf(t.getTrainNumber()));
                        trainNames.add(label);
                        //This area is creating the Box's
                        ComboBox<String> startingPlace =
                                createStartingStationBox(t.getTrainNumber());
                        ComboBox<String> endingPlace =
                                createEndingStationBox(t.getTrainNumber());
                        ComboBox<Direction> enumComboBox =
                                createDirectionBox(t.getTrainNumber());
                        HBox movingStation = new HBox(20);
                        movingStation.getChildren().addAll(startingPlace, endingPlace);
                        verticalMovingStation.getChildren().addAll(movingStation, enumComboBox);
                    }
                    break;
                }
            }
        });

        AnimationTime();
        start();

        startSim.setOnAction(event -> {
            for(int i  = 0; i < trainsToMove.size(); i++){
                String tempTrainLabel = trainsToMove.get(i);
                Train tempTrain = null;
                for(Train train1 : boardSetUp.trainList){
                    if(tempTrainLabel != null) {
                        if (train1.getTrainNumber() == (Integer.parseInt(tempTrainLabel))) {
                            tempTrain = train1;
                            break;
                        }
                    }
                }
                if (tempTrain != null && endingStation.get(i) != null &&
                        endingStation.get(i).charAt(0) != '\0' && directionEnum.get(i) != null) {
                    char startingStationLabel = tempTrain.getStationLabel();
                    tempTrain.addToQueue(new Message(MessageType.SEEK_PATH,
                            directionEnum.get(i), endingStation.get(i).charAt(0),
                            startingStationLabel));
                }
            }

            trainsToMove.clear();
            endingStation.clear();
            directionEnum.clear();
            for(Train t : boardSetUp.trainList){
                trainsToMove.add(String.valueOf(t.getTrainNumber()));
                endingStation.add(null);
                directionEnum.add(null);
            }
            System.out.println("saveStart Size " + trainsToMove.size() +
                    " endingStation size " + endingStation.size() + " directionEnum size "
                    + directionEnum.size());
        });


        //
        ComboBox<String> addTrainDropDown = new ComboBox<>();
        for (Station station : board.getStations()){
            addTrainDropDown.getItems().add(String.valueOf(station.getLabel()));
        }
        addTrainDropDown.setPromptText("Pick a Station");

        addTrainDropDown.setOnAction(event -> {
            String selectedOption = addTrainDropDown.getValue();
            stationChar = selectedOption.charAt(0);
        });

        //Box for the Buttons
        HBox startUpBox = new HBox(20);
        startUpBox.getChildren().addAll(addTrainDropDown, addTrain);

        VBox vBox = new VBox( 20);
        vBox.getChildren().addAll(startUpBox, verticalMovingStation, startSim);


        group.getChildren().addAll(vBox);

        return group;
    }

    /**
     * Creates the ComboBox for the List of Trains to be selected from.
     * @param trainNumber is the train label so we can keep track of which
     *                    train is where.
     * @return returns a ComboBox that is filled with the trainNumbers.
     */
    private ComboBox<String> createStartingStationBox(int trainNumber){
        ComboBox<String> startStation = new ComboBox<>();
        startStation.getItems().add(String.valueOf(trainNumber));
        startStation.setPromptText("Train Number " + trainNumber);

        startStation.setOnAction(event -> {
            trainsToMove.set(trainNumber, startStation.getValue());
            System.out.println("start Train Size " + trainsToMove.size());
        });
        return startStation;
    }

    /**
     * Creates the ComboBox that will hold all the
     * stations Labels to select from.
     * @param trainNumber is the train label so we can keep track
     *                   of which train is where.
     * @return returns a ComboBox that is filled with the Stations Labels.
     */
    private ComboBox<String> createEndingStationBox(int trainNumber){
        ComboBox<String> endingBox = new ComboBox<>();
        myBoard.getStations().forEach(station -> endingBox.getItems()
                .add(String.valueOf(station.getLabel())));
        endingBox.getItems().add("Stay");
        endingBox.setPromptText("Select Station");

        endingBox.setOnAction(event -> {
            endingStation.set(trainNumber, endingBox.getValue());

            System.out.println("Ending Station Size " + endingStation.size() +
                    " that was made with " + trainNumber + " Trainer Number");
        });
        return endingBox;
    }

    /**
     * Creates the ComboBox that will hold all the Enum Labels to select from.
     * @param trainNumber is the train label so we can keep track
     *                    of which train is where.
     * @return returns a ComboBox that is filled with the Enum Labels.
     */
    private ComboBox<Direction> createDirectionBox(int trainNumber) {
        ComboBox<Direction> directionBox = new ComboBox<>();
        directionBox.getItems().add(Direction.LEFT);
        directionBox.getItems().add(Direction.RIGHT);

        directionBox.setPromptText("Direction");

        directionBox.setOnAction(event -> {
            directionEnum.set(trainNumber, directionBox.getValue());
            System.out.println("Size of direction List " + directionEnum.size());
        });
        return directionBox;
    }

    /**
     * CreateTrains creates the train at its specific place.
     * If it is on a Rail, it will use the Rail MidPoints to draw its starting
     * point.
     * @param train this gives us which train is being drawn on the screen.
     */
    public void createTrain(Train train){
        Label label = new Label(String.valueOf(train.getTrainNumber()));
        double x;
        double y;
        if (train.getConnection() instanceof RailSegment railSegment) {
            x = boardSetUp.sizing(railSegment.getMidX());
            y = boardSetUp.sizing(railSegment.getMidY());
        } else {
            x = boardSetUp.sizing(train.getX());
            y = boardSetUp.sizing(train.getY());
        }

        boardSetUp.updateTrainGC.setFill(train.getColor());
        boardSetUp.updateTrainGC.fillRect(x, y, 30, 8);

        if(y-5 < 0){
            label.setLayoutY(y);
            label.setLayoutY(x + 50);
        } else {
            label.setLayoutY(y-5);
            label.setLayoutX(x+40);
        }

        if(boardSetUp.trainLabels.getChildren().size() == 2){
            boardSetUp.trainLabels.getChildren().remove(1);
        }
        boardSetUp.trainLabels.getChildren().add(label);
    }

    /**
     * This is a timer to redraw any changes that need to be
     * drawn on the screen.
     */
    private void AnimationTime() {
        //Future this should be an observer not animation timer
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                if (now - lastUpdateTime > (long) Board.getTimeDelay() * 1_000_000) {
                    boardSetUp.updateTrainGC.clearRect(0, 0,
                            boardSetUp.updateCanvas.getWidth(),
                            boardSetUp.updateCanvas.getHeight());
                    for(Train train : boardSetUp.trainList){
                        createTrain(train);
                    }
                    for(Component component : myBoard.getComponents()){
                        String classType = String.valueOf(component.getClass());

                        if(!classType.equals("class smart.rails.RailSegment")){
                            double x = boardSetUp.sizing(component.getX());
                            double y = boardSetUp.sizing(component.getY());
                            if(component.isLocked()){
                                boardSetUp.updateTrainGC.setFill(Color.RED);
                            }
                            else {
                                boardSetUp.updateTrainGC.setFill(Color.TRANSPARENT);
                            }
                            boardSetUp.updateTrainGC.fillOval(x, y, 25, 25);
                        }
                    }

                    for(RailSegment railSegment : myBoard.getRailSegments()){
                        double x = boardSetUp.sizing(railSegment.getMidX());
                        double y = boardSetUp.sizing(railSegment.getMidY());

                        //It is having the fat left AKA the starting point at the center...
                        x = x - 25 - (double) 25/2;

                        if(railSegment.isLocked()){
                            boardSetUp.updateTrainGC.setFill(Color.RED);
                        }
                        else {
                            boardSetUp.updateTrainGC.setFill(Color.TRANSPARENT);
                        }
                        boardSetUp.updateTrainGC.fillOval(x, y, 25, 25);
                    }
                    for(Train train : boardSetUp.trainList){
                        createTrain(train);
                    }
                    lastUpdateTime = now;
                }
            }
        };
    }

    /**
     * This starts the Animation Timer
     */
    private void start(){
        timer.start();
    }
}
