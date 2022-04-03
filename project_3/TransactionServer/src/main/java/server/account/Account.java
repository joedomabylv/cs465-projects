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
     * Get the account balance
     * @return account balance
     */
    public int getBalance()
    {
        return this.balance;
    }
    
    /**
     * Set a new account balance
     * @param newBalance new account balance
     */
    public void setBalance(int newBalance)
    {
        this.balance = newBalance;
    }
    
    /**
     * Get the account ID of this account
     * @return account ID
     */
    public int getAccountID()
    {
        return this.accountID;
    }
    
}
