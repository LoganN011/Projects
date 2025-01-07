package auctionHouse;

import agent.AgentBid;
import agent.AgentInfo;
import messages.Message;
import messages.MessageHandler;
import messages.MessageType;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * Handles the communication received by the Auction House from
 * agents or the bank.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 * , Project 5, Group 5, CS351 Fall24
 * @version 0.25 12/04/24
 */
public class AuctionHouseMessageHandler extends MessageHandler {
    private AuctionHouse ah;

    /**
     * Constructor for the Auction House Message Handler.
     *
     * @param currentConnection the socket connection
     * @param ah                the Auction House instance
     * @throws IOException if there is an error with input/output streams
     */
    public AuctionHouseMessageHandler(Socket currentConnection, AuctionHouse ah)
            throws IOException {
        super(currentConnection);
        this.ah = ah;
    }

    /**
     * All incoming message are processed here.
     * @param m the message to be processed
     * @return  a new message
     */
    protected Message<?> handleMessage(Message<?> m) {
        switch (m.getMessageType()) {
            case NEW_AUCTION_HOUSE -> {
                // Registering a new Auction House
                if (m.getMessageObject() instanceof AHInfo ahInfo) {
                    ah.registerAuctionHouse(ahInfo); // Save the AH information
                    ah.setAHInfo(ahInfo);
                    ah.setName(ahInfo.getName());
                    // now update each ItemForSale with the AH account number
                    ah.getRemainingItems().addAHAcct(ahInfo.acct());
                    ah.getActiveItems().addAHAcct(ahInfo.acct());
                }
            }
            case DISCONNECT -> {
                // Handle Agent disconnection
                if (m.getMessageObject() instanceof AgentInfo info) {
                    ah.getRegClients().getHandler(info.accountNumber()).
                            sendMessage(new Message<>(MessageType.DISCONNECT,
                                    ah.getAHInfo()));
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ah.getRegClients().remove(this);
                }
            }
            case REGISTER -> {
                if (m.getMessageObject() instanceof AgentInfo agentInfo) {
                    ah.registerClient(agentInfo,this);
                }

                // Agent registers with the Auction House
                ah.registerAgent(this); // Save the agent connection

                // Send a list of active items to the agent
                sendMessage(new Message<>(MessageType.LISTING, ah.getActiveItems()));
            }
            case BID -> {
                // Agent places a bid
                if (m.getMessageObject() instanceof AgentBid bid) {
                    // if bid too small
                    if(bid.amount() < ah.getActiveItems().
                            getItem(bid.itemNumber()).getMinBid()) {
                        AgentBid updBid = new AgentBid(bid,
                                "Rejected: too low.");
                        Message<AgentBid> message = new Message<>(
                                MessageType.BID_REJECTED, updBid);
                        ah.getRegClients().getHandler(
                                bid.agentAccount()).sendMessage(message);
                    }
                    // bid high enough, now request hold with bank
                    else {
                        Message<AgentBid> holdFundsMessage = new Message<>(
                                MessageType.HOLD_FUNDS, bid);
                        ah.bank.sendMessage(holdFundsMessage);
                    }
                }
            }
            case FUNDS_HELD -> {
                // Bank sent FUNDS_HELD message to AH
                // So AH sends bid accepted to Agent
                if (m.getMessageObject() instanceof AgentBid bid) {
                    // update bid status to Accepted
                    int itemNum = bid.itemNumber();
                    AgentBid updBid = new AgentBid(bid, "Accepted");
                    Message<AgentBid> message = new Message<>(
                            MessageType.BID_ACCEPTED, updBid);
                    // send confirmation message back to agent
                    ah.getRegClients().getHandler(
                            bid.agentAccount()).sendMessage(message);
                    // now update the active listing with updBid info and date
                    ah.updateActiveItems(updBid);
                    Date start = ah.getActiveItems().
                            getItem(itemNum).getBidTime();
                    Listing updAI = new Listing(ah.getActiveItems());
                    // check if bid timer already exists
                    // if so send OUTBID message to previous lead bidder
                    // and then remove the bidTimer which cancels it
                    if(ah.getBidTimers().exists(itemNum)) {
                        // first get previous amount from ItemForSale
                        double prevAmt = ah.getBidTimers().get(itemNum).
                                getItem().getCurrBid();
                        // send message to agent that he/she is outbid
                        Message<AgentBid> outbid =
                                new Message<>(MessageType.OUTBID, updBid);
                        ah.getBidTimers().get(itemNum).getAgentHandler().
                                sendMessage(outbid);
                        // remove old timer
                        ah.getBidTimers().rm(itemNum);
                        // send message to bank to release funds
                        AgentBid prevBid = new AgentBid(updBid,prevAmt);
                        Message<AgentBid> oldBid = new Message<>
                                (MessageType.RELEASE_FUNDS, prevBid);
                        ah.bank.sendMessage(oldBid);
                    }
                    // add new bidTimer to collection
                    ah.getBidTimers().add(itemNum,
                            new BidTimer(start,
                                    ah.getActiveItems().getItem(itemNum),
                                    ah.getRegClients().getHandler(bid.agentAccount())));
                    // notify all registered clients
                    ah.getRegClients().notify(new Message<>(MessageType.LISTING,
                            updAI));
                    AuctionHouseMain.update();
                }
            }
            case FUNDS_NOT_HELD -> {
                // Bank sent unable to hold fund message
                // so AH sends BID_REJECTED to agent
                if (m.getMessageObject() instanceof AgentBid bid) {
                    // update bid status to Accepted
                    AgentBid updBid = new AgentBid(bid,
                            "Rejected for insufficient funds");
                    Message<AgentBid> message = new Message<>(
                            MessageType.BID_REJECTED, updBid);
                    // send message to agent
                    ah.getRegClients().getHandler(
                            bid.agentAccount()).sendMessage(message);
                }
            }
            case FUNDS_TRANSFERRED -> {
                // the bank has transferred funds to the AH account so remove
                // from active list. Update list and notify others.
                if (m.getMessageObject() instanceof AgentBid bid) {
                    // remove item from listing and add one in its place
                    ah.itemSold(bid.itemNumber());
                    // create new activeItems listing object to send
                    Listing updAI = new Listing(ah.getActiveItems());
                    // notify all clients
                    ah.getRegClients().notify(new Message<>(MessageType.LISTING,
                            updAI));
//                    AHInfo ahInfo = ah.getAHInfo();
                    ah.bank.sendMessage(new Message<>(
                            MessageType.BALANCE, ah.getName()));
                }
                AuctionHouseMain.update();
            }
            case FUNDS_NOT_TRANSFERRED -> {
                // the bank was unable to transfer funds to cover the 'sold' item
                // sold remove agent and Date() and current bid from ItemForSale
                // and notify clients
                if (m.getMessageObject() instanceof AgentBid bid) {
                    ah.itemNotSold(bid.itemNumber());
                }
                // create new activeItems listing object to send
                Listing updAI = new Listing(ah.getActiveItems());
                // notify all clients
                ah.getRegClients().notify(new Message<>(MessageType.LISTING,
                        updAI));
                AuctionHouseMain.update();
            }
            case ACCOUNT_BALANCE -> {
                if (m.getMessageObject() instanceof AHInfo auctionHouse) {
                    ah.setBalance(auctionHouse.balance());
                } else if (m.getMessageObject() instanceof Double balance) {
                    ah.setBalance(balance);
                }
                AuctionHouseMain.update();
            }
            case BALANCE -> {
                if (m.getMessageObject() instanceof Double balance) {
                    ah.setBalance(balance);
                }
                AuctionHouseMain.update();
            }
        }
        return Message.getWaitingMessage();
    }
}