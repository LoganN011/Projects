package smart.rails;

import java.util.ArrayList;

/**
 * Message is passed between threads
 * Used to find a path for trains and move train
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Message {
    private MessageType type;
    private Direction direction;
    private ArrayList<Component> path;
    private char destination;
    private char startingStation;
    private Train train;

    /**
     * Constructor for a Message
     *
     * @param type            Type of the message (seek path, lock path,ect.)
     * @param direction       Direction that we are moving
     * @param destination     destination of the message
     * @param startingStation starting location of the train
     */
    public Message(MessageType type, Direction direction, char destination,
                   char startingStation) {
        this.type = type;
        this.direction = direction;
        this.destination = destination;
        path = new ArrayList<>();
        this.startingStation = startingStation;
    }

    /**
     * Copy constructor for a message
     * @param message message that will be copied
     */
    public Message(Message message) {
        this.type = message.type;
        this.direction = message.direction;
        this.destination = message.destination;
        this.startingStation = message.startingStation;
        this.path = new ArrayList<>(message.path);
        this.train = message.train;

    }

    /**
     * Gets the type of the message
     * @return the message type as an enum
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Gets the direction of the message
     * @return the direction as an enum
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * To string of a message
     * @return a string representation of a message
     */
    public String toString() {
        return type.toString() + " " + direction;
    }

    /**
     * Gets the path of the message
     * @return the path as an array list of the components visited
     */
    public ArrayList<Component> getPath() {
        return path;
    }

    /**
     * Add a component to the path
     * @param component component that will be added
     */
    public void addToPath(Component component) {
        path.add(component);
    }

    /**
     * Gets the destination of the message
     * @return the destination as a char
     */
    public char getDestination() {
        return destination;
    }

    /**
     * Gets the Starting Station of the message
     * @return the Starting Station as a char
     */
    public char getStartingStation() {
        return startingStation;
    }

    /**
     * Sets the type to path found
     */
    public void setPathFound() {
        this.type = MessageType.PATH_FOUND;
    }

    /**
     * sets the type to no path
     */
    public void setNoPathFound() {
        this.type = MessageType.NO_PATH;
    }

    /**
     * set the type to lock path
     */
    public void setLockPath() {
        this.type = MessageType.LOCK_PATH;
    }

    /**
     * sets the type as locked failed
     */
    public void setLockFailed() {
        this.type = MessageType.LOCK_FAILED;
    }

    /**
     * sets the type as lock successful
     */
    public void setLockSuccessful() {
        this.type = MessageType.LOCK_SUCCESS;
    }

    /**
     * sets the type to move train
     */
    public void setMoveTrain() {
        this.type = MessageType.MOVE_TRAIN;
    }

    /**
     * Gets the part of the path that is before a provided component
     * @param component component that we will find the path before
     * @return the path before the provided component
     */
    public ArrayList<Component> getPartPath(Component component) {
        ArrayList<Component> partPath = new ArrayList<>();
        for (Component c : path) {
            if (c.equals(component)) {
                partPath.add(c);
                break;
            } else {
                partPath.add(c);
            }
        }
        return partPath;
    }

    /**
     * sets the train for the message that is being moved
     * @param train train that is moving
     */
    public void setTrain(Train train) {
        this.train = train;
    }

    /**
     * Gets the train from the message and removes it from the message
     * @return the train that was in the message
     */
    public Train getTrain() {
        Train temp = train;
        train = null;
        return temp;
    }

}
