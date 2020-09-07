package test;

import java.util.Arrays;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaceCollectionTest {
    
    private static final String COLLECTION_ID = "test";

    @Test
    void buildFromFaces() {
    }

    @BeforeEach
    void setUp() {
        MockDBReader.clean();
        MockDBReader.addFace(COLLECTION_ID, new FaceEntity("face1", "image1", new double[]{1.0d, 1.0d, 1.0d}));
        MockDBReader.addFace(COLLECTION_ID, new FaceEntity("face2", "image2", new double[]{0.9d, 0.1d, 0.9d}));
        MockDBReader.addFace(COLLECTION_ID, new FaceEntity("face2", "image3", new double[]{0.9d, 0.9d, 0.1d}));
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreate() {
        FaceCollection faceCollection = FaceCollection.buildFromFaces(MockDBReader.findFaces(COLLECTION_ID));

        assertEquals(3, faceCollection.getFacesMap().size());
        assertTrue(Arrays.equals(new long[]{3, 3}, faceCollection.getEmbeddings().shape()));
        assertEquals(0, faceCollection.getFacesMap().get(new Face("", "image1")));
        assertEquals(1, faceCollection.getFacesMap().get(new Face("", "image2")));
        assertEquals(2, faceCollection.getFacesMap().get(new Face("", "image3")));
    }

    @Test
    void testAdd() {
        FaceCollection faceCollection = FaceCollection.buildFromFaces(MockDBReader.findFaces(COLLECTION_ID));

        faceCollection.addFace(new FaceEntity("face3", "image4", new double[]{0.8d, 0.8d, 0.2d}));

        assertEquals(4, faceCollection.getFacesMap().size());
        assertTrue(Arrays.equals(new long[]{4, 3}, faceCollection.getEmbeddings().shape()));
        assertEquals(3, faceCollection.getFacesMap().get(new Face("", "image4")));
    }

    @Test
    void testRemove() {
        FaceCollection faceCollection = FaceCollection.buildFromFaces(MockDBReader.findFaces(COLLECTION_ID));

        faceCollection.removeFace("image2");

        assertEquals(2, faceCollection.getFacesMap().size());
        assertEquals(0, faceCollection.getFacesMap().get(new Face("", "image1")));
        assertEquals(1, faceCollection.getFacesMap().get(new Face("", "image3")));
        assertTrue(Arrays.equals(new long[]{2, 3}, faceCollection.getEmbeddings().shape()));
        assertNull(faceCollection.getFacesMap().get(new Face("", "image2")));
    }


}