package chat;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
    
    public MeshClient(String clientIP, String name, int clientPort) {
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
        
        // get IP of the current machine
        String clientIP = getMyIP();
        
        // make this port whatever you want the client to listen on
        int clientPort = 20998;
        
        // ask the user for their name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();
        
        // create client thread
        new MeshClient(clientIP, name, clientPort).run();

    }

}
