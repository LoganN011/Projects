package Bank;

import agent.AgentBid;
import agent.AgentInfo;
import auctionHouse.AHInfo;
import messages.Message;
import messages.MessageType;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The Bank class represents a banking system where accounts can be managed, agents can be connected,
 * and Auction Houses can be registered.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class Bank {
    private final List<Accounts> agentAccounts = new ArrayList<>();
    private final List<Accounts> auctionHouseAccounts = new ArrayList<>();
    private final List<AHInfo> activeAuctionHouses = new ArrayList<>();
    private final List<AgentInfo> activeAgent = new ArrayList<>();
    private int nextAccountNumber = 1;
    private List<Accounts> accounts = new ArrayList<>();
    private BankMessageHandler handler;
    private List<BankMessageHandler> agentConnection = new ArrayList<>();
    private Map<Integer, BankMessageHandler> auctionHouseHandlers = new HashMap<>();
    private final ConcurrentHashMap<Integer, ReentrantLock> accountLocks = new ConcurrentHashMap<>();

    /**
     * Constructor for the Bank class. Initializes the server to listen for connections on the specified port.
     * Handles incoming connections from clients, creating a new thread for each connection.
     *
     * @param portNumber the port number the server will listen on
     */
    public Bank(int portNumber) {
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println(clientSocket);
                    handler = new BankMessageHandler(clientSocket, this);
                    Thread thread = new Thread(handler);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Adds a new Auction House to the active list.
     * Synchronized to ensure thread-safe modification of the list.
     *
     * @param ahInfo the Auction House information to add
     */
    public synchronized void addAuctionHouse(AHInfo ahInfo) {
        activeAuctionHouses.add(ahInfo);
        System.out.printf("Auction House added: %s:%d%n", ahInfo.ipHost(), ahInfo.locPort());
    }

    /**
     * Adds a new agent's message handler to the list of active connections.
     * Synchronized to ensure thread-safe modification of the list.
     *
     * @param handler the BankMessageHandler representing the agent connection
     */
    public synchronized void addAgentSocket(BankMessageHandler handler) {
        agentConnection.add(handler);
    }
    /**
     * Sends the list of active Auction Houses to all connected agents.
     * This method runs in a separate thread to avoid blocking the main thread.
     */
    public synchronized void sendAllAgentsAhInfo() {
        Thread test = new Thread(() -> {
            try {
                Thread.sleep(100);
                for (BankMessageHandler a : agentConnection) {
                    a.sendMessage(new Message<>(MessageType.LIST_AUCTION_HOUSES,
                            getActiveAuctionHouses()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        test.start();
    }
    /**
     * Removes an agent from the list of active agents by their name.
     * Synchronized to ensure thread-safe modification of the list.
     *
     * @param name the name of the agent to remove
     */
    public synchronized void removeAgent(String name) {
        // Assuming you have a list of agents stored as AgentInfo objects
        activeAgent.removeIf(agent -> agent.name().equals(name));
        System.out.printf("Agent removed: %s%n", name);
    }

    /**
     * Removes an Auction House from the active list based on its IP host and port.
     * Synchronized to ensure thread-safe modification of the active auction houses list.
     *
     * @param ipHost the IP address of the Auction House
     * @param port the port number of the Auction House
     */
    public synchronized void removeAuctionHouse(String ipHost, int port) {
        activeAuctionHouses.removeIf(ah -> ah.ipHost().equals(ipHost) && ah.locPort() == port);
        System.out.printf("Auction House removed: %s:%d%n", ipHost, port);
    }
    /**
     * Retrieves a list of all currently active Auction Houses.
     * Synchronized to ensure thread-safe access to the active auction houses list.
     *
     * @return a list of active Auction Houses
     */
    public synchronized List<AHInfo> getActiveAuctionHouses() {
        return new ArrayList<>(activeAuctionHouses);
    }
    /**
     * Creates a new account for an agent or Auction House.
     * If an account already exists for the given account holder, returns the existing account.
     * Synchronized to ensure thread-safe access and modification of account lists.
     *
     * @param accountHolder the name of the account holder
     * @param initialBalance the initial balance for the account
     * @return the created or existing account
     */
    public synchronized Accounts createAccount(String accountHolder,
                                               double initialBalance) {
        List<Accounts> accountList = accountHolder.contains("AuctionHouse")
                ? auctionHouseAccounts : agentAccounts;
        for (Accounts account : accountList) {
            if (account.getAccountHolder().equals(accountHolder)) {
                System.out.printf("Account for %s already exists (Account #%d).%n",
                        accountHolder, account.getAccountNumber());
                return account;
            }
        }

        Accounts account = new Accounts(accountHolder, nextAccountNumber++, initialBalance);
        accountList.add(account);

        System.out.printf("Created new account for %s (Account #%d) with balance $%.2f.%n",
                accountHolder, account.getAccountNumber(), initialBalance);

        addAccount(account);
        return account;
    }
    /**
     * Attempts to lock an account for exclusive access.
     * Creates a new lock for the account if one does not already exist.
     *
     * @param accountNumber the account number to lock
     * @return true if the lock was successfully acquired, false otherwise
     */
    public boolean lockAccount(int accountNumber) {
        // Get or create a lock for the account
        ReentrantLock lock = accountLocks.computeIfAbsent(
                accountNumber, k -> new ReentrantLock());
        lock.lock();
        return true;
    }
    /**
     * Unlocks an account that was previously locked.
     * Ensures that only the thread holding the lock can unlock it.
     *
     * @param accountNumber the account number to unlock
     */
    public void unlockAccount(int accountNumber) {
        ReentrantLock lock = accountLocks.get(accountNumber);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * Updates the name of the account holder for a specific auction house account.
     * Synchronized to ensure thread-safe modification of account information.
     *
     * @param name the new name to set
     * @param account the account to update
     */

    public synchronized void setName(String name, Accounts account) {
        for (Accounts a : auctionHouseAccounts) {
            if (account.getAccountHolder().equals(a.getAccountHolder())) {
                a.setAccountHolder(name);
            }
        }
        System.out.println();
    }
    /**
     * Retrieves an account based on the account holder's name.
     * Synchronized to ensure thread-safe access to the accounts list.
     *
     * @param accountHolder the name of the account holder
     * @return the matching account, or null if not found
     */
    public synchronized Accounts getAccount(String accountHolder) {
        for (Accounts account : accounts) {
            if (account.getAccountHolder().equals(accountHolder)) {
                return account;
            }
        }
        return null;
    }
    /**
     * Retrieves an account based on its account number.
     * Synchronized to ensure thread-safe access to the accounts list.
     *
     * @param accountNumber the account number to search for
     * @return the matching account, or null if not found
     */
    public synchronized Accounts getAccountByNumber(int accountNumber) {
        for (Accounts account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }

    /**
     * Blocks a specific amount of funds in a given account in a thread-safe manner.
     *
     * @param accountNumber the account number where funds need to be blocked.
     * @param amount        the amount to be blocked.
     * @return true if funds are successfully blocked; false if the account doesn't
     *         exist or has insufficient funds.
     *
     * Note: The method iterates through a collection of accounts to find the
     * matching account number. If found, it attempts to block the requested amount
     * using the `blockFunds` method of the account and prints appropriate messages.
     */
    public synchronized boolean blockFunds(int accountNumber, double amount) {
        for (Accounts account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.blockFunds(amount)) {
                    System.out.printf("Blocked $%.2f from Account #%d.%n",
                            amount, accountNumber);
                    return true;
                } else {
                    System.out.printf("Failed to block funds from Account" +
                            " #%d (insufficient funds).%n", accountNumber);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Unblocks a specified amount of funds from an agent's account.
     *
     * @param accountNumber the account number of the agent
     * @param amount the amount of funds to unblock
     * @return true if the funds were successfully unblocked, false otherwise
     */
    public synchronized boolean unblockFunds(int accountNumber, double amount) {
        for (Accounts account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.unblockFunds(amount)) {
                    System.out.printf("Unblocked $%.2f from " +
                            "Account #%d.%n", amount, accountNumber);
                    return true;
                } else {
                    System.out.printf("Failed to unblock funds from " +
                            "Account #%d. Insufficient blocked funds.%n", accountNumber);
                    return false;
                }
            }
        }
        System.out.printf("Account #%d not found. Cannot unblock funds.%n", accountNumber);
        return false;
    }

    /**
     * Adds a new account to the bank's account list.
     * Synchronized to ensure thread-safe access to the accounts list.
     *
     * @param account the account to add
     */
    private synchronized void addAccount(Accounts account) {
        accounts.add(account);
    }

    /**
     * Processes a hold funds request for a specific bid.
     * Checks if the funds can be held in the specified account and logs the result.
     * Synchronized to ensure thread-safe processing.
     *
     * @param bid the bid containing account details and amount to hold
     * @return true if the funds were successfully held, false otherwise
     */
    public synchronized boolean processHoldFundsRequest(AgentBid bid) {
        Accounts account = getAccountByNumber(bid.agentAccount());
        boolean fundsHeld = blockFunds(bid.agentAccount(), bid.amount());

        // Log and return result
        System.out.printf("Processing bid for Agent #%d: %s. Funds Held: %b%n",
                bid.agentAccount(), bid, fundsHeld);
        return fundsHeld;
    }
    /**
     * Handles an incoming request to hold funds for a bid.
     * Calls the processing logic and sends an appropriate response back to the sender.
     *
     * @param handler the handler used to communicate with the message sender
     * @param bid the bid containing account and amount details
     */
    public void handleHoldFundsRequest(BankMessageHandler handler, AgentBid bid) {
        boolean fundsHeld = processHoldFundsRequest(bid);

        // Create response message with bid and status
        Message<AgentBid> response = new Message<>(
                fundsHeld ? MessageType.FUNDS_HELD : MessageType.FUNDS_NOT_HELD,
                bid
        );

        // Send response back to the Auction House
        handler.sendMessage(response);
    }
    // New method to save Auction House handler
    public void saveAuctionHouseHandler(int accountNumber, BankMessageHandler handler) {
        // You can use a map or list to keep track of handlers for Auction Houses by their account number
        auctionHouseHandlers.put(accountNumber, handler);
    }


    /**
     * Entry point of the Bank application.
     * Initializes the Bank object with the provided port or default port (1234).
     *
     * @param args command-line arguments, expects the port number as the first argument
     */

    public static void main(String[] args) {
        if (args.length == 1) {
            Bank bank = new Bank(Integer.parseInt(args[0]));
        } else {
            Bank bank = new Bank(1234);
        }
    }
    /**
     * Sends a message to the Auction House identified by its account number.
     *
     * @param auctionHouseAccountNumber the account number of the Auction House
     * @param message the message to send to the Auction House
     * @return true if the message was successfully sent, false otherwise
     */

    public boolean sendMessageToAuctionHouse(int auctionHouseAccountNumber, Message<?> message) {
        // Search for the Auction House by account number
        AHInfo auctionHouseInfo = activeAuctionHouses.stream()
                .filter(ah -> ah.acct() == auctionHouseAccountNumber)
                .findFirst()
                .orElse(null);

        // If Auction House is found
        if (auctionHouseInfo != null) {
            // Get the Auction House handler from saved handlers
            BankMessageHandler handler = auctionHouseHandlers.get(auctionHouseAccountNumber);
            if (handler != null) {
                // Send the message to Auction House
                handler.sendMessage(message);
                System.out.printf("Message sent to Auction House #%d: %s%n", auctionHouseAccountNumber, message);
                return true;
            } else {
                System.err.printf("Auction House with account #%d has no valid message handler.%n", auctionHouseAccountNumber);
            }
        } else {
            System.err.printf("Auction House with account #%d not found.%n", auctionHouseAccountNumber);
        }

        return false;
    }


}