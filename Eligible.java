package prereqchecker;

import java.util.*;

/**
 * 
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
 * EligibleInputFile name is passed through the command line as args[1]
 * Read from EligibleInputFile with the format:
 * 1. c (int): Number of courses
 * 2. c lines, each with 1 course ID
 * 
 * Step 3:
 * EligibleOutputFile name is passed through the command line as args[2]
 * Output to EligibleOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class Eligible {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java -cp bin prereqchecker.Eligible <adjacency list INput file> <eligible INput file> <eligible OUTput file>");
            return;
        }

        String adjListFileName = args[0];
        String eligibleInputFileName = args[1];
        String eligibleOutputFileName = args[2];

        StdIn.setFile(adjListFileName);

        int numCourses = StdIn.readInt();
        Map<String, List<String>> adjacencyList = new HashMap<>();

        for (int i = 0; i < numCourses; i++) {
            String course = StdIn.readString();
            adjacencyList.put(course, new ArrayList<>());
        }

        int numEdges = StdIn.readInt();
        for (int i = 0; i < numEdges; i++) {
            String course = StdIn.readString();
            String prereq = StdIn.readString();
            adjacencyList.get(course).add(prereq);
        }

        StdIn.setFile(eligibleInputFileName);

        int numCompletedCourses = StdIn.readInt();
        Set<String> completedCourses = new HashSet<>();
        for (int i = 0; i < numCompletedCourses; i++) {
            completedCourses.add(StdIn.readString());
        }

        Set<String> allCompletedCourses = new HashSet<>(completedCourses);
        for (String completed : completedCourses) {
            addAllPrerequisites(completed, adjacencyList, allCompletedCourses);
        }

        Set<String> eligibleCourses = determineEligibleCourses(adjacencyList, allCompletedCourses);

        StdOut.setFile(eligibleOutputFileName);
        for (String course : eligibleCourses) {
            StdOut.println(course);
        }
    }

    private static void addAllPrerequisites(String course, Map<String, List<String>> adjacencyList,
            Set<String> allCompletedCourses) {
        List<String> directPrerequisites = adjacencyList.get(course);
        if (directPrerequisites != null) {
            for (String prereq : directPrerequisites) {
                if (allCompletedCourses.add(prereq)) {
                    addAllPrerequisites(prereq, adjacencyList, allCompletedCourses);
                }
            }
        }
    }

    private static Set<String> determineEligibleCourses(Map<String, List<String>> adjacencyList,
            Set<String> allCompletedCourses) {
        Set<String> eligibleCourses = new HashSet<>();

        for (String course : adjacencyList.keySet()) {
            if (!allCompletedCourses.contains(course)) {
                List<String> prerequisites = adjacencyList.get(course);
                if (prerequisites == null || prerequisites.isEmpty()
                        || allCompletedCourses.containsAll(prerequisites)) {
                    eligibleCourses.add(course);
                }
            }
        }

        return eligibleCourses;
    }
}
