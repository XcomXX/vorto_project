package model;

import utils.Utils;

import java.util.*;

public class Graph {
    private final Map<Integer, Map<Double, Integer>> adjacencyMap = new HashMap<>();
    private final List<Load> loads;
    private final Map<PointLocation, Set<Load>> pickupGroups = new HashMap<>() {{
        put(PointLocation.PLUS_PLUS, new HashSet<>());
        put(PointLocation.PLUS_MINUS, new HashSet<>());
        put(PointLocation.MINUS_PLUS, new HashSet<>());
        put(PointLocation.MINUS_MINUS, new HashSet<>());
    }};
    private final Map<PointLocation, Set<Load>> dropoffGroups = new HashMap<>() {{
        put(PointLocation.PLUS_PLUS, new HashSet<>());
        put(PointLocation.PLUS_MINUS, new HashSet<>());
        put(PointLocation.MINUS_PLUS, new HashSet<>());
        put(PointLocation.MINUS_MINUS, new HashSet<>());
    }};

    public Graph(List<Load> loads) {
        this.loads = loads;
        Point zeroPoint = new Point(0, 0);
        Load zeroLoad = new Load(0, zeroPoint, zeroPoint);
        loads.add(0, zeroLoad);
        for (Load load: loads) {
            fillNeighbors(load);
            fillGroups(load);
        }
    }

    private void fillGroups(Load loadToFill) {
        pickupGroups.merge(loadToFill.getPickupLocation(), new HashSet<>(Set.of(loadToFill)), (oldS, newS) -> {
            oldS.addAll(newS);
            return oldS;
        });
        dropoffGroups.merge(loadToFill.getDropoffLocation(), new HashSet<>(Set.of(loadToFill)), (oldS, newS) -> {
            oldS.addAll(newS);
            return oldS;
        });
    }

    private void fillNeighbors(Load loadToFill) {
        Map<Double, Integer> loadDistances = new TreeMap<>();
        for (Load load: loads) {
            int loadNumber = load.getLoadNumber();
            if (loadNumber == loadToFill.getLoadNumber()) {
                continue;
            }
            double distance = Utils.countDistance(loadToFill.getDropoff(), load.getPickup());
            loadDistances.put(distance, loadNumber);
        }
        adjacencyMap.put(loadToFill.getLoadNumber(), loadDistances);
    }

    public Map<Integer, Map<Double, Integer>> getAdjacencyMap() {
        return adjacencyMap;
    }
}
