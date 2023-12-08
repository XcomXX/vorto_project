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
        Load nextLoad = null;
        double driverShift = ZERO_SHIFT;
        Optional<Map.Entry<PointLocation, Set<Load>>> optional = graph.getPickupGroups().entrySet()
                .stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()));
        PointLocation startRegion = optional.map(Map.Entry::getKey).orElse(null);
        do {
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
        } while (nextLoad != null);
        return loads;
    }

    private Load getNextLoad(Load currentLoad, double driverShift, PointLocation currentRegion) {
        if (currentRegion == null) {
            throw new RuntimeException("Current region can't be null");
        }
        Load returnLoad = null;
        returnLoad = getNextLoad(currentLoad, true, currentRegion, currentRegion);
        returnLoad = getReturnLoadWithValidation(currentLoad, returnLoad, driverShift);
        if (returnLoad != null) {
            return returnLoad;
        }
        returnLoad = getNextLoad(currentLoad, false, currentRegion, currentRegion);
        returnLoad = getReturnLoadWithValidation(currentLoad, returnLoad, driverShift);
        if (returnLoad != null) {
            return returnLoad;
        }
        returnLoad = getClosestValidLoad(currentLoad, driverShift);
        return returnLoad;
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

    private Load getReturnLoadWithValidation(Load currentLoad, Load returnLoad, double driverShift) {
        if (returnLoad != null) {
            if (isValidTimeShift(currentLoad, returnLoad, driverShift)) {
                return returnLoad;
            }
        }
        return null;
    }

    private boolean isValidTimeShift(Load currentLoad, Load nextLoad, double driverShift) {
        return (driverShift + nextLoad.getRouteLength() + nextLoad.getToZeroLength()) < SHIFT_DURATION;
    }

    private Load getNextLoad(Load currentLoad, boolean isOutDirection, PointLocation pickUpRegion, PointLocation dropoffRegion) {
        for (Map.Entry<Double, Integer> curLoad: graph.getAdjacencyMap().get(currentLoad.getLoadNumber()).entrySet()) {
            Load cLoad = graph.getLoads().get(curLoad.getValue());
            if (visited.contains(cLoad.getLoadNumber())) {
                continue;
            }
            if (cLoad.isOutDirection() == isOutDirection && cLoad.getPickupLocation() == pickUpRegion && cLoad.getDropoffLocation() == dropoffRegion) {
                return cLoad;
            }

        }
        return null;
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
