package auctionHouse;

import agent.AgentBid;
import agent.AgentInfo;
import messages.Message;
import messages.MessageType;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Auction House portion of the distributed Auction simulation
 * Provides items for sale and handles bidding by agents
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 * Project 5, Group 5, CS351 Fall24
 * @version 0.25 12/04/24
 */
public class AuctionHouse {
    private String name;
    public final int NUM_OF_ACTIVE_ITEMS = 3; //max items to allow bidding on
    public final int TOT_NUM_ITEMS = 50; //max items to start with in AH

    public static AuctionHouse auctionHouse;
    public static ServerSocket serverSocketM;
    private int bankAccount;
    private Listing activeItems;
    private Listing remainingItems;
    private Clients regClients;
    private BidTimers bidTimers;
    private List<AuctionHouseMessageHandler> registeredAgents;
    public AuctionHouseMessageHandler bank;
    private String serverHost;
    private int serverPort;
    private Socket bankSocket;
    private List<AHInfo> activeAuctionHouses = new ArrayList<>();
    private AHInfo ahInfo;

    /**
     * Constructor for an auction house object
     */
    public AuctionHouse() {
        name = "";
        bankAccount = 0;
        activeItems = new Listing();
        remainingItems = new Listing();
        registeredAgents = new ArrayList<>();
        serverHost = "";
        serverPort = 0;
        bankSocket = null;
        bank = null;
        regClients = new Clients();
        bidTimers = new BidTimers();
        ahInfo = null;
    }

    /**
     * Set the AHInfo for the auction house
     * @param ahi   AHInfo to store in ahInfo
     */
    public void setAHInfo(AHInfo ahi) {
        ahInfo = ahi;
    }

    /**
     * Get the AHInfo for this auction house
     * @return the ahInfo
     */
    public AHInfo getAHInfo() {
        return ahInfo;
    }

    /**
     * Gets unique name of the auction house.
     * @return auction house name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the auction house.
     */
    public void setName(String aName) {
        this.name = aName;
    }

    /**
     * Gets the list of registered agents
     * @return all registered agents
     */
    public Clients getRegClients() {
        return regClients;
    }

    /**
     * Get the list of BidTimers
     * @return  bidTimers
     */
    public BidTimers getBidTimers() {
        return bidTimers;
    }

    /**
     * Populates remainingItems Listing with new ItemsForSale
     * remainingItems contains the initial list of items for sale
     * @param numItems  number of items to place into listing
     */
    public void initializeListing(int numItems) {
        remainingItems.createFromNounsAdj(numItems);
    }

    /**
     * Get the remainingItems Listing
     * @return  the Listing
     */
    public Listing getRemainingItems() {
        return remainingItems;
    }

    /**
     * Make items active by moving them to the activeItems List from the
     * remainingItems List
     * @param numToMove max number of items to move
     */
    public void makeItemsActive(int numToMove) {
        remainingItems.transferTo(numToMove, activeItems);
    }

    /**
     * Get the bank account of the AH
     * @return  account number
     */
    public int getAccount() {
        return bankAccount;
    }

    /**
     * Move the sold item out of the active list and move an item from
     * the remaining list into the active list
     * @param itemNumber    item that was sold
     */
    public void itemSold(int itemNumber) {
        for (int i = 0; i < activeItems.getSize(); i++) {
            if (activeItems.getListing().get(i).getNumber() == itemNumber) {
                activeItems.getListing().remove(i);  // Remove sold item from active items
                // transfer item from remaining to active
                if(remainingItems.getSize()>0) {
                    remainingItems.transferTo(1, activeItems);
                }
                break;
            }
        }
    }

    /**
     * Return the item to for sale status in active list
     * @param itemNumber    item that was NOT sold
     */
    public void itemNotSold(int itemNumber) {
        for (int i = 0; i < activeItems.getSize(); i++) {
            if (activeItems.getListing().get(i).getNumber() == itemNumber) {
                // this item should be restored, but using the curr bid for min
                ItemForSale ifs = activeItems.getItem(i);
                // create new item
                ItemForSale itemForSale = new ItemForSale(ifs.getNumber(),
                        ifs.getDescription(), ifs.getMinBid());
                // replace old IFS with new one
                activeItems.replace(itemNumber, itemForSale);
                break;
            }
        }
    }

    /**
     * Registers a client(observe) to receive messages
     * Adds an agent when bidding on items.
     *
     * @param info  the AgentInfo to register
     * @param handler the client to be registered via messHandler
     */
    public void registerClient(AgentInfo info, AuctionHouseMessageHandler handler) {
        regClients.register(info, handler);
    }


    /**
     * Notify all registered clients(observers) of update via message
     *
     * @param message the message to send to all the clients
     */
    public void notifyClients(Message<?> message) {
        regClients.notify(message);
    }

    /**
     * Sets the internal server socket info for the auctioneer
     * @param host  the host name for the AH server
     * @param port  the port number for the AH server
     */
    private void setServerSocket(String host, int port) {
        serverHost = host;
        serverPort = port;
    }

