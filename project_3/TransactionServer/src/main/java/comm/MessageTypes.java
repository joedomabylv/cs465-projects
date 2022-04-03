package comm;

/**
 * Message types
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public interface MessageTypes {

    static final int OPEN_TRANSACTION = 1;
    static final int CLOSE_TRANSACTION = 2;
    static final int READ_REQUEST = 3;
    static final int WRITE_REQUEST = 4;
    static final int TRANSACTION_COMMITTED = 5;
    static final int TRANSACTION_ABORTED = 6;
    
}
