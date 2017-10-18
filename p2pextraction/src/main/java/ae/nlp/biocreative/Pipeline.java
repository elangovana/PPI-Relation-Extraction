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

    private RelationExtractor relationExtractor;

    private Scorer scorer;

    private static Logger theLogger =
            Logger.getLogger(Pipeline.class.getName());


    /**
     * @param biocFileXmlWithGeneAnnotationsPath is a input bioc xml file annotated with gene names
     * @param biocFileXmlTrainingDataPath        is the training set with relations and is optional.
     * @param outputPath                         is the output directory
     * @return Returns a score
     * @throws Exception
     */
    public Double runRelationExtraction(String biocFileXmlWithGeneAnnotationsPath, String biocFileXmlTrainingDataPath, String outputPath) throws Exception {

        File biocFileXmlWithGeneAnnotations = Paths.get(biocFileXmlWithGeneAnnotationsPath).toAbsolutePath().toFile();
        BioCCollectionReader biocCollGeneAnnotations = new Parser().getBioCCollection(biocFileXmlWithGeneAnnotations);
        BioCCollection pred = getRelationExtractor().Extract(biocCollGeneAnnotations.readCollection());

        //Calculate Score
        Double result = 0.0;
        if (biocFileXmlTrainingDataPath != null) {
            result = CalculateScore(biocFileXmlTrainingDataPath, pred);
        }
        //Write predictions
        theLogger.info(String.format("Writing predictions for method %s", getRelationExtractor().getClass().getName()));
        writePredictions(pred, outputPath);


        return result;

    }

    private Double CalculateScore(String biocFileXmlTrainingDataPath, BioCCollection pred) throws XMLStreamException, IOException, SAXException, ParserConfigurationException {
        Double result;File biocFileXmlTrainingDatafile = Paths.get(biocFileXmlTrainingDataPath).toAbsolutePath().toFile();
        BioCCollection trainBioCCollection = new Parser().getBioCCollection(biocFileXmlTrainingDatafile).readCollection();
        result = getScorer().CalculateScore(trainBioCCollection, pred);
        String keyMethod = getRelationExtractor().getClass().getName() + "#" + getScorer().GetScoringMethodName();

        theLogger.info(String.format("The score for method %s is %2$.5f", keyMethod, result));
        return result;
    }

    private void writePredictions(BioCCollection pred, String outputPath) throws IOException, XMLStreamException {

        String formmatedDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        Path outputFile = Paths.get(outputPath, String.format("%s_%s.xml", "predictedRelations", formmatedDate));
        theLogger.info(String.format("Writing predictions to %s", outputFile));

        BioCCollectionWriter writer = new BioCCollectionWriter(outputFile);
        writer.writeCollection(pred);

    }


    public RelationExtractor getRelationExtractor() {
        if (relationExtractor == null) relationExtractor = new RelationExtractorCooccurancePmi();
        return relationExtractor;
    }

    public void setRelationExtractor(RelationExtractor relationExtractor) {
        this.relationExtractor = relationExtractor;
    }

    public Scorer getScorer() {
        if (scorer == null) scorer= new FScore();
        return scorer;
    }

    public void setScorer(Scorer scorer) {
        this.scorer = scorer;
    }
}
