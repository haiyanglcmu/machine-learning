package util;

import java.util.HashMap;
import java.util.Map;

public class SimilarityMatrix {
    private Map<String, Map<String, Double>> similarities = new HashMap<String, Map<String, Double>>();

    public double getSimilarity(String attrValue1, String attrValue2) {
        if (attrValue1.equals(attrValue2)) {
            return 1;
        }
        return similarities.get(attrValue1).get(attrValue2);
    }

    public void addSimilarity(String attrValue1, String attrValue2, double sim) {
        Map<String, Double> map = similarities.get(attrValue1);
        if (map == null) {
            map = new HashMap<String, Double>();
            similarities.put(attrValue1, map);
        }
        map.put(attrValue2, sim);
    }
}
