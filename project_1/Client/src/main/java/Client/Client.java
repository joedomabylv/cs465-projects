package Client;

// java imports
import java.io.*;
import java.util.*;

// custom imports
import static Client.utils.ServerPropHandler.getServerInfo;
import Client.utils.Message;

public class Client {
    
    // declare variables
    NodeInfo nodeInfo;
    Message message;
    private static final Scanner scanner = new Scanner(System.in);
    
    // private nodeInfo class
    private class NodeInfo {
        String serverIP;
        String name;
        int serverPort;
        
        // constructor
        NodeInfo(String serverIP, String name, int serverPort) {
            this.serverIP = serverIP;
            this.name = name;
            this.serverPort = serverPort;
        }
    }
    
    // constructor
    public Client(String serverIP, String name, int serverPort) {
        this.nodeInfo = new NodeInfo(serverIP, name, serverPort);
        this.message = new Message(null, null);
    }

    // dispatch the message to the server
    private static void dispatchMessage(Message message) {
        
        // declare variables
        Boolean shutdown = false;
        
        // connect to server
        
        switch (message.getType()) {
            case Message.JOIN:
                System.out.println("Join!");
                break;
            case Message.LEAVE:
                System.out.println("Leave!");
                break;
            case Message.SEND_NOTE:
                System.out.println("Send note!");
                break;
            case Message.SHUTDOWN:
                System.out.println("Shutdown!");
                shutdown = true;
                break;
        }
        
        // close connection to server
        
        // check if client wanted to shutdown
        if(shutdown){
            System.exit(0);
        }
    }

    private static void runChat(Client client) {
        
        // declare variables
        String inputString;
        
        // welcome the user
        System.out.println("Welcome to chat, " + client.nodeInfo.name + "! What would you like to do?\n");
        
        // run the chat loop
        while (true) {
            System.out.println("1. Join chat");
            System.out.println("2. Leave chat");
            System.out.println("3. Send note");
            System.out.println("4. Shutdown");

            // take input from user, stdin
            inputString = scanner.nextLine();

            // if the user input a valid message, accept
            if (client.message.isValidType(inputString)) {
                
                // set the clients message type
                client.message.setType(inputString);
                
                // dispatch the message to the server, as a thread?
                dispatchMessage(client.message);
                
                // user input incorrect message, try again
            } else {
                System.out.println("Please provide valid input, goofball");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));
        
        // get the users logical name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();

        runChat(new Client(serverIP, name, serverPort));

    }

}
