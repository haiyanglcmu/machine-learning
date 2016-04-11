package util;

import java.util.*;

public class Dataset implements Iterable<Instance> {
    private List<Instance> instances = new ArrayList<Instance>();

    // private Map<String, List<Attribute>> attributes = new HashMap<String, List<Attribute>>();

    private List<String> attributeNames = new ArrayList<String>();

    private Map<String, Attribute.AttributeType> attributeTypes =
            new HashMap<String, Attribute.AttributeType>();

    // private Map<String, Integer> classLabelCount = new HashMap<String, Integer>();

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
        Attribute leftThreshold = new Attribute(Double.NEGATIVE_INFINITY, threshold);
        Attribute rightThreshold = new Attribute(threshold, Double.POSITIVE_INFINITY);

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
//        String classLabel = instance.getClassLabel();
//        Integer count = this.classLabelCount.get(classLabel);
//        if (count == null) {
//            this.classLabelCount.put(classLabel, 1);
//        } else {
//            this.classLabelCount.put(classLabel, count + 1);
//        }
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
                inst.setAttribute(attrName, Double.parseDouble(splits[i]));
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
        Set<String> classLabels = new HashSet<String>();
        for (Instance instance : instances) {
            String classLabel = instance.getClassLabel();
            classLabels.add(classLabel);
        }
        return classLabels.size();
    }

    public int numAttributes() {
        return this.attributeNames.size();
    }

    public String majorityClass() {
        Map<String, Integer> classLabelCount = new HashMap<String, Integer>();
        for (Instance instance : instances) {
            String classLabel = instance.getClassLabel();
            Integer count = classLabelCount.get(classLabel);
            if (count == null) {
                classLabelCount.put(classLabel, 1);
            } else {
                classLabelCount.put(classLabel, count + 1);
            }
        }

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

    public List<Dataset> getFolds(int numFolds) {
        List<Dataset> folds = new ArrayList<Dataset>();
        for (int i = 0; i < numFolds; i++) {
            Dataset dataset = new Dataset();
            dataset.setAttributeNames(attributeNames);
            dataset.setAttributeTypes(attributeTypes);
            folds.add(dataset);
        }
        int foldSize = instances.size() / numFolds;
        for (int i = 0; i < instances.size(); i++) {
            int foldIndex = i / foldSize;
            if (foldIndex >= numFolds) foldIndex = numFolds - 1;
            folds.get(foldIndex).addInstance(instances.get(i));
        }
        return folds;
    }

    public void extend(Dataset dataset) {
        this.attributeNames = dataset.attributeNames;
        this.attributeTypes = dataset.attributeTypes;
        this.instances.addAll(dataset.instances);
    }

    private double calculateSimilarity(Map<String, SimilarityMatrix> similarities,
                                       Map<String, Double> weights,
                                       Instance inst1,
                                       Instance inst2) {
        double euclideanDistance = 0;
        List<String> attributeNames = getAttributeNames();
        for (String attrName : attributeNames) {
            double weight = weights.get(attrName);
            Attribute attr1 = inst1.get(attrName);
            Attribute attr2 = inst2.get(attrName);

            if (attr1.isNumeric()) {
                euclideanDistance += weight * Math.pow(attr1.getNumericValue() - attr2.getNumericValue(), 2);
            } else {
                SimilarityMatrix matrix = similarities.get(attrName);
                double sim;
                if (matrix != null) {
                    sim = matrix.getSimilarity(attr1.getNominalValue(), attr2.getNominalValue());
                } else {
                    sim = attr1.equals(attr2) ? 1 : 0;
                }
                euclideanDistance += weight * (1 - sim);
            }
        }
        return 1 / Math.sqrt(euclideanDistance);
    }

    public TreeMap<Double, Instance> sortInstances(Instance instance,
                                             Map<String, SimilarityMatrix> similarities,
                                             Map<String, Double> weights) {
        TreeMap<Double, Instance> knn = new TreeMap<Double, Instance>(Collections.<Double>reverseOrder());
        for (Instance inst : instances) {
            double sim = calculateSimilarity(similarities, weights, instance, inst);
            knn.put(sim, inst);
        }

        return knn;
    }

    public void normalize() {
        HashMap<String, Double> attrMinValues = new HashMap<String, Double>();
        HashMap<String, Double> attrMaxValues = new HashMap<String, Double>();

        for (Instance inst : instances) {
            for (String attrName : attributeNames) {
                Attribute attr = inst.get(attrName);
                if (attr.isNumeric()) {
                    // update min values
                    Double oldMin = attrMinValues.get(attrName);
                    if (oldMin == null) {
                        attrMinValues.put(attrName, attr.getNumericValue());
                    } else {
                        attrMinValues.put(attrName, Math.min(oldMin, attr.getNumericValue()));
                    }

                    // update max values
                    Double oldMax = attrMaxValues.get(attrName);
                    if (oldMax == null) {
                        attrMaxValues.put(attrName, attr.getNumericValue());
                    } else {
                        attrMaxValues.put(attrName, Math.max(oldMax, attr.getNumericValue()));
                    }
                }
            }
        }

        for (Instance inst : instances) {
            for (String attrName : attributeNames) {
                Attribute attr = inst.get(attrName);
                if (attr.isNumeric()) {
                    double min = attrMinValues.get(attrName);
                    double max = attrMaxValues.get(attrName);
                    double actual = attr.getNumericValue();
                    double normalizedValue =  (actual - min) / (max - min);
                    inst.setAttribute(attrName, normalizedValue);
                }
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(this.instances);
    }
}
