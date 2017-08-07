package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCRelation;

import java.util.Optional;

/**
 * Created by aparnaelangovan on 7/08/2017.
 */
public class BiocP2PRelation {

    private final BioCRelation bioRelation;

    public static final String RelationTypePPIM  = "PPIm";

    public BiocP2PRelation(BioCRelation bioCRelation){
        this.bioRelation = bioCRelation;
    }

    public String getGene1(){
        return bioRelation.getInfon("Gene1").get();
    }

    public String getGene2(){
        return bioRelation.getInfon("Gene2").get();
    }

    public String getRelationType(){
        Optional<String> predPpiRel = bioRelation.getInfon("relation");
        if (predPpiRel.isPresent() ) return  predPpiRel.get();

        return "";
    }
}
