package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MockDBReader {

    private static HashMap<String, List<FaceEntity>> dbMock = new HashMap<>();

    public static List<FaceEntity> findFaces(String collectionId) {
        return dbMock.getOrDefault(collectionId, new ArrayList<>());
    }

    public static void addFace(String collectionId, FaceEntity faceEntity) {
        if (dbMock.containsKey(collectionId)) {
            dbMock.get(collectionId).add(faceEntity);
        } else {
            List<FaceEntity> faces = new ArrayList<>();
            faces.add(faceEntity);
            dbMock.put(collectionId, faces);
        }
    }

    public static void deleteFace(String collectionId, FaceEntity faceEntity) {
        if (dbMock.containsKey(collectionId)) {
            dbMock.get(collectionId).remove(faceEntity);
        }
    }

    public static void clean() {
        dbMock.clear();
    }

}
