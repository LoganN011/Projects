package agent;

import auctionHouse.AHInfo;
import auctionHouse.ItemForSale;
import auctionHouse.Listing;
import javafx.util.Pair;
import messages.Message;
import messages.MessageHandler;
import messages.MessageType;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An agent is a user of the Auction house. It will bid on
 * items and also has an account at the bank
 *
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class Agent {
    private String name;
    private double currentBalance;
    private int accountNumber;
    private AgentMessageHandler bankHandler;
    private ConcurrentHashMap<Pair<String, Integer>, AgentMessageHandler>
            auctionHouseMessageHandlers;
    private ConcurrentHashMap<AHInfo, Pair<String, Integer>> auctionHouseInfo;
    private ConcurrentHashMap<Pair<String, Integer>, Listing> auctionHouseListings;
    private ArrayList<ItemForSale> inventory = new ArrayList<>();
    private boolean isAutoBidder;

    /**
     * Constructor for the Agent
     *
     * @param startingBalance starting balance of the agent
     * @param name            the name of the agent
     * @param host            String of the bank Ip host name
     * @param port            port that the bank is connected to
     * @param isAutoBidder    if the agent is an auto bidder
     */
    public Agent(double startingBalance, String name, String host, int port,
                 boolean isAutoBidder) {
        this.currentBalance = startingBalance;
        this.name = name;
        try {
            this.isAutoBidder = isAutoBidder;
            Socket socket = new Socket(host, port);
            Message<AgentBalanceInfo> m = new Message<>(MessageType.NEW_ACCOUNT,
                    new AgentBalanceInfo(name, -1, startingBalance));
            auctionHouseMessageHandlers = new ConcurrentHashMap<>();
            auctionHouseListings = new ConcurrentHashMap<>();
            auctionHouseInfo = new ConcurrentHashMap<>();

            AgentMessageHandler handler = new AgentMessageHandler(socket, this);
            Thread thread = new Thread(handler);
            thread.start();
            bankHandler = handler;

            handler.sendMessage(m);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the account number
     *
     * @return the account number
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Gets the current balance that the agent knows about from the bank
     *
     * @return the balance as a double
     */
    public double getBalance() {
        return currentBalance;
    }

    /**
     * Updates the current balance
     *
     * @param newBalance tne new balance as a double
     */
    public void setCurrentBalance(double newBalance) {
        currentBalance = newBalance;
    }

    /**
     * Sets the account number of the agent
     *
     * @param accountNumber the new account number
     */
    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Give an AH id String represent the computer address and integer
     * representing the port of the AH. Save the new listing for that AH
     *
     * @param auctionHouseId A pair of hold the computer address and port number of the AH
     * @param listing        The new listing for the AH
     */
    public void saveListing(Pair<String, Integer> auctionHouseId, Listing listing) {
        auctionHouseListings.remove(auctionHouseId);
        auctionHouseListings.put(auctionHouseId, listing);
    }

    /**
     * Give the AH ID return the listing and pair
     *
     * @param auctionHouseId A pair of hold the computer address and port number of the AH
     * @return An entry in the Map holding the listings
     */
    public ConcurrentHashMap.Entry<Pair<String, Integer>, Listing>
    getEntry(Pair<String, Integer> auctionHouseId) {
        for (ConcurrentHashMap.Entry<Pair<String, Integer>, Listing> entry :
                auctionHouseListings.entrySet()) {
            if (entry.getKey().getKey().equals(auctionHouseId.getKey())) {
                return entry;
            }
        }
        return null;
    }


    /**
     * Gets all listing saved in a map
     *
     * @return a map holding the AH id info and the listings
     */
    public ConcurrentHashMap<Pair<String, Integer>, Listing> getAllListings() {
        return auctionHouseListings;
    }

    /**
     * removes the unused AH bookkeeping
     *
     * @param availableAuctionHouses The list of Active AHs  gotten from the bank
     */
    public void removeUnusedSocketConnections(List<AHInfo> availableAuctionHouses) {
        for (Pair<String, Integer> pair : auctionHouseMessageHandlers.keySet()) {
            boolean found = false;
            for (AHInfo ahInfo : availableAuctionHouses) {
                Pair<String, Integer> connectionInfo = new Pair<>(ahInfo.ipHost(),
                        ahInfo.locPort());
                if (pair.equals(connectionInfo)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                auctionHouseMessageHandlers.get(pair).stop();
                auctionHouseMessageHandlers.remove(pair);
            }
        }
    }

    /**
     * Adds a new message handler for an AH
     *
     * @param socket socket that the messages will be sent and received from the AH
     */
    public void addNewAHMessageHandler(Socket socket) {
        try {
            AgentMessageHandler ahHandler = new AgentMessageHandler(socket, this);
            Thread thread = new Thread(ahHandler);
            thread.start();
            Pair<String, Integer> pair =
                    new Pair<>(socket.getInetAddress().getHostName(), socket.getPort());
            auctionHouseMessageHandlers.put(pair, ahHandler);
            ahHandler.sendMessage(new Message<>(MessageType.REGISTER,
                    new AgentInfo(name, accountNumber)));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A method to receive the AH info from the bank. Saves all info and removes all
     * unused info based on the list of AH
     *
     * @param ahInfos List of active AHs
     */
    public void receiveAhInfo(List<AHInfo> ahInfos) {
        for (AHInfo ahInfo : ahInfos) {
            Pair<String, Integer> pair = new Pair<>(ahInfo.ipHost(), ahInfo.locPort());
            if (!auctionHouseMessageHandlers.containsKey(pair)) {
                try {
                    auctionHouseInfo.put(ahInfo, pair);
                    Socket socket = new Socket(ahInfo.ipHost(), ahInfo.locPort());
                    addNewAHMessageHandler(socket);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        removeUnusedSocketConnections(ahInfos);
    }

    /**
     * A method to bid on an item and send that bid to the required AH
     *
     * @param auctionHouseId A pair of hold the computer address and port number of the AH
     * @param itemNumber     The item number that is being bid on
     * @param bidAmount      the amount of the bid
     */
    public void bidOnItem(Pair<String, Integer> auctionHouseId, int itemNumber, double bidAmount) {
        Listing listing = auctionHouseListings.get(auctionHouseId);
        if (listing != null && bidAmount <= currentBalance) {
            AgentMessageHandler handler = auctionHouseMessageHandlers.get(auctionHouseId);
            if (handler != null) {
                handler.sendMessage(new Message<>(MessageType.BID, new AgentBid(accountNumber, itemNumber, bidAmount, "")));

                AgentScreenMain.updateAllScreen();
            }
        }
    }

    /**
     * A method to disconnect the agent if available. Can disconnect if agent has
     * no active bids. Messages all AH of disconnect and Bank
     *
     * @return return if agent can disconnect
     */
    public boolean tryDisconnect() {
        for (Listing listing : auctionHouseListings.values()) {
            boolean flag = false;
            for (ItemForSale item : listing.getListing()) {
                if (item.getAgent() != null && item.getAgent().equals(name)) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                for (Pair<String, Integer> pair : auctionHouseMessageHandlers.keySet()) {
                    AgentMessageHandler handler = auctionHouseMessageHandlers.get(pair);
                    if (handler != null) {
                        handler.sendMessage(new Message<>(MessageType.DISCONNECT,
                                new AgentInfo(name, accountNumber)));
                        handler.stop();
                    }
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (auctionHouseListings.keySet().isEmpty()) {
            sendMessageToBank(new Message<>(MessageType.DISCONNECT,
                    new AgentInfo(name, accountNumber)));
            return true;
        }
        return false;

    }

    /**
     * Removes the bookkeeping of a provided AH
     *
     * @param socket the socket of the AH being disconnected
     * @param info   the information about The AH
     */
    public void removeBookKeeping(Socket socket, AHInfo info) {
        for (Pair<String, Integer> ipInfo : auctionHouseListings.keySet()) {
            if (ipInfo.getKey().equals(socket.getInetAddress().getHostName()) &&
                    socket.getPort() == ipInfo.getValue()) {
                auctionHouseListings.remove(ipInfo);
                auctionHouseMessageHandlers.get(ipInfo).stop();
                auctionHouseMessageHandlers.remove(ipInfo);
                auctionHouseInfo.remove(info);
            }
        }
    }

    /**
     * gets the name of the agent
     *
     * @return the name of the agent as a string
     */
    public String getName() {
        return name;
    }

    /**
     * Give a A pair of hold the computer address and port number of the AH. return the AH name
     *
     * @param auctionHouseId A pair of hold the computer address and port number of the AH
     * @return the name of the AH
     */
    public String getAHName(Pair<String, Integer> auctionHouseId) {
        for (AHInfo ahInfo : auctionHouseInfo.keySet()) {
            Pair<String, Integer> currentPair = new Pair<>(ahInfo.ipHost(), ahInfo.locPort());
            if (currentPair.equals(auctionHouseId)) {
                return ahInfo.getName();
            }
        }
        return null;
    }

    /**
     * Add the item won to the inventory
     *
     * @param item new item that was won
     */
    public synchronized void addToInventory(ItemForSale item) {
        inventory.add(item);
    }

    /**
     * Gets the array list holding all items won
     *
     * @return the list of items won by agent
     */
    public synchronized ArrayList<ItemForSale> getInventory() {
        return inventory;
    }

    /**
     * A method to send a message to the bank
     *
     * @param message the message being sent
     */
    public synchronized void sendMessageToBank(Message<?> message) {
        bankHandler.sendMessage(message);
    }

    /**
     * Method to request the balance from the bank
     */
    public void requestBalance() {
        bankHandler.sendMessage(new Message<>(MessageType.BALANCE, new AgentInfo(name, accountNumber)));
    }

    /**
     * Auto-bid on random items from and random AH if the bidder is able too
     * and the agent is not currently winning the same bid. Bids every 10 seconds
     */
    public void autoBid() {
        Thread thread = new Thread(() -> {
            final int NEXT_BID_DELAY = 10000;
            Random rand = new Random();
            while (true) {
                if (auctionHouseListings.keySet().toArray().length > 0) {
                    if (auctionHouseListings.keySet().toArray()
                            [rand.nextInt(auctionHouseListings.keySet().toArray().length)]
                            instanceof Pair<?, ?> pair) {

                        Listing listing = auctionHouseListings.get(pair);
                        if (!listing.getListing().isEmpty()) {
                            ItemForSale item = listing.getListing().get(rand.nextInt(listing.getSize()));
                            int itemNumber = item.getNumber();
                            requestBalance();
                            if (currentBalance >= item.getMinBid()
                                    && (item.getAgent() == null ||
                                    !item.getAgent().equals(this.name))) {
                                AgentBid bid = new AgentBid(accountNumber,
                                        itemNumber, item.getMinBid(), "");
                                Message<AgentBid> bidMsg = new Message<>(MessageType.BID, bid);
                                MessageHandler ahHandler = auctionHouseMessageHandlers.get(pair);
                                ahHandler.sendMessage(bidMsg);
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(NEXT_BID_DELAY);
                } catch (InterruptedException e) {
                    System.out.println("AUTO STOPPING");
                    break;

                }
            }
        });
        thread.start();
        //This stopping of Auto bidder semi works
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            thread.interrupt();
            while (true) {
                if (AgentCreation.agent.tryDisconnect()) {
                    System.exit(0);
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }));


    }

    /**
     * Method to check if an agent is an auto bidder
     *
     * @return if an agent is an auto bidder
     */
    public boolean isAutoBidder() {
        return isAutoBidder;
    }

}