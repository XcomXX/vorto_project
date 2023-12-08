import files.LoadsFileReader;
import model.Graph;
import model.Load;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        List<Load> loads = LoadsFileReader.readLoads(args[0]);
        Graph graph = new Graph(loads);
        System.out.println(System.currentTimeMillis());
        System.out.println("y");
    }
}