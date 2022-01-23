package chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MessageType.*;

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
        String userInput;
        Message newMessage;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
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
            
            // ensure the message begins as a null object to avoid
            // previous information being sent twice
            newMessage = null;
            
            // determine what to do with the input
            switch (userInput) {
                case "JOIN":
                    // check if the user is already JOINed
                    if(!isJoined)
                    {
                        // user is not JOINed, they may JOIN
                        newMessage = new Message(JOIN, nodeInfo);
                        isJoined = true;
                    }
                    else
                    {
                        System.out.println("You are already JOINed!");
                    }
                    break;
                case "LEAVE":
                    // check if user is JOINed, thus are eligible to LEAVE
                    if(isJoined)
                    {
                        // user is JOINed, they may leave
                        newMessage = new Message(LEAVE, nodeInfo);
                        isJoined = false;
                    }
                    else
                    {
                        System.out.println("You cannot leave a chat session "
                                + "that you haven\'t JOINed!");
                    }
                    break;
                case "SHUTDOWN":
                    newMessage = new Message(SHUTDOWN, nodeInfo);
                    running = false;
                    break;
                default:
                    // check if the user is JOINed, thus are eligible to SEND_NOTE
                    if(isJoined)
                    {
                        // user is JOINed, create new message
                        newMessage = new Message(SEND_NOTE, userInput);
                    }
                    else
                    {
                        System.out.println("Please JOIN the chat session before "
                            + "trying to chat!");
                    }
                    break;
            }

            // try to connect to the server, establish IO streams, and send message
            try {
                // check if the message is null
                if(newMessage != null)
                {
                    // message is not null, communicate with server
                    
                    // establish connection to server
                    serverConnection = new Socket(nodeInfo.serverIP, 
                            nodeInfo.serverPort);

                    // create IO streams
                    objectInputStream = new ObjectInputStream(serverConnection.getInputStream());
                    objectOutputStream = new ObjectOutputStream(serverConnection.getOutputStream());

                    // write the object to the output stream
                    objectOutputStream.writeObject(newMessage);

                    // close connection
                    serverConnection.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, 
                        null, ex);
            }
         
        }

    }
    
}
