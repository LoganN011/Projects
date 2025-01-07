package smart.rails.create.board;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import javafx.stage.Stage;
import smart.rails.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * boardSetUp is the main Class for the GUI.
 * In boardSetUp it has the creation of the Station, Rails, and Switches. These three will be static.
 * It has two Global variables, Group and Canvas that are being passed to Controller so the Locks and the Trains can
 * be Updated.
 *
 * @author Cameron Fox and Logan Nunno
 */
public class boardSetUp extends Application {
    public static Group trainLabels = new Group();
    public static double screenWidth = Screen.getPrimary().getBounds().getWidth() - 100;
    public static double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private static Board board;
    private static List<String> classes = new ArrayList<>();
    private static List<double[]> points = new ArrayList<>();
    public static List<Train> trainList = new ArrayList<>();
    public static GraphicsContext updateTrainGC;
    public static Canvas updateCanvas;

    /**
     * @param primaryStage the primary stage for this application.
     */
    @Override
    public void start(Stage primaryStage) {
        updateCanvas = new Canvas(screenWidth - 300, screenHeight);
        updateTrainGC = updateCanvas.getGraphicsContext2D();

        Canvas canvas = new Canvas(screenWidth - 300, screenHeight);
        trainLabels.getChildren().add(canvas);
        //Side of Screen
        Controller controller = new Controller();
        Canvas rightSideRectangle = new Canvas(300, screenHeight - 200);
        GraphicsContext rightGC = rightSideRectangle.getGraphicsContext2D();
        rightGC.setFill(Color.WHITE);
        rightGC.fillRect(0, 0, 300, screenHeight - 200);

        StackPane rightRectangle = new StackPane();
        rightRectangle.getChildren().addAll(rightSideRectangle, controller.setUp(board));

        Label label = new Label();
        getComponents();
        String labelText = classes.stream().collect(Collectors.joining("\n"));
        label.setText(labelText);
        label.setMaxWidth(screenWidth);
        label.setWrapText(true);

        BorderPane mainScreen = new BorderPane();
        mainScreen.setBackground(Background.fill(Color.WHITE));

        List<Group> groups = makeRailComponents();

        StackPane trainYardStackPane = new StackPane();
        for(Group group : groups) {
            trainYardStackPane.getChildren().add(group);
        }

        trainYardStackPane.setBorder(new Border(new BorderStroke(
                Color.BLACK,
                BorderStrokeStyle.SOLID,
                new CornerRadii(0),
                new BorderWidths(1)
        )));
        AnchorPane anchorPane = new AnchorPane(trainLabels);
        AnchorPane.setTopAnchor(trainLabels, 0.0);
        AnchorPane.setBottomAnchor(trainLabels, 0.0);
        AnchorPane.setLeftAnchor(trainLabels, 0.0);
        AnchorPane.setRightAnchor(trainLabels, 0.0);
        trainYardStackPane.getChildren().addAll(anchorPane, updateCanvas);

        //Bottom of Screen
        Canvas bottomRectangle = new Canvas(screenWidth, 200);
        GraphicsContext bottomRectangleGC = bottomRectangle.getGraphicsContext2D();
        bottomRectangleGC.setFill(Color.BEIGE);
        bottomRectangleGC.fillRect(0, 0, screenWidth, 200);

        mainScreen.setCenter(trainYardStackPane);
        mainScreen.setRight(rightRectangle);
        //mainScreen.setBottom(bottomStackPane);

        Scene scene = new Scene(mainScreen, screenWidth, screenHeight);
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @return List<Group> </Group> the groups of Components to be drawn on the screen.
     */
    private List<Group> makeRailComponents() {
        List<Group> components = new ArrayList<>();
        for(RailSegment railSegment : board.getRailSegments()) {
            components.add(createRail(railSegment));
        }

        for(Station station : board.getStations()){
            components.add(createStation(station));
        }
        int index = 0;
        for(String s : classes){
            if (s.equals("class smart.rails.Switch")){
                components.add(createSwitch(index));
            }
            index++;
        }
        return components;
    }

    /**
     * Create a group for a specific switch.
     * @param index the location of the component in classes and points.
     * @return returns a group of switches
     */
    private Group createSwitch(int index){
        Group switchGroup = new Group();
        Canvas canvas = new Canvas(screenWidth - 250, (screenHeight));
        GraphicsContext canvasGC = canvas.getGraphicsContext2D();
        double[] switchPoint = points.get(index);
        double x = sizing(switchPoint[0]);
        double y = sizing(switchPoint[1]);
        canvasGC.setFill(Color.YELLOW);
        canvasGC.fillRect(x, y, 25, 25);
        switchGroup.getChildren().add(canvas);
        return switchGroup;
    }

    /**
     * Create a group for a specific rail.
     * @param railSegment takes in a specific rail segment.
     * @return returns a group of rails.
     */
    private Group createRail(RailSegment railSegment) {
        //Initialize the stuff here
        Group rail = new Group();
        Canvas canvas = new Canvas(screenWidth - 250, (screenHeight));
        Line line = new Line();
        double middleY = 13;
        double spacingX = 8;
        double startingX = railSegment.getX();
        double startingY = railSegment.getY();
        double endingX = railSegment.getEndX();
        double endingY = railSegment.getEndY();
       startingX = sizing(startingX) + spacingX;
       startingY = sizing(startingY) + middleY;
       endingX = sizing(endingX) - spacingX;
       endingY = sizing(endingY) + middleY;

        //This is creating the lines
        line.setStartX(startingX);
        line.setStartY(startingY);
        line.setEndX(endingX);
        line.setEndY(endingY);
        line.setStroke(Color.SADDLEBROWN);
        line.setStrokeWidth(5);
        rail.getChildren().add(line);
        rail.getChildren().add(canvas);
        rail.setLayoutX(startingX);
        rail.setLayoutY(startingY);
        return rail;
    }

    /**
     * Create a group for a specific stations.
     * @param station Takes in a specific station.
     * @return returns a group of stations
     */
    private Group createStation(Station station) {
        //Initializing the stuff here
        Group stationGroup = new Group();
        Canvas canvas = new Canvas(screenWidth - 250, (screenHeight));
        GraphicsContext gc = canvas.getGraphicsContext2D();
        //Doing the Math here
        double x;
        double y;
        //This area is just to get X location
        x = sizing(station.getX());
        //This area is just to get Y location
        y = sizing(station.getY());

        //Creating the display here
        gc.setFill(Color.BLACK);
        gc.fillRect(x, y, 25,25);

        stationGroup.getChildren().add(canvas);
        Label label = new Label(String.valueOf(station.getLabel()));
        label.setLayoutX(x+8);
        label.setLayoutY(y-20);
        stationGroup.getChildren().add(label);
        stationGroup.setLayoutX(x);
        stationGroup.setLayoutY(y);

        return stationGroup;
    }

    /**
     * Scales the point
     * @param point the coordinate that needs to be scaled
     * @return the point that is scaled.
     */
    public static double sizing(double point){
        return point * 100;
    }

    /**
     * All the components are grabbed and placed in two global variables
     * which are being added into Classes and Points
     */
    private void getComponents(){
        for(Component c : board.getComponents()){
           // System.out.println(c.getClass());
            classes.add(String.valueOf(c.getClass()));
            //System.out.println(c.getX() + " " + c.getY());
            points.add(new double[] {c.getX(), c.getY()});
        }
    }

    /**
     * Main method launches the JavaFX
     * Takes at minimum 2 command line arguments
     * First is the file with the train components
     * second is the delay between handling messages
     *
     * @param args command line argument
     */
    public static void main(String[] args) {
        board = new Board();
        if (args.length >= 1) {
            board = new Board(args[0]);
        }
        if (args.length >= 2) {
            board.setTimeDelay(Integer.parseInt(args[1]));
        }
        launch(args);
    }
}
