package com.company.DSA.KafkaLLD;

/**
 * A single record published to a topic.
 *
 * key       -> used by the Partitioner to decide which partition
 *              (same key -> same partition -> preserved order for that key)
 * value     -> the payload
 * timestamp -> broker-side ingestion time (client-side clock in this simplified model)
 *
 * Immutable — safe to hand between producer, broker, and consumer without copying.
 */
public final class Message {

    private final String key;
    private final String value;
    private final long   timestamp;

    public Message(final String key, final String value) {
        this.key       = key;
        this.value     = value;
        this.timestamp = System.currentTimeMillis();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Message(key=" + key + ", value=" + value + ")";
    }
}
