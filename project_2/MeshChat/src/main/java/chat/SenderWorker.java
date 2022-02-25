/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Worker class for peer sender. Used to disperse messages to all participating
 * peers
 * @author Joe Domabyl V
 */
public class SenderWorker extends Thread {
    
    // declare variables
    Socket peerConnection = null;
    ObjectInputStream fromPeers = null;
    ObjectOutputStream toPeers = null;
    Object message;

    public SenderWorker(Socket peerConnection, Object message) {
        this.peerConnection = peerConnection;
        this.message = message;
    }
    
    @Override
    public void run() {

        try
        {                

            // establish IO streams
            toPeers = new ObjectOutputStream(peerConnection.getOutputStream());
            fromPeers = new ObjectInputStream(peerConnection.getInputStream());

            // write the message to the connected peer's receiver
            toPeers.writeObject(this.message);

            // close the connection to the peer
            peerConnection.close();

        } 
        catch (IOException ex)
        {
            Logger.getLogger(SenderWorker.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        
    }
    
    
}
