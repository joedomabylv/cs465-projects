package chat;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

// check out inet address for the video
// warning: only works if youre on the same network

public class Sender extends Thread {
    
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
            
            if(userInput.equals("JOIN") && !isJoined)
            {
                newMessage = new Message(Message.JOIN, this.nodeInfo);
                isJoined = true;
            }
            else if(userInput.equals("LEAVE") && isJoined)
            {
                newMessage = new Message(Message.LEAVE, nodeInfo);
                isJoined = false;
            }
            else if(userInput.equals("SHUTDOWN"))
            {
                newMessage = new Message(Message.SHUTDOWN, nodeInfo);
                running = false;
            }
            else if(isJoined)
            {
                newMessage = new Message(Message.SEND_NOTE, userInput);
            }

            // try to connect to the server, establish IO streams, and send message
            try {
                serverConnection = new Socket(nodeInfo.serverIP, 
                        nodeInfo.serverPort);
                
                objectOutputStream = new ObjectOutputStream(serverConnection.getOutputStream());
                objectInputStream = new ObjectInputStream(serverConnection.getInputStream());
                
                // try to write the object to the output stream
                objectOutputStream.writeObject(newMessage);

                serverConnection.close();
                
            } catch (IOException ex) {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, 
                        null, ex);
            }
         
        }

    }
    
}
