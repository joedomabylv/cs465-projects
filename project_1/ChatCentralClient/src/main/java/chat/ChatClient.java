package chat;

// java imports
import java.io.*;
import java.util.*;

// custom imports
import static utils.ServerPropHandler.getServerInfo;
import message.Message;

public class ChatClient {
    
    // declare variables
    NodeInfo nodeInfo;
    Message message;
    private static final Scanner scanner = new Scanner(System.in);
    
    // constructor
    public ChatClient(String serverIP, String name, int serverPort) {
        this.nodeInfo = new NodeInfo(serverIP, name, serverPort);
        this.message = new Message(null, null);
    }

    public static void main(String[] args) throws IOException {

        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

    }

}
