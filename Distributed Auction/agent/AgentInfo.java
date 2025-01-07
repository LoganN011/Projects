package agent;

import java.io.Serializable;

/**
 * A record to hold the info about an agent
 * @param name The name of the agent
 * @param accountNumber the account number of the agent
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public record AgentInfo(String name,
                        int accountNumber) implements Serializable {
    @Override
    public String toString() {
        return "Name: " + name + ", " +
                "AccountNumber: " + accountNumber;
    }

}
