package Client;

// java imports
import java.io.*;
import java.util.*;

// custom imports
import static Client.utils.ServerPropHandler.getServerInfo;
import Client.utils.Note;

public class Client {

    // declare constants
    private static final String JOIN = "1";
    private static final String LEAVE = "2";
    private static final String SEND_NOTE = "3";
    private static final String SHUTDOWN = "4";
    private static final String SHUTDOWN_ALL = "5";

    private static Boolean isValidMessage(String input) {
        return ( input.equals(JOIN)
                || input.equals(LEAVE)
                || input.equals(SEND_NOTE)
                || input.equals(SHUTDOWN)
                || input.equals(SHUTDOWN_ALL) );
    }

    private static void dispatchNote(Note newNote) {
        switch (newNote.getType()) {
            case JOIN:
                newNote.join();
                break;
            case LEAVE:
                newNote.leave();
                break;
            case SEND_NOTE:
                newNote.noteMessage();
                break;
            case SHUTDOWN:
                newNote.shutdown();
                System.exit(0);
            case SHUTDOWN_ALL:
                newNote.shutdownAll();
                break;
        }
    }

    private static void runChat() {
        Scanner scanner = new Scanner(System.in);
        String inputString = null;
        System.out.println("Welcome to chat! What would you like to do?");
        while (true) {
            System.out.println("  1. Join chat");
            System.out.println("  2. Leave chat");
            System.out.println("  3. Send message");
            System.out.println("  4. Shutdown");
            System.out.println("  5. Shutdown all");

            inputString = scanner.nextLine();

            if (isValidMessage(inputString)) {
                // dispatch as a thread?
                dispatchNote(new Note(inputString, null));
            } else {
                System.out.println("Please provide valid input, goofball");
            }
        }
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Hello, I am the client!");

        // gather variables
        Properties prop = getServerInfo("config/server.properties");
        String serverIP = prop.getProperty("SERVER_IP");
        int serverPort = Integer.parseInt(prop.getProperty("SERVER_PORT"));

        System.out.println("Attempting to connect to " + serverIP + ":" + serverPort);

        runChat();

    }

}
