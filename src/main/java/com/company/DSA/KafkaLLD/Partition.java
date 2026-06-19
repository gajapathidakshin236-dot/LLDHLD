package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A partition is an ordered, append-only log of messages.
 * The list index IS the offset: message at index 0 has offset 0, etc.
 *
 * Ordering is guaranteed WITHIN a partition (append order), which is why
 * messages that must stay ordered (same key) are routed to the same partition.
 *
 * Methods are synchronized: producers append while consumers read concurrently.
 */
public class Partition {
    private final int partitionId;
    private final List<Message> log = new ArrayList<>();   // index == offset

    public Partition(int partitionId) {
        this.partitionId = partitionId;
    }

    /** Append a message; its offset is its index in the log. */
    public synchronized void append(Message m) {
        log.add(m);
    }

    /** Read the message at a given offset, or null if we've caught up. */
    public synchronized Message read(int offset) {
        return (offset < log.size()) ? log.get(offset) : null;
    }

    public synchronized int size() { return log.size(); }
    public int getPartitionId()    { return partitionId; }
}
