package server.transaction;

import comm.Message;
import static comm.MessageTypes.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.transaction.TransactionServer.transactionManager;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionManagerWorker extends Thread {
    
    Socket client = null;
    ObjectInputStream fromClient = null;
    ObjectOutputStream toClient = null;
    Message messageReceived = null;
    int messageType;
    Transaction transaction;
    int accountID;
    int accountBalance;
    int transactionID;
    int mostRecentCommittedTransactionNumber;
    int[] writeContent;
    int amount;
    int transactionResult;
    int writeSetResult;
    
    public TransactionManagerWorker(Socket client, int transactionID) throws SocketException {
        this.client = client;
        this.transactionID = transactionID;
        
        // try to open IO streams
        try
        {
            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
        } catch (IOException ex)
        {
            Logger.getLogger(TransactionManagerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        
        Boolean transactionOpen = true;
        
        try {
            // start the loop such that the worker remains listening until
            // the transaction is to be closed
            while(transactionOpen)
            {

                // read the sent object
                messageReceived = (Message) fromClient.readObject();

                // determine the type of the object
                messageType = messageReceived.getMessageType();
            
                switch(messageType) {
                    // OPEN_TRANSACTION request received
                    case OPEN_TRANSACTION ->
                    {
                        // ensure synchronization of activeTransactions list
                        synchronized (transactionManager.activeTransactions)
                        {   
                            // get the ID of the last comitted transaction,
                            // necessary for creating a new transaction object
                            mostRecentCommittedTransactionNumber = transactionManager.getMostRecentComittedTransactionNumber();

                            // create a transaction object and give the transaction
                            // object a new ID and the number of the most recently
                            // committed transaction
                            transaction = new Transaction(transactionID,
                                    mostRecentCommittedTransactionNumber);


                            // store it in the active transactions list
                            transactionManager.addActiveTransaction(transaction);
                        }
                        System.out.println("[*] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getTransactionID());

                        // send the transaction ID back to the proxy
                        toClient.writeObject(transaction.getTransactionID());
                    }
                    case CLOSE_TRANSACTION ->
                    {
                        synchronized (transactionManager.activeTransactions)
                        {
                            
                            transactionManager.activeTransactions.remove(transaction);
                            
                            // validate transaction
                            if(transactionManager.validateTransaction(transaction))
                            {
                                // validation successful, update transaction list
                                transactionManager.addCommittedTransaction(transaction);
                                
                                // validation successful, commence update phase
                                transactionManager.updateTransaction(transaction);
                                
                                System.out.println("[+] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getTransactionID() + " - COMMITTED");
                                
                                // return committed transaction result to client
                                toClient.writeObject(TRANSACTION_COMMITTED);
                            }
                            else
                            {
                                // transaction failed validation, abort it
                                transactionManager.addAbortedTransaction(transaction);
                                System.out.println("[+] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getTransactionID() + " - ABORTED");

                                toClient.writeObject(TRANSACTION_ABORTED);
                            }

                        }
                        
                        // regardless of validation result, close the connection
                        toClient.close();
                        fromClient.close();
                        client.close();
                        
                        // end worker loop
                        transactionOpen = false;
                        
                        // check if there are any active transactions left
//                        if(transactionManager.getActiveTransactions().isEmpty())
//                        {
//                            TransactionServer.serverRunning = false;
//                        }
                       
                    }
                    case READ_REQUEST ->
                    {
                        // get the transaction ID from the message
                        accountID = (int) messageReceived.getContent();
                        System.out.println("[*] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] READ_REQUEST >>> account #" + accountID);

                        
                        // get the account balance from the given account ID
                        accountBalance = transaction.read(accountID);
                        System.out.println("[*] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] READ_REQUEST <<< account #" + accountID + ", balance $" + accountBalance);

                        // send the result back to the proxy
                        toClient.writeObject(accountBalance);
                    }
                    case WRITE_REQUEST ->
                    {
                        // get the account ID and the amount from the message
                        writeContent = (int[]) messageReceived.getContent();
                        
                        accountID = writeContent[0];
                        amount = writeContent[1];
                        
                        // write the given balance to the account
                        writeSetResult = transaction.write(accountID, amount);
                        System.out.println("[*] Transaction #" + this.transaction.getTransactionID() + " [TransactionManagerWorker.run] WRITE_REQUEST >>> account #" + accountID + ", new balance $" + amount);
                        
                        // send the result back to the proxy
                        toClient.writeObject(writeSetResult);
                    }
                }
                
            }
        
        } 
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(TransactionManagerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
