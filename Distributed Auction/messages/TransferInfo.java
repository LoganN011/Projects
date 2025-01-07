package messages;

import auctionHouse.ItemForSale;

import java.io.Serializable;

/**
 * Record used to represent a request for a transfer between two accounts in the
 * bank for an item
 *
 * @param fromAccount account number that money will be transferred from
 * @param toAccount   account number that money will be transferred to
 * @param amount      amount of money that will be transferred as a double
 * @param item        The item that was being bought
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public record TransferInfo(int fromAccount, int toAccount, double amount,
                           ItemForSale item) implements Serializable {
    @Override
    public String toString() {
        return "From " + fromAccount + " to " + toAccount + " for " + amount + "\nItem: " + item.toString();
    }

}
