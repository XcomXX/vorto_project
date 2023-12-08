package utils;

import model.Point;

public class Utils {
    public static double countDistance(Point start, Point end) {
        return Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
    }

    public static double countDistanceToZero(Point point) {
        return countDistance(new Point(0, 0), point);
    }
}
