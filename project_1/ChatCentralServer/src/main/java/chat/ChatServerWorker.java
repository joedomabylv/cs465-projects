package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

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
            System.out.println("Creating IO streams for client " + client.getLocalAddress() + client.getLocalPort() + "...");
            toClients = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
            
            // read the object and cast it to type Message
            System.out.println("Casting read object to message...");
            messageReceived = (Message) fromClient.readObject();
            
            // close connection
            System.out.println("Closing connection...");
            client.close();
            
            // display results
            System.out.println("Success!");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        // determine what to do with the message
        switch (messageReceived.getType()) {
            case Message.JOIN:
                System.out.println("Received JOIN message!");
                
                // cast content to NodeInfo
                NodeInfo newNode = (NodeInfo) messageReceived.getContent();
                
                // add the node
                System.out.println("Adding participant to ArrayList...");
                ChatServer.participants.add(newNode);
                
                System.out.println("Success!");
                
                break;
            case Message.LEAVE:            
                System.out.println("Received LEAVE message!");
                
                // cast content to NodeInfo
                NodeInfo leaveNode = (NodeInfo) messageReceived.getContent();
                
                // attempt to remove the node
                System.out.println("Removing participant from ArrayList...");
                // find the node
                int removeIndex = nodeEquals(leaveNode);
                
                if(removeIndex != NODE_NOT_FOUND)
                {
                    ChatServer.participants.remove(removeIndex);
                    System.out.println("Success!");
                }
                else
                {
                    System.out.println("Could not find node!");
                }
                
                break;
            case Message.SHUTDOWN:
                System.out.println("Received SHUTDOWN request!");
                
                // cast content to NodeInfo
                NodeInfo shutdownNode = (NodeInfo) messageReceived.getContent();
                
                // check if the participant is considered joined, i.e. it is
                // within the participants list
                System.out.println("Checking if participant is JOINED...");
                for(int i = 0; i < ChatServer.participants.size(); i++)
                {
                    // if found, remove it
                    if(ChatServer.participants.get(i).equals(shutdownNode))
                    {
                        System.out.println("Participant found, removing from ArrayList...");
                        ChatServer.participants.remove(shutdownNode);
                    }
                    
                    // NOTE: if the node is NOT found, do nothing
                    
                }
                
                System.out.println("Success!");
                
                break;
            default:
                System.out.println(messageReceived.getContent());
                
                // multiplex chat messages
                
                break;
        }

    }
    
    private int nodeEquals(NodeInfo otherNodeInfo) {
        // declare variables
        int index;
        String currentIP;
        int currentPort;
        String otherIP = otherNodeInfo.serverIP;
        int otherPort = otherNodeInfo.serverPort;
        
        // loop through ArrayList
        for(index = 0; index < ChatServer.participants.size(); index++)
        {
            currentIP = ChatServer.participants.get(index).serverIP;
            currentPort = ChatServer.participants.get(index).serverPort;
            // compare IP's and port numbers
            if(currentIP.equals(otherIP) && currentPort == otherPort)
            {
                // return found index
                return index;
            }
        }
        
        // node wasn't found
        return NODE_NOT_FOUND;
    }
    
}
