package smart.rails;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A component of a rail system.
 * An abstract class that implements runnable to become a thread.
 * Each component is its own active object
 *
 * @author Cameron Fox and Logan Nunno
 */
public abstract class Component implements Runnable {
    protected Point position;
    protected BlockingQueue<Message> queue;
    private Train train;
    private int lock;

    /**
     * Constructor for a component
     * Sets the position, initializes a blocking queue to handle messages,
     * and marks the component as unlocked
     * @param x x coordinate of the component
     * @param y y coordinate of the component
     */
    public Component(double x, double y) {
        position = new Point(x, y);
        queue = new LinkedBlockingQueue<>();
        lock = 0;
    }

    /**
     * checks to see if a component has a train
     * @return true is it has a train false if not
     */
    public synchronized boolean hasTrain() {
        return train != null;
    }

    /**
     * Gets the train from the component and removes it from the component
     * @return the train that was in the component
     */
    public Train getTrain() {
        Train temp = train;
        train = null;
        return temp;
    }

    /**
     * Sets the train of the component and updates the trains connection
     * @param train train that is being added to the component
     */
    public void setTrain(Train train) {
        train.setConnection(this);
        this.train = train;
    }

    /**
     * gets the x position of the component
     * @return x coordinate of the component
     */
    public double getX() {
        return position.getX();
    }

    /**
     * gets the y position of the component
     * @return y coordinate of the component
     */
    public double getY() {
        return position.getY();
    }

    /**
     * gets the position of the component
     * @return the position of the component
     */
    public Point getPosition() {
        return position;
    }

    /**
     * To string of the component
     * @return a string representation of the component
     */
    public String toString() {
        return this.getClass() + ": " + position.toString();
    }

    /**
     * Method that is called a thread starts
     * takes messages from the queue and handles them
     */
    public void run() {
        try {
            while (true) {
                Message value = queue.take();
                handleMessage(value);
            }

        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    /**
     * adds a message to the queue
     *
     * @param message message that is going to be added to the queue
     */
    public void addToQueue(Message message) {
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            System.err.println(e);
        }

    }

    /**
     * Handles the messages from the inbox.
     * Diffrent for each child of component
     * @param message message that needs to be handled
     */
    protected abstract void handleMessage(Message message);

    /**
     * Adds the component to the path of the provided message
     * @param message message that this component will be added to
     */
    protected void addToPath(Message message) {
        message.addToPath(this);
    }

    /**
     * Passes the provided message to the train of the current component
     * @param message message that is going to be passed to the train
     */
    protected void passToTrain(Message message) {
        if (train != null) {
            train.addToQueue(message);
        } else {
            System.err.println("Null Train broken");
        }

    }

    /**
     * Checks if the component is locked
     *
     * @return true if locked, false if not
     */
    public synchronized boolean isLocked() {
        return lock == 1;
    }

    /**
     * trys to lock the current component
     * @return true if it was locked, false if not
     */
    protected synchronized boolean tryLock() {
        if (lock == 0) {
            lock = 1;
            return true;
        }
        return false;
    }

    /**
     * unlocks the component
     */
    protected synchronized void unlock() {
        lock = 0;
    }

    /**
     * Sleep the current component for a provided about of time
     * @param time amount of time that the component is going to sleep for
     */
    protected void delay(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.err.println(e);
        }
    }

    public abstract boolean hasConnection();

}
