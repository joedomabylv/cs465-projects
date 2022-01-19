package message;

import java.io.Serializable;

public class Message implements Serializable, MessageType {

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

}
