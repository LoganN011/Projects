package smart.rails;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * A Switch has a number of connection on both the left and the right.
 * It also has bookkeeping to track of the messages that were sent out that it
 * is waiting for a repose on. Extends component
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Switch extends Component {
    private ArrayList<RailSegment> right;
    private ArrayList<RailSegment> left;
    private ConcurrentHashMap<ArrayList<Component>, AtomicInteger>
            messageTracking = new ConcurrentHashMap<>();

    /**
     * Switch constructor
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Switch(double x, double y) {
        super(x, y);
        right = new ArrayList<>();
        left = new ArrayList<>();
    }

    /**
     * Handles messages passed to the switch. Will wait for response when looking
     * for a path. Has checking for when passing messages back and forward based
     * on the path that was found.
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
                if (message.getDirection() == Direction.RIGHT) {
                    right.forEach(r -> {
                        r.addToQueue(message);

                    });
                } else if (message.getDirection() == Direction.LEFT) {
                    left.forEach(l -> {
                        l.addToQueue(message);
                    });
                }
            }
            case SEEK_PATH -> {
                message.addToPath(this);
                if (message.getDirection() == Direction.RIGHT) {
                    right.forEach(r -> {
                        r.addToQueue(new Message(message));
                        addToTracking(message);
                    });
                } else if (message.getDirection() == Direction.LEFT) {
                    left.forEach(l -> {

                        l.addToQueue(new Message(message));
                        addToTracking(message);
                    });
                }
            }
            case PATH_FOUND -> {
                if (removeKey(message)) {
                    passMessage(message, left, right);
                }
            }
            case NO_PATH -> {
                decrementTracking(message);
            }
            case LOCK_PATH -> {
                if (tryLock()) {
                    passMessage(message, right, left);
                } else {
                    message.setLockFailed();
                    this.addToQueue(message);
                }
            }
            case LOCK_SUCCESS -> {
                passMessage(message, left, right);
            }
            case LOCK_FAILED -> {
                passMessage(message, left, right);
                unlock();
            }
            case MOVE_TRAIN -> {
                if (!hasTrain()) {
                    setTrain(message.getTrain());
                    this.addToQueue(message);
                } else {
                    message.setTrain(getTrain());
                    passMessage(message, right, left);
                    unlock();
                }
            }
        }
    }

    /**
     * Adds a component to either the left or right connection
     * @param track Track that is going to be added to the left or right side
     * @param isRight true if the track is going on the right false if not
     */
    public void addConnection(RailSegment track, boolean isRight) {
        if (isRight) {
            right.add(track);
        } else {
            left.add(track);
        }
    }

    /**
     * Adds a messages to the tracking. does this but getting the path up to this
     * switch and uses that as the key and increase how many messages it is
     * waiting response from that message
     * @param message the message that is being added to tracking
     */
    public synchronized void addToTracking(Message message) {
        boolean flag = false;
        for (ArrayList<Component> current : messageTracking.keySet()) {
            if (current.equals(message.getPartPath(this))) {
                messageTracking.get(message.getPartPath(this)).getAndIncrement();
                flag = true;
            }
        }
        if (!flag) {
            messageTracking.put(message.getPartPath(this), new AtomicInteger(1));
        }
    }

    /**
     * Decreasing the tracking of the current message. Finds the message that
     * is being track if it exists then if it no longer waiting for
     * response send the message back.
     * @param message the message that is being decremented
     */
    public synchronized void decrementTracking(Message message) {

        for (ArrayList<Component> c : messageTracking.keySet()) {
            if (c.equals(message.getPartPath(this))) {
                messageTracking.get(c).getAndDecrement();
                if (messageTracking.get(c).get() == 0) {
                    messageTracking.remove(c);
                    passMessage(message, left, right);
                }
                break;
            }
        }
    }

    /**
     * Passes a message to the right or left connection based on the path that
     * was found by the message. This can be used to pass messages forwards or
     * backwards depending on what you provided as the left or right list of connection
     *
     * @param message the message that is being passed
     * @param right   the list of right rail segments
     * @param left    the list of left rail segments
     */
    private void passMessage(Message message, ArrayList<RailSegment>
            right, ArrayList<RailSegment> left) {
        if (message.getDirection() == Direction.RIGHT) {
            right.forEach(r -> {
                for (Component current : message.getPath()) {
                    if (current instanceof RailSegment c) {
                        if (c.equals(r)) {
                            r.addToQueue(message);
                            break;
                        }
                    }

                }
            });
        } else if (message.getDirection() == Direction.LEFT) {
            left.forEach(l -> {
                for (Component current : message.getPath()) {
                    if (current instanceof RailSegment c) {
                        if (c.equals(l)) {
                            l.addToQueue(message);
                            break;
                        }
                    }
                }
            });

        }
    }

    /**
     * removes a key from the message tracking tool
     * @param message message that will be removed from the tracking
     * @return if the key was removed or not
     */
    public synchronized boolean removeKey(Message message) {
        boolean flag = false;
        for (ArrayList<Component> c : messageTracking.keySet()) {
            if (c.equals(message.getPartPath(this))) {
                messageTracking.remove(c);
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean hasConnection() {
        return !right.isEmpty() && !left.isEmpty();
    }
}
