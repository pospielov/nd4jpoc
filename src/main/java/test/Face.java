package test;

import com.google.common.base.Objects;

public class Face {

    private String name;

    private String imageId;

    public Face(final String name, final String imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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
        final Face face = (Face) o;
        return Objects.equal(imageId, face.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(imageId);
    }
}
