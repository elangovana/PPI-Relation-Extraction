package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCRelation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.logging.Logger;


public class PrecisionScore implements Scorer {
    private static Logger theLogger =
            Logger.getLogger(PrecisionScore.class.getName());

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

               if (RelationExistsInDoc(trainingDoc, Gene1, Gene2)){
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

    private boolean RelationExistsInDoc(BioCDocument refDocument, String geneToMatch1, String geneToMatch2) {
        for (BioCRelation relation: refDocument.getRelations()) {

            BiocP2PRelation refp2pRel =  new BiocP2PRelation(relation);
            //If not ppim relation ignore
            if(! refp2pRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;

            String refGene1 = refp2pRel.getGene1();
            String refGene2 = refp2pRel.getGene2();

            //Use hashmap to check for undirected relationship between 2 genes
            HashSet<String> refGenesRelHash = new HashSet<>();
            refGenesRelHash.add(refGene1);
            refGenesRelHash.add(refGene2);

            //If self relationships then has should be one
            if (geneToMatch1.equals(geneToMatch2)) {
                if (refGenesRelHash.size() ==1 && refGenesRelHash.contains(geneToMatch1)) return  true;
                continue;

            }
            else if (refGenesRelHash.contains(geneToMatch1) && refGenesRelHash.contains(geneToMatch2))
                return  true;


        }
        return false;
    }


    private HashMap<String, BioCDocument>  buildTrainingSetHash(BioCCollection trainingSet) {
        HashMap<String, BioCDocument> docHashMap = new HashMap<>();
        for (BioCDocument doc: trainingSet.getDocuments()   ) {
            docHashMap.put(doc.getID(), doc);

        }
        return  docHashMap;
    }


}
