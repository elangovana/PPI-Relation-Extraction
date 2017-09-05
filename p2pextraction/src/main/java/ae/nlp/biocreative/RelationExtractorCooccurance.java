package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RelationExtractorCooccurance implements RelationExtractor {
    private static Logger theLogger =
            Logger.getLogger(RelationExtractorCooccurance.class.getName());


    @Override
    public BioCCollection Extract(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {
        try{
            BioCCollection outBiocCollection = new BioCCollection();

            for (Iterator<BioCDocument> doci = biocCollectionReader.readCollection().documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();

                HashSet<String> existingGeneRelationsFromPreviousPassage = new HashSet<>();

                for (BioCPassage passage : doc.getPassages()) {
                    List<String> genesInPassage = new BiocGeneHelper().getNormliasedGenes (passage);
                    DebugWriteGenesFound(doc.getID(), genesInPassage);
                    addRelationToDoc(doc, existingGeneRelationsFromPreviousPassage, genesInPassage);
                    //To avoid duplicates
                    existingGeneRelationsFromPreviousPassage.addAll(genesInPassage);

                }
                outBiocCollection.addDocument(doc);


            }



            return outBiocCollection;
        }
        finally {
            if (biocCollectionReader!= null) biocCollectionReader.close();
        }


    }

    private void DebugWriteGenesFound(String docId, List<String> genesInPassage) {
        if (theLogger.isLoggable(Level.FINEST))

            for (String gene : genesInPassage){
            theLogger.finest(String.format("Gene found in docid %s , geneid %s", docId, gene));

        }

    }

    private void addRelationToDoc(BioCDocument doc, HashSet<String> geneRelationShipsAlreadyAdded, List<String> genesInPassage) {


        for (int i = 0; i < genesInPassage.size(); i++) {

            for (int j = i + 1; j < genesInPassage.size(); j++) {

                 //Ignore if the relationship already exists
                String gene1 = genesInPassage.get(i);
                String gene2 = genesInPassage.get(j);
                if (CheckForDuplicateRelation(geneRelationShipsAlreadyAdded, gene1, gene2)) continue;

                BioCRelation relation = new BiocP2PRelation().getBioCRelation(gene1, gene2);


                doc.addRelation(relation);


            }
        }
    }



    private boolean CheckForDuplicateRelation(HashSet<String> geneList, String gene1, String gene2) {
        return geneList.contains(gene1) && geneList.contains(gene2);
    }


    /**
     * @param passage Biocpassage
     * @return Returns the list of genes from the annotations
     */


}

