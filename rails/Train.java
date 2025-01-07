package smart.rails;

import javafx.scene.paint.Color;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * A train is what will be moving around the train components.
 * Implements runnable to because its own active thread
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Train implements Runnable {
    private BlockingQueue<Message> queue;
    private Component connection;
    private Color color;
    private static int trainCount = 0;
    private int trainNumber;

    /**
     * Constructor for a new train
     * Sets the color to grey and sets the connection based on the station name
     * it was added to.
     *
     * @param components  The components of the board that the train is being added to
     * @param stationName The station name that the train is being added to
     */
    public Train(List<Component> components, char stationName) {
        setColor(Color.GREY);
        for (Component component : components) {
            if (component instanceof Station s) {
                if (s.getLabel() == stationName) {
                    connection = component;
                    component.setTrain(this);
                }
            }
        }
        queue = new LinkedBlockingQueue<>();
        trainNumber = trainCount++;

    }

    /**
     * reads messages from the station that it is connected to it
     */
    @Override
    public void run() {
        try {
            while (true) {
                Message value;
                value = queue.take();
                handleMessage(value);
            }
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }

    /**
     * Handle some messages that are passed to the train from the components that
     * are connected to it
     *
     * @param message message that is being handled
     */
    private void handleMessage(Message message) {
        System.out.println(this + " : " + message.getType());
        try {
            Thread.sleep(Board.getTimeDelay());
        } catch (InterruptedException e) {
            System.err.println(e);
        }

        switch (message.getType()) {
            case SEEK_PATH -> {
                connection.addToQueue(message);
                setColor(Color.YELLOW);
            }
            case PATH_FOUND -> {
                message.setLockPath();
                setColor(Color.RED);
                connection.addToQueue(message);
            }
            case NO_PATH -> {
                setColor(Color.GREY);
            }
            case LOCK_FAILED -> {
                if (message.getDirection() == Direction.LEFT) {
                    try {
                        Thread.sleep((Board.getTimeDelay() + 1) * 10L);
                    } catch (InterruptedException e) {
                        System.err.println(e);
                    }
                }
                message.setLockPath();
                connection.addToQueue(message);
            }
            case LOCK_SUCCESS -> {
                setColor(Color.GREEN);
                message.setMoveTrain();
                message.setTrain(this);
                this.addToQueue(message);
            }
            case MOVE_TRAIN -> {
                setColor(Color.GREEN);
                if (connection instanceof Station s) {
                    if (s.getLabel() == message.getStartingStation()) {
                        s.addToQueue(message);
                    } else {
                        System.out.println("Move Complete");
                        setColor(Color.GREY);
                    }
                }
            }

        }
    }

    /**
     * Gets the current color of the train
     *
     * @return The color of the train
     */
    public synchronized Color getColor() {
        return color;
    }

    /**
     * Sets the color of the train
     *
     * @param color the new color of the train
     */
    public synchronized void setColor(Color color) {
        this.color = color;
    }

    /**
     * Gets the x coordinate of the train
     *
     * @return the x coordinate of the train
     */
    public double getX() {

        return connection.getX();
    }

    /**
     * Gets the y coordinate of the train
     *
     * @return the y coordinate of the train
     */
    public double getY() {
        return connection.getY();
    }

    /**
     * Adds a new message to the trains inbox
     *
     * @param value message that is being added to the train
     */
    public void addToQueue(Message value) {
        try {
            queue.put(value);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }

    /**
     * Sets the connection for the train
     *
     * @param connection the new connection of the train
     */
    public void setConnection(Component connection) {
        this.connection = connection;
    }

    /**
     * Gets the connection of the train
     *
     * @return the connection of the train
     */

    public Component getConnection(){
        return connection;
    }

    public char getStationLabel() {
        if (connection instanceof Station s) {
            return s.getLabel();
        }
        //This should never happen
        System.out.println("How did we get here???");
        return '\0';
    }

    /**
     * To string for a train
     *
     * @return a string representation of a train
     */
    @Override
    public String toString() {
        return "Train#" + trainNumber;
    }

    public int getTrainNumber() {
        return trainNumber;
    }
}
