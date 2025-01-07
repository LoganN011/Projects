package auctionHouse;

import java.util.HashMap;

/**
 * Represents a collection of BidTimer(s) used in the auction
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.1 12/1/24
 */
public class BidTimers {
    // HashMap that holds the <item number and a timer
    HashMap<Integer, BidTimer> bidTimers;
    public BidTimers() {
        bidTimers = new HashMap<>();
    }


    /**
     * Adds a new timer to the collection
     */
    public void add(int itemNum, BidTimer bidTimer) {
        bidTimers.put(itemNum, bidTimer);
    }

    /**
     * Removes and cancels a bid timer in list
     * @param itemNum   the itemNum whose timer is cancelled and removed
     */
    public void rm(int itemNum) {
        BidTimer bt = bidTimers.remove(itemNum);
        bt.cancel();
    }

    /**
     * Check it timer already exists for this item
     * @param itemNum   the itemNum to check
     * @return true if exists, else false
     */
    public boolean exists(int itemNum) {
        return bidTimers.containsKey(itemNum);
    }

    /**
     * Get the timer for this item
     * @param itemNum   the itemNum to check
     * @return the bidTimer
     */
    public BidTimer get(int itemNum) {
        if(bidTimers.containsKey(itemNum)) {
            return bidTimers.get(itemNum);
        }
        return null;
    }

}
