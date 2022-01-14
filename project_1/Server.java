import java.io.*;
import java.util.*;

/* TODO:
   1. Register for participation in chat (JOIN)
   2. Leave the chat
   3. Send messages to all participating clients
*/

public class Server {
    public static void main(String[] args) throws IOException {
	     System.out.println("I am the server!\n");

       Properties property = getServerInfo( "server.properties" )
       System.out.println("Server IP: "+property.getProperty("SERVER_IP"));
       System.out.println("Server Port: "+property.getProperty("SERVER_PORT"));
    }

    public static Properties getServerInfo( String fileName ) throws IOException {
      FileInputStream fIStream = null;
      Properties properties = null;

      try {
        fIStream = new FileInputStream( filename );
        properties = new Properties();
        properties.load( fIStream );
      }
      catch( IOException iOExc ) {
        iOExc.printStackTrace();
      }
      catch( FileNotFoundExeception fNFExc ) {
        fNFExc.printStackTrace();
      }
      finally {
        fIStream.close();
      }

      return properties;
    }
}
