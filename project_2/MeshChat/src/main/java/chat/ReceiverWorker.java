package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MessageType.*;

/**
 * Worker class for peer receiver
 * @author Joe Domabyl V
 */
public class ReceiverWorker extends Thread {
    Socket peerConnection = null;
    ObjectInputStream fromPeers = null;
    ObjectOutputStream toPeers = null;
    Message message = null;
    int messageType = -1;
    Object messageContent = null;
    NodeInfo messageNodeInfo = null;
    ArrayList<NodeInfo> messageList = new ArrayList<>();
    
    public ReceiverWorker(Socket peerConnection) {
        this.peerConnection = peerConnection;
    }
    
    @Override
    public void run() {
        try
        {
            // establish IO streams
            fromPeers = new ObjectInputStream(
                    peerConnection.getInputStream());
            toPeers = new ObjectOutputStream(
                    peerConnection.getOutputStream());
            
            // read the object sent by the server
            message = (Message) fromPeers.readObject();
            
            // read the message type and react accordingly
            messageType = message.getType();
            messageContent = message.getContent();
            
            switch(messageType)
            {
                case JOIN:
                    // a peer wants to join, send them some nodeInfo
                    
                    // check if the JOINING participant is already in the list
                    // NOTE: this should never happen, but just in case
                    
                    // cast content to NodeInfo
                    messageNodeInfo = (NodeInfo) messageContent;
                    
                    if(!MeshClient.participants.contains(messageNodeInfo))
                    {
                        // add the new user to the list
                        MeshClient.participants.add(messageNodeInfo);
                        
                        // send the sender back this participants list
                        message = new Message(SEND_LIST, MeshClient.participants);
                        new SenderWorker(new Socket(messageNodeInfo.peerIP,
                                                    messageNodeInfo.peerPort),
                                                    message).start();
                        
                    }
                    else
                    {
                        System.out.println("This participant is already in "
                                + "the list!");
                    }
                    break;
                case LEAVE:
                    // a peer wants to leave, remove them from this participants
                    // list
                    
                    // cast content to NodeInfo
                    messageNodeInfo = (NodeInfo) messageContent;
                        
                    // find the leaving participant in the list based on
                    // their name
                    // NOTE: using iterator due to ArrayList limitations
                    for(Iterator<NodeInfo> iterator = MeshClient.participants.iterator(); iterator.hasNext(); )
                    {
                        String name = iterator.next().name;
                        if(name.equals(messageNodeInfo.name))
                        {
                            iterator.remove();
                        }
                    }
                    break;
                case SHUTDOWN:
                    // a peer is shutting down, shut down self
                    
                    // get the nodeinfo from the message
                    messageNodeInfo = (NodeInfo) messageContent;
                    
                    // display to the user who sent the SHUTDOWN request
                    System.out.println(messageNodeInfo.name + " sent a SHUTDOWN "
                            + "request! Goodbye!");
                    
                    // exit the system
                    System.exit(0);
                case SEND_LIST:
                    // a peer sent a list of participants, take it
                    messageList = (ArrayList<NodeInfo>) messageContent;
                    MeshClient.participants = messageList;
                    break;
                default:
                    // a peer is sending a note, display it
                    System.out.println(messageContent);
                    break;
            }

            // close the connection
            peerConnection.close();
            
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(ReceiverWorker.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }
}
