package com.company.DSA.KafkaLLD;

/**
 * Runnable demo of the in-memory Kafka-style pub-sub.
 *
 * Shows:
 *   - producing keyed messages (same key -> same partition, preserving order)
 *   - one consumer group with 2 consumers splitting 3 partitions
 *   - a SECOND consumer group independently reading the same data (own offsets)
 *   - offsets are remembered — a second poll only sees NEW messages
 *
 * Run:  javac *.java && java com.company.DSA.KafkaLLD.KafkaDemo
 */
public class KafkaDemo {

    private static final String TOPIC_NAME       = "orders";
    private static final int    PARTITION_COUNT  = 3;

    public static void main(final String[] args) {
        final Broker broker = new Broker(new KeyHashPartitioner());
        broker.createTopic(TOPIC_NAME, PARTITION_COUNT);

        produceInitialBatch(broker);
        runAnalyticsGroup(broker);
        runBillingGroup(broker);
        produceAdditionalBatchAndRepoll(broker);

        System.out.println("\n=== Done ===");
    }

    // ---------- steps ----------

    private static void produceInitialBatch(final Broker broker) {
        System.out.println("=== Producing 6 messages ===");
        final Producer producer = new Producer(broker);

        producer.send(TOPIC_NAME, "user_1", "order A");
        producer.send(TOPIC_NAME, "user_2", "order B");
        producer.send(TOPIC_NAME, "user_1", "order C"); // same key -> same partition as A
        producer.send(TOPIC_NAME, "user_3", "order D");
        producer.send(TOPIC_NAME, "user_2", "order E"); // same key -> same partition as B
        producer.send(TOPIC_NAME, "user_1", "order F"); // same key -> same partition as A, C
    }

    private static void runAnalyticsGroup(final Broker broker) {
        System.out.println("\n=== analytics-group (2 consumers, 3 partitions) ===");
        final ConsumerGroup analyticsGroup = new ConsumerGroup("analytics-group", broker);
        analyticsGroup.addConsumer("C1");
        analyticsGroup.addConsumer("C2");
        analyticsGroup.assign(TOPIC_NAME);

        System.out.println("  -- polling --");
        analyticsGroup.pollAll(TOPIC_NAME);
    }

    private static void runBillingGroup(final Broker broker) {
        System.out.println("\n=== billing-group (1 consumer, own offsets) ===");
        final ConsumerGroup billingGroup = new ConsumerGroup("billing-group", broker);
        billingGroup.addConsumer("B1");
        billingGroup.assign(TOPIC_NAME);

        System.out.println("  -- polling --");
        billingGroup.pollAll(TOPIC_NAME);
    }

    private static void produceAdditionalBatchAndRepoll(final Broker broker) {
        System.out.println("\n=== Produce 2 more, analytics polls again ===");
        final Producer producer = new Producer(broker);
        producer.send(TOPIC_NAME, "user_1", "order G");
        producer.send(TOPIC_NAME, "user_4", "order H");

        // Re-create the analytics group only to demo that offsets are per-group,
        // stored in the broker, so a re-poll picks up ONLY the new messages.
        final ConsumerGroup analyticsGroup = new ConsumerGroup("analytics-group", broker);
        analyticsGroup.addConsumer("C1");
        analyticsGroup.addConsumer("C2");
        analyticsGroup.assign(TOPIC_NAME);
        analyticsGroup.pollAll(TOPIC_NAME);
    }
}
