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

    @Test
    void runRelationExtraction() throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {
//Arrange
        File geneAnnotationsPredFilePath = Paths.get(_testdatadir, "relationtrainingdata_gnormplus_out.xml").toAbsolutePath().toFile();
        File trainingDataFilePath = Paths.get(_testdatadir, "relationtrainingdata.xml").toAbsolutePath().toFile();

        //Act
        HashMap<String, Double> actual= _sut.runRelationExtraction( geneAnnotationsPredFilePath.getAbsolutePath(),trainingDataFilePath.getAbsolutePath());

        //TODO:Fix Assert
        for (String scoringMethod: actual.keySet()   ) {
            System.out.printf("%s %f\n",scoringMethod, actual.get(scoringMethod));

        }
     }

}