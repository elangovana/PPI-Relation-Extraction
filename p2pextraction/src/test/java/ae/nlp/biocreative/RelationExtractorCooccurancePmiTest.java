package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import org.testng.annotations.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.logging.Logger;


public class RelationExtractorCooccurancePmiTest {

    private RelationExtractorCooccurancePmi _sut;
    private String _testdatadir;
    private static Logger theLogger =
            Logger.getLogger(RelationExtractorCooccurancePmiTest.class.getName());


    @BeforeTest
    void setUp() {
        _sut = new RelationExtractorCooccurancePmi();
        //Testdata
        _testdatadir = ConfigHelper.getTestDataDirectory();

    }

    @AfterTest
    void tearDown() {

    }


    @DataProvider(name = "runRelationExtractionTestCases")
    public static Object[][] runRelationExtractionTestCases() throws IOException {
        String tmpOutPath = Files.createTempDirectory("pipelineOut").toString();
        final String COOCCRecall = "ae.nlp.biocreative.RelationExtractorCooccurance#ae.nlp.biocreative.RecallScore";
        final String COOCCPrec = "ae.nlp.biocreative.RelationExtractorCooccurance#ae.nlp.biocreative.PrecisionScore";
        final String COOCCPMIPrec = "ae.nlp.biocreative.RelationExtractorCooccurancePmi#ae.nlp.biocreative.PrecisionScore";
        final String COOCCPMIRecall = "ae.nlp.biocreative.RelationExtractorCooccurancePmi#ae.nlp.biocreative.RecallScore";

        return new Object[][]{
                {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml", tmpOutPath,
                        new HashMap<String, Double>() {{
                            put(COOCCRecall, 1.0);
                            put(COOCCPrec,  0.154);
                            put(COOCCPMIPrec, 0.273);
                            put(COOCCPMIRecall,  0.375);

                        }}}
                , {"PMtask_Relations_TrainingSet_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.884);
                    put(COOCCPrec, 0.158);
                    put(COOCCPMIPrec, 0.307);
                    put(COOCCPMIRecall, 0.476);

                }}}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall,0.345);
                    put(COOCCPrec, 0.101);
                    put(COOCCPMIPrec, 0.233);
                    put(COOCCPMIRecall, 0.271);

                }}}

                , {"PMtask_Relations_TrainingSet_GoldGeneAnnotations.xml", "PMtask_Relations_TrainingSet.xml", tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall,  0.882);
                    put(COOCCPrec, 0.776);
                    put(COOCCPMIPrec, 0.859);
                    put(COOCCPMIRecall, 0.700);

                }}}

                , {"relationtrainingdata_gnomout_clean.xml", "relationtrainingdata.xml", tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.250);
                    put(COOCCPrec, .071);
                    put(COOCCPMIPrec, 0.200);
                    put(COOCCPMIRecall, 0.125);

                }}}
//End of array
        };

    }


    @Test(dataProvider = "runRelationExtractionTestCases")
    void runRelationExtraction(String geneAnnotatedBioCXml, String trainingDataBiocXml, String outputPath, HashMap<String, Double> expectedScores) throws Exception {
        //Arrange
        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();
        File trainingDataFilePath = Paths.get(_testdatadir, trainingDataBiocXml).toFile();

        //Act
        HashMap<String, Double> actual = _sut.Extract(geneAnnotationsPredFilePath.getAbsolutePath(), trainingDataFilePath.getAbsolutePath(), outputPath);


        for (String scoringMethod : actual.keySet()) {
            DecimalFormat numFormat = new DecimalFormat(  "0.000");
            Double actualScore = actual.get(scoringMethod);
            theLogger.info(String.format("Scoring Method %s has score  %2$.5f", scoringMethod, actualScore));

            if (expectedScores.containsKey(scoringMethod)) {
                Assert.assertEquals(scoringMethod + " = " +numFormat.format(actualScore), scoringMethod + " = " + numFormat.format(expectedScores.get(scoringMethod)));
            }

        }
    }

}