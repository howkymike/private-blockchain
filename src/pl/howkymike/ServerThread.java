package pl.howkymike;

import pl.howkymike.model.Message;
import pl.howkymike.utils.ServerMessageHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// it will send request to the other nodes
// 1. ask to mine a block
// 2. say that you mined a block
// 3. get blockchain from other nodes
// 4. ask for a list of nodes
public class ServerThread implements Runnable {

    private static ServerSocket server;


    public ServerThread(int serverPort) {
        try {
            server = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.out.println("could not create ServerSocket on port " + serverPort);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        // keep listening indefinitely
        while (true) {
            System.out.println("Waiting for the client message..");
            Socket socket;
            try {
                socket = server.accept();
            } catch (IOException e) {
                System.out.println("Could not accept a client: " + e.getMessage());
                continue;
            }

            //read from socket
            ObjectInputStream ois;
            try {
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Could not get InputStream from client.");
                tryToCloseServer();
                throw new RuntimeException(e);
            }
            Message message;
            try {
                message = (Message) ois.readObject();
            } catch (IOException e) {
                System.out.println("Could not read object from client");
                tryToCloseServer();
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                System.out.println("Unknown client message class object.");
                tryToCloseServer();
                throw new RuntimeException(e);
            }
            System.out.println("Received msg: " + message);

            //  handle message
            ServerMessageHandler msgHandler = new ServerMessageHandler();
            Message outputMessage = msgHandler.handleMessage(message);

            // return message to client
            ObjectOutputStream oos;
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Could not get output stream from client.");
                tryToCloseServer();
                throw new RuntimeException(e);
            }
            try {
                oos.writeObject(outputMessage);
            } catch (IOException e) {
                System.out.println("Could not write object to client.");
                tryToCloseServer();
                throw new RuntimeException(e);
            }

            // close resources
            try {
                ois.close();
            } catch (IOException e) {
                tryToCloseServer();
                throw new RuntimeException(e);
            }
            try {
                oos.close();
            } catch (IOException e) {
                tryToCloseServer();
                throw new RuntimeException(e);
            }
        }
    }

    private void tryToCloseServer() {
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
