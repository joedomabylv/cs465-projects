package message;

/**
 * Interface representing message type constants
 * @author Joe Domabyl V
 */
public interface MessageType {
    
    static final int JOIN = 0;
    static final int LEAVE = 1;
    static final int SEND_NOTE = 2;
    static final int SHUTDOWN = 3;
    static final int SEND_LIST = 4;

}
