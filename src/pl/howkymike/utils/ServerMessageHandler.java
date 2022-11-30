package pl.howkymike.utils;

import pl.howkymike.Main;
import pl.howkymike.model.Block;
import pl.howkymike.model.Message;
import pl.howkymike.model.MessageType;
import pl.howkymike.model.NotMinedBlock;

import java.time.Instant;
import java.util.List;

public class ServerMessageHandler {

    private static Thread miner;

    public Message handleMessage(Message message) {
        MessageType msgType = message.getMessageType();
        if (MessageType.GET_BLOCKCHAIN.equals(msgType)) {
            return getBlockchain();
        }
        if (MessageType.HASH_FOUND.equals(msgType)) {
            return updateBlockchainAndTerminateMiner(message);
        }
        if (MessageType.MINE_BLOCK.equals(msgType)) {
            return mineBlock(message);
        }
        if (MessageType.GET_NODES.equals(msgType)) {
            return getNodes();
        }
        if (MessageType.JOIN_NODES.equals(msgType)) {
            return setNewNode(message);
        }
        throw new RuntimeException("Undefined messageType: " + msgType);
    }


    private Message getBlockchain() {
        System.out.println(Main.getBlockchain());
        return new Message(MessageType.GET_BLOCKCHAIN, Main.getBlockchain());
    }

    private Message updateBlockchainAndTerminateMiner(Message message) {
        if (miner != null && miner.isAlive()) {
            miner.stop();
            miner = null;
        }
        Block newBlock = (Block) message.getData();
        Main.addBlock(newBlock);
        return new Message(MessageType.HASH_FOUND, "OK");
    }

    private Message mineBlock(Message message) {
        String blockData = (String) message.getData();

        List<Block> currentBlocks = Main.getBlockchain().getBlocks();
        NotMinedBlock toBeMinedBlock = new NotMinedBlock(currentBlocks.size(), Instant.now(), blockData, currentBlocks.get(currentBlocks.size() - 1).getCurrHash());
        if (miner != null) {
            miner.stop();
            miner = null;
        }
        miner = new Thread(new MinerThread(toBeMinedBlock));
        miner.start();
        return new Message(MessageType.MINE_BLOCK, "MINING...");
    }

    private Message getNodes() {
        return new Message(MessageType.GET_NODES, Main.getNodePorts());
    }

    private Message setNewNode(Message message) {
        Integer newNodePort = (Integer) message.getData();
        Main.addNode(newNodePort);
        return new Message(MessageType.JOIN_NODES, "OK");
    }
}
