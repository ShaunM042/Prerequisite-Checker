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
 * NeedToTakeInputFile name is passed through the command line as args[1]
 * Read from NeedToTakeInputFile with the format:
 * 1. One line, containing a course ID
 * 2. c (int): Number of courses
 * 3. c lines, each with one course ID
 * 
 * Step 3:
 * NeedToTakeOutputFile name is passed through the command line as args[2]
 * Output to NeedToTakeOutputFile with the format:
 * 1. Some number of lines, each with one course ID
 */
public class NeedToTake {
    public static void main(String[] args) {

        if (args.length < 3) {
            StdOut.println(
                    "Execute: java NeedToTake <adjacency list INput file> <need to take INput file> <need to take OUTput file>");
            return;
        }
        String adjListInputFileName = args[0];
        String needToTakeInputFileName = args[1];
        String outputFileName = args[2];

        StdIn.setFile(adjListInputFileName);
        int numCourses = StdIn.readInt();
        Map<String, Set<String>> adjacencyList = new HashMap<>();

        for (int i = 0; i < numCourses; i++) {
            String course = StdIn.readString();
            adjacencyList.put(course, new HashSet<>());
        }

        int numEdges = StdIn.readInt();
        for (int i = 0; i < numEdges; i++) {
            String course = StdIn.readString();
            String prereq = StdIn.readString();
            adjacencyList.get(course).add(prereq);
        }

        StdIn.setFile(needToTakeInputFileName);
        String targetCourse = StdIn.readString();
        int numTakenCourses = StdIn.readInt();
        Set<String> takenCourses = new HashSet<>();

        for (int i = 0; i < numTakenCourses; i++) {
            takenCourses.add(StdIn.readString());
        }

        Set<String> requiredCourses = findPrerequisites(targetCourse, adjacencyList, takenCourses);

        StdOut.setFile(outputFileName);
        for (String course : requiredCourses) {
            StdOut.println(course);
        }
    }

    private static Set<String> findPrerequisites(String course, Map<String, Set<String>> adjacencyList,
            Set<String> takenCourses) {
        Set<String> requiredCourses = new HashSet<>();
        addPrerequisites(course, requiredCourses, adjacencyList, takenCourses);
        requiredCourses.removeAll(takenCourses); // Remove courses that have already been taken
        return requiredCourses;
    }

    private static void addPrerequisites(String course, Set<String> requiredCourses,
            Map<String, Set<String>> adjacencyList, Set<String> takenCourses) {
        if (!takenCourses.contains(course) && !requiredCourses.contains(course)) {
            requiredCourses.add(course);
            // Recursively add all prerequisites of this course
            for (String prereq : adjacencyList.getOrDefault(course, Collections.emptySet())) {
                addPrerequisites(prereq, requiredCourses, adjacencyList, takenCourses);
            }
        }
    }
}