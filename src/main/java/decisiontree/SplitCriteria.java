package decisiontree;

/**
 * Created by Haiyang on 4/5/16.
 */
public class SplitCriteria {
    private String nominalValue;
    private double minValue;
    private double maxValue;

    public boolean match(String value) {
        return nominalValue.equals(value);
    }

    public boolean match(double value) {
        return value >= minValue && value <= maxValue;
    }
}
