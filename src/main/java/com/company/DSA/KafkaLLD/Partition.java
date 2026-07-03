package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A partition is an ordered, append-only LOG of messages.
 * The list index IS the offset — message at index 0 has offset 0, etc.
 *
 * Ordering is guaranteed WITHIN a partition (append order), which is why
 * messages that must stay ordered (same key) are routed to the same partition.
 *
 * Methods are synchronized: producers may append while consumers read
 * concurrently.
 */
public class Partition {

    private final int partitionId;
    private final List<Message> messageLog = new ArrayList<>();

    public Partition(final int partitionId) {
        this.partitionId = partitionId;
    }

    /** Append a message; its offset is its resulting index in the log. */
    public synchronized void append(final Message message) {
        messageLog.add(message);
    }

    /** Read the message at a given offset, or null if we've caught up. */
    public synchronized Message read(final int offset) {
        if (offset < 0 || offset >= messageLog.size()) {
            return null;
        }
        return messageLog.get(offset);
    }

    public synchronized int size() {
        return messageLog.size();
    }

    public int getPartitionId() {
        return partitionId;
    }
}
