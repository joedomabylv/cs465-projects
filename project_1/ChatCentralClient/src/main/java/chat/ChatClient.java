package chat;

// java imports
import java.io.*;
import java.util.*;

// custom imports
import static utils.ServerPropHandler.getServerInfo;
import message.Message;

public class ChatClient implements Runnable {
    
    // declare variables
    NodeInfo nodeInfo;
    Message message;
    Boolean isJoined;
    private static final Scanner scanner = new Scanner(System.in);
    
    // constructor
    public ChatClient(String serverIP, String name, int serverPort) {
        this.nodeInfo = new NodeInfo(serverIP, name, serverPort);
        this.message = new Message(9999, null);
        this.isJoined = false;
    }

    @Override
    public void run() {
        
        Boolean running = true;
        
        // ensure the receiver is running, looking for server output
        (new Receiver()).start();
        
        // run client
        while(running)
        {
            this.message.setContent(scanner.nextLine());

            Object userInput = this.message.getContent();

            if(userInput.equals("JOIN") && !isJoined)
            {
                System.out.println("Joining the chat!");
                this.message.setType(Message.JOIN);
                this.isJoined = true;
            }
            else if(userInput.equals("LEAVE") && isJoined)
            {
                System.out.println("Leaving the chat!");
                this.message.setType(Message.LEAVE);
                this.isJoined = false;
            }
            else if(userInput.equals("SHUTDOWN"))
            {
                System.out.println("Shutting down!");
                this.message.setType(Message.SHUTDOWN);
                running = false;
            }
            else if(isJoined)
            {
                System.out.println("Sending message!");
                this.message.setType(Message.SEND_NOTE);
            }
            
            if(!isJoined && running)
            {
                System.out.println("You have to join the chat first, goofball!");
            }
            else
            {
                // send the message to the server
                (new Sender(this)).start();
            }
        }
        
    }

    public static void main(String[] args) throws IOException {

        // gather server information
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));
        
        // gather users name
        System.out.println("Hello! What is your name?");
        String name = scanner.nextLine();
        
        System.out.println("Type \'JOIN\' to join the chat");
        System.out.println("Type \'LEAVE\' to leave the chat");
        System.out.println("Type \'SHUTDOWN\' to shutdown your connection");
        System.out.println("Otherwise, start chatting!");
        
        new ChatClient(serverIP, name, serverPort).run();

    }

}
