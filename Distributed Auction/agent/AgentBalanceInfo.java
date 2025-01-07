package agent;

import java.io.Serializable;

/**
 * A record Holding the name, account number, and current balance of an agent.
 * Used to register with Bank
 * @param name the name of the current agent
 * @param accountNumber the account number of the agent or -1 when registering
 * @param balance the balance of the agent
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public record AgentBalanceInfo(String name, int accountNumber,
                               double balance) implements Serializable {
    /**
     * A string representation of the Agent balance info
     *
     * @return a string version of the object
     */
    public String toString() {
        return "name=" + name + ", accountNumber=" + accountNumber + ", balance=" + balance;
    }
}
