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
    int receivedTransactionID;
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
            // establish IO streams
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            
            // create a message of type OPEN_TRANSACTION
            // NOTE: probably going to want to send account information as content
            messageToServer = new Message(OPEN_TRANSACTION, null);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // wait for the server response
            receivedTransactionID = (int) fromServer.readObject();
            
            // close IO streams
            fromServer.close();
            toServer.close();
            
            // send the received transaction ID back to the client
            return receivedTransactionID;
            
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
            // establish IO streams
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            
            // create a message of type CLOSE_TRANSACTION
            messageToServer = new Message(CLOSE_TRANSACTION, null);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // wait for the server response
            receivedResult = (int) fromServer.readObject();
            
            // close IO streams
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
     * @param transactionID
     */
    public int read(int transactionID)
    {
        
        int readResult;
        
        try
        {
            // establish IO streams
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            
            // create a message of type READ_REQUEST
            messageToServer = new Message(READ_REQUEST, transactionID);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            readResult = (int) fromServer.readObject();
            
            // close IO streams
            fromServer.close();
            toServer.close();
            
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
     * @param transactionID
     */
    public void write(int transactionID)
    {
        try
        {
            // establish IO streams
            fromServer = new ObjectInputStream(serverConnection.getInputStream());
            toServer = new ObjectOutputStream(serverConnection.getOutputStream());
            
            // create a message of type WRITE_REQUEST
            messageToServer = new Message(WRITE_REQUEST, transactionID);
            
            // send the message to the server
            toServer.writeObject(messageToServer);
            
            // maybe should have the server ping back a successful reception?
            
            // close IO streams
            fromServer.close();
            toServer.close();
            
        } catch (IOException ex)
        {
            Logger.getLogger(TransactionServerProxy.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
