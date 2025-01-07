package auctionHouse;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * Represents an item for sale by an auction house in the Distributed
 * Auction project.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.21 12/1/24
 */
public class ItemForSale implements Serializable {
    // Item number
    private int number;
    // Description of item
    private String description;
    // Minimum acceptable bid
    private double minBid;
    // Current bid (when active)
    private double currBid;
    // Name of agent who holds the current bid
    private String agent;
    // Start Date
    private Date bidTime;
    // AH account number
    private int ahAccount;

    /**
     * Constructor for item using item number, description, min bid.
     * The current bid, agent and time of bid submission are created in the
     * object once an agent bids on the item.
     * @param n item number
     * @param description   desciption of item
     * @param minBid    min bid in dollars
     */
    public ItemForSale(int n, String description, double minBid) {
        this.number = n;
        this.description = description;
        this.minBid = minBid;
        this.currBid = 0.0;
        this.agent = null;
        this.bidTime = null;
        this.ahAccount = -1;
    }

    /**
     * Copy constructor for ItemForSale
     * @param ifs The item that will be copied
     */
    public ItemForSale(ItemForSale ifs) {
        this(ifs.number, ifs.description, ifs.minBid);
        this.currBid = ifs.currBid;
        if(ifs.bidTime!=null) {
            this.bidTime = new Date(ifs.bidTime.getTime());
        }
        this.agent = ifs.agent;
        this.ahAccount = ifs.ahAccount;
    }

    /**
     * Get the item number
     * @return Item number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Get the start time of the current bid
     * @return a valid Date object or null if item has no bids
     */
    public Date getBidTime() {
        return bidTime;
    }

    /**
     * Get the item description
     * @return Item description
     */
    public String getDescription() { return description; };

    /**
     * Set the current bid and associated agent if greater than min bid
     * and then update min bid to reflect new min bid
     * @param bid   the bid from the agent
     * @param agent the agent making the bid
     */
    public synchronized void setCurrBid(double bid, String agent) {
        if(checkBid(bid)) {
            this.currBid = bid;
            this.agent = agent;
            this.bidTime = new Date();
            this.updateMinBid();
        }
    }

    /**
     * Get the current bid
     * @return Current bid amount
     */
    public double getCurrBid() {
        return currBid;
    }

    /**
     * Get the agents name holding the current bid
     * @return name of agent who holds the current bid
     */
    public String getAgent() {
        return agent;
    }


    /**
     * Check if the current bid is greater than or equal to the minimum bid
     * @param bid The bid to check
     * @return true if bid is valid, false otherwise
     */
    public boolean checkBid(double bid) {
        return bid >= minBid;
    }

    /**
     * Updates the min bid after a new bid has been placed.
     */
    private synchronized void updateMinBid() {
        if (currBid > 0) {
            final double INC_FACTOR = 1.05;
            this.minBid = Math.round(currBid * INC_FACTOR);
        }
    }

    /**
     * Get min bid
     */
    public double getMinBid() {
        return minBid;
    }

    /**
     * Set the AH account number in the item
     * @param acct The account that will be saved
     */
    public void setAHAcct(int acct) {
        this.ahAccount = acct;
    }

    /**
     * Get the AH account number for this item
     * @return the account number for the AH the item is listed in
     */
    public int getAHAcct() {
        return this.ahAccount;
    }

    public boolean activeBid() {
        return this.getBidTime() != null;
    }
    /**
     * Create a string representation of the ItemForSale
     * @return Formatted string of item details
     */
    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        String s = "Item#: " + number + ", ";
        s += "Description: " + description + ", ";
        s += "Min Bid: $" + df.format(minBid) + ", ";
        s += "Curr Bid: $" + df.format(currBid) + ", ";
        s += "Agent: " + (agent != null ? agent + ", " : "None, ");
        s += "Start Time: " + (bidTime != null ? bidTime.toString() : "None");
        return s;
    }

}
