package client;

import java.io.IOException;
import java.util.Properties;
import static utils.config.PropertiesHandler.getPropertyInfo;

/**
 * Transaction Client
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionClient {
    
    
    public static void main(String args[]) throws IOException
    {
        Properties prop = getPropertyInfo("src/main/java/utils/config/TransactionClient.properties");
        int numberTransactions = Integer.parseInt(prop.getProperty("NUMBER_TRANSACTIONS"));
        
        for(int index = 0; index < numberTransactions; index++)
        {
            new TransactionServerProxy().start();
        }
                                                                            
    }
    
}
