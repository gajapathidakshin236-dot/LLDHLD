package com.company.DSA.KafkaLLD;

import java.util.ArrayList;
import java.util.List;

/**
 * A named topic, split into a fixed number of partitions.
 *
 * Partitions give:
 *   - parallelism (different consumers read different partitions)
 *   - per-partition ordering
 */
public class Topic {

    private final String name;
    private final List<Partition> partitions = new ArrayList<>();

    public Topic(final String name, final int partitionCount) {
        this.name = name;
        for (int partitionId = 0; partitionId < partitionCount; partitionId++) {
            partitions.add(new Partition(partitionId));
        }
    }

    /** Get a partition by id (0 .. partitionCount-1). NOT an offset. */
    public Partition getPartition(final int partitionId) {
        return partitions.get(partitionId);
    }

    public int getPartitionCount() {
        return partitions.size();
    }

    public String getName() {
        return name;
    }
}
