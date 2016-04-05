package decisiontree;

/**
 * Created by Haiyang on 4/5/16.
 */
public abstract class Attribute {
    enum AttributeType {
        NOMINAL, NUMERIC
    }

    public abstract AttributeType getType();
}
