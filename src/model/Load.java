package model;

import utils.Utils;

import java.util.Objects;

public class Load {
    private final int loadNumber;
    private final Point pickup;
    private final Point dropoff;
    private final double routeLength;
    private boolean isOutDirection;
    private PointLocation pickupLocation;
    private PointLocation dropoffLocation;


    public Load(int loadNumber, Point pickup, Point dropoff) {
        this.loadNumber = loadNumber;
        this.pickup = pickup;
        this.dropoff = dropoff;
        this.routeLength = Math.sqrt(Math.pow(dropoff.getX() - pickup.getX(), 2)
                + Math.pow(dropoff.getY() - pickup.getY(), 2));
        setIsOutDirection();
        this.pickupLocation = getPointLocation(pickup);
        this.dropoffLocation = getPointLocation(dropoff);
    }

    public int getLoadNumber() {
        return loadNumber;
    }

    public Point getPickup() {
        return pickup;
    }

    public Point getDropoff() {
        return dropoff;
    }

    public double getRouteLength() {
        return routeLength;
    }

    public boolean isOutDirection() {
        return isOutDirection;
    }

    public PointLocation getPickupLocation() {
        return pickupLocation;
    }

    public PointLocation getDropoffLocation() {
        return dropoffLocation;
    }

    private void setIsOutDirection() {
        double pickupZeroDistance = Utils.countDistanceToZero(pickup);
        double dropoffZeroDistance = Utils.countDistanceToZero(dropoff);
        isOutDirection = dropoffZeroDistance > pickupZeroDistance;
    }

    private PointLocation getPointLocation(Point point) {
        if (point.getX() >= 0 && point.getY() >= 0) {
            return PointLocation.PLUS_PLUS;
        } else if (point.getX() >= 0 && point.getY() < 0) {
            return PointLocation.PLUS_MINUS;
        } else if (point.getX() < 0 && point.getY() >= 0) {
            return PointLocation.MINUS_PLUS;
        } else {
            return PointLocation.MINUS_MINUS;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Load load = (Load) o;
        return loadNumber == load.loadNumber && Objects.equals(pickup, load.pickup) && Objects.equals(dropoff, load.dropoff);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loadNumber, pickup, dropoff);
    }
}
