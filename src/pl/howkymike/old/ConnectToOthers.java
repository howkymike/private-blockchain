//package pl.howkymike.old;
//
//import java.io.IOException;
//import java.net.Socket;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class ConnectToOthers implements Runnable{
//
//    private ConcurrentHashMap<String, Socket> socketMapping;
//    private String serverName;
//    private String serverHostAddress;
//    private int serverPort;
//    private Socket socket;
//
//    public ConnectToOthers(int serverPort, String serverName, String serverHostAddress,
//                          ConcurrentHashMap<String, Socket> socketMapping)
//    {
//        this.socketMapping = socketMapping;
//        this.serverPort = serverPort;
//        this.serverName = serverName;
//        this.serverHostAddress = serverHostAddress;
//    }
//
//    @Override
//    public void run() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        try {
//            socket = new Socket(serverHostAddress, serverPort);
//            socketMapping.put(serverName, socket);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
