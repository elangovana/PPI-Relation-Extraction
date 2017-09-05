package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCAnnotation;
import com.pengyifan.bioc.BioCPassage;

import java.util.ArrayList;
import java.util.HashMap;
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
            if (IsAnnotationTypeGene(annotation)) {
                //Get NCBI gene name
                Optional<String> ncbiGeneInfo = getNCBIGene(annotation);


                if (ncbiGeneInfo.isPresent()) {
                    geneSet.add(ncbiGeneInfo.get());
                }
            }
        }

        return new ArrayList<>(geneSet);
    }

    private Optional<String> getNCBIGene(BioCAnnotation annotation) {
        //TODO: clean up, case sensitive annotation key
        Optional<String> ncbiGeneInfo = annotation.getInfon("NCBI GENE");
        if (!ncbiGeneInfo.isPresent()){
            ncbiGeneInfo= annotation.getInfon("NCBI Gene");
        }
        ;
        return ncbiGeneInfo;
    }

    public ArrayList<String> getGeneNames(BioCPassage passage) {
        HashSet<String> geneSet = new HashSet<String>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (IsAnnotationTypeGene(annotation)) {
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

    public HashMap<String, String> GeneNameToGeneidMap(BioCPassage passage) {
        HashMap<String, String> hashMap = new HashMap<>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (IsAnnotationTypeGene(annotation)) {
                //Get NCBI gene name
                //TODO: clean up, case sensitive annotation key
                Optional<String> geneName = annotation.getText();
                Optional<String> geneId = getNCBIGene(annotation);

                if (geneName.isPresent() && geneId.isPresent()) {
                    hashMap.put(geneName.get(), geneId.get());
                }
            }
        }

        return hashMap;
    }

    private boolean IsAnnotationTypeGene(BioCAnnotation annotation) {
        return annotation.getInfon("type").get().equals("Gene");
    }
}
