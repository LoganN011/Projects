package smart.rails;

/**
 * Class that is used to test the trains
 *
 * @author Cameron Fox and Logan Nunno
 */
public class Main {

    /**
     * Main method to start the trains in the console
     * Takes at minimum 2 command line arguments
     * First is the file with the train components
     * second is the delay between handling messages
     *
     * @param args command line argument
     */
    public static void main(String[] args) {
        Board board = new Board();
        if (args.length >= 1) {
            board = new Board(args[0]);
        }
        if (args.length >= 2) {
            board.setTimeDelay(Integer.parseInt(args[1]));
        }
        for (Component cur : board.getComponents()) {
            System.out.println(cur);
        }

        Train train = new Train(board.getComponents(), 'A');
        Thread trainThread = new Thread(train);
        trainThread.start();

        Train train2 = new Train(board.getComponents(), 'B');
        Thread trainThread2 = new Thread(train2);
        trainThread2.start();

        train.addToQueue(new Message(MessageType.SEEK_PATH, Direction.RIGHT, 'D', 'A'));
        train2.addToQueue(new Message(MessageType.SEEK_PATH, Direction.LEFT, 'C', 'B'));
    }


}
