package com.company.DSA.KafkaLLD;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The central broker (single-JVM, in-memory).
 *
 * Holds all topics, routes publishes through the Partitioner, and owns the
 * OFFSET STORE -- the bookmark of how far each consumer GROUP has read in
 * each partition.
 *
 * Offset store shape:  groupId -> (partitionId -> next offset to read)
 * Two different groups reading the same partition keep independent offsets,
 * so they never interfere.
 *
 * Real Kafka note: a real broker holds only a subset of partitions, replicated
 * across a cluster, and offsets live in an internal __consumer_offsets topic.
 * This single-broker, in-memory model scopes that out.
 */
public class Broker {
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final Partitioner partitioner;

    // groupId -> (partitionId -> next offset)
    private final Map<String, Map<Integer, Integer>> offsets = new ConcurrentHashMap<>();

    public Broker(Partitioner partitioner) {
        this.partitioner = partitioner;
    }

    public void createTopic(String name, int numPartitions) {
        topics.put(name, new Topic(name, numPartitions));
    }

    public Topic getTopic(String name) {
        return topics.get(name);
    }

    // ===== PRODUCE =====
    public void publish(String topicName, Message m) {
        Topic topic = topics.get(topicName);
        int partitionId = partitioner.selectPartition(m.getKey(), topic.getPartitionCount());
        topic.getPartition(partitionId).append(m);
        System.out.println("  [produce] " + m + " -> " + topicName + " p" + partitionId);
    }

    // ===== CONSUME =====
    // Read at the group's current offset for this partition, then advance it.
    public Message poll(String topicName, int partitionId, String groupId) {
        Topic topic = topics.get(topicName);
        Partition partition = topic.getPartition(partitionId);

        // 1. this group's bookmark for this partition (default 0)
        Map<Integer, Integer> groupOffsets =
                offsets.computeIfAbsent(groupId, g -> new ConcurrentHashMap<>());
        int currentOffset = groupOffsets.getOrDefault(partitionId, 0);

        // 2. read at the bookmark
        Message m = partition.read(currentOffset);

        // 3. advance only if we actually got a message
        if (m != null) {
            groupOffsets.put(partitionId, currentOffset + 1);
        }
        // 4. (null means caught up -> bookmark stays, returns null)
        return m;
    }

    /** Current committed offset for a group+partition (for visibility/debugging). */
    public int getOffset(String groupId, int partitionId) {
        return offsets.getOrDefault(groupId, new ConcurrentHashMap<>())
                      .getOrDefault(partitionId, 0);
    }
}
