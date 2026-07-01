package com.company.DSA.srivatsanDSA;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/* ============================================================
 *  Source: Srivatsan's DSA notes — Page 36
 *  Problem: Course Schedule
 *  Given numCourses and prerequisites pairs [a, b] (b must come before a),
 *  return true if all courses can be finished (no cycle).
 *
 *  APPROACH (from notes):
 *    Topological-sort with Kahn's algorithm (BFS).
 *    Build adjacency list + indegree[].
 *    Push every course with indegree 0 to a queue.
 *    Pop nodes; decrement indegree of neighbors; if 0 push.
 *    Count finished courses; equals numCourses ⇒ no cycle ⇒ true.
 * ============================================================ */
public class CourseSchedule {

    public boolean canFinish(final int totalCourses, final int[][] prerequisites) {
        final List<List<Integer>> adjacency = new ArrayList<>();
        for (int courseId = 0; courseId < totalCourses; courseId++) {
            adjacency.add(new ArrayList<>());
        }

        final int[] inboundEdgeCount = new int[totalCourses];
        for (final int[] requirement : prerequisites) {
            final int dependentCourse = requirement[0];
            final int prerequisiteCourse = requirement[1];
            adjacency.get(prerequisiteCourse).add(dependentCourse);
            inboundEdgeCount[dependentCourse]++;
        }

        final Deque<Integer> readyQueue = new ArrayDeque<>();
        for (int courseId = 0; courseId < totalCourses; courseId++) {
            if (inboundEdgeCount[courseId] == 0) {
                readyQueue.offer(courseId);
            }
        }

        int finishedCount = 0;
        while (!readyQueue.isEmpty()) {
            final int finishedCourse = readyQueue.poll();
            finishedCount++;

            for (final int unlockedCourse : adjacency.get(finishedCourse)) {
                if (--inboundEdgeCount[unlockedCourse] == 0) {
                    readyQueue.offer(unlockedCourse);
                }
            }
        }
        return finishedCount == totalCourses;
    }
}
