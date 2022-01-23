package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;
import static message.MessageType.*;

/**
 * Server thread that takes care of all IO
 * @author Joe
 */
public class ChatServerWorker extends Thread {

    // declare variables
    Socket client = null;
    ObjectInputStream fromClient = null;
    ObjectOutputStream toClients = null;
    Message messageReceived = null;
    int messageType;
    
    // declare constants
    private final int NODE_NOT_FOUND = -9999999;
    
    // constructor
    public ChatServerWorker(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        
        // get the streams, cast the message, close the connection
        try {
            // establish IO streams
            toClients = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
            
            // read the object and cast it to type Message
            messageReceived = (Message) fromClient.readObject();
            
            // close connection
            client.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        // determine what to do with the message
        switch (messageReceived.getType()) {
            case JOIN: // user wants to JOIN
                
                // cast content to NodeInfo
                NodeInfo newNode = (NodeInfo) messageReceived.getContent();
                
                // add the node
                ChatServer.participants.add(newNode);
                
                // display to server console
                System.out.println(newNode.name + " has joined!");
                                
                break;
            case LEAVE: // user wants to LEAVE       
                
                // cast content to NodeInfo
                NodeInfo leaveNode = (NodeInfo) messageReceived.getContent();
                
                // attempt to remove the node
                removeNode(leaveNode);
                
                // display to server console
                System.out.println(leaveNode.name + " has left!");
                
                break;
            case SHUTDOWN: // user wants to SHUTDOWN
                
                // cast content to NodeInfo
                NodeInfo shutdownNode = (NodeInfo) messageReceived.getContent();
                
                // check if the node is in the list, i.e. it is considered
                // JOINed
                if(isInList(shutdownNode))
                {
                    // the node is in the list, remove it
                    removeNode(shutdownNode);
                }
                
                // NOTE: if the node is not found in the list, the node already
                // send a LEAVE request. do nothing.
                
                // display to server console
                System.out.println(shutdownNode.name + " has shutdown!");
     
                break;
                
            default: // user wants to SEND_NOTE
                System.out.println(messageReceived.getContent());
                
                // multiplex chat messages
                
                break;

        }

        // temp debug message
        String names = "Names: ";
        for(int i = 0; i < ChatServer.participants.size(); i++)
        {
            names += ChatServer.participants.get(i).name + " ";
        }
        System.out.println(names);
        
    }
    
    /**
     * Finds the index of a given node that is equivalent to the node passed
     * into the method
     * 
     * @param otherNodeInfo the node to be compared to
     * @return index of equivalent node or signal that the node was not found
     */
    private int getIndex(NodeInfo otherNodeInfo) {
        
        // declare variables
        int index;
        String currentIP;
        int currentPort;
        String currentName; // checking for name is primarily driven when
                            // testing only on localhost
        String otherIP = otherNodeInfo.serverIP;
        int otherPort = otherNodeInfo.serverPort;
        String otherName = otherNodeInfo.name;
        
        // loop through ArrayList
        for(index = 0; index < ChatServer.participants.size(); index++)
        {
            // get IP/port from current participant in ArrayList
            currentIP = ChatServer.participants.get(index).serverIP;
            currentPort = ChatServer.participants.get(index).serverPort;
            currentName = ChatServer.participants.get(index).name;
            
            // compare IP's and port numbers
            if(currentIP.equals(otherIP)
                   && currentPort == otherPort
                   && currentName.equals(otherName))
            {
                // return found index
                return index;
            }
        }
        
        // node wasn't found
        return NODE_NOT_FOUND;
    }
    
    /**
     * Attempts to remove a given node from the 'participants' ArrayList 
     * @param nodeToRemove node to be removed
     * @return Boolean success or failure
     */
    private Boolean removeNode(NodeInfo nodeToRemove) {
        // get the index of the node to be removed
        int removeIndex = getIndex(nodeToRemove);
        
        // check if the index was found
        if(removeIndex != NODE_NOT_FOUND)
        {
            // remove the node
            ChatServer.participants.remove(removeIndex);
            // return success
            return true;
        }
        // return failure
        return false;
    }
    
    /**
     * Helper function to determine whether or not a node exists within the
     * 'participants' ArrayList
     * @param node target node
     * @return true if found, false if not found
     */
    private Boolean isInList(NodeInfo node) {
        // get the index of the target node
        int nodeIndex = getIndex(node);
        
        // check if the node was found
        if(nodeIndex != NODE_NOT_FOUND)
        {
            // return success
            return true;
        }
        // return failure
        return false;
    }
    
}
