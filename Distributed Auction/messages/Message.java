package messages;

import java.io.Serializable;

/**
 * A Message is an object that is passed over the network. Each message has a type
 * the type will be used to distinguish how to handle the message for each of the
 * components of the Distributed Auction. There is also a generic object that can be
 * used to pass more info around over the network.
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class Message<T> implements Serializable {
    private MessageType messageType;
    private T messageObject;

    /**
     * Constructor for a Message. Takes a message type and a message object of
     * type T (any type) object must be Serializable
     *
     * @param messageType   The type of the message
     * @param messageObject The object that is being passed along in the message
     *                      must be Serializable
     */
    public Message(MessageType messageType, T messageObject) {
        this.messageType = messageType;
        this.messageObject = messageObject;
    }

    /**
     * Gets the type of the message
     *
     * @return the type of the message
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Gets the message object
     *
     * @return the mssage object of type T
     */
    public T getMessageObject() {
        return messageObject;
    }

    /**
     * Returns a new message object that has the type of waiting and
     * null as the message object
     *
     * @return The waiting message object
     */
    public static Message<?> getWaitingMessage() {
        return new Message<>(MessageType.WAITING, null);
    }

    /**
     * The to string of the message
     *
     * @return A string representation of the message
     */
    public String toString() {
        return messageType + " " + messageObject.toString();
    }


}
