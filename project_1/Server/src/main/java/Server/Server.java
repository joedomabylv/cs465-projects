package Server;

import java.io.*;
import java.util.*;

import static Server.utils.ServerPropHandler.getServerInfo;
import Server.utils.Note;

class Server {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello, I am the server!");
        
        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

        System.out.println("Attempting to connect to " + serverIP + ":" + serverPort);
    }
}
