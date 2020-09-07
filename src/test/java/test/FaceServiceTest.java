package test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FaceServiceTest {

    private static final String COLLECTION_ID = "test";
    private static final String COLLECTION2_ID = "test2";

    @BeforeEach
    void setUp() {
        MockDBReader.clean();

        FaceService.addFace(new double[]{0.1d, 0.9d, 0.9d}, "image1", "face1", COLLECTION_ID);
        FaceService.addFace(new double[]{0.9d, 0.1d, 0.9d}, "image2", "face2", COLLECTION_ID);
        FaceService.addFace(new double[]{0.9d, 0.2d, 0.8d}, "image3", "face2", COLLECTION_ID);
    }

    @Test
    void recognize() {
        Set<FaceResult> resultList = FaceService.recognize(new double[]{0.9d, 0.1d, 0.9d}, COLLECTION_ID, 3);
        FaceResult[] faceResults = resultList.toArray(new FaceResult[0]);
        assertEquals("face2", faceResults[0].getName());

        resultList = FaceService.recognize(new double[]{0.8d, 0.25d, 0.7d}, COLLECTION_ID, 3);
        faceResults = resultList.toArray(new FaceResult[0]);
        assertEquals("face2", faceResults[0].getName());

        resultList = FaceService.recognize(new double[]{0.1d, 0.9d, 0.9d}, COLLECTION_ID, 3);
        faceResults = resultList.toArray(new FaceResult[0]);
        assertEquals("face1", faceResults[0].getName());

        resultList = FaceService.recognize(new double[]{0.3d, 0.8d, 0.7d}, COLLECTION_ID, 3);
        faceResults = resultList.toArray(new FaceResult[0]);
        assertEquals("face1", faceResults[0].getName());
    }

    @Test
    void testCache() {
        assertFalse(FaceCacheProvider.cached(COLLECTION2_ID));
        FaceCacheProvider.getOrLoad(COLLECTION2_ID);
        assertTrue(FaceCacheProvider.cached(COLLECTION2_ID));

        FaceService.addFace(new double[]{0.1d, 0.9d, 0.9d}, "image1", "face1", COLLECTION2_ID);
        FaceService.addFace(new double[]{0.9d, 0.1d, 0.9d}, "image2", "face2", COLLECTION2_ID);
        FaceService.addFace(new double[]{0.9d, 0.2d, 0.8d}, "image3", "face2", COLLECTION2_ID);

        Set<FaceResult> resultList = FaceService.recognize(new double[]{0.8d, 0.25d, 0.7d}, COLLECTION2_ID, 3);
        FaceResult[] faceResults = resultList.toArray(new FaceResult[0]);
        double probability1 = faceResults[0].getProbability();

        FaceCacheProvider.invalidate(COLLECTION2_ID);
        assertFalse(FaceCacheProvider.cached(COLLECTION2_ID));

        resultList = FaceService.recognize(new double[]{0.8d, 0.25d, 0.7d}, COLLECTION2_ID, 3);
        faceResults = resultList.toArray(new FaceResult[0]);
        double probability2 = faceResults[0].getProbability();
        // Probability of values added to cache directly and from read from db
        assertEquals(probability1, probability2);
    }
}