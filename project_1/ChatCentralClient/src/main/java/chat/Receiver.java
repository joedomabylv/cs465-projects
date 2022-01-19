package chat;

public class Receiver extends Thread {
    
    // default constructor
    public Receiver(){}
    
    @Override
    public void run() {
        System.out.println("Listening for server...");
        // receiver listens for server thread to take message output from
        // other clients and display them on the screen
    }
    
}
