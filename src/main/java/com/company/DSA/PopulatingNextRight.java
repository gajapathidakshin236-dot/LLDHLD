package com.company.DSA;

/**
 * LeetCode #116 - Populating Next Right Pointers in Each Node (perfect tree)
 * Use already-linked next on the level above to traverse without queue.
 * Time: O(n)  Space: O(1)
 */
public class PopulatingNextRight {
    static class Node { int val; Node left, right, next; Node(int v) { val = v; } }
    public Node connect(Node root) {
        Node leftmost = root;
        while (leftmost != null && leftmost.left != null) {
            Node head = leftmost;
            while (head != null) {
                head.left.next = head.right;
                if (head.next != null) head.right.next = head.next.left;
                head = head.next;
            }
            leftmost = leftmost.left;
        }
        return root;
    }
}
