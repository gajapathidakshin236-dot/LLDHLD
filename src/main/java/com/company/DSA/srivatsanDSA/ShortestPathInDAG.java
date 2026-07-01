package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 84
 *  Problem: Shortest Path in DAG (single-source, weighted, directed-acyclic)
 *
 *  APPROACH (from notes):
 *    Topological sort first (Kahn's BFS), then relax edges in topo order.
 *    distance[source] = 0; others = INF.
 *    For each node u in topo order:
 *       for each (u → v, weight w):
 *           if (dist[u] + w < dist[v]) dist[v] = dist[u] + w.
 *    Works because every predecessor of v is processed before v.
 *
 *  Edge representation here: edges[i] = {from, to, weight}.
 * ============================================================ */
public class ShortestPathInDAG {

    public int[] shortestPath(final int totalNodes, final int[][] edges, final int sourceNode) {
        final List<int[]>[] adjacencyList = buildAdjacencyList(totalNodes, edges);
        final int[] inboundEdgeCount      = countInboundEdges(totalNodes, edges);
        final int[] topologicalOrder      = topologicalSort(totalNodes, adjacencyList, inboundEdgeCount);

        final int[] shortestDistance = new int[totalNodes];
        Arrays.fill(shortestDistance, Integer.MAX_VALUE);
        shortestDistance[sourceNode] = 0;

        for (final int currentNode : topologicalOrder) {
            if (shortestDistance[currentNode] == Integer.MAX_VALUE) {
                continue;
            }
            for (final int[] outgoingEdge : adjacencyList[currentNode]) {
                final int neighborNode = outgoingEdge[0];
                final int edgeWeight   = outgoingEdge[1];
                final int candidate    = shortestDistance[currentNode] + edgeWeight;
                if (candidate < shortestDistance[neighborNode]) {
                    shortestDistance[neighborNode] = candidate;
                }
            }
        }
        return shortestDistance;
    }

    @SuppressWarnings("unchecked")
    private List<int[]>[] buildAdjacencyList(final int totalNodes, final int[][] edges) {
        final List<int[]>[] adjacencyList = new List[totalNodes];
        for (int nodeId = 0; nodeId < totalNodes; nodeId++) {
            adjacencyList[nodeId] = new ArrayList<>();
        }
        for (final int[] edge : edges) {
            adjacencyList[edge[0]].add(new int[] { edge[1], edge[2] });
        }
        return adjacencyList;
    }

    private int[] countInboundEdges(final int totalNodes, final int[][] edges) {
        final int[] inboundCount = new int[totalNodes];
        for (final int[] edge : edges) {
            inboundCount[edge[1]]++;
        }
        return inboundCount;
    }

    private int[] topologicalSort(final int totalNodes,
                                  final List<int[]>[] adjacencyList,
                                  final int[] inboundEdgeCount) {
        final Deque<Integer> readyQueue = new ArrayDeque<>();
        for (int nodeId = 0; nodeId < totalNodes; nodeId++) {
            if (inboundEdgeCount[nodeId] == 0) {
                readyQueue.offer(nodeId);
            }
        }
        final int[] topologicalOrder = new int[totalNodes];
        int writeIndex = 0;

        while (!readyQueue.isEmpty()) {
            final int finishedNode = readyQueue.poll();
            topologicalOrder[writeIndex++] = finishedNode;
            for (final int[] outgoingEdge : adjacencyList[finishedNode]) {
                if (--inboundEdgeCount[outgoingEdge[0]] == 0) {
                    readyQueue.offer(outgoingEdge[0]);
                }
            }
        }
        return topologicalOrder;
    }
}
