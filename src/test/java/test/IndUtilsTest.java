package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IndUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(IndUtilsTest.class);

    @Test
    void recognize() {
        for (int dimension = 1; dimension <= 512; dimension++) {
            INDArray positive = IndUtils.normalizeOne(Nd4j.zeros(dimension).addi(1));
            INDArray negative = IndUtils.normalizeOne(Nd4j.zeros(dimension).addi(-1));
            INDArray embeddings2 = Nd4j.concat(0, positive.reshape(1, dimension), negative.reshape(1, dimension));
            double[] distance = IndUtils.recognize(positive, embeddings2);
            log.info("dimension: {}, max_probability: {}, min_probability: {}", dimension, distance[0], distance[1]);
        }
    }

    @Test
    void recognize_distribution() {
        double minGlobal = 1.0;
        double maxGlobal = -1.0;
        for (int i = 0; i < 10000; i++) {
            INDArray newFace = IndUtils.normalizeOne(Nd4j.rand(512).subi(0.5).muli(2));
            INDArray embeddings2 = IndUtils.normalizeOne(Nd4j.rand(1000, 512).subi(0.5).muli(2));
            INDArray result = IndUtils.euclidean_distance(newFace, embeddings2).divi(2).rsubi(1);
            Nd4j.sort(result, true);
            double max = result.getDouble(999);
            double min = result.getDouble(0);
            if (max > maxGlobal) {
                maxGlobal = max;
            }
            if (min < minGlobal) {
                minGlobal = min;
            }
            log.info("max_probability: {}, min_probability: {}", result.get(NDArrayIndex.point(999)), result.get(NDArrayIndex.point(0)));
        }
        log.info("Global max_probability: {}, min_probability: {}", maxGlobal, minGlobal);
    }
}