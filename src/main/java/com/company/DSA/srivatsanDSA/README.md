# srivatsanDSA

Java solutions corresponding to problems found in **Srivatsan's DSA notes.pdf**
(85-page handwritten notebook).

The folder was created from the user's request and the package name uses
`srivatsanDSA` (without a space) so the Java package declaration is legal —
the original phrase "srivatsan DSA" is preserved in this README.

## Coverage map  (problem → notes page)

| # | Problem                                  | Notes page | Topic              |
|---|------------------------------------------|------------|--------------------|
| 1 | TwoSum                                   |   2 – 3    | hashing            |
| 2 | ContainsDuplicate                        |   4 – 5    | hashing            |
| 3 | BestTimeToBuySellStock                   |   6 – 7    | array              |
| 4 | MoveZeroes                               |   11       | two pointers       |
| 5 | MergeSortedArray                         |   12       | two pointers       |
| 6 | ValidAnagram                             |  13 – 15   | strings            |
| 7 | LRUCache                                 |  16 – 18   | design             |
| 8 | ValidateBST                              |  19 – 22   | BST                |
| 9 | DiameterOfBinaryTree                     |   23       | tree DFS           |
|10 | PathSumII                                |  24 – 25   | tree DFS           |
|11 | BinaryTreeRightSideView                  |   26       | tree BFS           |
|12 | BinaryTreeZigzagLevelOrder               |   27       | tree BFS           |
|13 | CloneGraph                               |   32       | graph              |
|14 | CourseSchedule                           |   36       | topological sort   |
|15 | SlidingWindowMaximum                     |   38       | monotonic deque    |
|16 | TopKFrequent                             |   40       | bucket sort        |
|17 | ThreeSum                                 |   43       | two pointers       |
|18 | ValidParentheses                         |  44 – 45   | stack              |
|19 | GenerateParentheses                      |   46       | backtracking       |
|20 | LongestSubarrayPrefixSum                 |   48       | prefix sum         |
|21 | MergeIntervals                           |   50       | sorting + sweep    |
|22 | ReverseLinkedList                        |   56       | linked list        |
|23 | MergeTwoSortedLists                      |   59       | linked list        |
|24 | RemoveNthFromEnd                         |   60       | linked list        |
|25 | ValidPalindromeII                        |   64       | two pointers       |
|26 | CopyRandomList                           |   66       | linked list        |
|27 | JumpGame                                 |   70       | greedy             |
|28 | FindPeakElement                          |   73       | binary search      |
|29 | LongestIncreasingSubsequence             |   74       | DP                 |
|30 | StringPatternMatching (Word Pattern)     |   78       | hashing            |
|31 | LongestCommonPrefix                      |   80       | strings            |
|32 | Subsets                                  |   83       | backtracking       |
|33 | ShortestPathInDAG                        |   84       | graph              |

Plus shared helpers: `TreeNode.java`, `ListNode.java`.

Each Java file starts with a comment block that:
  * cites the original notes page,
  * paraphrases the approach as written in the notebook,
  * then provides a clean, Java-8 style implementation.
