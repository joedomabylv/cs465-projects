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
import server.account.AccountManager;
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
    int transactionNumber;
    int mostRecentCommittedTransactionID;
    int[] writeContent;
    int amount;
    
    public TransactionManagerWorker(Socket client, int transactionNumber) throws SocketException {
        this.client = client;
        this.transactionNumber = transactionNumber;
        
        // try to open IO streams
        try
        {
            // establish IO streams
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
                    case OPEN_TRANSACTION ->
                    {
                        synchronized (transactionManager.activeTransactions)
                        {
                            // get the ID of the last comitted transaction,
                            // necessary for creating a new transaction object
                            mostRecentCommittedTransactionID = transactionManager.getMostRecentComittedTransactionID();

                            // create a transaction object and give the transaction
                            // object a new ID and the ID of the most recently
                            // committed transaction
                            transaction = new Transaction(transactionNumber,
                                    mostRecentCommittedTransactionID);


                            // store it in the active transactions list
                            transactionManager.addActiveTransaction(transaction);
                        }
                        System.out.println("[*] Transaction #" + transaction.getTransactionID() + " [TransactionManagerWorker.run] OPEN_TRANSACTION " + transaction.getTransactionID());

                        // send the transaction ID back to the proxy
                        toClient.writeObject(transaction.getTransactionID());
                    }
                    case CLOSE_TRANSACTION ->
                    {
                        // VALIDATE HERE, I DONT KNOW HOW
                        
                        // if success, we can commit: writeTransaction()
                        // NOTE: not sure if a commit constitutes a writeTransaction call,
                        // that's just what is says in his notes
                        
                        // remove the transaction from active transactions list
                        
                        // send the result back to the client
                        
                        // close the connection
                        toClient.close();
                        fromClient.close();
                        client.close();
                        transactionOpen = false;
                    }
                    case READ_REQUEST ->
                    {
                        // get the transaction ID from the message
                        accountID = (int) messageReceived.getContent();
                        
                        // get the account balance from the given account ID
                        accountBalance = AccountManager.read(accountID);
                        
                        // send the result back to the proxy
                        toClient.writeObject(accountBalance);
                    }
                    case WRITE_REQUEST ->
                    {
                        // get the transaction ID from the message
                        writeContent = (int[]) messageReceived.getContent();
                        
                        accountID = writeContent[0];
                        amount = writeContent[1];
                        
                        // write the given balance to the account
                        AccountManager.write(accountID, amount);
                        System.out.println("[*] Transaction #" + this.transaction.getTransactionID() + " [TransactionManagerWorker.run] WRITE_REQUEST >>> account #" + accountID + ", new balance $" + amount);
                        
                        // send the result back to the proxy?
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
