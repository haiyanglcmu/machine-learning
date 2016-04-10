package knn;

import util.*;

import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class Classifier {
    private static Map<String, Double> weights = new HashMap<String, Double>();
    private static Map<String, SimilarityMatrix> similarities;
    private static int k = 3;

    private static DecimalFormat df = new DecimalFormat("#.####");
    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static Map<String, SimilarityMatrix> loadSimilarity(String filename) throws FileNotFoundException {
        Map<String, SimilarityMatrix> similarities = new HashMap<String, SimilarityMatrix>();
        FileIterator iterator = new FileIterator(filename);
        SimilarityMatrix simMatrix = null;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith("[") && line.endsWith("]")) {
                String attributeName = line.substring(1, line.length() - 1);
                simMatrix = similarities.get(attributeName);
                if (simMatrix == null) {
                    simMatrix = new SimilarityMatrix();
                    similarities.put(attributeName, simMatrix);
                }
            } else {
                line = line.trim();
                if (line.equals("")) continue;
                String[] splits = line.split(",");
                String attrValue1 = splits[0];
                String attrValue2 = splits[1];
                double sim = Double.parseDouble(splits[2]);
                assert simMatrix != null;
                simMatrix.addSimilarity(attrValue1, attrValue2, sim);
                simMatrix.addSimilarity(attrValue2, attrValue1, sim);
            }
        }
        return similarities;
    }

    private static String classify(Dataset train, Instance instance) {
        TreeMap<Double, Instance> knn = train.sortInstances(instance, similarities, weights);
        Map<String, Double> classVotes = new HashMap<String, Double>();

        int i = 0;
        for (Map.Entry<Double, Instance> entries : knn.entrySet()) {
            double sim = entries.getKey();
            String classLabel = entries.getValue().getClassLabel();
            Double oldSim = classVotes.get(classLabel);
            if (oldSim == null) {
                classVotes.put(classLabel, sim);
            } else {
                classVotes.put(classLabel, oldSim + sim);
            }
            if (++i == k) {
                break;
            }
        }

        String bestClassLabel = null;
        double bestClassVote = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, Double> entries : classVotes.entrySet()) {
            if (entries.getValue() > bestClassVote) {
                bestClassVote = entries.getValue();
                bestClassLabel = entries.getKey();
            }
        }
        return bestClassLabel;
    }

    private static double calculateAccuracy(Dataset train, Dataset testSet) {
        int numCorrect = 0;
        int numError = 0;
        for (Instance inst : testSet) {
            String predictedClassLabel = classify(train, inst);
            if (inst.getClassLabel().equals(predictedClassLabel)) {
                numCorrect++;
            } else {
                numError++;
            }
        }
        int total = numCorrect + numError;
        double correctRate = (double) numCorrect / total;
        // System.out.println("correct: " + correctRate);
        return correctRate;
    }

    private static void crossValidation(int numFolds, Dataset dataset) {
        List<Dataset> folds = dataset.getFolds(numFolds);
        double correctRate = 0;

        for (int i = 0; i < numFolds; i++) {
            Dataset trainSet = new Dataset();
            Dataset testSet = null;
            for (int j = 0; j < numFolds; j++) {
                if (j == i) {
                    testSet = folds.get(j);
                } else {
                    trainSet.extend(folds.get(j));
                }
            }

            correctRate += calculateAccuracy(trainSet, testSet);
        }
        System.out.println("accuracy: " + df.format(correctRate / numFolds));
    }

    private static void optimizeWeights() {
        weights.put("Type", 1.0);
        weights.put("LifeStyle", 1.0);
        weights.put("Vacation", 1.0);
        weights.put("eCredit", 1.0);
        weights.put("salary", 1.0);
        weights.put("property", 1.0);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Dataset ds = DataLoader.load("data/random3");
        ds.normalize();
        optimizeWeights();
        similarities = loadSimilarity("data/similarities");

//        for (int i = 2; i < 150; i++) {
//            System.out.print(i + ": ");
//            crossValidation(i, ds);
//        }

        crossValidation(10, ds);
    }
}
