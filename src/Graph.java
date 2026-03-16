import java.util.*;

public class Graph {
    private Map<String, City> cities;
    private Map<String, List<Edge>> adjList;

    // Edge class — represents a road between 2 cities
    static class Edge {
        String destination;
        int distance;

        Edge(String destination, int distance) {
            this.destination = destination;
            this.distance = distance;
        }
    }

    // Constructor
    public Graph() {
        cities = new HashMap<>();
        adjList = new HashMap<>();
    }

    // Add a city
    public void addCity(String name, int x, int y) {
        cities.put(name, new City(name, x, y));
        adjList.put(name, new ArrayList<>());
    }

    // Add a road between two cities
    public void addRoad(String from, String to, int distance) {
        adjList.get(from).add(new Edge(to, distance));
        adjList.get(to).add(new Edge(from, distance)); // both directions
    }

    // Get all cities
    public Map<String, City> getCities() {
        return cities;
    }

    // Get neighbors of a city
    public List<Edge> getNeighbors(String cityName) {
        return adjList.getOrDefault(cityName, new ArrayList<>());
    }

    // Get all edges (for drawing roads)
    public Map<String, List<Edge>> getAdjList() {
        return adjList;
    }
}
