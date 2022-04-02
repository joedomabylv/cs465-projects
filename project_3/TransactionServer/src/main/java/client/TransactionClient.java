package client;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.account.Account;
import server.account.AccountManager;
import static utils.config.PropertiesHandler.getPropertyInfo;

/**
 * Transaction Client
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionClient {

    public static class TransactionThread extends Thread
    {
        
        int transactionID;
        int transactionResult;
        private TransactionServerProxy transactionServerProxy;
        int firstAccountID;
        int secondAccountID;
        int firstAccountBalance;
        int secondAccountBalance;
        int transferAmount;
        
        // empty contstructor
        public TransactionThread(){};

        @Override
        public void run() {
            
            try
            {
                // create a transaction server proxy
                transactionServerProxy = new TransactionServerProxy();
                                
                // choose two random account IDs for a transaction
                firstAccountID = getRandomNumber(0, 10);
                secondAccountID = getRandomNumber(0, 10);
                while(secondAccountID == firstAccountID)
                {
                    secondAccountID = getRandomNumber(0, 10);
                }
                
                // create a random amount to transfer between the accounts
                // NOTE: maximum size of transaction amount is arbitraily set
                // to 10
                transferAmount = getRandomNumber(0, 10);
                
                // open the transaction
                // NOTE: i don't really know if we can access static variables from
                // different threads in the way i think it works, this might
                // prove problematic in the future uh oh!
                transactionID = transactionServerProxy.openTransaction();
                
                System.out.println("[*] Transaction #" + transactionID + " started, transfer $" + transferAmount + ": " + firstAccountID + "->" + secondAccountID);

                // read/write to/from the accounts attached to the transaction ID
                // NOTE: not sure if we're supposed to be receiving two
                // balances back from the single read call? maybe should
                // determine accounts based on transaction ID then call
                // read twice
                // read/write to/from the first account
                firstAccountBalance = transactionServerProxy.read(firstAccountID);
                transactionServerProxy.write(firstAccountID, firstAccountBalance - transferAmount);
                
                // read/write to/from the second account
                secondAccountBalance = transactionServerProxy.read(secondAccountID);
                transactionServerProxy.write(secondAccountID, secondAccountBalance + transferAmount);
                
                // close the transaction
                transactionResult = transactionServerProxy.closeTransaction(transactionID);
                
                // if the result states that the transaction was aborted, restart it
                
                // otherwise, we're done
            } catch (IOException ex)
            {
                Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }
    
    /**
     * Get a random number between a given minimum and maximum value
     * @param min
     * @param max
     * @return random integer
     */
    public static int getRandomNumber(int min, int max)
    {
        return (int) (Math.random() * (max - min));
    }
    
    public static void main(String args[]) throws IOException
    {
        Properties prop = getPropertyInfo("src/main/java/utils/config/TransactionClient.properties");
        int numberTransactions = Integer.parseInt(prop.getProperty("NUMBER_TRANSACTIONS"));
        
        // create a number of transaction threads equivalent to the number
        // of total transaction
        for(int index = 0; index < numberTransactions; index++)
        {
            new TransactionThread().start();
        }
    }
    
}
