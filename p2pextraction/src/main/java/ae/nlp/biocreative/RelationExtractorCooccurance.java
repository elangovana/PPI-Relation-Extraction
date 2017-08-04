package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

public class RelationExtractorCooccurance {

    public BioCCollection Extract(BioCCollectionReader biocCollection) throws XMLStreamException, IOException, InterruptedException {
        BioCCollection output = new BioCCollection();

        for (Iterator<BioCDocument> doci = biocCollection.readCollection().documentIterator(); doci.hasNext(); ) {
            BioCDocument doc = doci.next();
            HashSet<String> geneSet = new HashSet<String>();
            System.out.println("--Doc" + doc.getID());

            for (BioCPassage passage : doc.getPassages()) {
                System.out.println("----passage");
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
            }


            //Add relation
            List<String> geneList = new ArrayList<String>(geneSet);
            for (int i = 0, r=0;  i < geneList.size(); i++) {

                for (int j = i + 1; j < geneList.size(); j++, r++) {
                    BioCRelation relation = new BioCRelation();


                    Map<String, String> infon = new HashMap<>();
                    infon.put("Gene1", geneList.get(i));
                    infon.put("Gene2", geneList.get(j));
                    infon.put("relation", "PPIm");
                    relation.setInfons(infon);

                    relation.setID("REL" + r);

                    output.addDocument(doc);

                    doc.addRelation(relation);


                }
            }



        }

        biocCollection.close();

        return  output;

    }

}

