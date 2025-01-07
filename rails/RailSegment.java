package smart.rails;

/**
 * A rail segment is a single length of track that has connections on both
 * the left and right side. Also has anm ending position. extends component
 *
 * @author Cameron Fox and Logan Nunno
 */
public class RailSegment extends Component {

    private Point endPosition;
    private Component right;
    private Component left;

    /**
     * Constructor for a rail segment
     * @param x starting x coordinate
     * @param y starting y coordinate
     * @param endX ending x coordinate
     * @param endY ending y coordinate
     */
    public RailSegment(double x, double y, double endX, double endY) {
        super(x, y);
        endPosition = new Point(endX, endY);
    }

    /**
     * Checks if two rail segments are equals
     * to be equal they need to both have the same starting and ending position
     * @param other the other rail that is being checked
     * @return if two segments are equal
     */
    public boolean equals(RailSegment other) {
        return this.endPosition.equals(other.endPosition) && this.position.equals(other.position);
    }

    /**
     * To string of the rail segment
     * @return if two rail segment are the same
     */
    public String toString() {
        return super.toString() + " | " + endPosition.toString();
    }

    /**
     * Handles the messages that are provided to the rail segment
     * @param message message that needs to be handled
     */
    @Override
    protected void handleMessage(Message message) {
        System.out.println(this + " : " + message.getType());
        delay(Board.getTimeDelay());
        switch (message.getType()) {
            case PRINT_CONNECTIONS -> {
                System.out.println(this);
                addToPath(message);
                //Just doing both directions for now
                if (message.getDirection() == Direction.RIGHT) {
                    right.addToQueue(message);
                } else if (message.getDirection() == Direction.LEFT) {
                    left.addToQueue(message);
                }
            }
            case SEEK_PATH -> {

                addToPath(message);
                if (message.getDirection() == Direction.RIGHT) {
                    right.addToQueue(message);
                } else if (message.getDirection() == Direction.LEFT) {
                    left.addToQueue(message);
                }
            }
            case PATH_FOUND, NO_PATH, LOCK_SUCCESS -> {
                if (message.getDirection() == Direction.RIGHT) {
                    left.addToQueue(message);
                } else if (message.getDirection() == Direction.LEFT) {
                    right.addToQueue(message);
                }
            }
            case LOCK_FAILED -> {
                if (message.getDirection() == Direction.RIGHT) {
                    left.addToQueue(message);
                } else if (message.getDirection() == Direction.LEFT) {
                    right.addToQueue(message);
                }
                unlock();
            }
            case LOCK_PATH -> {
                if (tryLock()) {
                    if (message.getDirection() == Direction.RIGHT && !right.isLocked()) {
                        right.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT && !left.isLocked()) {
                        left.addToQueue(message);
                    } else {
                        message.setLockFailed();
                        this.addToQueue(message);
                    }
                } else {
                    message.setLockFailed();
                    this.addToQueue(message);
                }
            }
            case MOVE_TRAIN -> {
                if (!hasTrain()) {
                    setTrain(message.getTrain());
                    this.addToQueue(message);
                } else {
                    message.setTrain(getTrain());
                    if (message.getDirection() == Direction.RIGHT) {
                        right.addToQueue(message);
                    } else if (message.getDirection() == Direction.LEFT) {
                        left.addToQueue(message);
                    }
                    unlock();
                }
            }

        }
    }

    /**
     * gets the end x coordinate of the rail segment
     * @return the ending x coordinate
     */
    public double getEndX() {
        return endPosition.getX();
    }

    /**
     * gets the end y coordinate of the rail segment
     *
     * @return the ending y coordinate
     */
    public double getEndY() {
        return endPosition.getY();
    }

    //Todo:add java doc comments to this please
    public double getMidX() {
        return (getX() + getEndX()) / 2;
    }

    public double getMidY() {
        return (getY() + getEndY()) / 2;
    }

    /**
     * gets the ending position of the rail segment
     *
     * @return the ending position
     */
    public Point getEndPosition() {
        return endPosition;
    }

    /**
     * Set the right connection of the rail segment
     * unless a switch is already in the connection
     * @param right the component that will be the right connection
     */
    public void setRight(Component right) {
        if (!(this.right instanceof Switch)) {
            this.right = right;
        }
    }

    /**
     * Set the left connection of the rail segment
     * unless a switch is already in the connection
     * @param left the component that will be the left connection
     */
    public void setLeft(Component left) {
        if (!(this.left instanceof Switch)) {
            this.left = left;
        }
    }

    @Override
    public boolean hasConnection() {
        return !(right == null || left == null);
    }
}
