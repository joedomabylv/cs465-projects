package chat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.PriorityBlockingQueue;
import message.Message;

/**
 *
 * @author Joe Domabyl V, Daniel Rydberg, Nick Nannen
 */
public class MeshClient implements Runnable {

    static void add(Object messageContent) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // declare variables
    NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    public static Boolean running = true;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();
    public static Queue<Message> messageQueue = new PriorityBlockingQueue<>();
    public static Boolean participantLock = false;
    
    // constructor
    public MeshClient(String clientIP, String name, int clientPort) {
        // create nodeInfo object
        this.nodeInfo = new NodeInfo(clientIP, name, clientPort);
    }

    @Override
    public void run() {
        // start receiver
        (new Receiver(this.nodeInfo)).start();

        // start sender
        (new Sender(this.nodeInfo)).start();
    }
    
    /**
     * Gets the users IP address
     * @return IP address if found, null if not found
     */
    public static String getMyIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp() || networkInterface.isVirtual() || networkInterface.isPointToPoint()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();

                    final String myIP = address.getHostAddress();
                    if (Inet4Address.class == address.getClass()) {
                        return myIP;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        
        // declare variables
        Boolean invalidSetup = true;
        String knownPeerInput;
        int clientPort = -1;

        // get IP of the current machine
        String clientIP = getMyIP();
        
        // ask the user for their name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();

        // ask the user if they want to act as the client that has the port/IP
        // that the others "just know"
        System.out.println("Do you want this client to act as the peer "
                + "that everyone else already knows? (y/n)");
        
        while(invalidSetup)
        {
            knownPeerInput = scanner.nextLine();
            
            if(knownPeerInput.equals("y"))
            {
                // if the client wants to act as the peer everyone knows, they
                // use localhost as the IP and Dr. Otte's birthday as a port
                clientIP = "127.0.0.1";
                clientPort = 23657;
                
                // break out of the loop
                invalidSetup = false;
            }
            else if(knownPeerInput.equals("n"))
            {
                // if the client doesn't want to act as the peer everyone knows,
                // they use the IP of the current machine and their own port.
                // NOTE: if on using multiple machines, change the port number
                clientPort = 20998;
                
                // break out of the loop
                invalidSetup = false;
            }
            else
            {
                System.out.println("Please type either \'y\' or \'n\'!");
            }
        }
                
        // create client thread
        new MeshClient(clientIP, name, clientPort).run();

    }

}
