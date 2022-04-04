package server.transaction;

import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Transaction Manager
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionManager {
    
    public ArrayList<Transaction> abortedTransactions;
    public ArrayList<Transaction> activeTransactions;
    public ArrayList<Transaction> committedTransactions;
    int transactionNumber;
    String logString;
    
    // create ID for transactions
    int transactionID = 0;
    
    /**
     * Construct a TransactionManager
     */
    public TransactionManager()
    {
        this.abortedTransactions = new ArrayList<>();
        this.activeTransactions = new ArrayList<>();
        this.committedTransactions = new ArrayList<>();
        
        // initialize transaction number to 0
        transactionNumber = 0;
        
        logString = "[+] TransactionManager created";
        TransactionServer.updateLogList(logString);
        System.out.println(logString);
    }
    
    /**
     * Add a given aborted transaction to the transaction list
     * @param abortedTransaction aborted transaction
     */
    public void addAbortedTransaction(Transaction abortedTransaction)
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
     * Add a given active transaction to the transaction list
     * @param abortedTransaction active transaction
     */
    public void addActiveTransaction(Transaction abortedTransaction)
    {
        this.activeTransactions.add(abortedTransaction);
    }
    
    /**
     * Get a list of all active transactions.
     * @return a list of all active transactions
     */
    public ArrayList<Transaction> getActiveTransactions()
    {
        return this.activeTransactions;
    }
    
    /**
     * Add a given committed transaction to the transaction list
     * @param committedTransaction 
     */
    public void addCommittedTransaction(Transaction committedTransaction)
    {
        this.committedTransactions.add(committedTransaction);
    }
    
    /**
     * Get a list of all committed transactions
     * @return committed transactions list
     */
    public ArrayList<Transaction> getCommittedTransactions()
    {
        return this.committedTransactions;
    }
    
    /**
     * Get the number of the most recently committed transaction
     * 
     * The most recently committed transaction is the last element in the
     * committedTransactions list
     * @return number of most recently committed transaction, -1 if the list is
     * empty/null
     */
    public int getMostRecentComittedTransactionNumber()
    {
        // check if the committedTransactions list is empty
        if( (!committedTransactions.isEmpty()) && (committedTransactions != null))
        {
            // not empty, return the last transaction number in the list
            return committedTransactions.get(committedTransactions.size() - 1).getTransactionNumber();
        }
        return -1;
    }
    
    /**
     * Handle a transaction from a given client connection
     * @param transactionClient 
     * @throws java.net.SocketException 
     */
    public void runTransaction(Socket transactionClient) throws SocketException {
        // create a new transaction manager worker
        new TransactionManagerWorker(transactionClient, transactionID).start();
        // increment the transaction ID
        transactionID += 1;
    }
    
    /**
     * Validate a transaction
     * @param transaction
     * @return success/failure of validation
     */
    public Boolean validateTransaction(Transaction transaction)
    {
        // declare variables
        HashMap<Integer, Integer> overlapWriteSet;
        int firstReadAccountID;
        int secondReadAccountID;
        
        // assign transaction number to transaction
        transaction.setTransactionNumber(this.transactionNumber);

        // increment transaction number
        this.transactionNumber++;
        
        // get the readset of the current transaction
        ArrayList<Integer> currentReadSet = transaction.getReadSet();
        firstReadAccountID = currentReadSet.get(0);
        secondReadAccountID = currentReadSet.get(1);
        
        ArrayList<Transaction> overlappingTransactions = transaction.getOverlappingTransactions(transaction, committedTransactions);

        // validate read-write conflicts
        for (Transaction overlappingTransaction: overlappingTransactions)
        {
            overlapWriteSet = overlappingTransaction.getWriteSet();
            
            for(Integer accountID: overlapWriteSet.keySet())
            {
                if(accountID == firstReadAccountID)
                {
                    // an overlapping transaction wrote to an account the current
                    // transaction has read from, CONFLICT, return failed validation
                    logString = "[!] Transaction #" + transaction.getTransactionID() + " [TransactionManager.validateTransaction] Transaction #" + transaction.getTransactionID() + " failed: r/w conflict on account #" + firstReadAccountID + " with Transaction #" + overlappingTransaction.getTransactionID();
                    TransactionServer.updateLogList(logString);
                    System.out.println(logString);
                    this.transactionNumber--;
                    return false;
                }
                else if(accountID == secondReadAccountID)
                {
                    // an overlapping transaction wrote to an account the current
                    // transaction has read from, CONFLICT, return failed validation
                    logString = "[!] Transaction #" + transaction.getTransactionID() + " [TransactionManager.validateTransaction] Transaction #" + transaction.getTransactionID() + " failed: r/w conflict on account #" + secondReadAccountID + " with Transaction #" + overlappingTransaction.getTransactionID();
                    TransactionServer.updateLogList(logString);
                    System.out.println(logString);
                    this.transactionNumber--;
                    return false;
                }
            }
        }
        
        // no conflict, validation is a success
        logString = "[+] Transaction #" + transaction.getTransactionID() + " [TransactionManager.validateTransaction] Transaction #" + transaction.getTransactionID() + " successfully validated";
        TransactionServer.updateLogList(logString);
        System.out.println(logString);
        return true;
    }
    
    /**
     * Update a transaction by committing tentative data to operational data
     * @param transaction
     */
    public void updateTransaction(Transaction transaction)
    {
        transaction.update();
    }
    
}
