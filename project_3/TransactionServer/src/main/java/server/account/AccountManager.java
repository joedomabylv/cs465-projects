package server.account;

import java.util.ArrayList;

/**
 * Account Manager
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class AccountManager {
    
    ArrayList<Account> accounts;
    
    /**
     * Constructor
     * @param numberAccounts
     * @param initialBalance
     */
    public AccountManager(int numberAccounts, int initialBalance)
    {
        // initialize accounts list
        this.accounts = new ArrayList();
        
        // create accounts
        for(int index = 0; index < numberAccounts; index++)
        {
            this.accounts.add(createAccount(index, initialBalance));
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

}
