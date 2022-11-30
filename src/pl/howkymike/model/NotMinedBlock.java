package pl.howkymike.model;

import java.io.Serializable;
import java.time.Instant;

public class NotMinedBlock implements Serializable {
    private int index;
    private Instant timestamp;
    private String data;
    private String prevHash;

    public NotMinedBlock(int index, Instant timestamp, String data, String prevHash) {
        this.prevHash = prevHash;
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
    }


    public int getIndex() {
        return index;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getPrevHash() {
        return prevHash;
    }

    @Override
    public String toString() {
        return "NotMinedBlock{" +
                "index=" + index +
                ", timestamp=" + timestamp +
                ", data='" + data + '\'' +
                ", prevHash='" + prevHash + '\'' +
                '}';
    }
}
