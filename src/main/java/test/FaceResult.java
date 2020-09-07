package test;

import com.google.common.base.Objects;

public class FaceResult {

    private String name;

    private double probability;

    public FaceResult(final String name, final double probability) {
        this.name = name;
        this.probability = probability;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(final double probability) {
        this.probability = probability;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FaceResult that = (FaceResult) o;
        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "FaceResult{" +
                "name='" + name + '\'' +
                ", probability=" + probability +
                '}';
    }
}