    /**
     * Get the Server Host name
     *
     * @return host as String
     */
    private String getServerHost() {
        return serverHost;
    }

    /**
     * Get the Server port number
     *
     * @return port number
     */
    private int getServerPort() {
        return serverPort;
    }
    public void registerAuctionHouse(AHInfo ahInfo) {
        activeAuctionHouses.add(ahInfo);
    }


    /**
     * Registers agent handler in AH Collection
     * @param handler the message handler that is handling AH and agent communication
     */
    public void registerAgent(AuctionHouseMessageHandler handler) {
        registeredAgents.add(handler);
    }

    /**
     * Get the activeItems Listing from the auction house
     * @return  the Listing
     */
    public Listing getActiveItems() {
        return activeItems;
    }

    /**
     * Update the active items listing with the latest bid information
     * @param bid the agent bid to update the listing with
     */
    public synchronized void updateActiveItems(AgentBid bid) {
        String name = regClients.findAgentInfo(bid.agentAccount()).name();
        // create a new IFS from the original
        ItemForSale updItem = new ItemForSale(activeItems.getItem(bid.itemNumber()));
        // set the bid amount and name
        updItem.setCurrBid(bid.amount(),name);
        // replace the item in the list
        activeItems.replace(bid.itemNumber(), updItem);
    }

    /**
     * Set the balance of the bankAccount
     * @param balance value to set the balance to
     */
    public void setBalance(Double balance) {
        this.bankAccount = balance.intValue();
    }

    /**
     * Attempts to disconnect the AH from users and the bank
     * @return true if all user and bank have been disconnected, else false
     */
    public void disconnectMe() {
        // empty the remaining items so you can't move any more
        // items to activeItems listing
        remainingItems = new Listing();
        Listing noItems = new Listing();
        // check if any active bids
        if(!activeItems.bidsActive()) {
            System.out.println("Auction House Shutdown in progress.");
            // first send empty listing
            notifyClients(new Message<>(MessageType.LISTING, noItems));
            // if no active bids then disconnect all agents
            notifyClients(new Message<>(MessageType.DISCONNECT, ahInfo));
            // disconnect bank
            bank.sendMessage(new Message<>(MessageType.DISCONNECT, ahInfo));

        }
        else {
            // some active clients so remove nonactive items from listing and
            // send this updated list to agents
            Listing newList = new Listing();
            newList.getActiveBids(activeItems);
            notifyClients(new Message<>(MessageType.LISTING, newList));

        }
    }

    /**
     * Empty the remainingItem Listing
     */
    public void emptyRemainingList() {
        remainingItems = new Listing();
    }


    /**
     * Get the AH account balance
     * @return The balance of the AH as a double
     */
    public int getBalance(){
        return auctionHouse.bankAccount;
    }


    /**
     * Start the Auction House
     * @param args  command-line arguments
     * @throws IOException Throws when connection fails
     */
    public static void start(String[] args) throws IOException{
        // get ip info
        String ahHostName = InetAddress.getLocalHost().getHostName();

        int ahPortNumber = Integer.parseInt(args[0]);
        String bankHostName = args[1];
        int bankPortNumber = Integer.parseInt(args[2]);
        // create new instance of AH
        auctionHouse = new AuctionHouse();
        serverSocketM = new ServerSocket(ahPortNumber);
        auctionHouse.setServerSocket(ahHostName, ahPortNumber);
        // initialize the Listing of items for sale
        auctionHouse.initializeListing(auctionHouse.TOT_NUM_ITEMS);  // Initialize all items
        auctionHouse.makeItemsActive(auctionHouse.NUM_OF_ACTIVE_ITEMS);  // Move items to active
        // define socket to communicate with the  Bank
        auctionHouse.bankSocket = new Socket(bankHostName, bankPortNumber);
        auctionHouse.bank = new AuctionHouseMessageHandler(auctionHouse.bankSocket, auctionHouse);
        // create thread for bank communications
        Thread bankThread = new Thread(auctionHouse.bank);
        bankThread.start();
        auctionHouse.bank.sendMessage(new Message<>(MessageType.NEW_AUCTION_HOUSE,
                new AHInfo(auctionHouse.getServerHost(),
                        auctionHouse.getServerPort(),-1, 0.0)));

        Thread readingConnection= new Thread(() -> {
            try {
                while (true) {
                    // create new client sockets as agents register with AH
                    Socket clientSocket = serverSocketM.accept();
                    AuctionHouseMessageHandler agentHandler =
                            new AuctionHouseMessageHandler(clientSocket, auctionHouse);
                    // create and start new client thread
                    Thread agentThread = new Thread(agentHandler);
                    agentThread.start();
                }
            } catch (IOException e){
                System.err.println("Error accepting client connection " + e.getMessage());
            }
        });
        readingConnection.start();
    }
}