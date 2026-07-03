package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A consumer group. Holds consumers and ASSIGNS partitions among them so that
 * each partition is owned by exactly one consumer in the group (the core rule).
 *
 * Assignment here is simple round-robin of partitions across consumers.
 * Real Kafka uses a Group Coordinator + rebalancing protocol; scoped out here.
 *
 * Rule encoded: partitions are the unit of parallelism. If there are MORE
 * consumers than partitions, the extra consumers get nothing (sit idle).
 */
public class ConsumerGroup {

    private final String groupId;
    private final Broker broker;
    private final List<Consumer> consumers = new ArrayList<>();

    public ConsumerGroup(final String groupId, final Broker broker) {
        this.groupId = groupId;
        this.broker  = broker;
    }

    public Consumer addConsumer(final String consumerId) {
        final Consumer freshConsumer = new Consumer(consumerId, groupId, broker);
        consumers.add(freshConsumer);
        return freshConsumer;
    }

    /**
     * Assign a topic's partitions across this group's consumers, round-robin.
     * Called on group join / rebalance. Each partition → exactly one consumer.
     */
    public void assign(final String topicName) {
        final int partitionCount = broker.getTopic(topicName).getPartitionCount();

        clearAllAssignments();
        distributePartitionsRoundRobin(partitionCount);
        printAssignmentSnapshot(topicName);
    }

    /** Every consumer polls its assigned partitions once. */
    public void pollAll(final String topicName) {
        for (final Consumer consumer : consumers) {
            consumer.poll(topicName);
        }
    }

    public String getGroupId() {
        return groupId;
    }

    // ---------- helpers ----------

    private void clearAllAssignments() {
        for (final Consumer consumer : consumers) {
            consumer.clearAssignments();
        }
    }

    private void distributePartitionsRoundRobin(final int partitionCount) {
        if (consumers.isEmpty()) {
            return;
        }
        for (int partitionId = 0; partitionId < partitionCount; partitionId++) {
            final Consumer owner = consumers.get(partitionId % consumers.size());
            owner.assignPartition(partitionId);
        }
    }

    private void printAssignmentSnapshot(final String topicName) {
        System.out.println("  [rebalance] group '" + groupId + "' over " + topicName + ":");
        for (final Consumer consumer : consumers) {
            System.out.println("     " + consumer.getConsumerId()
                    + " -> partitions " + consumer.getAssignedPartitions());
        }
    }
}
