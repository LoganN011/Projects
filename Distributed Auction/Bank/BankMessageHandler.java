package Bank;

import agent.AgentBalanceInfo;
import agent.AgentBid;
import agent.AgentInfo;
import auctionHouse.AHInfo;
import messages.Message;
import messages.MessageHandler;
import messages.MessageType;
import messages.TransferInfo;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
/**
 * This is responsible for handling messages between the Bank and its clients (agents and auction houses).
 * It extends the MessageHandler class and processes communication over a socket connection.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */

public class BankMessageHandler extends MessageHandler {
    private final Bank bank;

    /**
     * Constructor for BankMessageHandler. Initializes the handler with the given
     * socket connection and associated Bank instance.
     *
     * @param currentConnection the socket connection for this client (agent or auction house)
     * @param bank the Bank instance to interact with for account management
     * @throws IOException if an I/O error occurs when setting up the connection
     */
    public BankMessageHandler(Socket currentConnection, Bank bank) throws IOException {
        super(currentConnection);
        this.bank = bank;
    }


    /**
     * Processes a message from the client.
     * This method is overridden to handle incoming messages
     * specific to the bank's operations.
     * It could include actions such as processing payments,
     * checking balances, or updating account information.
     *
     * @param m the incoming message containing data and instructions
     */
    protected Message<?> handleMessage(Message<?> m) {
        switch (m.getMessageType()) {
            case BALANCE -> {
                if (m.getMessageObject() instanceof AgentInfo a) {
                    Accounts account = bank.getAccount(a.name());
                    if (account != null) {
                        Double balance = account.getAvailableFunds();
                        return new Message<>(MessageType.BALANCE, balance);
                    }
                } else if (m.getMessageObject() instanceof String a) {
                    Accounts account = bank.getAccount(a);
                    if (account != null) {
                        Double balance = account.getAvailableFunds();
                        return new Message<>(MessageType.BALANCE, balance);
                    }
                }
            }
            case NEW_ACCOUNT -> {
                if (m.getMessageObject() instanceof AgentBalanceInfo a) {
                    bank.addAgentSocket(this);
                    Accounts account = bank.createAccount(a.name(), a.balance());
                    AgentBalanceInfo response = new AgentBalanceInfo(a.name(),
                            account.getAccountNumber(), a.balance());
                    bank.sendAllAgentsAhInfo();
                    return new Message<>(MessageType.NEW_ACCOUNT, response);
                }
            }
            case NEW_AUCTION_HOUSE -> {
                if (m.getMessageObject() instanceof AHInfo ah) {
                    int accountNumber = ah.acct();
                    if (accountNumber == -1) {
                        Accounts ahAccount = bank.createAccount(ah.getName(), 0.0);
                        accountNumber = ahAccount.getAccountNumber();
                        bank.setName("AuctionHouse-" + accountNumber, ahAccount);
                    }
                    AHInfo response = new AHInfo(ah.ipHost(), ah.locPort(), accountNumber, ah.balance());
                    bank.addAuctionHouse(response);
                    bank.sendAllAgentsAhInfo();
                    bank.saveAuctionHouseHandler(accountNumber, this);
                    return new Message<>(MessageType.NEW_AUCTION_HOUSE, response);
                }
            }
            case HOLD_FUNDS -> {
                if (m.getMessageObject() instanceof AgentBid bid) {
                    Accounts fromAccount = bank.getAccountByNumber(bid.agentAccount());
                    if (fromAccount != null) {
                        boolean accountLocked = bank.lockAccount(bid.agentAccount());
                        if (accountLocked) {
                            try {
                                boolean success = fromAccount.blockFunds(bid.amount());
                                if (success) {
                                    System.out.printf("Bid accepted: $%.2f " +
                                                    "from Account #%d%n", bid.amount(),
                                            bid.agentAccount());
                                    AgentBid newBid = new AgentBid(bid, "Accepted");
                                    return new Message<>(MessageType.FUNDS_HELD, newBid);
                                } else {
                                    System.out.printf("Bid rejected:" +
                                                    " Insufficient funds in Account #%d%n",
                                            bid.agentAccount());
                                    AgentBid newBid = new AgentBid(bid, "Rejected");
                                    return new Message<>(MessageType.FUNDS_NOT_HELD, newBid);
                                }
                            } finally {
                                bank.unlockAccount(bid.agentAccount());
                            }
                        } else {
                            System.out.printf("Bid rejected: " +
                                            "Account #%d is locked by another process.%n",
                                    bid.agentAccount());
                            return new Message<>(MessageType.FUNDS_NOT_HELD, bid);
                        }
                    } else {
                        System.out.printf("Bid rejected: Account" +
                                " #%d not found.%n", bid.agentAccount());
                        return new Message<>(MessageType.FUNDS_NOT_HELD, bid);
                    }
                }
            }
            case RELEASE_FUNDS -> {
                if (m.getMessageObject() instanceof AgentBid bid) {
                    boolean accountLocked = bank.lockAccount(bid.agentAccount());
                    if (accountLocked) {
                        try {
                            boolean success = bank.unblockFunds(bid.agentAccount(), bid.amount());
                            if (success) {
                                System.out.printf("Funds " +
                                                "unblocked for outbid: $%.2f from " +
                                                "Account #%d%n", bid.amount(),
                                        bid.agentAccount());
                                return new Message<>(MessageType.FUNDS_UNBLOCKED, bid);
                            } else {
                                System.out.printf("Failed to unblock funds for " +
                                                "outbid: $%.2f from Account #%d%n",
                                        bid.amount(), bid.agentAccount());
                                return new Message<>(MessageType.FUNDS_NOT_UNBLOCKED, bid);
                            }
                        } finally {
                            bank.unlockAccount(bid.agentAccount());
                        }
                    } else {
                        System.out.printf("Failed to unblock funds: Account #%d" +
                                " is locked by another process.%n", bid.agentAccount());
                        return new Message<>(MessageType.FUNDS_NOT_UNBLOCKED, bid);
                    }
                }
            }
            case TRANSFER_FUNDS -> {
                if (m.getMessageObject() instanceof TransferInfo transferInfo) {
                    Accounts fromAccount = bank.getAccountByNumber(transferInfo.fromAccount());
                    Accounts toAccount = bank.getAccountByNumber(transferInfo.toAccount());
                    double amount = transferInfo.amount();
                    // Lock both accounts before proceeding
                    boolean fromLocked = bank.lockAccount(transferInfo.fromAccount());
                    boolean toLocked = bank.lockAccount(transferInfo.toAccount());
                    try {
                        if (fromLocked && toLocked) {
                            if (fromAccount != null && toAccount != null) {
                                boolean success = fromAccount.transferFunds(toAccount, amount);
                                if (success) {
                                    // Log the successful transfer
                                    System.out.printf("Funds transfer successful:" +
                                                    " $%.2f from Account #%d to Account #%d%n",
                                            amount, transferInfo.fromAccount(), transferInfo.toAccount());
                                    // Create an AgentBid (if needed for further processing)
                                    AgentBid bid = new AgentBid(transferInfo.fromAccount(),
                                            transferInfo.item().getNumber(), // Using item number if necessary
                                            transferInfo.amount(), "Transferred");
                                    // Send the message to Auction House about funds transfer
                                    bank.sendMessageToAuctionHouse(
                                            transferInfo.toAccount(),
                                            new Message<>(MessageType.FUNDS_TRANSFERRED, bid)
                                    );
                                    return new Message<>(MessageType.FUNDS_TRANSFERRED, bid);
                                } else {
                                    System.out.printf("Funds transfer failed:" +
                                                    " Insufficient funds in Account #%d%n",
                                            transferInfo.fromAccount());
                                    AgentBid bid = new AgentBid(transferInfo.fromAccount(),
                                            transferInfo.item().getNumber(), // Using item number if necessary
                                            transferInfo.amount(), "Not Transferred");
                                    bank.sendMessageToAuctionHouse(
                                            transferInfo.toAccount(),
                                            new Message<>(MessageType.FUNDS_TRANSFERRED, bid)
                                    );
                                    return new Message<>(MessageType.FUNDS_NOT_TRANSFERRED, transferInfo);
                                }
                            } else {
                                System.out.printf("Funds transfer " +
                                                "failed: Account #%d or " +
                                                "Account #%d not found.%n",
                                        transferInfo.fromAccount(), transferInfo.toAccount());
                                return new Message<>(MessageType.FUNDS_NOT_TRANSFERRED, transferInfo);
                            }
                        } else {
                            System.out.println("Funds transfer failed:" +
                                    " Unable to acquire locks on accounts.");
                            return new Message<>(MessageType.FUNDS_NOT_TRANSFERRED,
                                    transferInfo);
                        }
                    } finally {
                        // Always unlock accounts after processing
                        if (fromLocked)
                            bank.unlockAccount(transferInfo.fromAccount());
                        if (toLocked)
                            bank.unlockAccount(transferInfo.toAccount());
                    }
                }
            }
            case DISCONNECT -> {
                Object messageObject = m.getMessageObject();
                if (messageObject instanceof AgentInfo agentInfo) {
                    bank.removeAgent(agentInfo.name());
                    System.out.println("Agent disconnected: " + agentInfo.name());
                } else if (messageObject instanceof AHInfo ahInfo) {
                    bank.removeAuctionHouse(ahInfo.ipHost(), ahInfo.locPort());
                    return new Message<>(MessageType.AUCTION_HOUSE_REMOVED, ahInfo);
                }
                this.stop();
            }
            case LIST_AUCTION_HOUSES -> {
                List<AHInfo> activeAhs = bank.getActiveAuctionHouses();
                return new Message<>(MessageType.LIST_AUCTION_HOUSES, activeAhs);
            }
        }
        return Message.getWaitingMessage();
    }
}