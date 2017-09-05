package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCRelation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by aparnaelangovan on 7/08/2017.
 */
public class BiocP2PRelation {

    private  BioCRelation bioRelation;

    public static final String RelationTypePPIM  = "PPIm";

    public BiocP2PRelation(BioCRelation bioCRelation){
        this.bioRelation = bioCRelation;
    }

    public BiocP2PRelation(){

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

    public BioCRelation getBioCRelation(String gene1, String gene2) {
        BioCRelation relation = new BioCRelation();


        Map<String, String> infon = new HashMap<>();
        infon.put("Gene1", gene1);
        infon.put("Gene2", gene2);
        infon.put("relation", "PPIm");
        relation.setInfons(infon);

        relation.setID(gene1 + "#" + gene2);
        return relation;
    }
}
