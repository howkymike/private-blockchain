package pl.howkymike.model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Block extends NotMinedBlock implements Serializable {
    private static final int DIFFICULTY = 4;

    private final String currHash;
    private int nonce;

    public Block(NotMinedBlock block) {
        this(block.getIndex(), block.getTimestamp(), block.getData(), block.getPrevHash());
    }

    public Block(int index, Instant timestamp, String data, String prevHash) {
        super(index, timestamp, data, prevHash);
        this.currHash = proofOfWork(DIFFICULTY);
        System.out.println("New block: " + this);
    }

    private Block(int index, Instant timestamp, String data, String prevHash, int nonce) {
        // for the genesis block creation
        super(index, timestamp, data, prevHash);
        this.nonce = nonce;
        this.currHash = proofOfWork(DIFFICULTY);
    }

    public static Block getGenesisBlock() {
        return new Block(0, Instant.now(), "Genesis block", "", 0);
    }

    private String calcBlockHash() {
        String hashData = getIndex() + getTimestamp().toString() + getData() + getPrevHash() + this.nonce;
        byte[] hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            hash = messageDigest.digest(hashData.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown : " + e);
        }
        return bytesToHex(hash);
    }

    /**
     * Proof of work Mining method. It finds a hash with a @zerocount zeroes.
     * @param zeroCount number of zeroes the hash must have to be valid
     * @return the valid hash
     */
    private String proofOfWork(int zeroCount) {
        this.nonce = 0;
        String hash = calcBlockHash();

        String requiredPrefix = "0".repeat(zeroCount); //Stream.generate(() -> "0").limit(zeroCount).collect(Collectors.joining());

        while(!requiredPrefix.equals(hash.substring(0,zeroCount))) {
            this.nonce = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE - 1); //this.nonce++;
            hash = calcBlockHash();
        }
        return hash;
    }

    /**
     * Private helper class to convert bytes to hex
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String getCurrHash() {
        return currHash;
    }

    public int getNonce() {
        return nonce;
    }

    @Override
    public String toString() {
        return "\r\n    Block{" +
                "i=" + getIndex() +
                ", ts=" + getTimestamp() +
                ", d='" + getData() + '\'' +
                ", pHash='" + humanReadableHash(getPrevHash()) + '\'' +
                ", nonce=" + nonce +
                ", cHash='" + humanReadableHash(currHash) + '\'' +
                "}";
    }

    private static String humanReadableHash(String hash) {
        if(hash != null && hash.length() > 8) {
            int len = hash.length();
            return hash.substring(0,4) + "..." + hash.substring(len-4,len);
        }
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return nonce == block.nonce && Objects.equals(currHash, block.currHash)
                && getIndex() == block.getIndex();
    }

    @Override
    public int hashCode() {
        return Objects.hash(currHash, nonce, getIndex());
    }
}
