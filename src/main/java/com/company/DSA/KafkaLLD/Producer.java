package com.company.DSA.KafkaLLD;

/**
 * Publishes messages to a topic via the broker.
 *
 * Thin client wrapper — the broker + partitioner do the routing work.
 * In real Kafka the producer batches, compresses, and retries; scoped out here.
 */
public class Producer {

    private final Broker broker;

    public Producer(final Broker broker) {
        this.broker = broker;
    }

    public void send(final String topicName, final String key, final String value) {
        broker.publish(topicName, new Message(key, value));
    }
}
