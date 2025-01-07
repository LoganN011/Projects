package auctionHouse;

import messages.Message;
import messages.MessageType;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Represents a bidding timer which should run for an item that was bid on.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.15 12/1/24
 */
public class BidTimer {
    public static final int BID_INACTIVITY_PERIOD = 30000; //milliseconds
    // timer
    Timer timer;
    // ItemForSale
    ItemForSale itemForSale;
    // message handler
    AuctionHouseMessageHandler agentHandler;

    /**
     * Constructor for a bid timer
     * Create one when a bid is successful
     * Cancel it and create a new one if outbid
     * After BID_INACTIVITY_PERIOD it send a WINNER message to AH
     *
     * @param start       the start Date to reference to.
     * @param itemForSale   itemForSale item that the timer is tied to
     * @param agentHandler  message handler for the agent
     */
    public BidTimer(Date start, ItemForSale itemForSale,
                    AuctionHouseMessageHandler agentHandler) {
        this.agentHandler = agentHandler;
        this.itemForSale = itemForSale;

        TimerTask task = new myTimerTask();
        if(start==null) {
            start = new Date();
        }
        Date now = new Date();
        long delay = BID_INACTIVITY_PERIOD + (now.getTime() - start.getTime());
        timer = new Timer();
        timer.schedule(task, delay);
    }


    /**
     * Get the handler from the BidTimer
     * @return  handler for the agent who made the bid
     */
    public AuctionHouseMessageHandler getAgentHandler() {
        return agentHandler;
    }

    /**
     * Get the item from the BidTimer
     * @return itemForSale that was bid on
     */
    public ItemForSale getItem() {
        return itemForSale;
    }

    /**
     * Get the item Number from the BidTimer
     * @return item number for the item that was bid on
     */
    public int getItemNumber() {
        return itemForSale.getNumber();
    }

    /**
     * The method that executes after the delay
     */
    public class myTimerTask extends TimerTask {
        @Override
        public void run() {
            // after delay then announce winner by sending a message to the AH
            Message<ItemForSale> winner = new Message<>(MessageType.WINNER,
                    itemForSale);
            agentHandler.sendMessage(winner);
            timer.cancel();
        }
    }

    /**
     * Cancel timer
     * Cancels the thread for this timer.
     */
    public void cancel(){
        this.timer.cancel();
    }

}
