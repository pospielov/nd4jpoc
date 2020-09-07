package test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.concurrent.TimeUnit;

public class FaceCacheProvider {

    private static final long CACHE_EXPIRATION = 10;
    private static final long CACHE_MAXIMUM_SIZE = 3;

    private static Cache<String, FaceCollection> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(CACHE_EXPIRATION, TimeUnit.SECONDS)
                                 .maximumSize(CACHE_MAXIMUM_SIZE)
                                 .build();



    public static FaceCollection getOrLoad(String collectionId) {
        if (collectionId == null) {
            return null;
        }

        FaceCollection result = cache.getIfPresent(collectionId);

        if (result == null) {
            result = FaceCollection.buildFromFaces(MockDBReader.findFaces(collectionId));
            cache.put(collectionId, result);
        }

        return result;
    }

    public static boolean cached(String collectionId) {
        return cache.getIfPresent(collectionId) != null;
    }

    public static void invalidate(String collectionId) {
        cache.invalidate(collectionId);
    }

}
