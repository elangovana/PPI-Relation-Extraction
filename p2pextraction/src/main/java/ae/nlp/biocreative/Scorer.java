package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;

/**
 * Created by aparnaelangovan on 5/08/2017.
 */
public interface Scorer {
    double CalculateScore(BioCCollection trainingSet, BioCCollection predictedSet);

    String GetScoringMethodName();
}
