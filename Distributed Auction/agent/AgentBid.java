package agent;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Represents a bid made by an agent
 * @param agentAccount The agents account number
 * @param itemNumber The item number that is being bid on
 * @param amount the amount of the bid
 * @param bidStatus the current status of the bid
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public record AgentBid(int agentAccount, int itemNumber,
                       double amount, String bidStatus) implements Serializable {

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");
        return "Agent Acct: " + agentAccount + ", Item= " + itemNumber + ", " +
                "Agent bid= $" + df.format(amount) + ", " +
                "bid status= " + bidStatus;
    }

    /**
     * Copy constructor to enable keeping the agentBid, but changing the
     * status
     * @param agentBid  the bid to copy
     * @param status    can be used to supply the reason a bid was rejected.
     */
    public AgentBid(AgentBid agentBid, String status) {
        this(agentBid.agentAccount(), agentBid.itemNumber(),
                agentBid.amount(), status);
    }

    /**
     * Copy constructor to enable keeping the agentBid, but changing the
     * bid amount
     * @param agentBid  the bid to copy
     * @param amount    the new amount to use.
     */
    public AgentBid(AgentBid agentBid, double amount) {
        this(agentBid.agentAccount(), agentBid.itemNumber(),
                amount, agentBid.bidStatus);
    }
}
