package Server.utils;

import java.io.*;
import java.util.*;

public class ServerPropHandler {
    public static Properties getServerInfo(String fileName) throws IOException {
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
