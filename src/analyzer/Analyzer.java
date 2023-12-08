package analyzer;

import model.Load;

import java.util.List;

public interface Analyzer {
    int SHIFT_DURATION = 720;
    double ZERO_SHIFT = 0.0;
    List<List<Integer>> analyze();
}
