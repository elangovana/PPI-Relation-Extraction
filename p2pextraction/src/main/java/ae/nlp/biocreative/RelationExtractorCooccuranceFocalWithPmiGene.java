package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;


public class RelationExtractorCooccuranceFocalWithPmiGene implements RelationExtractor {


    @Override
    public BioCCollection Extract(BioCCollection biocCollection) throws Exception {


        try {
            BioCCollection outBiocCollection = new BioCCollection();
            HashMap<String, BioCDocument> pmiGeneBiocCollectionHash = buildDocIdDocHash(new RelationExtractorCooccurancePmi().Extract(            new DeepCopyBiocCCollection().deepCopy(biocCollection)
            ));



            HashMap<String, BioCDocument> focalGeneBiocCollectionHash = buildDocIdDocHash(new RelationExtractorCooccuranceFocalGene().Extract(            new DeepCopyBiocCCollection().deepCopy(biocCollection)
            ));




            for (Iterator<BioCDocument> doci = biocCollection.documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();


                HashSet<UnorderedPair> relations = getRelations(pmiGeneBiocCollectionHash.get(doc.getID()));
                relations.addAll( getRelations(focalGeneBiocCollectionHash.get(doc.getID())));

                ArrayList<UnorderedPair> totalRelationsList = new ArrayList<>(relations);
                for (UnorderedPair pair: totalRelationsList  ) {
                    List<String> items = pair.getItems();
                    String gene1 = items.get(0);
                    String gene2 = items.get(0);
                    if (items.size() == 2){
                        gene2=items.get(1);
                    }
                    BioCRelation relation = new BiocP2PRelation().getBioCRelation (gene1, gene2);

                    doc.addRelation(relation);
                }

                outBiocCollection.addDocument(doc);

            }


            return outBiocCollection;
        } finally {

        }


    }

    private HashSet<UnorderedPair> getRelations(BioCDocument bioCDocument) {
        HashSet<UnorderedPair> result = new HashSet<>();
        for (BioCRelation predRelation : bioCDocument.getRelations()) {
            BiocP2PRelation predp2pRel = new BiocP2PRelation(predRelation);
            //If not ppim relation ignore
            if (!predp2pRel.getRelationType().equals(BiocP2PRelation.RelationTypePPIM)) continue;

            String Gene1 = predp2pRel.getGene1();
            String Gene2 = predp2pRel.getGene2();

            result.add(new UnorderedPair(Gene1, Gene2));
        }

        return  result;
    }

    private HashMap<String, BioCDocument> buildDocIdDocHash(BioCCollection bioCCollection) {
        HashMap<String, BioCDocument> docHashMap = new HashMap<>();
        for (BioCDocument doc : bioCCollection.getDocuments()) {
            docHashMap.put(doc.getID(), doc);

        }
        return docHashMap;
    }

    private BioCRelation getBioCRelation(UnorderedPair entry) {
        ArrayList<String> genes = entry.getItems();
        String gene1 = genes.get(0);
        String gene2 = gene1;

        if (genes.size() == 2) {

            gene2 = genes.get(1);
        }
        return new BiocP2PRelation().getBioCRelation(gene1, gene2);
    }
}

