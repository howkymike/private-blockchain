package pl.howkymike;

import pl.howkymike.model.Message;
import pl.howkymike.utils.ClientMessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

// it will listen for the requests
// 1. MINE_BLOCK - start Miner thread
// 2. MINED_BLOCK - terminate Miner thread
// 3. GET_BLOCKCHAIN - send whole blockchain
// 4. GET_NODES = get all connected nodes
public class ClientThread implements Runnable {

    private final InetAddress host;
    private final Message message;


    public ClientThread(Message message) {
        this.message = message;
        try {
            host = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        sendMessageToAll();
    }


    private void sendMessageToAll() {
        for (Integer port : Main.getNodePorts()) {
            if (port.intValue() == Main.getOwnServerPort().intValue())
                continue;

            // create a socket
            Socket socket;
            try {
                socket = new Socket(host.getHostName(), port);
            } catch (IOException e) {
                System.out.println("Could not create a client socket.");
                throw new RuntimeException(e);
            }

            // create an output stream
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Could not create a client output stream.");
                throw new RuntimeException(e);
            }

            // send message to server
            System.out.println("Sending " + message.getMessageType() + " request to node: " + port);
            try {
                oos.writeObject(message);
            } catch (IOException e) {
                System.out.println("Could not send a request to a server node: " + port);
                throw new RuntimeException(e);
            }

            // read server response
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Could not create an input stream with node: " + port);
                throw new RuntimeException(e);
            }
            Message outputMessage;
            try {
                outputMessage = (Message) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Could not process message from node: " + port);
                throw new RuntimeException(e);
            }
            System.out.println("Receiver received message: " + outputMessage);

            // handle response
            ClientMessageHandler.handleMessage(outputMessage);

            // close resources
            try {
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
