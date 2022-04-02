package client;

import comm.Message;
import static comm.MessageTypes.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static utils.config.PropertiesHandler.getPropertyInfo;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionServerProxy {
    
    // declare variables
    String host;
    int port;
    int numberAccounts;
    int initialBalance;
    Socket serverConnection;
    ObjectOutputStream toServer;
    ObjectInputStream fromServer;
    Message messageToServer;
    int transactionID;
    int receivedResult;
    
    /**
     * Constructor
     * 
     * Construct a server proxy for client use
     * @throws java.io.IOException
     */
    public TransactionServerProxy() throws IOException {
        Properties prop = getPropertyInfo("src/main/java/utils/config/TransactionServer.properties");
        this.host = getHost(prop);
        this.port = getPort(prop);
        
        // create a connection to the server
        serverConnection = new Socket(host, port);
        
        // establish IO streams
        fromServer = new ObjectInputStream(serverConnection.getInputStream());
        toServer = new ObjectOutputStream(serverConnection.getOutputStream());
    }
    
    /**
     * Get the configured host from the server properties file
     * @param prop
     * @return host
     */
    private String getHost(Properties prop) {
        return prop.getProperty("HOST");
    }
    
    /**
     * Get the configured port from the server properties file
     * @param prop
     * @return port
     */
    private int getPort(Properties prop) {
        return Integer.parseInt(prop.getProperty("PORT"));
    }
    
    public int openTransaction() {
        try
        {   
            
            // create a message of type OPEN_TRANSACTION
            // NOTE: probably going to want to send account information as content
            messageToServer = new Message(OPEN_TRANSACTION, null);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // wait for the server response
            this.transactionID = (int) fromServer.readObject();
            
            // send the received transaction ID back to the client
            return transactionID;
            
        } catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(TransactionServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // something went wrong, return failure
        return -999;
        
    }
    
    /**
     * Close a transaction and determine the result
     * @param transactionID ID of transaction
     * @return commit or abort
     */
    public int closeTransaction(int transactionID)
    {
        try
        {
            // create a message of type CLOSE_TRANSACTION
            messageToServer = new Message(CLOSE_TRANSACTION, null);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // wait for the server response
            receivedResult = (int) fromServer.readObject();
            
            serverConnection.close();
            fromServer.close();
            toServer.close();

            // send the received transaction ID back to the client
            return receivedResult;    
        } catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(TransactionServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // something went wrong, return result
        return -999;
    }
    
    /**
     * Create a message of type READ_REQUEST and send it to the server
     * @param accountID
     * @return account balance
     */
    public int read(int accountID)
    {
        
        int readResult;
        
        try
        {
            
            // create a message of type READ_REQUEST
            messageToServer = new Message(READ_REQUEST, accountID);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            System.out.println("[*] Transaction #" + this.transactionID + " [TransactionServerProxy] READ_REQUEST >>> account #" + accountID);
            
            readResult = (int) fromServer.readObject();
            System.out.println("[*] Transaction #" + this.transactionID + " [TransactionServerProxy] READ_REQUEST <<< account #" + accountID + ", balance $" + readResult);
            
            // return the result to the client
            return readResult;
            
        } catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(TransactionServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // something went wrong, return result
        return -999;
    }
    
    /**
     * Create a message of type WRITE_REQUEST and send it to the server
     * @param accountID
     * @param amount
     */
    public void write(int accountID, int amount)
    {
        try
        {  
            int[] messageContent = {accountID, amount};
            
            // create a message of type WRITE_REQUEST
            messageToServer = new Message(WRITE_REQUEST, messageContent);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // maybe should have the server ping back a successful reception?

            
        } catch (IOException ex)
        {
            Logger.getLogger(TransactionServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
