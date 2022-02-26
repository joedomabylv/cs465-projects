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
    public static NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    public static Boolean running = true;
    public static ArrayList<NodeInfo> participants = new ArrayList<>();
    public static Boolean receivedParticipantsList = false;
    
    // constructor
    public MeshClient(String clientIP, String name, int clientPort) {
        // create nodeInfo object
        this.nodeInfo = new NodeInfo(clientIP, name, clientPort);
        
        // add self to participants list
        participants.add(this.nodeInfo);
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

        // get IP of the current machine
        String clientIP = getMyIP();
        
        // ask the user for their name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();
        
        System.out.println("What port number do you want to listen on?");
        int clientPort = Integer.parseInt(scanner.nextLine());
        
                
        // create client thread
        new MeshClient(clientIP, name, clientPort).run();

    }

}
