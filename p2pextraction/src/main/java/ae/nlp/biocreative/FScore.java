package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;

import java.util.HashMap;
import java.util.logging.Logger;


public class FScore implements Scorer {
    private static Logger theLogger =
            Logger.getLogger(FScore.class.getName());

    private HashMap<String, BioCDocument> _docHashMap;
    private Scorer precisionCalculator;
    private int beta=1;
    private Scorer recallCalculator;

    @Override
    public  double CalculateScore(BioCCollection trainingSet, BioCCollection predictedSet){
        double precision = getPrecisionCalculator().CalculateScore(trainingSet, predictedSet);
        double recall = getRecallCalculator().CalculateScore(trainingSet, predictedSet);

        theLogger.info(String.format("The recall  is %1$.5f", recall));
        theLogger.info(String.format("The score for method %s is %1$.5f", precision));

        return  (double)((beta +1)*precision * recall)/(double)(beta*beta*precision + recall);
    }



    @Override
    public String GetScoringMethodName() {
        return this.getClass().getName();
    }



    public void setPrecisionCalculator(Scorer precisionCalculator) {
        precisionCalculator = precisionCalculator;
    }

    public Scorer getPrecisionCalculator() {
        if (precisionCalculator == null) precisionCalculator = new PrecisionScore();

        return precisionCalculator;
    }

    public Scorer getRecallCalculator() {
        if (recallCalculator == null) recallCalculator = new RecallScore();
        return recallCalculator;
    }
}
