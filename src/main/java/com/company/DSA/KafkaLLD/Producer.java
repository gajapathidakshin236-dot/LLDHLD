package com.company.DSA.KafkaLLD;

/**
 * Publishes messages to a topic via the broker.
 * Thin wrapper -- the broker + partitioner do the routing work.
 */
public class Producer {
    private final Broker broker;

    public Producer(Broker broker) {
        this.broker = broker;
    }

    public void send(String topic, String key, String value) {
        broker.publish(topic, new Message(key, value));
    }
}
