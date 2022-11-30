package pl.howkymike.utils;

import pl.howkymike.Main;
import pl.howkymike.model.Blockchain;
import pl.howkymike.model.Message;
import pl.howkymike.model.MessageType;

import java.util.List;

public final class ClientMessageHandler {
    public static void handleMessage(Message message) {
        MessageType msgType = message.getMessageType();
        if (MessageType.GET_BLOCKCHAIN.equals(msgType)) {
            getBlockchain(message);
            return;
        }
        if (MessageType.HASH_FOUND.equals(msgType)) {
            checkHashFoundMsg(message);
            return;
        }
        if (MessageType.MINE_BLOCK.equals(msgType)) {
            mineBlock(message);
            return;
        }
        if (MessageType.GET_NODES.equals(msgType)) {
            getNodes(message);
            return;
        }
        if (MessageType.JOIN_NODES.equals(msgType)) {
            printJoinNodesStatus(message);
            return;
        }
        throw new RuntimeException("Undefined messageType: " + msgType);
    }

    private static void getBlockchain(Message message) {
        Blockchain receivedBlockchain = (Blockchain) message.getData();
        Blockchain myBlockchain = Main.getBlockchain();

        // simple without validation
        for (int i = 0; i < receivedBlockchain.getBlocks().size(); i++) {
            if (myBlockchain.getBlocks().size() > i)
                continue;
            myBlockchain.addBlock(receivedBlockchain.getBlocks().get(i));
        }
    }

    private static void checkHashFoundMsg(Message message) {
        String success = (String) message.getData();
        assert "OK".equals(success);
    }

    private static void mineBlock(Message message) {
        String success = (String) message.getData();
        assert "MINING...".equals(success);
        //System.out.println("Block is being mined!");
    }

    private static void getNodes(Message message) {
        List<Integer> nodes = (List<Integer>) message.getData();
        Main.setNodes(nodes);
    }

    private static void printJoinNodesStatus(Message message) {
        String status = (String) message.getData();
        System.out.println("Join node status: " + status);
    }
}
