package chat;

// java imports
import java.io.*;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

// custom imports
import static utils.ServerPropHandler.getServerInfo;

public class ChatClient implements Runnable {
    
    // declare variables
    NodeInfo nodeInfo;
    private static final Scanner scanner = new Scanner(System.in);
    
    // constructor
    public ChatClient(String serverIP, String name, int serverPort) {
        this.nodeInfo = new NodeInfo(serverIP, name, serverPort);
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

        // gather server information
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        
        // uncomment the next line if you need your own IP for running
        // this application over the internet, not localhost
        // System.out.println(serverIP);
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));
        
        // ask the user for their name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();
        
        // create client thread
        new ChatClient(serverIP, name, serverPort).run();

    }

}
