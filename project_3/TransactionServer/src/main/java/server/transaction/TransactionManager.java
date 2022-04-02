package server.transaction;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Transaction Manager
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionManager {
    
    ArrayList<Transaction> abortedTransactions;
    ArrayList<Transaction> activeTransactions;
    
    /**
     * Construct a TransactionManager
     */
    public TransactionManager()
    {
        this.abortedTransactions = new ArrayList<>();
        this.activeTransactions = new ArrayList<>();
        System.out.println("[+] TransactionManager created");
    }
    
    /**
     * Add a given aborted transaction to the transaction list
     * @param abortedTransaction aborted transaction
     */
    void addAbortedTransaction(Transaction abortedTransaction)
    {
        this.abortedTransactions.add(abortedTransaction);
    }
    
    /**
     * Get a list of all aborted transactions.
     * @return a list of all aborted transactions
     */
    public ArrayList<Transaction> getAbortedTransactions()
    {
        return this.abortedTransactions;
    }
    
    /**
     * Handle a transaction from a given client connection
     * @param transactionClient 
     */
    public void runTransaction(Socket transactionClient)
    {
        new TransactionManagerWorker(transactionClient).start();
    }
}
