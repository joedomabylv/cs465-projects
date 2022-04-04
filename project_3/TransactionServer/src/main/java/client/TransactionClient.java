package client;

import static comm.MessageTypes.*;
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
        int transactionResult = TRANSACTION_ABORTED;
        private TransactionServerProxy transactionServerProxy;
        int firstAccountID;
        int secondAccountID;
        int firstAccountBalance;
        int secondAccountBalance;
        int firstAccountWriteSetResult;
        int secondAccountWriteSetResult;
        int transferAmount;
        int previousTransactionID = -1;
        
        // empty contstructor
        public TransactionThread(){};

        @Override
        public void run() {
            
            // choose two random account IDs for a transaction
            // NOTE: need to set the upper bound to the total amount
            // of accounts
            firstAccountID = getRandomNumber(0, 10);
            secondAccountID = getRandomNumber(0, 10);
            // ensure the same account isn't selected
            while(secondAccountID == firstAccountID)
            {
                secondAccountID = getRandomNumber(0, 10);
            }

            // create a random amount to transfer between the accounts
            // NOTE: maximum size of transaction amount is arbitraily set
            // to 10
            transferAmount = getRandomNumber(0, 10);
            
            while(transactionResult == TRANSACTION_ABORTED)
            {
                try
                {
                    // create a transaction server proxy
                    transactionServerProxy = new TransactionServerProxy();

                    // open the transaction and wait for a transaction ID
                    transactionID = transactionServerProxy.openTransaction();

                    // check if this transaction is being restarted after an
                    // abortion
                    if(previousTransactionID == -1)
                    {
                        // fresh transaction
                        System.out.println("[*] Transaction #" + transactionID + " started, transfer $" + transferAmount + ": " + firstAccountID + "->" + secondAccountID);
                    }
                    else
                    {
                        // restarting after abortion
                        System.out.println("[*] Transaction #" + previousTransactionID + " restarted as transaction #" + transactionID + ", transfer $" + transferAmount + ": " + firstAccountID + "->" + secondAccountID);
                    }

                    // read/write to/from the first account
                    firstAccountBalance = transactionServerProxy.read(firstAccountID);
                    firstAccountWriteSetResult = transactionServerProxy.write(firstAccountID, firstAccountBalance - transferAmount);

                    // read/write to/from the second account
                    secondAccountBalance = transactionServerProxy.read(secondAccountID);
                    secondAccountWriteSetResult = transactionServerProxy.write(secondAccountID, secondAccountBalance + transferAmount);

                    // close the transaction
                    transactionResult = transactionServerProxy.closeTransaction(transactionID);

                    // if the result states that the transaction was aborted, restart it
                    switch(transactionResult)
                    {
                        case TRANSACTION_COMMITTED ->
                        {
                            System.out.println("[+] Transaction #" + transactionID + " COMMITTED!");
                        }
                        case TRANSACTION_ABORTED ->
                        {
                            System.out.println("\t[-] Transaction #" + transactionID + " ABORTED!");
                            previousTransactionID = transactionID;
                        }
                    }
                } catch (IOException ex)
                {
                    Logger.getLogger(TransactionClient.class.getName()).log(Level.SEVERE, null, ex);
                }
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
