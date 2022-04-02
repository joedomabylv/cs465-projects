package server.transaction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.account.AccountManager;
import static utils.config.PropertiesHandler.getPropertyInfo;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionServer {
    
    String serverHost;
    int serverPort;
    int numberAccounts;
    int initialBalance;
    TransactionManager transactionManager = null;
    AccountManager accountManager = null;
    private ServerSocket server = null;
    
    /**
     * Construct a transaction server listening on the given port
     * @param serverHost
     * @param serverPort 
     * @param numberAccounts 
     * @param initialBalance 
     */
    public TransactionServer(String serverHost, int serverPort, int numberAccounts, int initialBalance) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.numberAccounts = numberAccounts;
        this.initialBalance = initialBalance;
        
        // create all managers
        transactionManager = new TransactionManager();
        accountManager = new AccountManager(numberAccounts, initialBalance);
        
        // open the server socket
        try {
            server = new ServerSocket(serverPort);
            System.out.println("[+] ServerSocket created");
        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runServerLoop() throws IOException {
        while(true)
        {
            // listen for a transaction from the client
            Socket client = server.accept();
            
            // inform the transaction manager that a transaction is about
            // to occur
            transactionManager.runTransaction(client);
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        // gather variables
        Properties prop = getPropertyInfo("src/main/java/utils/config/TransactionServer.properties");
        String serverHost = prop.getProperty("HOST");
        int serverPort = Integer.parseInt(prop.getProperty("PORT"));
        int numberAccounts = Integer.parseInt(prop.getProperty("NUMBER_ACCOUNTS"));
        int initialBalance = Integer.parseInt(prop.getProperty("INITIAL_BALANCE"));
        
        // create the transaction server and run the server loop
        new TransactionServer(serverHost,
                serverPort,
                numberAccounts,
                initialBalance).runServerLoop();
        
    }
    
}
