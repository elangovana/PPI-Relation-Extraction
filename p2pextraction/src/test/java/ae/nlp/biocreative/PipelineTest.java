package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;


public class PipelineTest {
    private Pipeline _sut;
    private String _testdatadir;
    private static Logger theLogger =
            Logger.getLogger(PipelineTest.class.getName());


    @BeforeTest
    void setUp() {
        _sut = new Pipeline();
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

        ArrayList<RelationExtractor> relExtractorCoOccPmi2 = new ArrayList<>(Arrays.asList(new RelationExtractorCooccurancePmi(2)));
        ArrayList<RelationExtractor> relExtractorCoOcc = new ArrayList<>(Arrays.asList(new RelationExtractorCooccurance()));
        ArrayList<RelationExtractor> relExtractorCoOccPmi3 = new ArrayList<>(Arrays.asList(new RelationExtractorCooccurancePmi(3)));

        return new Object[][]{
                {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml", relExtractorCoOccPmi2, tmpOutPath,
                        new HashMap<String, Double>() {{
                            put(COOCCPMIPrec, 0.273);
                            put(COOCCPMIRecall, 0.375);
                        }}}
                , {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml", relExtractorCoOcc, tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 1.0);
                    put(COOCCPrec, 0.154);

                }}}
                , {"PMtask_Relations_TrainingSet_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOcc, tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.884);
                    put(COOCCPrec, 0.158);


                }}}, {"PMtask_Relations_TrainingSet_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi2, tmpOutPath,
                new HashMap<String, Double>() {{

                    put(COOCCPMIPrec, 0.307);
                    put(COOCCPMIRecall, 0.476);

                }}}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi2, tmpOutPath,
                new HashMap<String, Double>() {{

                    put(COOCCPMIPrec, 0.233);
                    put(COOCCPMIRecall, 0.271);

                }}}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi3, tmpOutPath,
                new HashMap<String, Double>() {{

                    put(COOCCPMIPrec, 0.308);
                    put(COOCCPMIRecall, 0.247);

                }}}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOcc, tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.345);
                    put(COOCCPrec, 0.101);


                }}}
                , {"PMtask_Relations_TrainingSet_GoldGeneAnnotations.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi2, tmpOutPath,
                new HashMap<String, Double>() {{

                    put(COOCCPMIPrec, 0.859);
                    put(COOCCPMIRecall, 0.700);

                }}}, {"PMtask_Relations_TrainingSet_GoldGeneAnnotations.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOcc, tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.882);
                    put(COOCCPrec, 0.776);


                }}}

                , {"relationtrainingdata_gnomout_clean.xml", "relationtrainingdata.xml", relExtractorCoOccPmi2, tmpOutPath,
                new HashMap<String, Double>() {{

                    put(COOCCPMIPrec, 0.200);
                    put(COOCCPMIRecall, 0.125);

                }}}
                , {"relationtrainingdata_gnomout_clean.xml", "relationtrainingdata.xml", relExtractorCoOcc, tmpOutPath,
                new HashMap<String, Double>() {{
                    put(COOCCRecall, 0.250);
                    put(COOCCPrec, .071);


                }}}
                //End of array
        };

    }


    @Test(dataProvider = "runRelationExtractionTestCases")
    void runRelationExtraction(String geneAnnotatedBioCXml, String trainingDataBiocXml, List<RelationExtractor> relationExtractors, String outputPath, HashMap<String, Double> expectedScores) throws Exception {
        //Arrange
        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();
        File trainingDataFilePath = Paths.get(_testdatadir, trainingDataBiocXml).toFile();
        _sut.setRelationExtractor(relationExtractors);

        //Act
        HashMap<String, Double> actual = _sut.runRelationExtraction(geneAnnotationsPredFilePath.getAbsolutePath(), trainingDataFilePath.getAbsolutePath(), outputPath);


        //Assert
        for (String scoringMethod : actual.keySet()) {
            DecimalFormat numFormat = new DecimalFormat("0.000");
            Double actualScore = actual.get(scoringMethod);
            theLogger.info(String.format("Scoring Method %s has score  %2$.5f", scoringMethod, actualScore));

            if (expectedScores.containsKey(scoringMethod)) {
                Assert.assertEquals(scoringMethod + " = " + numFormat.format(actualScore), scoringMethod + " = " + numFormat.format(expectedScores.get(scoringMethod)));
            }

        }
    }

    @DataProvider(name = "runRelationExtractionTestOnlyTestCases")
    public static Object[][] runRelationExtractionTestOnlyTestCases() throws IOException {
        return new Object[][]{
                {"PMtask_TestSet_GNormplus_out.xml"}
        };
    }

    @Test(dataProvider = "runRelationExtractionTestOnlyTestCases")
    void runRelationExtractionTestOnly(String geneAnnotatedBioCXml) throws Exception {
        //Arrange
        String tmpOutPath = Files.createTempDirectory("pipelineOut").toString();

        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();

        //Act
        HashMap<String, Double> actual = _sut.runRelationExtraction(geneAnnotationsPredFilePath.getAbsolutePath(), null, tmpOutPath);


    }


}