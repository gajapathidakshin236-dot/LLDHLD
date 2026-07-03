package com.company.DSA.KafkaLLD;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The central broker (single-JVM, in-memory).
 *
 * Responsibilities:
 *   - Owns all topics.
 *   - Routes publishes through the Partitioner.
 *   - Owns the OFFSET STORE — the bookmark of how far each consumer GROUP has
 *     read in each partition.
 *
 * Offset store shape:
 *   Map< groupId , Map< partitionId , nextOffsetToRead > >
 *
 * Two different groups reading the same partition keep independent offsets,
 * so they never interfere.
 *
 * Real Kafka note: a real broker holds only a subset of partitions, replicated
 * across a cluster, and offsets live in an internal __consumer_offsets topic.
 * This single-broker, in-memory model scopes that out.
 */
public class Broker {

    private static final int INITIAL_OFFSET = 0;

    private final Map<String, Topic> topicsByName            = new ConcurrentHashMap<>();
    private final Map<String, Map<Integer, Integer>> offsetsByGroup = new ConcurrentHashMap<>();

    private final Partitioner partitioner;

    public Broker(final Partitioner partitioner) {
        this.partitioner = partitioner;
    }

    public void createTopic(final String topicName, final int partitionCount) {
        topicsByName.put(topicName, new Topic(topicName, partitionCount));
    }

    public Topic getTopic(final String topicName) {
        return topicsByName.get(topicName);
    }

    // ===== PRODUCE =====

    public void publish(final String topicName, final Message message) {
        final Topic topic          = topicsByName.get(topicName);
        final int   partitionId    = partitioner.selectPartition(message.getKey(), topic.getPartitionCount());
        final Partition partition  = topic.getPartition(partitionId);

        partition.append(message);
        System.out.println("  [produce] " + message + " -> " + topicName + " p" + partitionId);
    }

    // ===== CONSUME =====

    /**
     * Read at the group's current offset for this partition, then advance it.
     * Returns null when the group has caught up (nothing new).
     */
    public Message poll(final String topicName, final int partitionId, final String groupId) {
        final Topic topic          = topicsByName.get(topicName);
        final Partition partition  = topic.getPartition(partitionId);

        final Map<Integer, Integer> offsetsForGroup =
                offsetsByGroup.computeIfAbsent(groupId, ignored -> new ConcurrentHashMap<>());

        final int currentOffset = offsetsForGroup.getOrDefault(partitionId, INITIAL_OFFSET);
        final Message message   = partition.read(currentOffset);

        if (message != null) {
            offsetsForGroup.put(partitionId, currentOffset + 1);
        }
        return message;
    }

    /** Committed offset for a group + partition (for visibility / debugging). */
    public int getOffset(final String groupId, final int partitionId) {
        final Map<Integer, Integer> offsetsForGroup = offsetsByGroup.get(groupId);
        if (offsetsForGroup == null) {
            return INITIAL_OFFSET;
        }
        return offsetsForGroup.getOrDefault(partitionId, INITIAL_OFFSET);
    }
}
