package utils.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Handle properties files
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class PropertiesHandler {
    public static Properties getPropertyInfo(String fileName) throws IOException {
        FileInputStream inputStream = null;
        Properties prop = null;
        
        try
        {
            inputStream = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(inputStream);
        }
        catch(FileNotFoundException notFoundExc)
        {
            System.out.println("Properties at " + fileName + " could not be "
                    + "found, exiting.\n" + notFoundExc);
        }
        catch(IOException ioExc)
        {
            System.out.println("IO Exception at " + fileName + ", exiting.\n"
                    + ioExc);
        }
        finally
        {
            inputStream.close();
        }
        return prop;
    }
}
