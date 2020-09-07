package test;

import com.google.common.collect.BiMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

public class FaceService {

    public static Set<FaceResult> recognize(double[] embedding, String collectionId, int resultCount) {
        INDArray inputFace = Nd4j.create(embedding);
        inputFace = IndUtils.normalizeOne(inputFace);

        FaceCollection faceCollection = FaceCacheProvider.getOrLoad(collectionId);

        double[] probabilities = IndUtils.recognize(inputFace, faceCollection.getEmbeddings());

        int[] argsort = IndUtils.argsort(probabilities);

        BiMap<Integer, Face> facesMap = faceCollection.getFacesMap().inverse();
        Set<FaceResult> result = new LinkedHashSet<>(resultCount);
        int foundFaces = 0;
        int i = 0;
        int maxCount = Math.min(resultCount, argsort.length);
        while (foundFaces < maxCount) {
            Face face = facesMap.get(argsort[i]);
            if (!result.contains(face)) {
                double prob = probabilities[argsort[i]];
                prob = prob < 0.0 ? 0 : prob;
                result.add(new FaceResult(face.getName(), prob));
                foundFaces++;
            }
            i++;
        }
        return result;
    }

    public static void addFace(double[] embedding, String imageId, String faceName, String collectionId) {
        INDArray inputFace = Nd4j.create(embedding);
        inputFace = IndUtils.normalizeOne(inputFace);

        FaceEntity faceEntity = new FaceEntity(faceName, imageId, inputFace.toDoubleVector());
        MockDBReader.addFace(collectionId, faceEntity);
        if (FaceCacheProvider.cached(collectionId)) {
            FaceCacheProvider.getOrLoad(collectionId).addFace(faceEntity);
        }
    }

    public static void deleteFace(String imageId, String collectionId) {
        MockDBReader.deleteFace(collectionId, new FaceEntity(null, imageId, null));
        if (FaceCacheProvider.cached(collectionId)) {
            FaceCacheProvider.getOrLoad(collectionId).removeFace(imageId);
        }
    }

}
