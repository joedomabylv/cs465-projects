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
        System.out.println("Looking for connection...");
    }
    
}
