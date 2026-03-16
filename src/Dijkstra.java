import java.util.*;

public class Dijkstra {

    public static Map<String, Integer> findShortestPath(
            Graph graph, String source) {

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(
                Comparator.comparingInt(a -> distances.getOrDefault(a, Integer.MAX_VALUE))
        );

        // Set all distances to infinity first
        for (String city : graph.getCities().keySet()) {
            distances.put(city, Integer.MAX_VALUE);
            previous.put(city, null);
        }

        // Source city distance = 0
        distances.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            String current = pq.poll();
            int currentDist = distances.get(current);

            // Visit all neighbors
            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int newDist = currentDist + edge.distance;

                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previous.put(edge.destination, current);
                    pq.add(edge.destination);
                }
            }
        }

        return distances;
    }

    // Get the actual path as a list
    public static List<String> getPath(
            Graph graph, String source, String destination) {

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(
                Comparator.comparingInt(a -> distances.getOrDefault(a, Integer.MAX_VALUE))
        );

        for (String city : graph.getCities().keySet()) {
            distances.put(city, Integer.MAX_VALUE);
            previous.put(city, null);
        }

        distances.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            if (current.equals(destination)) break;

            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int newDist = distances.get(current) + edge.distance;

                if (newDist < distances.get(edge.destination)) {
                    distances.put(edge.destination, newDist);
                    previous.put(edge.destination, current);
                    pq.add(edge.destination);
                }
            }
        }

        // Build path by backtracking
        List<String> path = new ArrayList<>();
        String step = destination;

        while (step != null) {
            path.add(0, step);
            step = previous.get(step);
        }

        // If no path found
        if (path.size() == 1 && !path.get(0).equals(source)) {
            return new ArrayList<>();
        }

        return path;
    }

    // Get total distance of shortest path
    public static int getDistance(
            Graph graph, String source, String destination) {

        Map<String, Integer> distances = findShortestPath(graph, source);
        return distances.getOrDefault(destination, -1);
    }
}