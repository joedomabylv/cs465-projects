package client;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        // empty contstructor
        public TransactionThread(){};

        @Override
        public void run() {
            
            try
            {
                // create a transaction server proxy
                transactionServerProxy = new TransactionServerProxy();
                
                // NOTE: i don't really know if we can access static variables from
                // different threads in the way i think it works, this might
                // prove problematic in the future uh oh!
                transactionID = transactionServerProxy.openTransaction();

                // read from the accounts attached to the transaction ID
                // NOTE: not sure if we're supposed to be receiving two
                // balances back from the single read call? maybe should
                // determine accounts based on transaction ID then call
                // read twice
                transactionServerProxy.read(transactionID);
                
                // write the changes to each account
                
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
