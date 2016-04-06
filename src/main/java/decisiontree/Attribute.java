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
        this.minValue = numericValue;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (attrType != attribute.attrType) return false;

        if (isNumeric()) {
            return attribute.maxValue >= minValue && attribute.maxValue <= maxValue;
        } else {
            return nominalValue != null ? nominalValue.equals(attribute.nominalValue) : attribute.nominalValue == null;
        }
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = attrType != null ? attrType.hashCode() : 0;
        result = 31 * result + (nominalValue != null ? nominalValue.hashCode() : 0);
        temp = Double.doubleToLongBits(maxValue);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
