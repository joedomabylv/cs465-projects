package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.Message;

public class Receiver extends Thread {
    
    NodeInfo nodeInfo = null;
    Socket serverConnection = null;
    ObjectInputStream fromServer = null;
    ObjectOutputStream toServer = null;
    Message contentFromServer = null;
    
    // default constructor
    public Receiver(NodeInfo nodeInfo){
        this.nodeInfo = nodeInfo;
    }
    
    @Override
    public void run() {
//        while(true)
//        {
//            // open connection to server
//            try {
//                System.out.println("Attemping to connect listener...");
//                serverConnection = new Socket(nodeInfo.serverIP, nodeInfo.serverPort);
//                System.out.println("Listener connected!");
//            } catch (IOException ex) {
//                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            // try to open IO streams
//            try {
//                fromServer = new ObjectInputStream(serverConnection.getInputStream());
//                toServer = new ObjectOutputStream(serverConnection.getOutputStream());
//            } catch (IOException ex) {
//                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//            // try to read output from server
//            try {
//                // cast to message
//                contentFromServer = (Message) fromServer.readObject();
//                System.out.println(contentFromServer.getContent());
//            } catch (IOException | ClassNotFoundException ex) {
//                Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }
    
}
