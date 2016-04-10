package util;

import java.util.HashMap;
import java.util.Map;

public class Instance {
    private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
    private String classLabel;

    public boolean isSameClass(Instance inst, Instance prevInstance) {
        return inst.getClassLabel().equals(prevInstance.getClassLabel());
    }

    public void addAttribute(String attrName, String nominalValue) {
        Attribute attr = new Attribute(nominalValue);
        this.attributes.put(attrName, attr);
    }

    public void setAttribute(String attrName, Double numericValue) {
        Attribute attr = new Attribute(numericValue);
        this.attributes.put(attrName, attr);
    }

    public void setClassLabel(String classLabel) {
        this.classLabel = classLabel;
    }

    public String getClassLabel() {
        return classLabel;
    }

    public Attribute get(String attrName) {
        return this.attributes.get(attrName);
    }
}
