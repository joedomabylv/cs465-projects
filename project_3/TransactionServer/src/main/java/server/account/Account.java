package server.account;

/**
 * Account
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class Account {
    
    int balance;
    int accountID;
    
    /**
     * Constructor
     * @param balance initial balance
     * @param accountID ID assigned to this account
     */
    public Account(int accountID, int balance)
    {
        this.balance = balance;
        this.accountID = accountID;
    }
    
    /**
     * Get the account ID of this account
     * @return account ID
     */
    public int getAccountID()
    {
        return this.accountID;
    }
    
    /**
     * Read the balance from this account
     * @return account balance
     */
    public int read()
    {
        return this.balance;
    }
    
    /**
     * Write a given amount to this accounts balance
     * @param amount new account balance
     */
    public void write(int amount)
    {
        this.balance = amount;
        
    }
    
}
