package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MessageType.*;
import static utils.PropHandler.getServerInfo;

/**
 *
 * @author Joe Domabyl V
 */
public class Sender extends Thread {
    
    // declare variables
    Boolean isJoined = false;
    Socket peerConnection = null;
    NodeInfo nodeInfo = null;
    String peerIP = null;
    int peerPort = -1;
    private static final Scanner scanner = new Scanner(System.in);

    // constructor
    public Sender(NodeInfo nodeInfo) {
        this.nodeInfo = nodeInfo;
    }
    
    @Override
    public void run() {
        
        // declare variables
        String userInput;
        Message newMessage;
        ObjectOutputStream toPeers;
        ObjectInputStream fromPeers;
        
        // display controls
        
        System.out.println("Type \'JOIN\' to join the chat\n"
                          + "Type \'LEAVE\' to leave the chat\n"
                          + "Type \'SHUTDOWN\' to shut the entire system down\n"
                          + "Otherwise, start chatting!");
        
        // run the client's sender
        while(MeshClient.running)
        {

            userInput = scanner.nextLine();
            
            // ensure the message begins as a null object to avoid previous
            // information being sent twice
            newMessage = null;
            
            switch(userInput)
            {
                case "JOIN":
                    // check if the user is already joined
                    if(!isJoined)
                    {
                        // user is not JOINed, they may JOIN
                        newMessage = new Message(JOIN, nodeInfo);
                        isJoined = true;
                    }
                    else
                    {
                        System.out.println("You\'ve already joined!");
                    }
                    break;
                case "LEAVE":
                    // check if the user is JOINed, thus are eligible to LEAVE
                    if(isJoined)
                    {
                        // user is JOINed, they may leave
                        newMessage = new Message(LEAVE, nodeInfo);
                        isJoined = false;
                        
                        // clear this client's participants list
                        MeshClient.participants = new ArrayList<>();
                    }
                    else
                    {
                        System.out.println("You cannot leave a chat session "
                                         + "that you haven\'t JOINed!");
                    }
                    break;
                case "SHUTDOWN":
                    if(isJoined)
                    {
                        newMessage = new Message(SHUTDOWN, nodeInfo);
                        MeshClient.running = false;
                    }
                    else
                    {
                        System.out.println("You can\'t SHUTDOWN a chat that you "
                                + "haven\'t joined!");
                    }
                    break;
                // case for SEND_NOTE
                default:
                    // check if the user is JOINed, thus are eligible to SEND_NOTE
                    if(isJoined)
                    {
                        // prepend sender name to note
                        userInput = this.nodeInfo.name + ": " + userInput;
                        newMessage = new Message(SEND_NOTE, userInput);
                    }
                    else
                    {
                        System.out.println("Please JOIN the chat before "
                                         + "trying to chat!");
                    }
                    break;
            }
            
            // try to connect to the server, establish IO streams, and send message
            try {
                // check if the message is null
                if(newMessage != null)
                {
                    // message is not null, communicate with peers
                 
                    // check if the arraylist of participants only contains self,
                    // thus we need to assume that this peer "just knows"
                    // the IP/port of one other peer
                    // NOTE: in essence, this will only be used to JOIN before
                    //       the application is "warmed up"

                    if(MeshClient.participants.size() == 1)
                    {
                        // get the IP and port of the Peer that we're assuming
                        // we already know
                        Properties prop = getServerInfo("config/peer.properties");
                        peerIP = prop.getProperty("PEER_KNOWN_IP");       
                        peerPort = Integer.parseInt(prop.getProperty("PEER_KNOWN_PORT"));

                        new SenderWorker(new Socket(peerIP, peerPort),
                                                    newMessage).start();
                    }
                    else
                    {
                        // otherwise, we need to send the message to all
                        // participating peers

                        // loop through each participant in the list and 
                        // create a new thread
                        for (NodeInfo participant: MeshClient.participants)
                        {
                            new SenderWorker(new Socket(participant.peerIP,
                                                 participant.peerPort),
                                             newMessage).start();
                        }
                    }
                }
            } 
            catch (IOException ex)
            {
                Logger.getLogger(Sender.class.getName()).log(Level.SEVERE, 
                        null, ex);
            }

        }
        
    }
}
