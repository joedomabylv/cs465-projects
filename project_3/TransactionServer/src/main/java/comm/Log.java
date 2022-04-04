package comm;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Write a log file at the end of transaction execution
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class Log {
    
    ArrayList<String> finalLogList;
    
    public Log()
    {
        this.finalLogList = new ArrayList<>();
    }
    
    public void addString(String logString)
    {
        this.finalLogList.add(logString);
    }
    
    /**
     * Create and log a string to a file
     * @throws IOException 
     */
    public void logToFile() throws IOException
    {
        try (PrintWriter fileWriter = new PrintWriter("log.txt", "UTF-8"))
        {
            int index = 0;
            while (index < finalLogList.size())
            {
                fileWriter.println(finalLogList.get(index));
                index++;
            }
//        Logger logger = Logger.getLogger("log");
//        
//        FileHandler fileHandler = new FileHandler("log.txt");
//        
//        logger.addHandler(fileHandler);
//        
//        SimpleFormatter formatter = new SimpleFormatter();
//        
//        fileHandler.setFormatter(formatter);
//        
//        logger.setUseParentHandlers(false);
//        
//        int index = 0;
//        while(index < finalLogList.size())
//        {
//            logger.info(finalLogList.get(index));
//            index++;
//        }
        }
        }
    }

