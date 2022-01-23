package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverWorker extends Thread {
    
    Socket client = null;
    
    public ReceiverWorker(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {

    }
    
}
