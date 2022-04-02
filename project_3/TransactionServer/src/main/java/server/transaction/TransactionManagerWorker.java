package server.transaction;

import comm.Message;
import static comm.MessageTypes.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    int transactionID;
    
    public TransactionManagerWorker(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        // create a new transaction
        System.out.println("[!] TransactionManagerWorker called");
        
        try {
            // establish IO streams
            toClient = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
            
            // read the sent object
            messageReceived = (Message) fromClient.readObject();
        
            // determine the type of the object
            messageType = messageReceived.getMessageType();
            
            // start the loop such that the worker remains listening until
            // the transaction is to be closed
            while(messageType != CLOSE_TRANSACTION)
            {
                switch(messageType) {
                    case OPEN_TRANSACTION ->
                    {
                        System.out.println("[!] TransactionManagerWorker received a message of type OPEN_TRANSACTION");
                        // create a transaction object

                        // give the transaction object a new ID and the ID of the
                        // most recently committed transaction

                        // store it in the active transactions list

                        // send the transaction ID back to the proxy
                    }
                    case CLOSE_TRANSACTION ->
                    {
                        System.out.println("[!] TransactionManagerWorker received a message of type CLOSE_TRANSACTION");
                        // VALIDATE HERE, I DONT KNOW HOW
                        
                        // if success, we can commit: writeTransaction()
                        // NOTE: not sure if a commit constitutes a writeTransaction call,
                        // that's just what is says in his notes
                        
                        // remove the transaction from active transactions list
                        
                        // send the result back to the client
                        
                        // close the connection
                    }
                    case READ_REQUEST ->
                    {
                        System.out.println("[!] TransactionManagerWorker received a message of type READ_REQUEST");
                        // get the transaction ID from the message
                        transactionID = (int) messageReceived.getContent();
                        
                        // get the account numbers associated with the transaction
                        
                        // call read on the transaction object
                        
                        // send the result back to the proxy
                    }
                    case WRITE_REQUEST ->
                    {
                        System.out.println("[!] TransactionManagerWorker received a message of type WRITE_REQUEST");
                        // get the transaction ID from the message
                        transactionID = (int) messageReceived.getContent();
                        
                        // get the account numbers associated with the transaction
                        
                        // call write on the transaction object
                        
                        // send the result back to the proxy
                    }
                }
                
                // wait for the next message
                messageReceived = (Message) fromClient.readObject();

                // determine the type of the message
                messageType = messageReceived.getMessageType();
            }
        
        } 
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(TransactionManagerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
