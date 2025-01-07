package auctionHouse;

import agent.AgentInfo;
import messages.Message;

import java.util.HashMap;

/**
 * Represents the clients(agents) connected to the Auction House
 * via their AuctionHouseMessageHandler
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk:
 * Project 5, Group 5, CS351 Fall24
 * @version 0.15 11/30/24
 */
public class Clients {
    // the Collection that holds agents AHMessHandler
    HashMap<AgentInfo, AuctionHouseMessageHandler> clients;

    /**
     * Default constructor for empty agent list
     */
    public Clients() {
        clients = new HashMap<>();
    }

    /**
     * Register (add to Clients) an Agents AHMessHandler
     * @param handler The message handler that will be saved
     */
    public void register(AgentInfo info, AuctionHouseMessageHandler handler) {
        clients.put(info, handler);
    }

    /**
     * Remove agent from the Client list by using the handler to identify it
     * @param handler   the MessageHandler for the agent to remove from clients
     */
    public void remove(AuctionHouseMessageHandler handler) {
        for(AgentInfo key:clients.keySet()) {
            if (clients.get(key) == handler ) {
                clients.remove(key);
                return;
            }
        }
    }


    /**
     * Send a message to all Clients in this
     * @param message The message that will be sent
     */
    public void notify(Message<?> message) {
        for(AgentInfo key:clients.keySet()) {
            clients.get(key).sendMessage(message);
        }
    }

    /**
     * Get client handler using Agent account
     */
    public AuctionHouseMessageHandler getHandler(int account) {
        for(AgentInfo key:clients.keySet()) {
            if (key.accountNumber() == account) {
                return  clients.get(key);
            }
        }
        return null;
    }

    /**
     * Get the agentInfo of associated with the account
     * @param account   associated with the agentInfo
     * @return  agentInfo or null if not found;
     */
    public AgentInfo findAgentInfo(int account) {
        for(AgentInfo key:clients.keySet()) {
            if (key.accountNumber() == account) {
                return  key;
            }
        }
        return null;
    }

    /**
     * Generates a string representation of Clients
     * @return string representing the Clients
     */
    public String toString() {
        String s = "";
        for(AgentInfo key:clients.keySet()) {
            if(key!=null && clients.get(key)!=null) {
                s += key + "," + clients.get(key).toString();
            }
        }
        return s;
    }

}
