package server.transaction;

import static comm.MessageTypes.*;
import java.util.ArrayList;
import java.util.HashMap;
import server.account.AccountManager;

/**
 * Transaction
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class Transaction {
    
    int transactionNumber;
    int transactionID;
    int lastCommittedTransactionNumber;
    String log;
    
    ArrayList<Integer> readSet;
    
    // note: first integer is the account ID, second is the new balance
    HashMap<Integer, Integer> writeSet;
    
    /**
     * Constructor for a Transaction
     * @param transactionID ID of the transaction, NOT THE NUMBER
     * @param lastCommittedTransactionNumber number of the last committed transaction,
     * NOT THE ID
     */
    Transaction(int transactionID, int lastCommittedTransactionNumber)
    {
        this.transactionID = transactionID;
        this.lastCommittedTransactionNumber = lastCommittedTransactionNumber;
        
        // initialize read and write sets
        this.readSet = new ArrayList<>();
        this.writeSet = new HashMap<>();
        this.log = "";
    }
    
    /**
     * Get the last committed transaction.
     * @return ID of last committed transaction
     */
    public int getLastCommittedTransactionNumber()
    {
        return this.lastCommittedTransactionNumber;
    }
    
    /**
     * Get the log
     * @return the log in the form of a String 
     */
    public String getLog()
    {
        return this.log;
    }
    
    /**
     * Get the read set
     * @return read set in the form of an ArrayList
     */
    public ArrayList<Integer> getReadSet()
    {
       return this.readSet; 
    }
    
    /**
     * Get the transaction ID of this Transaction
     * @return this transaction ID in the form of an integer
     */
    public int getTransactionID()
    {
        return this.transactionID;
    }
    
    /**
     * Get the transaction number of this Transaction
     * @return transaction number
     */
    public int getTransactionNumber()
    {
        return this.transactionNumber;
    }
    
    /**
     * Set the transaction number for this Transaction
     * @param transactionNumber
     */
    public void setTransactionNumber(int transactionNumber)
    {
        this.transactionNumber = transactionNumber;
    }
    
    /**
     * Get the write set of this transaction
     * @return the write set in the form of an integer
     */
    public HashMap getWriteSet()
    {
        return this.writeSet;
    }
    
    /**
     * Log the given string to this Transaction log.
     * @param logString string to be logged
     */
    public void log(String logString)
    {
        this.log += logString;
    }
    
    /**
     * Read a balance from an account and update the read set of this transaction
     * @param accountID account number of account to be read from
     * @return account balance
     */
    public int read(int accountID)
    {
        // read the balance of the account from the account ID
        int accountBalance = AccountManager.read(accountID);
        
        // add the account ID to the read set
        readSet.add(accountID);
        
        // return the account balance
        return accountBalance;
    }
    
    /**
     * Update the write set of this transaction
     * @param accountID number of the desired account
     * @param amount amount to be written
     * @return 
     */
    public int write(int accountID, int amount)
    {
        // update the write set of tentative data
        this.writeSet.put(accountID, amount);
        
        // always return success
        return WRITESET_SUCCESS;
    }
    
    /**
     * Open a transaction between two clients and commence the transaction
     * @param transactionID
     * @return 
     */
    private Boolean openTransaction(int clientA, int clientB, int amount)
    {
        
        // open a connection between two clients, A and B
        
        // read balance A
        // write to balance A balance A's total and subtract the amount
        
        // read balance B
        // write to balance B the current amount + the new amount
        
        // close the connection
        
        // return the success of the transaction
        return null;
    }
    
    /**
     * Close a transaction. Commit the transaction if it was successful,
     * abort the transaction if failure.
     * @return Boolean success/failure
     */
    private Boolean closeTransaction()
    {
        // commit the transaction if success
        
        // abort if a transaction is already going on between either account
        
        // write result to log?
        
        return null;
    }
    
    /**
     * Get all overlapping transactions based on a given transaction
     * @param transaction
     * @param committedTransactions
     * @return ArrayList of all overlapping transactions
     */
    public ArrayList<Transaction> getOverlappingTransactions(Transaction transaction, ArrayList<Transaction> committedTransactions)
    {
        ArrayList<Transaction> overlappingTransactions = new ArrayList<>();
                                
        if(!committedTransactions.isEmpty())
        {
            // generate a list of overlapping transactions
            for(int index = transaction.getLastCommittedTransactionNumber() + 1; index < transaction.getTransactionNumber() - 1; index++)
            {
                overlappingTransactions.add(committedTransactions.get(index));
            }
        }
        
        return overlappingTransactions;
    }
    
    /**
     * Commit the tentative data to operational data of this Transaction
     */
    public void update()
    {
        for(Integer accountID: writeSet.keySet())
        {
            AccountManager.write(accountID, writeSet.get(accountID));
        }
    }
}
