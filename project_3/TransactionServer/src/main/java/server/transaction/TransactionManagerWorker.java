package server.transaction;

import java.net.Socket;

/**
 *
 * @author Joe Domabyl V, Nick Nannen, Daniel Rydberg
 */
public class TransactionManagerWorker extends Thread {
    
    Socket client = null;
    
    public TransactionManagerWorker(Socket client) {
        this.client = client;
    }
    
    @Override
    public void run() {
        // create a new transaction
        System.out.println("[!] TransactionManagerWorker called");
        
        // CREATE TRANSACTION OBJECT SOMEWHERE HERE
    }
    
}
