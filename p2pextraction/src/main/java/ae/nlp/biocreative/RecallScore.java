package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCRelation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;


class RecallScore implements  Scorer  {
    private static Logger theLogger =
            Logger.getLogger(RecallScore.class.getName());


    private HashMap<String, BioCDocument> _docHashMap;

    public double CalculateScore(BioCCollection trainingSet, BioCCollection predictedSet){
        //Preprocess , build training set hash
        int actualTotalRel = GetTotalRelation(trainingSet);
        int predCorrectRel = 0;
        HashMap<String, BioCDocument> trainDocHashMap =  buildDocIdDocumentHash(trainingSet);

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
               }else{
                   theLogger.finest(String.format("The document id %s does not contain any relationship between %s & %s in the  training set", trainingDoc.getID(), Gene1, Gene2));

               }



            }
        }
        if (theLogger.isLoggable(Level.FINEST))  DebugWriteMissingRelations(trainingSet, predictedSet);
        return  (double)predCorrectRel/(double)actualTotalRel;
    }

    private void DebugWriteMissingRelations(BioCCollection trainingSet, BioCCollection predictedSet) {
        HashMap<String, BioCDocument> predDocIdHash = buildDocIdDocumentHash(predictedSet);
        for (BioCDocument trainDoc: trainingSet.getDocuments()) {
            BioCDocument predDoc = predDocIdHash.get(trainDoc.getID());

            for (BioCRelation trainRelation : trainDoc.getRelations()){

                BiocP2PRelation trainp2pRel =  new BiocP2PRelation(trainRelation);
                //If not ppim relation ignore
                if(! trainp2pRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;

                String Gene1 = trainp2pRel.getGene1();
                String Gene2 = trainp2pRel.getGene2();


               if (!RelationExistsInDoc(predDoc, Gene1, Gene2)){
                    theLogger.finest(String.format("The document id %s does not contain any relationship between %s & %s in the  set", predDoc.getID(), Gene1, Gene2));

                }



            }
        }
    }

    private int GetTotalRelation(BioCCollection trainingSet) {
        int total =0;
        for (BioCDocument doc:trainingSet.getDocuments()   ) {
            for (BioCRelation relation: doc.getRelations()) {

                BiocP2PRelation ppiRel = new BiocP2PRelation(relation);
                //If not ppim relation ignore relation
                if (!ppiRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;
                 total++;

            }

        }
        return  total;
    }

    private boolean RelationExistsInDoc(BioCDocument bioCDocument, String predGeneRelGene1, String predGeneRelGene2) {
        for (BioCRelation relation: bioCDocument.getRelations()) {

            BiocP2PRelation trainp2pRel =  new BiocP2PRelation(relation);
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
       return false;
    }

    private HashMap<String, BioCDocument> buildDocIdDocumentHash(BioCCollection trainingSet) {
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
