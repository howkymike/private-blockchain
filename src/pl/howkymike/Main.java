package pl.howkymike;

import pl.howkymike.model.Block;
import pl.howkymike.model.Blockchain;
import pl.howkymike.model.Message;
import pl.howkymike.model.MessageType;
import pl.howkymike.utils.ClientMessageCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    // first program must run with the 1234 port !
    private static final int PORT_FIRST_NODE = 1234;

    // initialize a blockchain
    private static final Blockchain blockchain = new Blockchain();

    private static Integer ownServerPort = -1;

    // initialize Node list
    private static List<Integer> nodePorts = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        nodePorts.add(PORT_FIRST_NODE);

        if (args.length != 1) {
            if (args.length == 2) {
                String blockData = args[1];
                callBlockMine(blockData);
                return;
            }
            System.err.println("Run this program by: java Main.o <serverPort> OR java Main.o <serverPort> <blockData>");
            return;
        }
        ownServerPort = Integer.parseInt(args[0]);

        if (ownServerPort != PORT_FIRST_NODE) {
            initNode();
        }

        Thread serverThread = new Thread(new ServerThread(ownServerPort));
        serverThread.start();
    }

    private static void initNode() {
        // get list of nodes
        Thread downloadNodeThread = downloadNodeList();
        try {
            downloadNodeThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // get blockchain
        Thread downloadBlockchain = downloadBlockchain();
        try {
            downloadBlockchain.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // ask to join the nodes
        askToJoinNetwork();
    }

    private static Thread askToJoinNetwork() {
        Message msg = ClientMessageCreator.createMessage(MessageType.JOIN_NODES, Main.ownServerPort);
        Thread clientThread = new Thread(new ClientThread(msg));
        clientThread.start();
        return clientThread;
    }

    private static Thread downloadBlockchain() {
        Message msg = ClientMessageCreator.createMessage(MessageType.GET_BLOCKCHAIN, "");
        Thread clientThread = new Thread(new ClientThread(msg));
        clientThread.start();
        return clientThread;
    }

    private static Thread downloadNodeList() {
        Message msg = ClientMessageCreator.createMessage(MessageType.GET_NODES, "");
        Thread clientThread = new Thread(new ClientThread(msg));
        clientThread.start();
        return clientThread;
    }

    private static Thread callBlockMine(String blockData) {
        Thread downloadNodeListThread = downloadNodeList();
        try {
            downloadNodeListThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Message msg = ClientMessageCreator.createMessage(MessageType.MINE_BLOCK, blockData);
        Thread clientThread = new Thread(new ClientThread(msg));
        clientThread.start();
        return clientThread;
    }

    public static void addBlock(Block b) {
        if (!blockchain.getBlocks().contains(b)) {
            blockchain.addBlock(b);

            Message msg = ClientMessageCreator.createMessage(MessageType.HASH_FOUND, b);
            Thread clientThread = new Thread(new ClientThread(msg));
            clientThread.start();
        }
    }

    public static Blockchain getBlockchain() {
        return blockchain;
    }

    public static List<Integer> getNodePorts() {
        return nodePorts;
    }

    public static void setNodes(List<Integer> nodePorts) {
        Main.nodePorts = nodePorts;
    }

    public static void addNode(Integer nodePort) {
        Main.nodePorts.add(nodePort);
    }

    public static Integer getOwnServerPort() {
        return ownServerPort;
    }
}
