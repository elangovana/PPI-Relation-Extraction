package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCRelation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;


public class PrecisionScore implements Scorer {

    private HashMap<String, BioCDocument> _docHashMap;

    @Override
    public  double CalculateScore(BioCCollection trainingSet, BioCCollection predictedSet){
        //Preprocess , build training set hash
        int predTotalRel =0;
        int predCorrectRel = 0;
        HashMap<String, BioCDocument> trainDocHashMap =  buildTrainingSetHash(trainingSet);

        for (BioCDocument predDoc: predictedSet.getDocuments()) {
            BioCDocument trainingDoc = trainDocHashMap.get(predDoc.getID());

            for (BioCRelation predRelation : predDoc.getRelations()){
               BiocP2PRelation predp2pRel =  new BiocP2PRelation(predRelation);
                //If not ppim relation ignore
                if(! predp2pRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;

               String Gene1 = predp2pRel.getGene1();
               String Gene2 = predp2pRel.getGene2();

               if (ExistsInTraining(trainingDoc, Gene1, Gene2)){
                   predCorrectRel++;
               }
               predTotalRel++;


            }
        }
        return  (double)predCorrectRel/(double)predTotalRel;
    }

    @Override
    public String GetScoringMethodName() {
        return this.getClass().getName();
    }

    private boolean ExistsInTraining(BioCDocument trainingDoc, String predGeneRelGene1, String predGeneRelGene2) {
        boolean result = false;
        for (BioCRelation trainRelation: trainingDoc.getRelations()) {
            BiocP2PRelation trainp2pRel =  new BiocP2PRelation(trainRelation);
            //If not ppim relation ignore
            if(! trainp2pRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;

            String trainGene1 = trainp2pRel.getGene1();
            String trainGene2 = trainp2pRel.getGene2();

            //Use hashmap to check for undirected relationship between 2 genes
            HashSet<String> trainGenesRelHash = new HashSet<>();
            trainGenesRelHash.add(trainGene1);
            trainGenesRelHash.add(trainGene2);

            if (trainGenesRelHash.contains(predGeneRelGene1) && trainGenesRelHash.contains(predGeneRelGene2))
                return  true;


        }

        return  result;
    }

    private HashMap<String, BioCDocument>  buildTrainingSetHash(BioCCollection trainingSet) {
        HashMap<String, BioCDocument> docHashMap = new HashMap<>();
        for (BioCDocument doc: trainingSet.getDocuments()   ) {
            docHashMap.put(doc.getID(), doc);

        }
        return  docHashMap;
    }


}
