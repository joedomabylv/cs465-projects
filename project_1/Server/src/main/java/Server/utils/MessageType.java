package Server.utils;

interface MessageType {

    // constants
    static final int JOIN = 0;
    static final int LEAVE = 1;
    static final int NOTE_MESSAGE = 2;
    static final int SHUTDOWN = 3;
    static final int SHUTDOWN_ALL = 4;

    // interface
    public void join();
    public void leave();
    public void noteMessage();
    public void shutdown();
    public void shutdownAll();
    
}
