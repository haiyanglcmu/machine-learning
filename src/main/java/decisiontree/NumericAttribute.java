package decisiontree;

/**
 * Created by Haiyang on 4/5/16.
 */
public class NumericAttribute extends Attribute {
    private String attrName;
    public NumericAttribute(String attrName) {
        this.attrName = attrName;
    }

    @Override
    public AttributeType getType() {
        return AttributeType.NUMERIC;
    }
}
