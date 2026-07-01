package com.company.DSA.srivatsanDSA;

import java.util.HashMap;
import java.util.Map;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 16-18
 *  Problem: LRU Cache  (capacity-bounded least-recently-used cache)
 *
 *  APPROACH (from notes):
 *    HashMap<key, Node> for O(1) lookup.
 *    Doubly linked list to maintain recency order:
 *      head sentinel <-> [most recently used] <-> ... <-> [LRU] <-> tail sentinel
 *    get(k): if missing return -1; else move node next to head; return val.
 *    put(k,v): if exists, update + move to head;
 *              else create node, add after head;
 *                   if size > capacity → remove the node before tail.
 * ============================================================ */
public class LRUCache {

    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(final int key, final int value) {
            this.key   = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<Integer, Node> nodeByKey = new HashMap<>();
    private final Node mostRecentSentinel = new Node(0, 0);
    private final Node leastRecentSentinel = new Node(0, 0);

    public LRUCache(final int capacity) {
        this.capacity = capacity;
        mostRecentSentinel.next = leastRecentSentinel;
        leastRecentSentinel.prev = mostRecentSentinel;
    }

    public int get(final int key) {
        final Node existingNode = nodeByKey.get(key);
        if (existingNode == null) {
            return -1;
        }
        moveToMostRecent(existingNode);
        return existingNode.value;
    }

    public void put(final int key, final int value) {
        final Node existingNode = nodeByKey.get(key);
        if (existingNode != null) {
            existingNode.value = value;
            moveToMostRecent(existingNode);
            return;
        }

        final Node freshNode = new Node(key, value);
        nodeByKey.put(key, freshNode);
        insertAfterMostRecentSentinel(freshNode);

        if (nodeByKey.size() > capacity) {
            final Node leastRecentNode = leastRecentSentinel.prev;
            removeNode(leastRecentNode);
            nodeByKey.remove(leastRecentNode.key);
        }
    }

    private void moveToMostRecent(final Node node) {
        removeNode(node);
        insertAfterMostRecentSentinel(node);
    }

    private void insertAfterMostRecentSentinel(final Node node) {
        node.prev = mostRecentSentinel;
        node.next = mostRecentSentinel.next;
        mostRecentSentinel.next.prev = node;
        mostRecentSentinel.next      = node;
    }

    private void removeNode(final Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}
