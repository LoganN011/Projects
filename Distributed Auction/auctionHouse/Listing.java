package auctionHouse;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Represents a listing of items for sale at the auction house
 * Gets distributed to the Agents.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.18 12/1/24
 */
public class Listing implements Serializable {

    private ArrayList<ItemForSale> listing;
    private int highestEntry;

    /**
     * default constructor
     */
    public Listing() {
        this.listing = new ArrayList<>();
    }

    /**
     * Copy constructor - to create a Listing of items from another Listing
     * @param orig   the original Listing from which to make a copy
     */
    public Listing(Listing orig) {
        this();
        this.highestEntry = orig.highestEntry;
        for (ItemForSale ifs: orig.listing) {
            ItemForSale tmpIFS = new ItemForSale(ifs);
            this.listing.add(tmpIFS);
        }
    }

    /**
     * Gets the size of the Listing
     * @return the size
     */
    public int getSize() {
        return listing.size();
    }

    /**
     * Get the listing of ItemsForSale from this Listing
     */
    public ArrayList<ItemForSale> getListing() {
        return listing;
    }

    /**
     * Read the list of items from resource Items
     * @return ArrayList of Strings
     */
    private ArrayList<String> readNouns() {
        String itemsFile = "resources/Items";
        ArrayList<String> out = new ArrayList<>();
        try(Scanner inp = new Scanner(Paths.get(itemsFile))) {
            while(inp.hasNextLine()) {
                out.add(inp.nextLine());
            }
        }
        catch (IOException trouble) {
            System.out.println("Error reading item listing");
        }
        return out;
    }

    /**
     * Read the list of adjectives from resource adjectives
     * @return ArrayList of Strings
     */
    private ArrayList<String> readAdj() {
        String itemsFile = "resources/adjectives";
        ArrayList<String> out = new ArrayList<>();
        try(Scanner inp = new Scanner(Paths.get(itemsFile))) {
            while(inp.hasNextLine()) {
                out.add(inp.nextLine());
            }
        }
        catch (IOException trouble) {
            System.out.println("Error reading adjectives listing");
        }
        return out;
    }

    /**
     * Creates a listing from a file of nouns and a file of adjectives
     * to create the description, then a random min_bid
     * E.g., "description of item" 2.35
     * The id number is assigned as the items are placed in listing
     */
    public void createFromNounsAdj(int maxNum) {
        final double MIN_STARTING_BID = 1.00;
        final double MAX_STARTING_BID = 100.00;
        ArrayList<String> nouns = readNouns();
        ArrayList<String> adj = readAdj();
        Random rand = new Random();
        Random ranNum = new Random();
        // create num ItemForSale by randomly picking adj &  noun
        // then min bid and write to listing
        for(int cntr=0; cntr<=maxNum; cntr++) {
            // get the description string
            String s = adj.get(rand.nextInt(adj.size())) + " " +
                    nouns.get(rand.nextInt(nouns.size()));
            // get a min bid randomly chosen between min and max starting bid
            double minBid = Math.round(ranNum.nextDouble(MIN_STARTING_BID,
                    MAX_STARTING_BID));
            ItemForSale item = new ItemForSale(cntr, s, minBid);
            listing.add(item);
            highestEntry = cntr;
        }
    }

    /**
     * Transfer at most maxNum items from Listing this to Listing other
     * @param maxNum    the max number to move, if available
     */
    public void transferTo(int maxNum, Listing other) {
        int most = Math.min(maxNum, this.getSize());
        for (int i = 0; i < most; i++) {
            other.listing.add(this.listing.removeFirst());
        }
    }


    /**
     * Add the ItemForSale associated with the number n
     * @param item to be add to the list
     */
    public void addItem(ItemForSale item) {
        listing.add(item);
    }

    /**
     * Gets the ItemForSale associated with the number n
     * @param ahItemNumber the unique AH item number
     * @return ItemForSale or null in n is outside range of listing
     */
    public ItemForSale getItem(int ahItemNumber) {
        for(ItemForSale ifs:listing) {
            if( ifs.getNumber() == ahItemNumber) {
                return ifs;
            }
        }
        return null;
    }

    /**
     * Replace an existing item for sale of int this with another
     * @param ahItemNumber  the AH number of the original item for sale
     * @param itemForSale   the new ItemForSale to put in its place
     */
    public void replace(int ahItemNumber, ItemForSale itemForSale) {
        int size = listing.size();
        for(int i = 0; i<size; i++) {
            ItemForSale item = listing.get(i);
            if(item.getNumber() == ahItemNumber) {
                // replace this item
                listing.remove(i);
                listing.add(i,itemForSale);
                return;
            }
        }
    }

    /**
     * Add the AH Bank account number to all items in a listing
     * @param acct  AH account number
     */
    public void addAHAcct(int acct) {
        for(ItemForSale i:listing) {
            i.setAHAcct(acct);
        }
    }

    /**
     * Check if any bids are active
     * @return true if one or more active else false
     */
    public boolean bidsActive() {
        // if Date is null then not active
        for(ItemForSale i:this.listing) {
            if(i.getBidTime()!=null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return a listing that consists of the active items only
     * @param orig the listing to find the active items in
     * @return a new listing containing only the active items
     */
    public Listing getActiveBids(Listing orig) {
        Listing newListing = new Listing();
        for (ItemForSale ifs:orig.listing) {
            if(ifs.activeBid()) {
                newListing.addItem(new ItemForSale(ifs));
            }
        }
        return newListing;
    }

    /**
     * Create string representation of a Listing
     * @return The String version of the listing
     */
    public String toString() {
        String s ="";
        if(this.getSize()>0) {
            for (int i = 0; i < this.getSize(); i++) {
                s += this.getListing().get(i).toString() + "\n";
            }
        }
        return s;
    }

}