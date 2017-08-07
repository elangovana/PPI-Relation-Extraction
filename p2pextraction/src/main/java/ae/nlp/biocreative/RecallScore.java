package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCRelation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;


class RecallScore implements  Scorer  {
    private static Logger theLogger =
            Logger.getLogger(RecallScore.class.getName());


    private HashMap<String, BioCDocument> _docHashMap;

    public double CalculateScore(BioCCollection trainingSet, BioCCollection predictedSet){
        //Preprocess , build training set hash
        int actualTotalRel = GetTotalRelation(trainingSet);
        int predCorrectRel = 0;
        HashMap<String, BioCDocument> trainDocHashMap =  buildTrainingSetHash(trainingSet);

        for (BioCDocument predDoc: predictedSet.getDocuments()) {
            BioCDocument trainingDoc = trainDocHashMap.get(predDoc.getID());

            for (BioCRelation predRelation : predDoc.getRelations()){
                Optional<String> predPpiRel = predRelation.getInfon("relation");
                //If not ppim relation ignore relation
                if(! (predPpiRel.isPresent() && predPpiRel.get().equals("PPIm"))) continue;

               String Gene1 = predRelation.getInfon("Gene1").get();
               String Gene2 =  predRelation.getInfon("Gene2").get();

               if (ExistsInTraining(trainingDoc, Gene1, Gene2)){
                   predCorrectRel++;
               }



            }
        }
        return  (double)predCorrectRel/(double)actualTotalRel;
    }

    private int GetTotalRelation(BioCCollection trainingSet) {
        int total =0;
        for (BioCDocument doc:trainingSet.getDocuments()   ) {
            for (BioCRelation relation: doc.getRelations()) {

                Optional<String> ppiRel = relation.getInfon("relation");
                //If not ppim relation ignore relation
                if (!(ppiRel.isPresent() && ppiRel.get().equals("PPIm"))) continue;

                 total++;

            }

        }
        return  total;
    }

    private boolean ExistsInTraining(BioCDocument trainingDoc, String predGeneRelGene1, String predGeneRelGene2) {
        for (BioCRelation relation: trainingDoc.getRelations()) {

            Optional<String> ppiRel = relation.getInfon("relation");
            //If not ppim relation ignore relation
            if (!(ppiRel.isPresent() && ppiRel.get().equals("PPIm"))) continue;

            String trainGene1 = relation.getInfon("Gene1").get();
            String trainGene2 =  relation.getInfon("Gene2").get();

            //Use hashmap to check for undirected relationship between 2 genes
            HashSet<String> trainGenesRelHash = new HashSet<>();
            trainGenesRelHash.add(trainGene1);
            trainGenesRelHash.add(trainGene2);

            if (trainGenesRelHash.contains(predGeneRelGene1) && trainGenesRelHash.contains(predGeneRelGene2))
                return  true;


        }
        theLogger.finest(String.format("The document id %s does not contain any relationship between %s & %s in the training set", trainingDoc.getID(), predGeneRelGene1, predGeneRelGene2));
        return false;
    }

    private HashMap<String, BioCDocument>  buildTrainingSetHash(BioCCollection trainingSet) {
        HashMap<String, BioCDocument> docHashMap = new HashMap<>();
        for (BioCDocument doc: trainingSet.getDocuments()   ) {
            docHashMap.put(doc.getID(), doc);

        }
        return  docHashMap;
    }

    @Override
    public String GetScoringMethodName() {
        return this.getClass().getName();
    }
}
