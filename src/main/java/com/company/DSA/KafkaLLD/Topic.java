package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A named topic, split into a fixed number of partitions.
 * Partitions give parallelism (different consumers read different partitions)
 * and per-partition ordering.
 */
public class Topic {
    private final String name;
    private final List<Partition> partitions = new ArrayList<>();

    public Topic(String name, int numPartitions) {
        this.name = name;
        for (int i = 0; i < numPartitions; i++) {
            partitions.add(new Partition(i));
        }
    }

    /** Get a partition by its id (0 .. numPartitions-1). NOT an offset. */
    public Partition getPartition(int partitionId) {
        return partitions.get(partitionId);
    }

    public int getPartitionCount() { return partitions.size(); }
    public String getName()        { return name; }
}
