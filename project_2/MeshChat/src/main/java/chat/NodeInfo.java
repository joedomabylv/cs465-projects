package chat;

import java.io.Serializable;

/**
 * NodeInfo class representing meta information for a node in the chat
 * @author Joe Domabyl V
 */
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    
    String clientIP;
    String name;
    int clientPort;
    
    NodeInfo(String clientIP, String name, int clientPort) {
        this.clientIP = clientIP;
        this.name = name;
        this.clientPort = clientPort;
    }
    
}
