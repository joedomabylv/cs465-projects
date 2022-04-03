package server.transaction;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Transaction Manager
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionManager {
    
    public static ArrayList<Transaction> abortedTransactions;
    public static ArrayList<Transaction> activeTransactions;
    public static ArrayList<Transaction> committedTransactions;
    // create numbers for transactions, acting as transaction ID's
    int transactionNumber = 0;
    
    /**
     * Construct a TransactionManager
     */
    public TransactionManager()
    {
        TransactionManager.abortedTransactions = new ArrayList<>();
        TransactionManager.activeTransactions = new ArrayList<>();
        TransactionManager.committedTransactions = new ArrayList<>();
        System.out.println("[+] TransactionManager created");
    }
    
    /**
     * Add a given aborted transaction to the transaction list
     * @param abortedTransaction aborted transaction
     */
    static void addAbortedTransaction(Transaction abortedTransaction)
    {
        TransactionManager.abortedTransactions.add(abortedTransaction);
    }
    
    /**
     * Get a list of all aborted transactions.
     * @return a list of all aborted transactions
     */
    public static ArrayList<Transaction> getAbortedTransactions()
    {
        return TransactionManager.abortedTransactions;
    }
    
    /**
     * Add a given active transaction to the transaction list
     * @param abortedTransaction active transaction
     */
    void addActiveTransaction(Transaction abortedTransaction)
    {
        TransactionManager.activeTransactions.add(abortedTransaction);
    }
    
    /**
     * Get a list of all active transactions.
     * @return a list of all active transactions
     */
    public static ArrayList<Transaction> getActiveTransactions()
    {
        return TransactionManager.activeTransactions;
    }
    
    /**
     * Add a given committed transaction to the transaction list
     * @param committedTransaction 
     */
    void addCommittedTransaction(Transaction committedTransaction)
    {
        TransactionManager.committedTransactions.add(committedTransaction);
    }
    
    /**
     * Get the ID of the most recently committed transaction
     * 
     * The most recently committed transaction is the last element in the
     * committedTransactions list
     * @return ID of most recently committed transaction, 0 if the list is
     * empty
     */
    public int getMostRecentComittedTransactionID()
    {
        // check if the committedTransactions list is empty
        if( (!committedTransactions.isEmpty()) && (committedTransactions != null))
        {
            // not empty, return the last transaction in the list
            return committedTransactions.get(committedTransactions.size() - 1).getTransactionID();
        }
        return 0;
    }
    
    /**
     * Handle a transaction from a given client connection
     * @param transactionClient 
     * @throws java.net.SocketException 
     */
    public void runTransaction(Socket transactionClient) throws SocketException {
        // create a new transaction manager worker
        new TransactionManagerWorker(transactionClient, transactionNumber).start();
        // increment the transaction number
        transactionNumber += 1;
    }
}
