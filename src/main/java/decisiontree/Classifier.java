package decisiontree;

import util.DataLoader;

import java.io.FileNotFoundException;
import java.util.List;

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

    public static void main(String[] args) throws FileNotFoundException {
        Dataset ds = DataLoader.load("data/random1");
        DecisionTreeNode root = TreeBuilder.createTree(ds);
        printTree(root, "");
        test(root, ds);
    }
}
