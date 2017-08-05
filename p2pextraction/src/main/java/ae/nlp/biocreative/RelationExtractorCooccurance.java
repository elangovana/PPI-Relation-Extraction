package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

public class RelationExtractorCooccurance {

    public BioCCollection Extract(BioCCollectionReader biocCollection) throws XMLStreamException, IOException, InterruptedException {
        try{
            BioCCollection outBiocCollection = new BioCCollection();

            for (Iterator<BioCDocument> doci = biocCollection.readCollection().documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();

                HashSet<String> genesInDoc = new HashSet<>();

                for (BioCPassage passage : doc.getPassages()) {
                    List<String> genesInPassage = getGenes(passage);
                    addRelationToDoc(doc, genesInDoc, genesInPassage);
                    //To avoid duplicates
                    genesInDoc.addAll(genesInPassage);

                }
                outBiocCollection.addDocument(doc);


            }

            biocCollection.close();

            return outBiocCollection;
        }
        finally {
            if (biocCollection!= null) biocCollection.close();
        }


    }

    private void addRelationToDoc(BioCDocument doc, HashSet<String> genesInDoc, List<String> genesInPassage) {
        for (int i = 0; i < genesInPassage.size(); i++) {

            for (int j = i + 1; j < genesInPassage.size(); j++) {
                if (CheckForDuplicateRelation(genesInDoc, genesInPassage.get(i), genesInPassage.get(j))) continue;

                BioCRelation relation = new BioCRelation();


                Map<String, String> infon = new HashMap<>();
                infon.put("Gene1", genesInPassage.get(i));
                infon.put("Gene2", genesInPassage.get(j));
                infon.put("relation", "PPIm");
                relation.setInfons(infon);

                relation.setID(genesInPassage.get(i) + "#" + genesInPassage.get(j));


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
    private ArrayList<String> getGenes(BioCPassage passage) {
        HashSet<String> geneSet = new HashSet<String>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (annotation.getInfon("type").get().equals("Gene")) {
                //Get NCBI gene name
                Optional<String> ncbiGeneInfo = annotation.getInfon("NCBI GENE");
                if (ncbiGeneInfo.isPresent()) {
                    geneSet.add(ncbiGeneInfo.get());
                }
            }
        }

        return new ArrayList<>(geneSet);
    }

}

