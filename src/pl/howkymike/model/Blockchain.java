package pl.howkymike.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Blockchain implements Serializable {
    List<Block> blocks;

    public Blockchain() {
        blocks = Collections.synchronizedList(new ArrayList<>());
        blocks.add(Block.getGenesisBlock());
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void addBlock(Block b) {
        blocks.add(b);
    }

    @Override
    public String toString() {
        return "Blockchain{" +
                "blocks=" + blocks +
                '}';
    }
}
