package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionWriter;
import org.testng.Assert;
import org.testng.annotations.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;


public class PipelineTest {
    private Pipeline _sut;
    private String _testdatadir;

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
        return new Object[][]{
                {"relationtrainingdata_gnormplus_out.xml", "relationtrainingdata.xml",tmpOutPath}
                , {"PMtask_Relations_TrainingSet_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml",tmpOutPath}
                , {"PMtask_Relations_TrainingSet_NoRelations.xml", "PMtask_Relations_TrainingSet.xml",tmpOutPath}

                ,{"relationtrainingdata_gnomout_clean.xml", "relationtrainingdata.xml",tmpOutPath}

        };

    }


    @Test(dataProvider = "runRelationExtractionTestCases")
    void runRelationExtraction(String geneAnnotatedBioCXml, String trainingDataBiocXml, String outputPath) throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {
        //Arrange
        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, geneAnnotatedBioCXml).toFile();
        File trainingDataFilePath = Paths.get(_testdatadir, trainingDataBiocXml).toFile();

        //Act
        HashMap<String, Double> actual = _sut.runRelationExtraction(geneAnnotationsPredFilePath.getAbsolutePath(), trainingDataFilePath.getAbsolutePath(), outputPath);

        //TODO:Fix Assert
        for (String scoringMethod : actual.keySet()) {
            System.out.printf("%s %f\n", scoringMethod, actual.get(scoringMethod));

        }
    }

}