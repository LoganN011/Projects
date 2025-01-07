package auctionHouse;

import java.io.Serializable;


/**
 * Represents the Auction  House IP host and port, name, acct number and bank balance
 *
 * @param ipHost Ip address of the AH
 * @param locPort local port number
 * @param acct account number of the AH
 * @param balance balance of the AH
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public record AHInfo(String ipHost, int locPort, int acct, double balance)
        implements Serializable {


    /**
     * Create a custom string representation of the AH info
     * @return A String representation of the AHInfo
     */
    @Override
    // user auction house iphost:port as name
    public String toString() {
        return "AuctionHouse= " + ipHost+ ":" + locPort + ", accountNumber= " +
            acct + ", balance= " + balance;
    }

    /**
     * Get the name of the auction house
     * @return name of Auction house
     */
    public String getName() {
        return "AuctionHouse-"+acct;
    }
}
