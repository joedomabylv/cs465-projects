package server;

import java.io.*;
import java.util.*;

import static utils.ServerPropHandler.getServerInfo;
import message.Message;

class ChatServer implements Runnable {

    String serverIP;
    int serverPort;
    
    public ChatServer(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }
    
    @Override
    public void run() {
        
    }
    
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, I am the server!");
        
        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

    }
    
}
