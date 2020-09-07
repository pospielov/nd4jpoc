package test;

import com.google.common.base.Objects;

public class FaceEntity {

    private String name;

    private String imageId;

    private double[] embedding;

    public FaceEntity(final String name, final String imageId, final double[] embedding) {
        this.name = name;
        this.imageId = imageId;
        this.embedding = embedding;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public double[] getEmbedding() {
        return embedding;
    }

    public void setEmbedding(final double[] embedding) {
        this.embedding = embedding;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(final String imageId) {
        this.imageId = imageId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FaceEntity that = (FaceEntity) o;
        return Objects.equal(imageId, that.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(imageId);
    }
}
