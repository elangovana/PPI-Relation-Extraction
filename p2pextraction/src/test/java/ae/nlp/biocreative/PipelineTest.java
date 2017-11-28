package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

        RelationExtractor relExtractorCoOccPmi2 = new RelationExtractorCooccurancePmi(2);
        RelationExtractor relExtractorCoOcc = new RelationExtractorCooccurance();
        RelationExtractor relExtractorCoOccPmi3 = new RelationExtractorCooccurancePmi(3);

        return new Object[][]{
                {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml", relExtractorCoOccPmi2, tmpOutPath, .3159}
                , {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml", relExtractorCoOcc, tmpOutPath, .2668}
                , {"PMtask_Relations_TrainingSet_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOcc, tmpOutPath, .269}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi2, tmpOutPath, .250}
                , {"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi3, tmpOutPath, .2741}
                , {"PMtask_Relations_TrainingSet_GoldGeneAnnotations.xml", "PMtask_Relations_TrainingSet.xml", relExtractorCoOccPmi2, tmpOutPath, .7713}

                //End of array
        };

    }


    @Test(dataProvider = "runRelationExtractionTestCases")
    void runRelationExtraction(String geneAnnotatedBioCXml, String trainingDataBiocXml, RelationExtractor relationExtractors, String outputPath, Double expectedScores) throws Exception {
        //Arrange
        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();
        File trainingDataFilePath = Paths.get(_testdatadir, trainingDataBiocXml).toFile();
        _sut.setRelationExtractor(relationExtractors);
        //Create tmp outputfile
        String formmatedDate = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String outputFile = Paths.get(outputPath, String.format("%s_%s.xml", "predictedRelations", formmatedDate)).toAbsolutePath().toString();

        //Act
        double actual = _sut.runRelationExtraction(geneAnnotationsPredFilePath.getAbsolutePath(), trainingDataFilePath.getAbsolutePath(), outputFile);


        //Assert
        DecimalFormat numFormat = new DecimalFormat("0.000");
        Assert.assertEquals(numFormat.format(actual), numFormat.format(expectedScores));

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
        String tmpOutPath = Files.createTempFile("test_runRelationExtractionTestOnly", ".xml").toString();

        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();

        //Act
        Double actual = _sut.runRelationExtraction(geneAnnotationsPredFilePath.getAbsolutePath(), null, tmpOutPath);


    }


}