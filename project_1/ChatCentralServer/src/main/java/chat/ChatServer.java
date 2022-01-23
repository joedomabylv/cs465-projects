package chat;

import java.io.*;
import java.net.ServerSocket;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static utils.ServerPropHandler.getServerInfo;

class ChatServer {

    String serverIP;
    int serverPort;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();
    
    private ServerSocket server = null;
    
    public ChatServer(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        
        // open the server socket
        try {
            server = new ServerSocket(serverPort);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void runServerLoop() throws IOException {
        while(true)
        {
            new ChatServerWorker(server.accept()).start();
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));
       
        // run the server
        new ChatServer(serverIP, serverPort).runServerLoop();

    }
    
}
