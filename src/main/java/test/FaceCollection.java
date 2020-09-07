package test;

import static java.util.stream.Collectors.toMap;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;

public class FaceCollection {

    private final BiMap<Face, Integer> facesMap;

    private INDArray embeddings;

    private INDArray embeddingsCopy;

    private final AtomicInteger size;

    private FaceCollection(final BiMap<Face, Integer> facesMap, INDArray embeddings, AtomicInteger size) {
        this.facesMap = facesMap;
        this.embeddings = embeddings;
        if (embeddings != null) {
            this.embeddingsCopy = embeddings.dup();
        }
        this.size = size;
    }

    public BiMap<Face, Integer> getFacesMap() {
        return facesMap;
    }

    public INDArray getEmbeddings() {
        embeddingsCopy.assign(embeddings);
        return embeddingsCopy;
    }

    public static FaceCollection buildFromFaces(List<FaceEntity> faces) {
        if (faces.size() < 1) {
            return new FaceCollection(HashBiMap.create(), null, new AtomicInteger());
        }
        double[][] rawEmbeddings = faces.stream().map(FaceEntity::getEmbedding).toArray(double[][]::new);
        INDArray indArray = Nd4j.create(rawEmbeddings);
        AtomicInteger index = new AtomicInteger();
        Map<Face, Integer> facesMap = faces.stream().collect(toMap(
                face -> new Face(face.getName(), face.getImageId()),
                face -> index.getAndIncrement()));

        return new FaceCollection(HashBiMap.create(facesMap), indArray, index);
    }

    synchronized public void addFace(FaceEntity faceEntity) {
        facesMap.put(new Face(faceEntity.getName(), faceEntity.getImageId()), size.get());
        if (embeddings == null) {
            embeddings = Nd4j.create(new double[][]{faceEntity.getEmbedding()});
        } else {
            embeddings = Nd4j.concat(0, embeddings, Nd4j.create(new double[][]{faceEntity.getEmbedding()}));
        }

        embeddingsCopy = embeddings.dup();
        size.getAndIncrement();
    }

    synchronized public void removeFace(String imageId) {
        if (facesMap.size() == 0) {
            return;
        }
        Face faceToDelete = new Face(null, imageId);
        Integer index = facesMap.get(faceToDelete);
        facesMap.remove(faceToDelete);
        facesMap.entrySet().forEach(entry -> {
            if (entry.getValue() > index) {
                entry.setValue(entry.getValue() - 1);
            }
        });

        embeddings = Nd4j.concat(0, embeddings.get(NDArrayIndex.interval(0,index), NDArrayIndex.all()),
                embeddings.get(NDArrayIndex.interval(index + 1, size.get()), NDArrayIndex.all()));
        embeddingsCopy = embeddings.dup();

        size.getAndIncrement();
    }
}
