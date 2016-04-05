package decisiontree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Dataset implements Iterable<Instance> {
    private List<Instance> instances = new ArrayList<Instance>();
    private List<Attribute> attributes = new ArrayList<Attribute>();
    private Object splitCriteria;

    public int length() {
        return 0;
    }

    public int numAttributes() {
        return 0;
    }

    public List<Dataset> getSubsets(int i) {
        return null;
    }

    public int numClassLabels() {
        return 0;
    }

    public DecisionTreeNode majorityClass() {
        return null;
    }

    public Instance get(int i) {
        return null;
    }

    public Attribute getAttribute(int bestAttributeIndex) {
        return null;
    }

    public Object getSplitCriteria() {
        return splitCriteria;
    }

    public Iterator<Instance> iterator() {
        return instances.iterator();
    }

    public void addNumericAttribute(String attrName) {
        new NumericAttribute(attrName);
    }

    public void addNominalAttributes(String attrName, String attrValues) {
        NominalAttribute attr = new NominalAttribute(attrName);
        String[] values = attrValues.replace("{}", "").split(",");
        for (String value : values) {
            attr.addValue(value);
        }
    }

    public void addInstance(String line) {
        Instance inst = new Instance();

        String[] splits = line.split(",");
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attr = attributes.get(i);
            // for nominal attributes
            if (attr.getType().equals(Attribute.AttributeType.NOMINAL)) {
                NominalAttribute nomAttr = (NominalAttribute) attr;
                if (!nomAttr.hasValue(splits[i])) {
                    throw new IllegalArgumentException();
                }
                inst.addAttribute();
            } else { // for numeric attributes
            }
        }
    }
}
