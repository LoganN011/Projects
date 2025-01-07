package agent;

import auctionHouse.AHInfo;
import auctionHouse.ItemForSale;
import auctionHouse.Listing;
import javafx.scene.control.Alert;
import javafx.util.Pair;
import messages.Message;
import messages.MessageHandler;
import messages.MessageType;
import messages.TransferInfo;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The message handler for the agent. This will handle all messages from
 * both AH and the bank. Each is its own thread so multiple communication
 * can be handled at once
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class AgentMessageHandler extends MessageHandler {
    private final Agent agent;
    private final Socket socket;

    /**
     * Agent message handler constructor.
     * Makes a new handler by using the parents class
     *
     * @param socket the current socket
     * @param agent  the agent that is message handler is being used with
     * @throws IOException throws if there is socket problems
     */
    public AgentMessageHandler(Socket socket, Agent agent) throws IOException {
        super(socket);
        this.agent = agent;
        this.socket = socket;
    }

    /**
     * Method to handle each message from a bank or and AH for the agent
     *
     * @param m The message that is input
     * @return the response message or the waiting message for no response
     */
    protected Message<?> handleMessage(Message<?> m) {
        switch (m.getMessageType()) {
            case OUTBID -> {
                AgentScreenMain.bidInfoPopUp("Outbid ", Alert.AlertType.INFORMATION,
                        agent.isAutoBidder());
                AgentScreenMain.updateAllScreen();
                agent.requestBalance();
            }
            case DISCONNECT -> {
                if (m.getMessageObject() instanceof AHInfo info) {
                    agent.removeBookKeeping(socket, info);
                    this.stop();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AgentScreenMain.disconnectUpdate();
                AgentScreenMain.updateAllScreen();

            }
            case WINNER -> {
                if (m.getMessageObject() instanceof ItemForSale item) {
                    agent.addToInventory(item);
                    AgentScreenMain.bidInfoPopUp("You won: " + item.getDescription(),
                            Alert.AlertType.INFORMATION, agent.isAutoBidder());

                    agent.sendMessageToBank(new Message<>(MessageType.TRANSFER_FUNDS,
                            new TransferInfo(agent.getAccountNumber(),
                                    item.getAHAcct(), item.getCurrBid(),
                                    item)));
                }
                agent.requestBalance();
                AgentScreenMain.updateAllScreen();
            }
            case BID_ACCEPTED, FUNDS_TRANSFERRED -> {
                agent.requestBalance();

            }
            case BID_REJECTED -> {
                if (m.getMessageObject() instanceof AgentBid agentBid) {
                    AgentScreenMain.bidInfoPopUp(agentBid.bidStatus(),
                            Alert.AlertType.ERROR, agent.isAutoBidder());
                }

            }
            case BALANCE -> {
                if (m.getMessageObject() instanceof Double amount) {
                    agent.setCurrentBalance(amount);
                }
                AgentScreenMain.updateAllScreen();
            }
            case NEW_ACCOUNT -> {
                if (m.getMessageObject() instanceof AgentBalanceInfo a) {
                    agent.setAccountNumber(a.accountNumber());
                    agent.setCurrentBalance(a.balance());
                }
                AgentScreenMain.updateAllScreen();
            }


            case LIST_AUCTION_HOUSES -> {
                if (m.getMessageObject() instanceof ArrayList<?> ahInfos) {
                    agent.receiveAhInfo((ArrayList<AHInfo>) ahInfos);
                }
            }

            case LISTING -> {
                if (m.getMessageObject() instanceof Listing listing) {
                    String ip = this.socket.getInetAddress().getHostName();
                    int port = this.socket.getPort();
                    Pair<String, Integer> auctionHouseId = new Pair<>(ip, port);
                    agent.saveListing(auctionHouseId, listing);
                    AgentScreenMain.updateAllScreen();
                }
            }
        }
        return Message.getWaitingMessage();
    }
}