package com.company.DSA.KafkaLLD;

/**
 * A single record published to a topic.
 * key   -> used by the Partitioner to decide which partition (same key = same partition)
 * value -> the payload
 */
public class Message {
    private final String key;
    private final String value;
    private final long timestamp;

    public Message(String key, String value) {
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public String getKey()     { return key; }
    public String getValue()   { return value; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Message(key=" + key + ", value=" + value + ")";
    }
}
