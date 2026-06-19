package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A consumer group. Holds consumers and ASSIGNS partitions among them so that
 * each partition is owned by exactly one consumer in the group (the core rule).
 *
 * Assignment here is simple round-robin of partitions across consumers. Real
 * Kafka uses a Group Coordinator + rebalancing protocol; we scope that out.
 *
 * Rule encoded: partitions are the unit of parallelism. If there are more
 * consumers than partitions, the extra consumers get nothing (sit idle).
 */
public class ConsumerGroup {
    private final String groupId;
    private final Broker broker;
    private final List<Consumer> consumers = new ArrayList<>();

    public ConsumerGroup(String groupId, Broker broker) {
        this.groupId = groupId;
        this.broker = broker;
    }

    public Consumer addConsumer(String consumerId) {
        Consumer c = new Consumer(consumerId, groupId, broker);
        consumers.add(c);
        return c;
    }

    /**
     * Assign a topic's partitions across this group's consumers, round-robin.
     * Called on join / rebalance. Each partition -> exactly one consumer.
     */
    public void assign(String topicName) {
        int numPartitions = broker.getTopic(topicName).getPartitionCount();

        // reset existing assignments (a rebalance)
        for (Consumer c : consumers) c.clearAssignments();

        // round-robin partitions across consumers
        for (int p = 0; p < numPartitions; p++) {
            Consumer owner = consumers.get(p % consumers.size());
            owner.assignPartition(p);
        }

        // show the assignment
        System.out.println("  [rebalance] group '" + groupId + "' over " + topicName + ":");
        for (Consumer c : consumers) {
            System.out.println("     " + c.getConsumerId() + " -> partitions "
                    + c.getAssignedPartitions());
        }
    }

    /** Every consumer polls its assigned partitions once. */
    public void pollAll(String topic) {
        for (Consumer c : consumers) {
            c.poll(topic);
        }
    }

    public String getGroupId() { return groupId; }
}
