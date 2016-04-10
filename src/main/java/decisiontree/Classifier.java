package decisiontree;

import util.Attribute;
import util.DataLoader;
import util.Dataset;
import util.Instance;

import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class Classifier {
    private static DecimalFormat df = new DecimalFormat("#.####");
    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }
    private static String classify(DecisionTreeNode root, Instance instance) {
        DecisionTreeNode node = root.getChild(instance);
        if (node.isLeafNode()) {
            return node.getClassLabel();
        }
        return classify(node, instance);
    }

    private static double calculateAccuracy(DecisionTreeNode root, Dataset testSet) {
        int numCorrect = 0;
        int numError = 0;
        for (Instance inst : testSet) {
            String predictedClassLabel = classify(root, inst);
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

    private static void printTree(DecisionTreeNode root, String indent) {
        if (root.isLeafNode()) {
            System.out.println(indent + root.getClassLabel());
            return;
        }

        System.out.println(indent + root.getAttrName() + ":");
        List<Attribute> attributes = root.getSplittingCriteria();
        for (Attribute attr : attributes) {
            System.out.println(indent + attr);
            printTree(root.getChild(attr), indent + "  ");
        }
    }

    private static void crossValidation(int numFolds, Dataset dataset) {
        List<Dataset> folds = dataset.getFolds(numFolds);
        double correctRate = 0;

//        for (Dataset fold : folds) {
//            System.out.println(fold);
//        }

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

            DecisionTreeNode root = TreeBuilder.createTree(trainSet);
            // printTree(root, "");
            correctRate += calculateAccuracy(root, testSet);
        }
        System.out.println("accuracy: " + df.format(correctRate / numFolds));
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Dataset ds = DataLoader.load("data/train.b.shuffled");
        Dataset ds = DataLoader.load("data/random3");
        for (int i = 2; i < 150; i++) {
            System.out.print(i + ": ");
            crossValidation(i, ds);
        }
    }
}
