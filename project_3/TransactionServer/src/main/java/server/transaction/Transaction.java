package server.transaction;

import java.util.ArrayList;

/**
 * Transaction
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class Transaction {
    
    int transactionID;
    int lastCommittedTransaction;
    String log;
    
    // is the read set the account ID's of the two accounts involved in a
    // transaction???????? but then why does Otte have it as an array list
    ArrayList<Integer> readSet;
    int writeSet;
    
    /**
     * Constructor for a Transaction
     * @param transactionID
     * @param lastCommittedTransaction 
     */
    Transaction(int transactionID, int lastCommittedTransaction)
    {
        this.transactionID = transactionID;
        this.lastCommittedTransaction = lastCommittedTransaction;
        this.log = "";
    }
    
    /**
     * Get the last committed transaction.
     * @return ID of last committed transaction
     */
    public int getLastCommittedTransaction()
    {
        return this.lastCommittedTransaction;
    }
    
    /**
     * Get the log.
     * @return the log in the form of a String 
     */
    public String getLog()
    {
        return this.log;
    }
    
    /**
     * Get the read set.
     * @return read set in the form of an ArrayList
     */
    public ArrayList<Integer> getReadSet()
    {
       return this.readSet; 
    }
    
    /**
     * Get the transaction ID of this Transaction.
     * @return this transaction ID in the form of an integer
     */
    public int getTransactionID()
    {
        return this.transactionID;
    }
    
    /**
     * Get the write set of this transaction
     * @return the write set in the form of an integer(?)
     */
    public int getWriteSet()
    {
        return this.writeSet;
    }
    
    /**
     * Log the given string to this Transaction log.
     * @param logString string to be logged
     */
    private void log(String logString)
    {
        this.log += logString;
    }
    
    /**
     * Read a balance from an account
     * @param accountNumber account number of account to be read from
     * @return account balance
     */
    int read(int accountNumber)
    {
        int balance = 0;
        // read the balance of an account
        
        // return the balance
        return balance;
    }
    
    /**
     * Write the given amount to the given account
     * @param accountNumber number of the desired account
     * @param amount amount to be written
     * @return Boolean success/failure
     */
    Boolean write(int accountNumber, int amount)
    {
        // write the given amount to the given account
        
        // return success/failure
        return null;
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
}
