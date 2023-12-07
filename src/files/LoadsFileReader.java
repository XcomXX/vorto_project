package files;

import model.Load;
import model.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadsFileReader {
    public static List<Load> readLoads(String path) {
        if (path == null || path.isEmpty()) {
            System.out.println("File path is empty");
        }
        List<Load> loads = new ArrayList<>();
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                String load = reader.nextLine().trim();
                String[] parts = load.split(" ");
                if ("loadNumber".equals(parts[0])) {
                    continue;
                }
                if (parts.length != 3) {
                    System.out.println("Incorrect data format");
                    throw new RuntimeException("Incorrect data format");
                }
                int loadNumber = Integer.parseInt(parts[0]);
                Point pickup = createPoint(parts[1]);
                Point dropoff = createPoint(parts[2]);
                loads.add(new Load(loadNumber, pickup, dropoff));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist");
        }
        return loads;
    }

    private static Point createPoint(String stringPoint) {
        String[] xy = stringPoint.substring(1, stringPoint.length() - 1).split(",");
        return new Point(Double.parseDouble(xy[0]), Double.parseDouble(xy[1]));
    }
}
