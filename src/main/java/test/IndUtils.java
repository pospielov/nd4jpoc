package test;

import com.google.common.primitives.Doubles;
import java.util.Arrays;
import org.nd4j.linalg.api.ndarray.INDArray;

public class IndUtils {

    public static INDArray normalizeOne(INDArray embeddings) {
        INDArray embeddings1Norm = embeddings.norm2();
        return embeddings.divi(embeddings1Norm);
    }

    public static INDArray normalize(INDArray embeddings) {
        INDArray embeddings1Norm = embeddings.norm2(1);
        return embeddings.divi(embeddings1Norm);
    }

    public static double[] recognize(INDArray newFace, INDArray existingFaces) {
        INDArray dist = euclidean_distance(newFace, existingFaces);
        dist.divi(2).rsubi(1);
        return dist.toDoubleVector();
    }

    public static INDArray euclidean_distance(INDArray newFace, INDArray existingFaces) {
        existingFaces = existingFaces.subi(newFace);
        return existingFaces.norm2(1);
    }

    public static int[] argsort(final double[] a) {
        Integer[] indexes = new Integer[a.length];

        for(int i = 0; i < indexes.length; ++i) {
            indexes[i] = i;
        }

        Arrays.sort(indexes, (i1, i2) -> -Doubles.compare(a[i1], a[i2]));
        int[] ret = new int[indexes.length];

        for(int i = 0; i < ret.length; ++i) {
            ret[i] = indexes[i];
        }

        return ret;
    }

}
