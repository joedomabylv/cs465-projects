package Client.utils;

public class Message implements java.io.Serializable, MessageType {

    // variables
    String type;
    Object content;
    
    // constructor
    public Message(String type, Object content) {
	this.type = type;
	this.content = content;
    }

    // getter & setter
    public void setType(String type) {
	this.type = type;
    }
	
    public String getType() {
	return this.type;
    }

    public void setContent(Object content) {
	this.content = content;
    }

    public Object getContent() {
	return this.content;
    }

    // check that the user has input a valid message, one of the primary
    // operations
    @Override
    public Boolean isValidType(String type) {
        return ( type.equals(JOIN)
                || type.equals(LEAVE)
                || type.equals(SEND_NOTE)
                || type.equals(SHUTDOWN) );
    }
    
}
