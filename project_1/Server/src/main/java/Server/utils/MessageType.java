package Server.utils;

interface MessageType {

    // constants
    static final String JOIN = "1";
    static final String LEAVE = "2";
    static final String SEND_NOTE = "3";
    static final String SHUTDOWN = "4";
    
    public Boolean isValidType(String type);

}
