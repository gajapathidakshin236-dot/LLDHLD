package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * Reads messages from the partitions it has been ASSIGNED (by its group).
 * It does not choose partitions itself -- the ConsumerGroup assigns them.
 * For each assigned partition, it polls the broker, which tracks the offset.
 */
public class Consumer {
    private final String consumerId;
    private final String groupId;
    private final Broker broker;
    private final List<Integer> assignedPartitions = new ArrayList<>();

    public Consumer(String consumerId, String groupId, Broker broker) {
        this.consumerId = consumerId;
        this.groupId = groupId;
        this.broker = broker;
    }

    public void assignPartition(int partitionId) {
        assignedPartitions.add(partitionId);
    }

    public void clearAssignments() {
        assignedPartitions.clear();
    }

    /**
     * Poll all assigned partitions once, draining whatever is available.
     * In real Kafka this is the poll() loop; here we drain to completion for the demo.
     */
    public void poll(String topic) {
        for (int partitionId : assignedPartitions) {
            Message m;
            while ((m = broker.poll(topic, partitionId, groupId)) != null) {
                System.out.println("  [" + groupId + "/" + consumerId + "] read p"
                        + partitionId + ": " + m);
            }
        }
    }

    public String getConsumerId()           { return consumerId; }
    public List<Integer> getAssignedPartitions() { return assignedPartitions; }
}
