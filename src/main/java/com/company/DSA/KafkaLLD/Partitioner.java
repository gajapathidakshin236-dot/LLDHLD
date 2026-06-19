package com.company.DSA.KafkaLLD;

/**
 * STRATEGY: decides which partition a message goes to.
 * Swappable — Kafka lets you plug in custom partitioners.
 */
public interface Partitioner {
    int selectPartition(String key, int numPartitions);
}

/**
 * Key-hash partitioner: same key always maps to the same partition,
 * so all messages for a given key stay ordered together.
 * Null key -> partition 0 (could be round-robin instead).
 */
class KeyHashPartitioner implements Partitioner {
    @Override
    public int selectPartition(String key, int numPartitions) {
        if (key == null) return 0;
        return Math.abs(key.hashCode()) % numPartitions;
    }
}

/**
 * Round-robin partitioner: spreads messages evenly, ignores key.
 * Use when you don't need per-key ordering.
 */
class RoundRobinPartitioner implements Partitioner {
    private int counter = 0;
    @Override
    public synchronized int selectPartition(String key, int numPartitions) {
        int p = counter % numPartitions;
        counter++;
        return p;
    }
}
