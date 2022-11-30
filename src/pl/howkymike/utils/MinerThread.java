package pl.howkymike.utils;

import pl.howkymike.Main;
import pl.howkymike.model.Block;
import pl.howkymike.model.NotMinedBlock;

public class MinerThread implements Runnable {

    private final NotMinedBlock block;

    public MinerThread(NotMinedBlock block) {
        this.block = block;
    }

    @Override
    public void run() {
        Block b = new Block(block);
        System.out.print("I mined a block! (" + Main.getOwnServerPort() + ")" + System.lineSeparator());
        Main.addBlock(b);
    }
}
