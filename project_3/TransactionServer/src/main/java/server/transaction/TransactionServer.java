package server.transaction;

import comm.Log;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    public static TransactionManager transactionManager = null;
    public static AccountManager accountManager = null;
    private ServerSocket server = null;
    public static Boolean serverRunning = true;
    private Boolean clientConnected = false;
    public static Log finalLog;
    public String logString;
    
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
        
        // create a new log for final execution
        finalLog = new Log();
        
        // create all managers
        transactionManager = new TransactionManager();
        accountManager = new AccountManager(numberAccounts, initialBalance);
        
        // open the server socket
        try {
            server = new ServerSocket(serverPort);
            logString = "[+] ServerSocket created";
            System.out.println(logString);
            updateLogList(logString);
        } catch (IOException ex) {
            Logger.getLogger(TransactionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runServerLoop() throws IOException {
        while(serverRunning)
        {
            try
            {
                // wait for 3 seconds
                server.setSoTimeout(3000);

                // listen for a transaction from the client
                Socket client = server.accept();

                // inform the transaction manager that a transaction is about
                // to occur
                transactionManager.runTransaction(client);
                
                clientConnected = true;
            } catch (SocketTimeoutException ex)
            {
                // check if the client has connected before shutting down
                if(clientConnected)
                {
                    logString = "[$$$] Socket closed! Shutting down.";
                    System.out.println(logString);
                    updateLogList(logString);
                    serverRunning = false;
                }
            }
        }
        
        logString = "Final branch total: " + accountManager.getBranchTotal();
        System.out.println(logString);
        updateLogList(logString);
        logToFile();
    }
    
    /**
     * Update the final log string for the end of execution
     * @param logString 
     */
    public static void updateLogList(String logString)
    {
        finalLog.addString(logString);
    }
    
    /**
     * After program execution, create final log file
     * @throws java.io.IOException
     */
    public void logToFile() throws IOException
    {
        finalLog.logToFile();
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
