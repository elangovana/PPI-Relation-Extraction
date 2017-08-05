package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aparnaelangovan on 5/08/2017.
 */
public class Pipeline {

    private RelationExtractor relationExtractor;

    private List<Scorer> scorers;




    public HashMap<String, Double> runRelationExtraction(String biocFileXmlWithGeneAnnotationsPath, String biocFileXmlTrainingDataPath  ) throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {

        File biocFileXmlWithGeneAnnotations = Paths.get(biocFileXmlWithGeneAnnotationsPath).toAbsolutePath().toFile();


        BioCCollectionReader biocCollGeneAnnotations = new Parser().getBioCCollection(biocFileXmlWithGeneAnnotations);

        BioCCollection pred= getRelationExtractor().Extract(biocCollGeneAnnotations);

        //Calculate Score
        HashMap<String, Double> result = new HashMap<>();
        if (biocFileXmlTrainingDataPath != null){
            File  biocFileXmlTrainingDatafile = Paths.get(biocFileXmlTrainingDataPath).toAbsolutePath().toFile();
            BioCCollection trainBioCCollection = new Parser().getBioCCollection(biocFileXmlTrainingDatafile).readCollection();
            for (Scorer scorer: getScorers()) {

                result.put(scorer.GetScoringMethodName(), scorer.CalculateScore(trainBioCCollection, pred));
            }

        }

        return result;

    }

    public RelationExtractor getRelationExtractor() {
        if ( relationExtractor == null) relationExtractor = new RelationExtractorCooccurance();

        return relationExtractor;
    }

    public void setRelationExtractor(RelationExtractor relationExtractor) {
        this.relationExtractor = relationExtractor;
    }

    public List<Scorer> getScorers() {
        if ( scorers == null) {
            scorers=new ArrayList<Scorer>();
            scorers.add(new PrecisionScore());
            scorers.add(new RecallScore());
        }

        return  scorers;
    }

    public void setScorers(List<Scorer> scorers) {
        this.scorers = scorers;
    }


}
