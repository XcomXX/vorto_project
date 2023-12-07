package model;

public class Load {
    private final int loadNumber;
    private final Point pickup;
    private final Point dropoff;

    public Load(int loadNumber, Point pickup, Point dropoff) {
        this.loadNumber = loadNumber;
        this.pickup = pickup;
        this.dropoff = dropoff;

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
}
