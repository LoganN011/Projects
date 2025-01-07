package messages;

import java.io.Serializable;

/**
 * The type of the message that is being sent
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public enum MessageType implements Serializable {
    //ACCEPTANCE,
    //REJECTION,
    OUTBID,
    WINNER,
    DISCONNECT,
    BID,
    TRANSFER,
    NEW_ACCOUNT,
    HOLD_FUNDS, //AH sends this to bank
    RELEASE_FUNDS, // AH sends this to bank
    FUNDS_UNBLOCKED,
    FUNDS_NOT_UNBLOCKED ,
    BALANCE,
    WAITING,
    NEW_AUCTION_HOUSE, //AH sends this to bank
    ACCOUNT_BALANCE, //bank sends this to AH, Agent
    FUNDS_HELD, // Bank sends this confirmation to AH
    FUNDS_NOT_HELD, // Bank sends this negative confirmation to AH
    TRANSFER_FUNDS, // Agent sends this to BANK
    FUNDS_TRANSFERRED, // Bank Sends this to AH
    FUNDS_NOT_TRANSFERRED,
    BID_ACCEPTED, // AH sends this confirmation to Agent
    BID_REJECTED, // AH sends this confirmation to Agent
    REGISTER, // Agent sends this to register with AH
    LISTING, // AH sends this list of items for sale to Agent
    AUCTION_HOUSE_REMOVED,
    LIST_AUCTION_HOUSES,NEW_ACTIVE_ITEMS;
}
