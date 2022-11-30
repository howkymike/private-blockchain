//package pl.howkymike.old.client;
//
//import java.io.IOException;
//import java.net.DatagramPacket;
//import java.net.InetAddress;
//import java.net.MulticastSocket;
//
//public class MulticastClient implements Runnable {
//
//    private static final String MULTICAST_ADDRESS = "230.0.0.0";
//    private static final int MULTICAST_PORT = 4321;
//
//    @Override
//    public void run() {
//        try {
//            receiveUDPMessage(MULTICAST_ADDRESS, MULTICAST_PORT);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public void receiveUDPMessage(String ip, int port) throws
//            IOException {
//        byte[] buffer = new byte[1024];
//        MulticastSocket socket = new MulticastSocket(MULTICAST_PORT);
//        InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
//        socket.joinGroup(group);
//        while (true) {
//            System.out.println("Waiting for multicast message...");
//            DatagramPacket packet = new DatagramPacket(buffer,
//                    buffer.length);
//            socket.receive(packet);
//            String msg = new String(packet.getData(),
//                    packet.getOffset(), packet.getLength());
//            System.out.println("[Multicast UDP message received] " + msg);
//            if ("OK".equals(msg)) {
//                System.out.println("No more message. Exiting : " + msg);
//                break;
//            }
//        }
//        socket.leaveGroup(group);
//        socket.close();
//    }
//
//
////    public static void main(String[] args) {
////        Thread t = new Thread(new MulticastClient());
////        t.start();
////    }
//}
