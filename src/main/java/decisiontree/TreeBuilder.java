package decisiontree;

/**
 * Created by Jackie on 4/4/16.
 */
import java.text.AttributedCharacterIterator;
import java.util.*;
public class TreeBuilder {

    private static Double calculateEntropy (Dataset ds) {
        int numEntries = ds.length();
        Map<Attribute, Integer> labelCount = new HashMap<Attribute, Integer>();

        for (Instance instance : ds) {
            Attribute f = instance.getLabel();
            if (!labelCount.containsKey(f)) {
                labelCount.put(f, 1);
            } else {
                labelCount.put(f, labelCount.get(f) + 1);
            }
        }
        double shannonEnt = 0.0;
        for (Map.Entry<Attribute, Integer> entry : labelCount) {
            double prob = ((double) labelCount.get(entry.getKey()).intValue()) / ((double) numEntries);
            shannonEnt -= prob * (Math.log(prob) / Math.log(2));
        }
        return shannonEnt;
    }


    private static int bestFeature(Dataset ds) {
        int numFeatures = ds.getNumFeatures();
        double baseEntropy = calculateEntropy(ds);
        double bestInfoGain = 0.0;
        int bestFeature = -1;

        for (int i = 0; i < numFeatures; i++) {
            List<Dataset> subsets = ds.getSubsets(i);
            double newEntropy = 0.0;
            for (Dataset subset : subsets) {
                double prob = (double) subset.length() / ds.length();
                newEntropy += prob * calculateEntropy(subset)
            }
            double infoGain = baseEntropy - newEntropy;
            if (infoGain > bestInfoGain) {
                bestInfoGain = infoGain;
                bestFeature = i;
            }
        }
        return bestFeature;
    }

    public static DecisionTreeNode createTree(Dataset ds) {
        if (ds.numClass() == 1) {
            return new DecisionTreeNode(ds.get(0).getLabel());
        }
        if (ds.numLabels() == 1) {
            return ds.majorityClass();
        }
        int bestFeature = bestFeature(ds);
        DecisionTreeNode node = new DecisionTreeNode(ds.getFeatureName(bestFeature));
        for (Dataset dataset : ds.getSubsets(bestFeature)) {
            node.addChild(dataset.getSplitCriteria(), createTree(dataset));
        }
        return node;
    }
}
