package smart.rails;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A board where all the train components are and the active trains are
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Board {
    private List<Component> components;
    private List<Station> stations;
    private List<RailSegment> railSegments;
    private List<Switch> switches;
    private static int timeDelay = 500;

    /**
     * Constructor to have a board of empty components
     */
    public Board() {
        components = new ArrayList<>();
        stations = new ArrayList<>();
        railSegments = new ArrayList<>();
        switches = new ArrayList<>();
    }

    /**
     * Based on a file that is read in as a parameter
     * Build all of the required components in the file and check if the
     * file was valid
     *
     * @param fileName file path to where the list of the components are
     */
    public Board(String fileName) {
        this();
        List<String> input = FileIO.readFile(fileName);
        for (String s : input) {
            String[] line = s.split(" ");
            switch (line[0]) {
                case "station" -> {
                    Station newStation = new Station(Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]));
                    components.add(newStation);
                    stations.add(newStation);
                }
                case "track" -> {
                    if (line.length == 5) {
                        RailSegment temp = new RailSegment(Double.parseDouble(line[1]),
                                Double.parseDouble(line[2]), Double.parseDouble(line[3]),
                                Double.parseDouble(line[4]));
                        components.add(temp);
                        railSegments.add(temp);
                    } else {
                        double x = Double.parseDouble(line[1]);
                        double y = Double.parseDouble(line[2]);
                        double endX = Double.parseDouble(line[3]);
                        double endY = Double.parseDouble(line[4]);
                        int numSegments = Integer.parseInt(line[5]);
                        if (x > endX) {
                            double temp = x;
                            x = endX;
                            endX = temp;
                            temp = y;
                            y = endY;
                            endY = temp;
                        }
                        double deltaX = (endX - x) / numSegments;
                        double deltaY = (endY - y) / numSegments;
                        for (int i = 0; i < numSegments; i++) {
                            RailSegment temp = new RailSegment(x, y,
                                    x + deltaX, y + deltaY);
                            railSegments.add(temp);
                            components.add(temp);
                            x += deltaX;
                            y += deltaY;
                        }
                    }

                }
                case "switch" -> {
                    Switch newSwitch = new Switch(Double.parseDouble(line[1]),
                            Double.parseDouble(line[2]));
                    components.add(newSwitch);
                    switches.add(newSwitch);
                }
            }
        }
        //Checking for crossing of two track
        for (int i = 0; i < railSegments.size(); i++) {
            for (int j = 0; j < railSegments.size(); j++) {
                if (i != j) {
                    if (checkForCrossing(railSegments.get(i), railSegments.get(j))) {
                        System.err.println("Crossing " + railSegments.get(i) + " and "
                                + railSegments.get(j));
                        System.err.println("BROKEN INPUT PLEASE FIX");
                        components.clear();
                        stations.clear();
                        railSegments.clear();
                        switches.clear();
                    }
                }
            }
        }
        makeConnections();
        if (!validationOfInput()) {
            System.err.println("SOMETHING DISCONNECTED");
            System.err.println("BROKEN INPUT PLEASE FIX");
            components.clear();
            stations.clear();
            railSegments.clear();
            switches.clear();
        }

        startComponents();
    }

    /**
     * Gets the components from the board
     * @return the list of components
     */
    public List<Component> getComponents() {
        return components;
    }

    /**
     * Makes all of the connections for the components
     */
    public void makeConnections() {
        for (Component currentComponent : components) {
            if (currentComponent instanceof Station) {
                for (RailSegment railSegment : railSegments) {
                    if (currentComponent.getPosition()
                            .equals(railSegment.getPosition())) {
                        ((Station) currentComponent).setRight(railSegment);
                        break;
                    } else if (currentComponent.getPosition()
                            .equals(railSegment.getEndPosition())) {
                        ((Station) currentComponent).setLeft(railSegment);
                        break;
                    }
                }
            } else if (currentComponent instanceof Switch) {
                for (RailSegment railSegment : railSegments) {
                    if (currentComponent.getPosition()
                            .equals(railSegment.getPosition())) {
                        ((Switch) currentComponent).addConnection(railSegment,
                                true);

                    } else if (currentComponent.getPosition()
                            .equals(railSegment.getEndPosition())) {
                        ((Switch) currentComponent).addConnection(railSegment,
                                false);

                    }
                }

            } else if (currentComponent instanceof RailSegment) {
                for (Component component : components) {
                    if (component instanceof RailSegment) {
                        if (component.getPosition().equals(((RailSegment)
                                currentComponent).getEndPosition())) {
                            ((RailSegment) currentComponent).setRight(component);
                        } else if (((RailSegment) component).getEndPosition()
                                .equals(currentComponent.getPosition())) {
                            ((RailSegment) currentComponent).setLeft(component);
                        }
                    } else {
                        if (component.getPosition().equals(currentComponent
                                .getPosition())) {
                            ((RailSegment) currentComponent).setLeft(component);
                        } else if (component.getPosition()
                                .equals(((RailSegment) currentComponent)
                                        .getEndPosition())) {
                            ((RailSegment) currentComponent).setRight(component);
                        }
                    }
                }

            }
        }
        System.out.println();
    }

    /**
     * Gets the list of stations
     * @return The list of stations
     */
    public List<Station> getStations(){
        return stations;
    }

    /**
     * Gets the list of rail segments
     * @return the list of rail segments
     */
    public List<RailSegment> getRailSegments(){return railSegments;
    }

    /**
     * Start all of the component threads
     */
    public void startComponents() {
        for (Component currentComponent : components) {
            Thread thread = new Thread(currentComponent);
            thread.start();
        }
    }

    /**
     * Checks to see if two rail segments are crossing
     *
     * @param rail1 first rail segment
     * @param rail2 second rail segment
     * @return if the two segments cross
     */
    private boolean checkForCrossing(RailSegment rail1, RailSegment rail2) {
        if (rail1.getPosition().equals(rail2.getPosition()) &&
                rail1.getEndPosition().equals(rail2.getEndPosition())) {
            return true;
        }
        if (rail1.getPosition().equals(rail2.getPosition())) {
            return false;
        } else if (rail1.getPosition().equals(rail2.getEndPosition())) {
            return false;
        } else if (rail1.getEndPosition().equals(rail2.getEndPosition())) {
            return false;
        } else if (rail1.getEndPosition().equals(rail2.getPosition())) {
            return false;
        }
        Point one = rail1.getPosition();
        Point two = rail1.getEndPosition();
        Point three = rail2.getPosition();
        Point four = rail2.getEndPosition();
        Line2D line = new Line2D.Double(one.getX(), one.getY(), two.getX(),
                two.getY());
        Line2D line2 = new Line2D.Double(three.getX(), three.getY(), four.getX(),
                four.getY());
        return line.intersectsLine(line2);
    }

    /**
     * Returns the time delay of the board
     * @return the time delay as an int
     */
    public static int getTimeDelay() {
        return timeDelay;
    }

    /**
     * sets the time delay of all of the boards
     * @param time the time that messages will be delayed when handling
     */
    public void setTimeDelay(int time) {
        timeDelay = time;
    }

    private boolean validationOfInput() {
        for (Component currentComponent : components) {
            switch (currentComponent) {
                case Station station -> {
                    if (!station.hasConnection()) {
                        return false;
                    }
                }
                case Switch Switch -> {
                    if (!Switch.hasConnection()) {
                        return false;
                    }
                }
                case RailSegment railSegment -> {
                    if (!railSegment.hasConnection()) {
                        return false;
                    }
                }
                default -> {
                    throw new IllegalStateException("Unexpected value: " + currentComponent);
                }

            }
        }
        return true;
    }
}
