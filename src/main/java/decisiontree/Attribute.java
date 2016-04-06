package decisiontree;

/**
 * Created by Haiyang on 4/5/16.
 */
public class Attribute implements Comparable<Attribute> {
    private AttributeType attrType;
    private String nominalValue;
    private double minValue;
    private double maxValue;

    public enum AttributeType {
        NOMINAL, NUMERIC
    }

    public Attribute(String nominalValue) {
        this.nominalValue = nominalValue;
        this.attrType = AttributeType.NOMINAL;
    }

    public Attribute(double numericValue) {
        this.maxValue = numericValue;
        this.attrType = AttributeType.NUMERIC;
    }

    public Attribute(double min, double max) {
        this.minValue = min;
        this.maxValue = max;
        this.attrType = AttributeType.NUMERIC;
    }

    public AttributeType getType() {
        return attrType;
    }

    public boolean isNumeric() {
        return this.attrType.equals(AttributeType.NUMERIC);
    }

    public double getNumericValue() {
        return this.maxValue;
    }

    public String getNominalValue() {
        return this.nominalValue;
    }

    public boolean contains(Attribute attr) {
        double value = attr.getNumericValue();
        return value >= minValue && value <= maxValue;
    }

    public int compareTo(Attribute other) {
        if (other.getType().equals(AttributeType.NOMINAL)) {
            throw new IllegalArgumentException();
        }

        double com = this.maxValue - other.maxValue;
        if (com < 0) {
            return -1;
        } else if (com > 0) {
            return 1;
        }
        return 0;
    }

    public String toString() {
        if (this.attrType.equals(AttributeType.NOMINAL)) {
            return this.nominalValue;
        } else {
            return minValue + ":" + maxValue;
        }
    }
}
