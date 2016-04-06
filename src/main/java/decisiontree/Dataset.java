package decisiontree;

import java.util.*;

public class Dataset implements Iterable<Instance> {
    private List<Instance> instances = new ArrayList<Instance>();

    // private Map<String, List<Attribute>> attributes = new HashMap<String, List<Attribute>>();

    private List<String> attributeNames = new ArrayList<String>();

    private Map<String, Attribute.AttributeType> attributeTypes =
            new HashMap<String, Attribute.AttributeType>();

    private Map<String, Integer> classLabelCount = new HashMap<String, Integer>();

    public int length() {
        return this.instances.size();
    }

    public Attribute.AttributeType getAttributeType(String attrName) {
        return this.attributeTypes.get(attrName);
    }

    public void removeAttribute(String attrName) {
        this.attributeNames.remove(attrName);
    }

    /**
     * split the data set according to a numeric attribute threshold
     */
    public Map<Attribute, Dataset> getSubsets(String attrName, double threshold) {
        Map<Attribute, Dataset> subsets = new HashMap<Attribute, Dataset>();
        Attribute leftThreshold = new Attribute(Double.MIN_VALUE, threshold);
        Attribute rightThreshold = new Attribute(threshold, Double.MAX_VALUE);

        for (Instance inst : instances) {
            Attribute splitAttribute;
            if (leftThreshold.contains(inst.get(attrName))) {
                splitAttribute = leftThreshold;
            } else {
                splitAttribute = rightThreshold;
            }

            Dataset subset = subsets.get(splitAttribute);
            if (subset == null) {
                subset = createSubset(attrName);
                subsets.put(splitAttribute, subset);
            }

            subset.addInstance(inst);
        }

        return subsets;
    }

    /**
     * split the data set according to a nominal attribute
     */
    public Map<Attribute, Dataset> getSubsets(String attrName) {
        Map<Attribute, Dataset> subsets = new HashMap<Attribute, Dataset>();

        for (Instance instance : instances) {
            Attribute attrValue = instance.get(attrName);
            Dataset subset = subsets.get(attrValue);
            if (subset == null) {
                subset = createSubset(attrName);
                subsets.put(attrValue, subset);
            }
            subset.addInstance(instance);
        }
        return subsets;
    }

    private Dataset createSubset(String attrName) {
        Dataset subset = new Dataset();
        List<String> attrNames = getAttributeNames();
        attrNames.remove(attrName);
        subset.setAttributeNames(attrNames);
        subset.setAttributeTypes(this.attributeTypes);
        return subset;
    }

    public Instance get(int i) {
        return this.instances.get(i);
    }

    public Iterator<Instance> iterator() {
        return instances.iterator();
    }

    private void setAttributeNames(List<String> attributeNames) {
        this.attributeNames = attributeNames;
    }

    public List<String> getAttributeNames() {
        List<String> copy = new ArrayList<String>();
        for (String name : attributeNames) {
            copy.add(name);
        }
        return copy;
    }

    private void setAttributeTypes(Map<String, Attribute.AttributeType> attributeTypes) {
        this.attributeTypes = attributeTypes;
    }

    public void addAttribute(String attrName, Attribute.AttributeType type) {
        this.attributeNames.add(attrName);
        this.attributeTypes.put(attrName, type);
    }

    public void addInstance(Instance instance) {
        this.instances.add(instance);
        String classLabel = instance.getClassLabel();
        Integer count = this.classLabelCount.get(classLabel);
        if (count == null) {
            this.classLabelCount.put(classLabel, 1);
        } else {
            this.classLabelCount.put(classLabel, count + 1);
        }
    }

    public void addInstance(String line) {
        Instance inst = new Instance();
        String[] splits = line.split(",");

        for (int i = 0; i < attributeNames.size(); i++) {
            String attrName = attributeNames.get(i);
            Attribute.AttributeType type = attributeTypes.get(attrName);
            if (type.equals(Attribute.AttributeType.NOMINAL)) {
                inst.addAttribute(attrName, splits[i]);
            } else {
                inst.addAttribute(attrName, Double.parseDouble(splits[i]));
            }
        }
        inst.setClassLabel(splits[splits.length - 1]);
        this.addInstance(inst);
    }

    public List<Double> getThresholds(final String attrName) {
        if (getAttributeType(attrName).equals(Attribute.AttributeType.NOMINAL)) {
            throw new IllegalArgumentException(); // can only be numeric
        }

        Collections.sort(instances, new Comparator<Instance>() {
            public int compare(Instance o1, Instance o2) {
                Attribute attr1 = o1.get(attrName);
                Attribute attr2 = o2.get(attrName);
                return attr1.compareTo(attr2);
            }
        });

        List<Double> thresholds = new ArrayList<Double>();

        Instance prevInstance = null;
        for (Instance inst : instances) {
            if (prevInstance != null && !inst.isSameClass(inst, prevInstance)) {
                double prevValue = prevInstance.get(attrName).getNumericValue();
                double currentValue = inst.get(attrName).getNumericValue();
                thresholds.add((prevValue + currentValue) / 2);
            }
            prevInstance = inst;
        }

        return thresholds;
    }

    public int numClassLabels() {
        return this.classLabelCount.keySet().size();
    }

    public int numAttributes() {
        return this.attributeNames.size();
    }

    public String majorityClass() {
        int majorityCount = Integer.MIN_VALUE;
        String majorityClassLabel = null;
        for (Map.Entry<String, Integer> entry : classLabelCount.entrySet()) {
            int count = entry.getValue();
            if (count > majorityCount) {
                majorityCount = count;
                majorityClassLabel = entry.getKey();
            }
        }
        return majorityClassLabel;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String attrName : attributeNames) {
            sb.append(attrName);
            sb.append(":");
            sb.append(attributeTypes.get(attrName));
            sb.append("\n");
        }
        for (Instance inst : instances) {
            for (String attrName : attributeNames) {
                Attribute attr = inst.get(attrName);
                if (attr.isNumeric()) {
                    sb.append(attr.getNumericValue());
                } else {
                    sb.append(attr.getNominalValue());
                }
                sb.append(",");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
