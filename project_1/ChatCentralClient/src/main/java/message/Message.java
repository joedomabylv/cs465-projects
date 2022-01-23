package message;

import java.io.Serializable;

public class Message implements Serializable, MessageType {
    
  private static final long serialVersionUID = 6529685098267757690L;

    // variables
    int type;
    Object content;
    
    // constructor
    public Message(int type, Object content) {
	this.type = type;
	this.content = content;
    }

    // getter & setter
    public void setType(int type) {
	this.type = type;
    }
	
    public int getType() {
	return this.type;
    }

    public void setContent(Object content) {
	this.content = content;
    }

    public Object getContent() {
	return this.content;
    }

}
