package com.company.DSA.KafkaLLD;

/**
 * Demo of the in-memory Kafka-style pub-sub.
 *
 * Shows:
 *  - producing keyed messages (same key -> same partition, ordered)
 *  - one group with 2 consumers splitting partitions
 *  - a SECOND group independently reading the same data (separate offsets)
 *
 * Run: javac *.java && java KafkaDemo
 */
public class KafkaDemo {
    public static void main(String[] args) {
        Broker broker = new Broker(new KeyHashPartitioner());
        broker.createTopic("orders", 3);   // 3 partitions

        // ---- Produce ----
        System.out.println("=== Producing 6 messages ===");
        Producer producer = new Producer(broker);
        producer.send("orders", "user_1", "order A");
        producer.send("orders", "user_2", "order B");
        producer.send("orders", "user_1", "order C");   // same key -> same partition as A
        producer.send("orders", "user_3", "order D");
        producer.send("orders", "user_2", "order E");   // same key -> same partition as B
        producer.send("orders", "user_1", "order F");   // same key -> same partition as A,C

        // ---- Group 1: analytics, 2 consumers split 3 partitions ----
        System.out.println("\n=== analytics-group (2 consumers, 3 partitions) ===");
        ConsumerGroup analytics = new ConsumerGroup("analytics-group", broker);
        analytics.addConsumer("C1");
        analytics.addConsumer("C2");
        analytics.assign("orders");
        System.out.println("  -- polling --");
        analytics.pollAll("orders");

        // ---- Group 2: billing, reads the SAME data independently ----
        System.out.println("\n=== billing-group (1 consumer, own offsets) ===");
        ConsumerGroup billing = new ConsumerGroup("billing-group", broker);
        billing.addConsumer("B1");
        billing.assign("orders");
        System.out.println("  -- polling --");
        billing.pollAll("orders");

        // ---- Produce more, analytics polls again (offsets resume) ----
        System.out.println("\n=== Produce 2 more, analytics polls again ===");
        producer.send("orders", "user_1", "order G");
        producer.send("orders", "user_4", "order H");
        analytics.pollAll("orders");   // only sees the NEW messages (offsets remembered)

        System.out.println("\n=== Done ===");
    }
}
