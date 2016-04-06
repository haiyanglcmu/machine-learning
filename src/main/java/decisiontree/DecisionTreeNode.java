package decisiontree;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haiyang on 4/5/16.
 */
public class DecisionTreeNode {
    private String attrName;
    private String classLabel;
    private boolean isLeafNode;
    private Map<Attribute, DecisionTreeNode> children = new HashMap<Attribute, DecisionTreeNode>();

    public DecisionTreeNode(String name, boolean isLeafNode) {
        if (isLeafNode) {
            this.classLabel = name;
        } else {
            this.attrName = name;
        }
        this.isLeafNode = isLeafNode;
    }

    public boolean isLeafNode() {
        return isLeafNode;
    }

    public String getClassLabel() {
        return this.classLabel;
    }

    public void addChild(Attribute splitAttribute, DecisionTreeNode child) {
        children.put(splitAttribute, child);
    }

    public DecisionTreeNode getChild(Instance instance) {
        Attribute attr = instance.get(attrName);
        // compare with each splitting criteria
        Attribute splitAttr = null;
        for (Attribute attribute : children.keySet()) {
            if (attribute.getType().equals(Attribute.AttributeType.NOMINAL)) {
                if (attribute.getNominalValue().equals(attr.getNominalValue())) {
                    splitAttr = attribute;
                    break;
                }
            } else if (attribute.contains(attr)){
                splitAttr = attribute;
                break;
            }
        }
        // TODO: how to deal with missing attribute values
        if (splitAttr == null) {
            splitAttr = children.keySet().iterator().next();
        }
        return children.get(splitAttr);
    }

}
