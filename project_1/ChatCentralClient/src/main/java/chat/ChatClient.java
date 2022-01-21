package chat;

// java imports
import java.io.*;
import java.util.*;

// custom imports
import static utils.ServerPropHandler.getServerInfo;

public class ChatClient implements Runnable {
    
    // declare variables
    NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    
    // constructor
    public ChatClient(String serverIP, String name, int serverPort) {
        this.nodeInfo = new NodeInfo(serverIP, name, serverPort);
    }

    @Override
    public void run() {
        // start receiver
        (new Receiver(this.nodeInfo)).start();

        // start sender
        (new Sender(this.nodeInfo)).start();
    }

    public static void main(String[] args) throws IOException {

        // gather server information
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));
        
        // ask the user for their name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();
        
        // create client thread
        new ChatClient(serverIP, name, serverPort).run();

    }

}
