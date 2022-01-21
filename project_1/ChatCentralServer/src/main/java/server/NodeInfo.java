package server;

import java.io.Serializable;

public class NodeInfo implements Serializable {
    
    String serverIP;
    String name;
    int serverPort;

    // constructor
    NodeInfo(String serverIP, String name, int serverPort) {
        this.serverIP = serverIP;
        this.name = name;
        this.serverPort = serverPort;
    }

}
