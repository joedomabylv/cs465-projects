package server.account;

import java.util.ArrayList;

/**
 * Account Manager
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class AccountManager {
    
    public static ArrayList<Account> accounts;
    
    /**
     * Constructor
     * @param numberAccounts
     * @param initialBalance
     */
    public AccountManager(int numberAccounts, int initialBalance)
    {
        // initialize accounts list
        AccountManager.accounts = new ArrayList();
        
        // create accounts
        for(int index = 0; index < numberAccounts; index++)
        {
            AccountManager.accounts.add(createAccount(index, initialBalance));
        }
        System.out.println("[+] AccountManager created");
    }
    
    /**
     * Create a new account
     * @param accountID ID of new account
     * @param initialBalance starting balance
     * @return new Account
     */
    public static Account createAccount(int accountID, int initialBalance)
    {
        return new Account(accountID, initialBalance);
    }
    
    /**
     * Get the total number of accounts in the account list
     * @return account number/list size
     */
    public static int getTotalAccountNumber()
    {
        System.out.println(AccountManager.accounts);
        return AccountManager.accounts.size();
    }
    
    /**
     * Read an accounts balance
     * @param accountID
     * @return account balance
     */
    public static int read(int accountID)
    {
        // get the account object associated with the ID
        Account account = accounts.get(accountID);

        // return the balance of the account
        return account.getBalance();
    }
    
    /**
     * Write a new balance to an account
     * @param accountID ID of the account to be written to
     * @param amount new balance for the account
     */
    public static void write(int accountID, int amount)
    {
        // get the account object associated with the ID
        Account account = accounts.get(accountID);
        
        // set the new account balance
        account.setBalance(amount);
    }
    

}
