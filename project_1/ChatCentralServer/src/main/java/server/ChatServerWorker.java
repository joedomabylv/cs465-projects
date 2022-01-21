package server;

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
        
        // try to get the streams
        try {
            this.fromClient = new ObjectInputStream(client.getInputStream());
            this.toClients = new ObjectOutputStream(client.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // try to cast to message
        try {
            // SOMETHING IS WRONG WITH THIS LINE
            messageReceived = (Message) fromClient.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // determine what to do with the message
        messageType = messageReceived.getType();
        switch (messageType) {
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

        // close the connection
        try {
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatServerWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
