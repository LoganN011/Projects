package Bank;

import java.io.Serializable;

/**
 * The Accounts class represents a bank account with details such as account holder,
 * balance, blocked funds, and lock status.
 * It provides methods for managing account operations such as blocking/unblocking funds,
 * transferring funds, and locking/unlocking the account.
 * @author Cameron Fox, Logan Nunno, Tanisha Patel, and Lawrence Rybarcyk
 */
public class Accounts implements Serializable {

    private String accountHolder;     // The account holder's name
    private final int accountNumber;  // Unique account number
    private double totalBalance;      // Total balance in the account
    private double blockedFunds;      // Funds that are temporarily blocked for a transaction
    private boolean locked;           // Tracks if the account is locked for processing

    /**
     * Constructor to initialize an account with the given details.
     *
     * @param accountHolder the name of the account holder
     * @param accountNumber the unique account number
     * @param initialBalance the initial balance for the account
     */
    public Accounts(String accountHolder, int accountNumber, double initialBalance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.totalBalance = initialBalance;
        this.blockedFunds = 0.0;
        this.locked = false;  // Initially, the account is not locked
    }
    /**
     * Gets the name of the account holder.
     *
     * @return the name of the account holder
     */
    public String getAccountHolder() {
        return accountHolder;  // Get the account holder's name
    }
    /**
     * Sets the name of the account holder.
     *
     * @param accountHolder the new account holder's name
     */
    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;  // Set the account holder's name
    }
    /**
     * Gets the account number.
     *
     * @return the unique account number
     */
    public int getAccountNumber() {
        return accountNumber;  // Get the account number
    }
    /**
     * Gets the total balance in the account.
     *
     * @return the total balance
     */
    public double getTotalBalance() {
        return totalBalance;  // Get the total balance of the account
    }
    /**
     * Returns the available funds, which is the total balance minus any blocked funds.
     *
     * @return available funds in the account
     */
    public synchronized double getAvailableFunds() {
        return totalBalance - blockedFunds;
    }

    /**
     * Blocks the specified amount of funds for a transaction.
     *
     * @param amount the amount to block in the account
     * @return true if the funds are successfully blocked, false if there
     * are insufficient available funds
     */
    public synchronized boolean blockFunds(double amount) {
        if (getAvailableFunds() >= amount) {
            blockedFunds += amount;
            System.out.printf("Blocked $%.2f in account %d. " +
                            "Blocked funds: $%.2f, Available funds: $%.2f.%n",
                    amount, accountNumber, blockedFunds, getAvailableFunds());
            return true;
        } else {
            System.out.printf("Insufficient available funds to " +
                    "block $%.2f in account %d.%n", amount, accountNumber);
            return false;
        }
    }

    /**
     * Unblocks the specified amount of funds.
     *
     * @param amount the amount to unblock from the account
     * @return true if the funds are successfully unblocked, false
     * if there are insufficient blocked funds
     */
    public synchronized boolean unblockFunds(double amount) {
        if (blockedFunds >= amount) {
            blockedFunds -= amount;
            System.out.printf("Unblocked $%.2f in account %d. " +
                            "Remaining blocked funds: $%.2f.%n",
                    amount, accountNumber, blockedFunds);
            return true;
        } else {
            System.out.printf("Insufficient blocked funds to " +
                            "unblock $%.2f in account %d. Blocked funds: $%.2f.%n",
                    amount, accountNumber, blockedFunds);
            return false;
        }
    }

    /**
     * Transfers the specified amount of funds from blocked funds to total balance.
     *
     * @param amount the amount to transfer
     * @return true if the transfer is successful,
     * false if there are insufficient blocked funds
     */
    public synchronized boolean transferFunds(double amount) {
        if (blockedFunds >= amount) {
            blockedFunds -= amount;
            totalBalance -= amount;
            System.out.printf("Transferred $%.2f from account %d." +
                            " New balance: $%.2f.%n",
                    amount, accountNumber, totalBalance);
            return true;
        } else {
            System.out.printf("Insufficient blocked funds to " +
                    "transfer $%.2f from account %d.%n", amount, accountNumber);
            return false;
        }
    }

    /**
     * Transfers funds between two accounts.
     *
     * @param toAccount the account to transfer funds to
     * @param amount the amount to transfer
     * @return true if the transfer is successful,
     * false if there are insufficient blocked funds
     */
    public boolean transferFunds(Accounts toAccount, double amount) {
        if (this.blockedFunds >= amount) {
            this.totalBalance -= amount;
            this.blockedFunds -= amount;
            toAccount.totalBalance += amount;
            return true;
        }
        return false;
    }

    /**
     * Deposits the specified amount into the account, increasing the total balance.
     *
     * @param amount the amount to deposit
     */
    public synchronized void deposit(double amount) {
        totalBalance += amount;
        System.out.printf("Deposited $%.2f into account %d. New balance: $%.2f.%n",
                amount, accountNumber, totalBalance);
    }

    /**
     * Checks if the account is currently locked.
     *
     * @return true if the account is locked, false otherwise
     */
    public synchronized boolean isLocked() {
        return locked;
    }

    /**
     * Locks the account for processing. The thread waits if the account is already locked.
     */
    public synchronized void lockAccount() {
        while (locked) {  // Wait if already locked
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // Restore interrupted status
            }
        }
        locked = true;  // Lock the account
        System.out.printf("Account %d is now locked for processing.%n", accountNumber);
    }

    /**
     * Sets the lock status of the account.
     *
     * @param locked the new lock status of the account
     */
    public synchronized void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Releases the lock on the account, allowing further processing.
     */
    public synchronized void releaseLock() {
        locked = false;  // Unlock the account
        notifyAll();  // Notify any threads waiting on the lock
        System.out.printf("Account %d is now unlocked for further processing.%n", accountNumber);
    }

    /**
     * Returns a string representation of the account, including account number, holder, and balance information.
     *
     * @return string representation of the account
     */
    @Override
    public String toString() {
        return String.format("Account #%d - %s: Total Balance: $%.2f, Available Funds: $%.2f",
                accountNumber, accountHolder, totalBalance, getAvailableFunds());
    }

    /**
     * Returns the current amount of blocked funds in the account.
     *
     * @return the blocked funds
     */
    public double getBlockedFunds() {
        return blockedFunds;  // Return the blocked funds
    }
}
