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
    ObjectOutputStream toClient = null;
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
            toClient = new ObjectOutputStream(client.getOutputStream());
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
                
                // multiplex chat messages
                for(int i = 0; i < ChatServer.participants.size(); i++)
                {
                    sendMessage(ChatServer.participants.get(i), messageReceived);
                }

                break;

        }

        // temp debug message
        for(int i = 0; i < ChatServer.participants.size(); i++)
        {
            System.out.println("User " + ChatServer.participants.get(i).name + "\n\t> IP: " + ChatServer.participants.get(i).clientIP + "\n\t> Port: " + ChatServer.participants.get(i).clientPort);
        }
        
    }

    /**
     * Creates a connection to the client receiver and sends a message
     * @param participant node of a client
     * @param message message to be sent
     */
    private void sendMessage(NodeInfo participant, Message messageObject) {
        
        Socket clientReceiver;
        ObjectOutputStream toReceiver;
        ObjectInputStream fromReceiver = null;
        String fullNote;
        String messageString;
        
        try
        {
            
            clientReceiver = new Socket(participant.clientIP, participant.clientPort);
            toReceiver = new ObjectOutputStream(clientReceiver.getOutputStream());
            fromReceiver = new ObjectInputStream(clientReceiver.getInputStream());
            
            messageString = (String) messageObject.getContent();
            fullNote = participant.name + ": " + messageString;
            
            toReceiver.writeObject(fullNote);
            
            clientReceiver.close();
        } catch (IOException ex)
        {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        
        // loop through ArrayList
        for(index = 0; index < ChatServer.participants.size(); index++)
        {

            if(nodeEquals(ChatServer.participants.get(index), otherNodeInfo)) {
                return index;
            }
        }
        
        // node wasn't found
        return NODE_NOT_FOUND;
    }
    
    /**
     * Check if two nodes are equivalent based on IP, port, and name
     * @param firstNode first node to be compared to the second
     * @param secondNode second node to be compared to the first
     * @return returns Boolean equivalence
     */
    private Boolean nodeEquals(NodeInfo firstNode, NodeInfo secondNode)
    {
        return (firstNode.clientIP.equals(secondNode.clientIP)
                && firstNode.clientPort == secondNode.clientPort
                && firstNode.name.equals(secondNode.name));
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
        // determine index and return result
        return getIndex(node) != NODE_NOT_FOUND;
    }
    
}
