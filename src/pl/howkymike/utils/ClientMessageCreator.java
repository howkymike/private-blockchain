package pl.howkymike.utils;

import pl.howkymike.Main;
import pl.howkymike.model.Block;
import pl.howkymike.model.Message;
import pl.howkymike.model.MessageType;

public final class ClientMessageCreator {
    public static Message createMessage(MessageType msgType, Object data) {
        if (MessageType.GET_BLOCKCHAIN.equals(msgType)) {
            return createBlockchainRequest();
        }
        if (MessageType.HASH_FOUND.equals(msgType)) {
            return createHashFoundRequest((Block) data);
        }
        if (MessageType.MINE_BLOCK.equals(msgType)) {
            return createMineBlockRequest((String) data);
        }
        if (MessageType.GET_NODES.equals(msgType)) {
            return createNodeRequest();
        }
        if (MessageType.JOIN_NODES.equals(msgType)) {
            return createJoinNodesRequest();
        }
        throw new RuntimeException("Undefined messageType: " + msgType);
    }

    private static Message createBlockchainRequest() {
        return new Message(MessageType.GET_BLOCKCHAIN, "");
    }

    private static Message createHashFoundRequest(Block b) {
        return new Message(MessageType.HASH_FOUND, b);
    }

    private static Message createMineBlockRequest(String blockData) {
        return new Message(MessageType.MINE_BLOCK, blockData);
    }

    private static Message createNodeRequest() {
        return new Message(MessageType.GET_NODES, "");
    }

    private static Message createJoinNodesRequest() {
        return new Message(MessageType.JOIN_NODES, Main.getOwnServerPort());
    }
}
