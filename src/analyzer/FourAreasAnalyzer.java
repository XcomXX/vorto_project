package analyzer;

import model.Graph;
import model.Load;
import model.PointLocation;
import utils.Utils;

import java.util.*;

public class FourAreasAnalyzer implements Analyzer {
    private final Graph graph;
    private final Set<Integer> visited;
    public FourAreasAnalyzer(Graph graph) {
        this.graph = graph;
        visited = new HashSet<>();
        visited.add(0);
    }
    @Override
    public List<List<Integer>> analyze() {
        List<List<Integer>> result = new ArrayList<>();
        while (!graph.getPickupGroups().values().stream().allMatch(Set::isEmpty)) {
            List<Integer> driverLoads = countDriverLoads();
            result.add(driverLoads);
        }
        return result;
    }

    private List<Integer> countDriverLoads() {
        List<Integer> loads = new ArrayList<>();
        Load currentLoad = graph.getZeroLoad();
        Load nextLoad;
        double driverShift = ZERO_SHIFT;
        Optional<Map.Entry<PointLocation, Set<Load>>> optional = graph.getPickupGroups().entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()));
        PointLocation startRegion = optional.map(Map.Entry::getKey).orElse(null);
        while (true) {
            nextLoad = getNextLoad(currentLoad, driverShift, startRegion);
            if (nextLoad == null) {
                break;
            }
            driverShift = driverShift + Utils.countDistance(currentLoad.getDropoff(), nextLoad.getPickup()) + nextLoad.getRouteLength();
            currentLoad = nextLoad;
            loads.add(currentLoad.getLoadNumber());
            removeFromPickupGroup(currentLoad);
            startRegion = currentLoad.getDropoffLocation();
            visited.add(currentLoad.getLoadNumber());
        }
        return loads;
    }

    private Load getNextLoad(Load currentLoad, double driverShift, PointLocation currentRegion) {
        if (currentRegion == null) {
            throw new RuntimeException("Current region can't be null");
        }
        Load nextLoad;
        nextLoad = getNextLoad(currentLoad, true, currentRegion, currentRegion);
        nextLoad = getLoadWithReturnValidation(currentLoad, nextLoad, driverShift);
        if (nextLoad != null) {
            return nextLoad;
        }
        nextLoad = getNextLoad(currentLoad, false, currentRegion, currentRegion);
        nextLoad = getLoadWithReturnValidation(currentLoad, nextLoad, driverShift);
        if (nextLoad != null) {
            return nextLoad;
        }
        nextLoad = getClosestValidLoad(currentLoad, driverShift);
        return nextLoad;
    }

    private Load getClosestValidLoad(Load currentLoad, double driverShift) {
        for (Map.Entry<Double, Integer> curLoad: graph.getAdjacencyMap().get(currentLoad.getLoadNumber()).entrySet()) {
            Load cLoad = graph.getLoads().get(curLoad.getValue());
            if (visited.contains(cLoad.getLoadNumber())) {
                continue;
            }
            if (isValidTimeShift(currentLoad, cLoad, driverShift)) {
                return cLoad;
            }
        }
        return null;
    }

    private Load getLoadWithReturnValidation(Load currentLoad, Load nextLoad, double driverShift) {
        if (nextLoad != null) {
            if (isValidTimeShift(currentLoad, nextLoad, driverShift)) {
                return nextLoad;
            }
        }
        return null;
    }

    private boolean isValidTimeShift(Load currentLoad, Load nextLoad, double driverShift) {
        return (driverShift + Utils.countDistance(currentLoad.getDropoff(), nextLoad.getPickup()) + nextLoad.getRouteLength() + nextLoad.getToZeroLength()) < SHIFT_DURATION;
    }

    private Load getNextLoad(Load currentLoad, boolean isOutDirection, PointLocation pickUpRegion, PointLocation dropoffRegion) {
        List<Load> closestList = new ArrayList<>();

        for (Map.Entry<Double, Integer> curLoad: graph.getAdjacencyMap().get(currentLoad.getLoadNumber()).entrySet()) {
            Load cLoad = graph.getLoads().get(curLoad.getValue());
            if (visited.contains(cLoad.getLoadNumber())) {
                continue;
            }
            closestList.add(cLoad);
        }
        if (closestList.isEmpty()) {
            return null;
        }
        Optional<Load> filteredList = closestList.stream()
                .filter(v -> {
                    if (isOutDirection) {
                        return v.isOutDirection();
                    } else {
                        return true;
                    }})
                .filter(v -> {
                    if (pickUpRegion != null) {
                        return v.getPickupLocation() == pickUpRegion;
                    } else {
                        return true;
                    }})
                .filter(v -> {
                    if (dropoffRegion != null) {
                        return v.getDropoffLocation() == dropoffRegion;
                    } else {
                        return true;
                    }
                }).findFirst();
        return filteredList.orElse(null);
    }

    private void removeFromPickupGroup(Load load) {
        for (Map.Entry<PointLocation, Set<Load>> entry: graph.getPickupGroups().entrySet()) {
            Set<Load> currentLoads = entry.getValue();
            if (currentLoads.contains(load)) {
                currentLoads.remove(load);
                return;
            }
        }
    }
}
