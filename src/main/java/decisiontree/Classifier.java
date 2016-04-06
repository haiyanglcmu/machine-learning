package decisiontree;

import util.DataLoader;

import java.io.FileNotFoundException;

/**
 * Created by Jackie on 4/4/16.
 */
public class Classifier {
    private static String classify(DecisionTreeNode root, Instance instance) {
        DecisionTreeNode node = root.getChild(instance);
        if (node.isLeafNode()) {
            return node.getClassLabel();
        }
        return classify(node, instance);
    }

    private static void test(DecisionTreeNode root, Dataset testSet) {
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
        System.out.println("total: " + total);
        System.out.println("correct: " + ((double) numCorrect / total));
        System.out.println("error: " + ((double) numError / total));
    }

    public static void main(String[] args) throws FileNotFoundException {
        Dataset ds = DataLoader.load("data/random1");
        DecisionTreeNode root = TreeBuilder.createTree(ds);
        test(root, ds);
    }
}
