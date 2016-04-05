package decisiontree;

/**
 * Created by Jackie on 4/4/16.
 */
import java.util.*;
public class TreeBuilder {

    private static Double calculateEntropy (Dataset ds) {
        int numInstances = ds.length();
        Map<Attribute, Integer> classCount = new HashMap<Attribute, Integer>();

        for (Instance instance : ds) {
            Attribute f = instance.getClassLabel();
            if (!classCount.containsKey(f)) {
                classCount.put(f, 1);
            } else {
                classCount.put(f, classCount.get(f) + 1);
            }
        }
        double entropy = 0d;
        for (Map.Entry<Attribute, Integer> entry : classCount.entrySet()) {
            double prob = ((double) entry.getValue()) / ((double) numInstances);
            entropy -= prob * (Math.log(prob) / Math.log(2));
        }
        return entropy;
    }


    private static int getBestAttribute(Dataset ds) {
        int numFeatures = ds.numAttributes();
        double baseEntropy = calculateEntropy(ds);
        double bestInfoGain = 0.0;
        int bestFeature = -1;

        for (int i = 0; i < numFeatures; i++) {
            List<Dataset> subsets = ds.getSubsets(i);
            double newEntropy = 0.0;
            for (Dataset subset : subsets) {
                double prob = (double) subset.length() / ds.length();
                newEntropy += prob * calculateEntropy(subset);
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
        if (ds.numClassLabels() == 1) {
            return new DecisionTreeNode(ds.get(0).getClassLabel());
        }
        if (ds.numAttributes() == 1) {
            return ds.majorityClass();
        }
        int bestAttribute = getBestAttribute(ds);
        DecisionTreeNode node = new DecisionTreeNode(ds.getAttribute(bestAttribute));
        for (Dataset dataset : ds.getSubsets(bestAttribute)) {
            node.addChild(dataset.getSplitCriteria(), createTree(dataset));
        }
        return node;
    }
}
