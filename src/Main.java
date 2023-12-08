import analyzer.Analyzer;
import analyzer.FourAreasAnalyzer;
import files.LoadsFileReader;
import model.Graph;
import model.Load;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        List<Load> loads = LoadsFileReader.readLoads(args[0]);
        Graph graph = new Graph(loads);
        Analyzer analyzer = new FourAreasAnalyzer(graph);
        List<List<Integer>> drives = analyzer.analyze();
        for (List<Integer> driverPath: drives) {
            System.out.println(driverPath);
        }
        System.out.println(System.currentTimeMillis());
    }
}