package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads messages from the partitions it has been ASSIGNED (by its group).
 *
 * A Consumer does not choose partitions itself — the ConsumerGroup assigns them.
 * For each assigned partition, it polls the broker, which tracks the offset.
 */
public class Consumer {

    private final String consumerId;
    private final String groupId;
    private final Broker broker;
    private final List<Integer> assignedPartitionIds = new ArrayList<>();

    public Consumer(final String consumerId, final String groupId, final Broker broker) {
        this.consumerId = consumerId;
        this.groupId    = groupId;
        this.broker     = broker;
    }

    public void assignPartition(final int partitionId) {
        assignedPartitionIds.add(partitionId);
    }

    public void clearAssignments() {
        assignedPartitionIds.clear();
    }

    /**
     * Poll all assigned partitions once, draining whatever is available.
     * In real Kafka this is the poll() loop with max.poll.records + timeout;
     * we drain to completion for the demo.
     */
    public void poll(final String topicName) {
        for (final int partitionId : assignedPartitionIds) {
            drainPartition(topicName, partitionId);
        }
    }

    private void drainPartition(final String topicName, final int partitionId) {
        Message message;
        while ((message = broker.poll(topicName, partitionId, groupId)) != null) {
            System.out.println("  [" + groupId + "/" + consumerId + "] read p"
                    + partitionId + ": " + message);
        }
    }

    public String getConsumerId() {
        return consumerId;
    }

    public List<Integer> getAssignedPartitions() {
        return new ArrayList<>(assignedPartitionIds);
    }
}
