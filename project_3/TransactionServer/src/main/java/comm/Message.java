package comm;

import java.io.Serializable;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class Message implements Serializable, MessageTypes {
    
    private static final long serialVersionUID = 6529685098267757690L;
    
    int messageType;
    Object content;
    
    public Message(int messageType, Object content) {
        this.messageType = messageType;
        this.content = content;
    }
    
    /**
     * Get message type
     * @return message type
     */
    public int getMessageType() {
        return messageType;
    }

    /**
     * Set a message type
     * @param messageType 
     */
    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    /**
     * Get the content of the message
     * @return message content
     */
    public Object getContent() {
        return content;
    }

    /**
     * Set the content of a message
     * @param content 
     */
    public void setContent(Object content) {
        this.content = content;
    }
    
    
}
