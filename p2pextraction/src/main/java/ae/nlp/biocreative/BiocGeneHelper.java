package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCPassage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

/**
 * Created by aparnaelangovan on 5/09/2017.
 */
public class BiocGeneHelper {

    public ArrayList<String> getNormliasedGenes(BioCPassage passage) {
        HashSet<String> geneSet = new HashSet<String>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (annotation.getInfon("type").get().equals("Gene")) {
                //Get NCBI gene name
                //TODO: clean up, case sensitive annotation key
                Optional<String> ncbiGeneInfo = annotation.getInfon("NCBI GENE");
                if (!ncbiGeneInfo.isPresent()){
                    ncbiGeneInfo= annotation.getInfon("NCBI Gene");
                };

                if (ncbiGeneInfo.isPresent()) {
                    geneSet.add(ncbiGeneInfo.get());
                }
            }
        }

        return new ArrayList<>(geneSet);
    }

    public ArrayList<String> getGeneNames(BioCPassage passage) {
        HashSet<String> geneSet = new HashSet<String>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (annotation.getInfon("type").get().equals("Gene")) {
                //Get NCBI gene name
                //TODO: clean up, case sensitive annotation key
                Optional<String> geneName = annotation.getText();

                if (geneName.isPresent()) {
                    geneSet.add(geneName.get());
                }
            }
        }

        return new ArrayList<>(geneSet);
    }
}
