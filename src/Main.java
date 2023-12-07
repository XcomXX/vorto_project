import files.LoadsFileReader;
import model.Load;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Load> loads = LoadsFileReader.readLoads(args[0]);
    }
}