package prereqchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * AdjListInputFile name is passed through the command line as args[0]
 * Read from AdjListInputFile with the format:
 * 1. a (int): number of courses in the graph
 * 2. a lines, each with 1 course ID
 * 3. b (int): number of edges in the graph
 * 4. b lines, each with a source ID
 * 
 * Step 2:
 * ValidPreReqInputFile name is passed through the command line as args[1]
 * Read from ValidPreReqInputFile with the format:
 * 1. 1 line containing the proposed advanced course
 * 2. 1 line containing the proposed prereq to the advanced course
 * 
 * Step 3:
 * ValidPreReqOutputFile name is passed through the command line as args[2]
 * Output to ValidPreReqOutputFile with the format:
 * 1. 1 line, containing either the word "YES" or "NO"
 */
public class ValidPrereq {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.ValidPrereq <adjacency list INput file> <valid prereq INput file> <valid prereq OUTput file>");
            return;
        }
        String adjListInputFileName = args[0];
        StdIn.setFile(adjListInputFileName);

        int numCourses = StdIn.readInt();
        String[] courseIDs = new String[numCourses];
        for (int i = 0; i < numCourses; i++) {
            courseIDs[i] = StdIn.readString();
        }

        int numEdges = StdIn.readInt();
        Map<String, List<String>> adjacencyList = new HashMap<>();
        for (int i = 0; i < numEdges; i++) {
            String courseID = StdIn.readString();
            String prereqID = StdIn.readString();
            adjacencyList.computeIfAbsent(courseID, k -> new ArrayList<>()).add(prereqID);
        }

        String validPreReqInputFileName = args[1];
        StdIn.setFile(validPreReqInputFileName);
        String advancedCourse = StdIn.readString();
        String proposedPrereq = StdIn.readString();

        String validPreReqOutputFileName = args[2];
        StdOut.setFile(validPreReqOutputFileName);

        if (isRedundantPrereq(adjacencyList, advancedCourse, proposedPrereq)) {
            StdOut.println("YES");
        } else {
            StdOut.println("NO");
        }
    }

    private static boolean isRedundantPrereq(Map<String, List<String>> adjacencyList, String advancedCourse,
            String proposedPrereq) {
        Set<String> visited = new HashSet<>();
        visited.add(advancedCourse);
        return isAncestorOrCreatesCycle(adjacencyList, advancedCourse, proposedPrereq, visited);
    }

    private static boolean isAncestorOrCreatesCycle(Map<String, List<String>> adjacencyList, String currentCourse,
            String targetPrereq, Set<String> visited) {
        List<String> prereqs = adjacencyList.getOrDefault(currentCourse, new ArrayList<>());
        for (String prereq : prereqs) {
            if (prereq.equals(targetPrereq)) {
                return true;
            }
            if (visited.contains(prereq)) {
                return false;
            }
            visited.add(prereq);
            if (isAncestorOrCreatesCycle(adjacencyList, prereq, targetPrereq, visited)) {
                return true;
            }
            visited.remove(prereq);
        }
        return false;
    }
}
