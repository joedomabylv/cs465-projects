package chat;

import java.net.*;

public class Sender extends Thread {
    
    ChatClient client = null;
    Socket connection = null;
    
    // default constructor
    public Sender(ChatClient client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        // wrap the message contents into an OutputStream and send it to the server
        System.out.println("Sender received a message of type: " + this.client.message.getType());
    }
    
}
