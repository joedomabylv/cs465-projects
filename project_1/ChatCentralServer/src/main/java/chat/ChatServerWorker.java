package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

public class ChatServerWorker extends Thread {

    Socket client = null;
    ObjectInputStream fromClient = null;
    ObjectOutputStream toClients = null;
    Message messageReceived = null;
    int messageType;
    
    // constructor
    public ChatServerWorker(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        
        // get the streams, cast the message, close the connection
        try {
            toClients = new ObjectOutputStream(client.getOutputStream());
            fromClient = new ObjectInputStream(client.getInputStream());
            messageReceived = (Message) fromClient.readObject();
            client.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
        
        // determine what to do with the message
        switch (messageReceived.getType()) {
            case Message.JOIN:                
                // cast content to NodeInfo
                NodeInfo newNode = (NodeInfo) messageReceived.getContent();
                
                // add the node
                ChatServer.participants.add(newNode);
                
                break;
            case Message.LEAVE:                
                // cast content to NodeInfo
                NodeInfo leaveNode = (NodeInfo) messageReceived.getContent();
                
                // remove the node
                ChatServer.participants.remove(leaveNode);
                
                break;
            case Message.SHUTDOWN:                
                // cast content to NodeInfo
                NodeInfo shutdownNode = (NodeInfo) messageReceived.getContent();
                
                // check if the participant is considered joined, i.e. it is
                // within the participants list
                for(int i = 0; i < ChatServer.participants.size(); i++)
                {
                    // if found, remove it
                    if(ChatServer.participants.get(i).equals(shutdownNode))
                    {
                        ChatServer.participants.remove(shutdownNode);
                    }
                    
                    // NOTE: if the node is NOT found, do nothing
                    
                }
                
                break;
            default:
                System.out.println(messageReceived.getContent());
                
                // multiplex chat messages
                
                break;
        }

    }
    
}
