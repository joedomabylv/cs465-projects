package chat;

import java.io.Serializable;

/**
 * NodeInfo class representing meta information for a node in the chat
 * @author Joe Domabyl V
 */
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    
    String peerIP;
    String name;
    int peerPort;
    
    NodeInfo(String peerIP, String name, int peerPort) {
        this.peerIP = peerIP;
        this.name = name;
        this.peerPort = peerPort;
    }
    
}
