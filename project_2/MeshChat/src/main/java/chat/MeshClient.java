package chat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

/**
 *
 * @author Joe Domabyl V, Daniel Rydberg, Nick Nannen
 */
public class MeshClient implements Runnable {
    
    // declare variables
    NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    public static Boolean running = true;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();
    public static Boolean receivedParticipantsList = false;
    
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
        System.out.println("Which client do you want to be?\n"
                + "1. The one that is \'known\'!\n"
                + "2. The second one!\n"
                + "3. The third one!");
        
        while(invalidSetup)
        {
            knownPeerInput = scanner.nextLine();
            
            if(knownPeerInput.equals("1"))
            {
                // if the client wants to act as the peer everyone knows, they
                // use localhost as the IP and Dr. Otte's birthday as a port
                System.out.println(clientIP);
                clientPort = 23657;
                
                // break out of the loop
                invalidSetup = false;
            }
            else if(knownPeerInput.equals("2"))
            {
                // if the client wants to be the second peer, they use the IP
                // of the current machine and their own port
                clientIP = "127.0.0.1";
                clientPort = 10998;
                
                // break out of the loop
                invalidSetup = false;
            }
            else if(knownPeerInput.equals("3"))
            {
                // if the client wants to be the third peer, they use a random
                // IP and a random port
                clientIP = "248.235.64.79";
                clientPort = 10998;
                
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
