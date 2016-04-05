package decisiontree;

import util.DataLoader;

/**
 * Created by Jackie on 4/4/16.
 */
public class Classifier {
    private static String classify(DecisionTreeNode root, Instance instance) {
        DecisionTreeNode node = root.getChild(instance);
        if (node.isLeafNode()) {
            return node.getClass();
        }
        return classify(node, instance);
    }
    public static void main(String[] args) {
        Dataset ds = DataLoader.load("");
        DecisionTreeNode root = TreeBuilder.createTree(ds);
        for (Instance instance : ds) {
            System.out.println(classify(root, instance));
        }
    }
}
