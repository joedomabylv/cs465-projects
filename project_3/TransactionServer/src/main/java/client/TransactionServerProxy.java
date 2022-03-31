package client;

import java.io.IOException;
import java.util.Properties;
import static utils.config.PropertiesHandler.getPropertyInfo;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionServerProxy extends Thread {
    
    // declare variables
    String host;
    int port;
    int numberAccounts;
    int initialBalance;
    
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
        
        // create a transaction
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
    
    @Override
    public void run() {
        // connect to the actual server
        
    }
    
}
