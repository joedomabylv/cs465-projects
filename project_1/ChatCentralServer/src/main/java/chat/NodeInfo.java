package chat;

import java.io.Serializable;

public class NodeInfo implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

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
