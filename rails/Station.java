package smart.rails;

/**
 * A station has both a left and right connection and a label.
 * This class extends Component
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Station extends Component {
    private RailSegment right;
    private RailSegment left;
    private char label = 'A';
    private static int stationCounter = 0;

    /**
     * Station constructor
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Station(double x, double y) {
        super(x, y);
        label = (char) (label + stationCounter);
        stationCounter++;
    }

    /**
     * Handles the message that is provided to the station.
     * Will also pass messages to the train when required
     * @param message message that needs to be handled
     */
    @Override
    protected void handleMessage(Message message) {
        System.out.println(this + " : " + message.getType());
        delay(Board.getTimeDelay());
        switch (message.getType()) {
            case PRINT_CONNECTIONS -> {
                System.out.print(this.label + ": ");
                System.out.println(this);
                addToPath(message);

                if (message.getDirection() == Direction.RIGHT && right != null) {
                    right.addToQueue(message);
                } else if (message.getDirection() == Direction.LEFT && left != null) {
                    left.addToQueue(message);
                }
            }
            case SEEK_PATH -> {
                if (message.getDestination() == label && !hasTrain()) {
                    System.out.println("Path found");
                    message.setPathFound();
                    this.addToQueue(message);
                } else {
                    if (message.getDirection() == Direction.RIGHT && right != null) {
                        right.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && left != null) {
                        left.addToQueue(message);
                    } else {
                        System.out.println("No path found ");
                        message.setNoPathFound();
                        this.addToQueue(message);
                    }
                }
            }
            case PATH_FOUND -> {
                if (message.getStartingStation() == label) {
                    passToTrain(message);
                    System.out.println("Path received");
                } else {
                    if (message.getDirection() == Direction.RIGHT && left != null) {
                        left.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && right != null) {
                        right.addToQueue(message);
                    }
                }
            }
            //Combine this with lock failed and lock success and with path found
            case NO_PATH -> {
                //Maybe combine these two because they are the same
                if (message.getStartingStation() == label) {
                    passToTrain(message);
                    System.out.println("No path received");
                } else {
                    if (message.getDirection() == Direction.RIGHT && left != null) {
                        left.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && right != null) {
                        right.addToQueue(message);
                    }
                }
            }
            case LOCK_PATH -> {
                if (tryLock()) {
                    if (message.getDestination() == label) {
                        System.out.println("Lock successful");
                        message.setLockSuccessful();
                        this.addToQueue(message);
                    } else {
                        if (message.getDirection() == Direction.RIGHT && right != null) {
                            right.addToQueue(message);
                        } else if (message.getDirection() == Direction.LEFT && left != null) {
                            left.addToQueue(message);
                        }
                    }
                } else {
                    message.setLockFailed();
                    passToTrain(message);
                }
            }
            case LOCK_SUCCESS -> {
                if (message.getStartingStation() == label) {
                    passToTrain(message);
                    System.out.println("Lock Completed");
                } else {
                    if (message.getDirection() == Direction.RIGHT && left != null) {
                        left.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && right != null) {
                        right.addToQueue(message);
                    }
                }
            }
            case LOCK_FAILED -> {
                if (message.getStartingStation() == label) {
                    passToTrain(message);
                    System.out.println("Lock failed");
                } else {
                    if (message.getDirection() == Direction.RIGHT && left != null) {
                        left.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && right != null) {
                        right.addToQueue(message);
                    }
                }
                unlock();
            }
            case MOVE_TRAIN -> {
                if (message.getDestination() == label) {
                    setTrain(message.getTrain());
                    passToTrain(message);
                    unlock();
                } else {
                    if (!hasTrain()) {
                        setTrain(message.getTrain());
                        this.addToQueue(message);
                    } else {
                        message.setTrain(getTrain());
                        if (message.getDirection() == Direction.RIGHT && right != null) {
                            right.addToQueue(message);
                        } else if (message.getDirection() == Direction.LEFT && left != null) {
                            left.addToQueue(message);
                        }
                        unlock();
                    }
                }
            }
        }
    }

    /**
     * Sets the left connection of the train station.
     * Stations can only connect to rail segments
     * @param left the new left connection
     */
    public void setLeft(RailSegment left) {
        this.left = left;
    }

    /**
     * Sets the right connection of the train station.
     * Stations can only connect to rail segments
     * @param right the new right connection
     */
    public void setRight(RailSegment right) {
        this.right = right;
    }

    /**
     * Gets the label of the station
     * @return the label of the station as a char
     */
    public char getLabel() {
        return label;
    }

    public boolean hasConnection() {
        return !(right == null && left == null);
    }
}
