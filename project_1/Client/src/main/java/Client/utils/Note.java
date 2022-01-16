package Client.utils;

import java.util.*;

public class Note implements java.io.Serializable, MessageType {

    // variables
    String type;
    Object content;

    // constructor
    public Note(String type, Object content) {
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

    // tell the server that a client wants to join the chat
    @Override
    public void join() {
	System.out.println("join() called!");
    }

    // tell the server that a client wants to leave the chat
    @Override
    public void leave() {
	System.out.println("leave() called!");
    }

    // send a content message to the server
    @Override
    public void noteMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input your message here:");
        this.setContent(scanner.nextLine());
	System.out.println("noteMessage() called with the message \'" + this.getContent() + "\'!");
    }

    // send a shutdown signal to the server
    @Override
    public void shutdown() {
	System.out.println("shutdown() called!");
    }

    // send a nuclear killall shutdown message to the server
    @Override
    public void shutdownAll() {
	System.out.println("shutdownAll() called!");
    }
    
}
