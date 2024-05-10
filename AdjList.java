package prereqchecker;

import java.util.*;

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
 * AdjListOutputFile name is passed through the command line as args[1]
 * Output to AdjListOutputFile with the format:
 * 1. c lines, each starting with a different course ID, then
 * listing all of that course's prerequisites (space separated)
 */
public class AdjList {
    public static void main(String[] args) {

        if (args.length < 2) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.AdjList <adjacency list INput file> <adjacency list OUTput file>");
            return;
        }
        String inputFileName = args[0];
        StdIn.setFile(inputFileName);

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

        String outputFileName = args[1];
        StdOut.setFile(outputFileName);

        for (String courseID : courseIDs) {
            List<String> prerequisites = adjacencyList.getOrDefault(courseID, new ArrayList<>());

            StdOut.print(courseID);
            for (String prereq : prerequisites) {
                StdOut.print(" " + prereq);
            }
            StdOut.println();
        }
    }
}
