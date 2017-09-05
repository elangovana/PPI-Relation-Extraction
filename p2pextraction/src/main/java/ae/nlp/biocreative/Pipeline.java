package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionReader;
import com.pengyifan.bioc.io.BioCCollectionWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by aparnaelangovan on 5/08/2017.
 */
public class Pipeline {

    private List<RelationExtractor> relationExtractors;

    private List<Scorer> scorers;

    private static Logger theLogger =
            Logger.getLogger(Pipeline.class.getName());


    public HashMap<String, Double> runRelationExtraction(String biocFileXmlWithGeneAnnotationsPath, String biocFileXmlTrainingDataPath, String outputPath  ) throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {

        File biocFileXmlWithGeneAnnotations = Paths.get(biocFileXmlWithGeneAnnotationsPath).toAbsolutePath().toFile();



        //Calculate Score
        HashMap<String, Double> result = new HashMap<>();

        for (RelationExtractor relationextractor: getRelationExtractors()  ) {
            BioCCollectionReader biocCollGeneAnnotations = new Parser().getBioCCollection(biocFileXmlWithGeneAnnotations);
            BioCCollection pred= relationextractor.Extract(biocCollGeneAnnotations);

             if (biocFileXmlTrainingDataPath != null){
                File  biocFileXmlTrainingDatafile = Paths.get(biocFileXmlTrainingDataPath).toAbsolutePath().toFile();
                BioCCollection trainBioCCollection = new Parser().getBioCCollection(biocFileXmlTrainingDatafile).readCollection();
                for (Scorer scorer: getScorers()) {

                    result.put(relationextractor.getClass().getName()+ "#" + scorer.GetScoringMethodName(), scorer.CalculateScore(trainBioCCollection, pred));
                }

            }

            writePredictions(pred, outputPath);
        }


        return result;

    }

    private void writePredictions(BioCCollection pred, String outputPath) throws IOException, XMLStreamException {

        String formmatedDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        Path outputFile =Paths.get(outputPath, String.format("%s_%s.xml","predictedRelations", formmatedDate));
        theLogger.info(String.format("Writing predictions to %s", outputFile));

        BioCCollectionWriter writer = new BioCCollectionWriter(outputFile);
        writer.writeCollection(pred);

    }

    public List<RelationExtractor> getRelationExtractors() {
        if ( relationExtractors == null) {
            relationExtractors = new ArrayList<>();
            relationExtractors.add(new RelationExtractorCooccurance());
            relationExtractors.add(new RelationExtractorCooccurancePmi());
        }

        return relationExtractors;
    }

    public void setRelationExtractor(List<RelationExtractor> relationExtractors) {
        this.relationExtractors = relationExtractors;
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
