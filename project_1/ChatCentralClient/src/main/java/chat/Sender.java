package chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

/**
 * Sender thread that sends all user input to the server
 * @author Joe
 */
public class Sender extends Thread {
    
    // declare variables
    Socket serverConnection = null;
    DataOutputStream toServer = null;
    NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    
    // constructor
    public Sender(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
    
    @Override
    public void run() {
        
        // declare variables
        Boolean isJoined = false;
        String userInput = null;
        Message newMessage = null;
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        Boolean running = true;
        
        // display controls
        System.out.println("Type \'JOIN\' to join the chat\n"
                         + "Type \'LEAVE\' to leave the chat\n"
                         + "Type \'SHUTDOWN\' to shutdown your connection\n"
                         + "Otherwise, start chatting!");

        // run sender client
        while(running)
        {

            // get user input
            userInput = scanner.nextLine();
            
            // determine what to do with the input
            if(userInput.equals("JOIN") && !isJoined)
            {
                System.out.println("Sending JOIN message!");
                newMessage = new Message(Message.JOIN, nodeInfo);
                isJoined = true;
            }
            else if(userInput.equals("LEAVE") && isJoined)
            {
                System.out.println("Sending LEAVE message!");
                newMessage = new Message(Message.LEAVE, nodeInfo);
                isJoined = false;
            }
            else if(userInput.equals("SHUTDOWN"))
            {
                System.out.println("Sending SHUTDOWN message!");
                newMessage = new Message(Message.SHUTDOWN, nodeInfo);
                running = false;
            }
            else if(isJoined)
            {
                newMessage = new Message(Message.SEND_NOTE, userInput);
            }

            // try to connect to the server, establish IO streams, and send message
            try {
                // establish connection to server
                System.out.println("Creating connection to " + nodeInfo.serverIP + ":" + nodeInfo.serverPort + "...");
                serverConnection = new Socket(nodeInfo.serverIP, 
                        nodeInfo.serverPort);
                
                // create IO streams
                System.out.println("Creating IO streams...");
                objectInputStream = new ObjectInputStream(serverConnection.getInputStream());
                objectOutputStream = new ObjectOutputStream(serverConnection.getOutputStream());
                
                // write the object to the output stream
                System.out.println("Writing object...");
                objectOutputStream.writeObject(newMessage);

                // close connection
                System.out.println("Closing connection...");
                serverConnection.close();
                
                // display success
                System.out.println("Success!");
                
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, 
                        null, ex);
            }
         
        }

    }
    
}
