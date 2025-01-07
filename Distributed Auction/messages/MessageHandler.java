package messages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Abstract class that is used to handle messages that are received from over the
 * network. Implements runnable to be used as its own thread
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public abstract class MessageHandler implements Runnable {
    protected Socket currentConnection;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    private boolean running;

    /**
     * Constructor to make a new message handler. Takes the socket that the messages
     * will be passed to and received from. Throw IO exception when connection is broken
     * @param currentConnection The current socket that is being used in the message handler
     * @throws IOException When Connection is broken in some way
     */
    public MessageHandler(Socket currentConnection) throws IOException {
        this.currentConnection = currentConnection;
        out = new ObjectOutputStream(currentConnection.getOutputStream());
        in = new ObjectInputStream(currentConnection.getInputStream());
        running = true;
    }

    /**
     * Run method of the thread that will read in from the input object stream
     * and handle the message and send the response message in the object output stream
     * as the response if it is not of type waiting. It will do this until the
     * message handler is told to stop
     */
    public void run() {
        Message<?> outputLine;
        do {
            try {
                outputLine = handleMessage((Message<?>) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                //I am guessing that we will just ignore the java.io.EOFException
                //That this prints because it does not mean anything
                // e.printStackTrace();
                break;

            }
            try {
                if (outputLine.getMessageType() != MessageType.WAITING) {
                    out.writeObject(outputLine);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (outputLine == null) {
                break;
            }

        } while (running);
    }

    /**
     * A method to handle a message and produce a response based on the message
     * that was provided and the person handling the message
     *
     * @param m The message that is input
     * @return the response to the message
     */
    protected abstract Message<?> handleMessage(Message<?> m);

    /**
     * A method to stop the run method of the thread
     */
    public void stop() {
        running = false;
    }

    /**
     * Sends a message over the network
     *
     * @param m the message that will be sent
     */
    public void sendMessage(Message<?> m) {
        try {
            out.writeObject(m);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
