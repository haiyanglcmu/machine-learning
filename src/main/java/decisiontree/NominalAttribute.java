package decisiontree;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Haiyang on 4/5/16.
 */
public class NominalAttribute extends Attribute {
    private String attrName;
    private Set<String> validValues = new HashSet<String>();

    public NominalAttribute(String attrName) {
        this.attrName = attrName;
    }

    public void addValue(String value) {
        validValues.add(value);
    }

    @Override
    public AttributeType getType() {
        return AttributeType.NOMINAL;
    }

    public boolean hasValue(String value) {
        return validValues.contains(value);
    }
}
