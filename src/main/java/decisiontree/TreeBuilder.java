package decisiontree;

import util.Attribute;
import util.Dataset;
import util.Instance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class TreeBuilder {

    private static Double calculateEntropy (Dataset ds) {
        int numInstances = ds.length();
        Map<String, Integer> classCount = new HashMap<String, Integer>();

        for (Instance instance : ds) {
            String classLabel = instance.getClassLabel();
            if (!classCount.containsKey(classLabel)) {
                classCount.put(classLabel, 1);
            } else {
                classCount.put(classLabel, classCount.get(classLabel) + 1);
            }
        }
        double entropy = 0d;
        for (Map.Entry<String, Integer> entry : classCount.entrySet()) {
            double prob = ((double) entry.getValue()) / ((double) numInstances);
            entropy -= prob * (Math.log(prob) / Math.log(2));
        }
        return entropy;
    }

    private static double calculateInfoGain(int datasetLength,
                                            double baseEntropy,
                                            Map<Attribute, Dataset> subsets) {
        double newEntropy = 0.0;
        for (Dataset subset : subsets.values()) {
            double prob = (double) subset.length() / datasetLength;
            newEntropy += prob * calculateEntropy(subset);
        }
        return baseEntropy - newEntropy;
    }

    /**
     * get the name of the best attribute to split the data set
     */
    private static Map<String, Map<Attribute, Dataset>> getBestSplit(Dataset ds) {
        double baseEntropy = calculateEntropy(ds);
        double bestInfoGain = Double.NEGATIVE_INFINITY;
        String bestAttrName = "";
        Map<Attribute, Dataset> bestSubsets = null;

        for (String attrName : ds.getAttributeNames()) {
            Map<Attribute, Dataset> subsets;
            if (ds.getAttributeType(attrName).equals(Attribute.AttributeType.NOMINAL)) {
                subsets = ds.getSubsets(attrName);
                double infoGain = calculateInfoGain(ds.length(), baseEntropy, subsets);
                if (infoGain > bestInfoGain) {
                    bestInfoGain = infoGain;
                    bestAttrName = attrName;
                    bestSubsets = subsets;
                }
            } else {
                List<Double> thresholds =  ds.getThresholds(attrName);
                for (double th : thresholds) {
                    subsets = ds.getSubsets(attrName, th);
                    double infoGain = calculateInfoGain(ds.length(), baseEntropy, subsets);
                    if (infoGain > bestInfoGain) {
                        bestInfoGain = infoGain;
                        bestAttrName = attrName;
                        bestSubsets = subsets;
                    }
                }
            }
        }

        Map<String, Map<Attribute, Dataset>> bestSplit = new HashMap<String, Map<Attribute, Dataset>>();
        bestSplit.put(bestAttrName, bestSubsets);
        return bestSplit;
    }

    public static DecisionTreeNode createTree(Dataset ds) {
        if (ds.numClassLabels() == 1) {
            return new DecisionTreeNode(ds.get(0).getClassLabel(), true);
        }
        if (ds.numAttributes() == 1) {
            String majorityClassLabel = ds.majorityClass();
            return new DecisionTreeNode(majorityClassLabel, true);
        }
        Map<String, Map<Attribute, Dataset>> bestSplit = getBestSplit(ds);
        String attrName = bestSplit.keySet().iterator().next();

        DecisionTreeNode node = new DecisionTreeNode(attrName, false);
        Map<Attribute, Dataset> subsets = bestSplit.get(attrName);

        for (Map.Entry<Attribute, Dataset> subset : subsets.entrySet()) {
            node.addChild(subset.getKey(), createTree(subset.getValue()));
        }
        return node;
    }
}
