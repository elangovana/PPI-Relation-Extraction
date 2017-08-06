package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import org.testng.Assert;
import org.testng.annotations.*;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;



public class RecallScoreTest {
    private RecallScore _sut;
    private String _testdatadir;

    @BeforeTest
    void setUp() {
        _sut = new RecallScore();
        //Testdata
        _testdatadir = ConfigHelper.getTestDataDirectory();

    }

    @AfterTest
    void tearDown() {

    }

    @DataProvider(name = "calculateScoreTestCases")
    public static Object[][] calculateScoreTestCases() {
        return new Object[][] {{"relationPred_relation.xml", "relationtrainingdata.xml", .750}};
    }

    @Test(dataProvider = "calculateScoreTestCases")
    void calculateScore( String iPredictedRelBiocXML,  String iTrainingDataBiocXml , double expectedScore ) throws SAXException, XMLStreamException, ParserConfigurationException, IOException {
        File sampletraindatafile = Paths.get(_testdatadir, iTrainingDataBiocXml).toAbsolutePath().toFile();
        File samplePreddatafile = Paths.get(_testdatadir, iPredictedRelBiocXML).toAbsolutePath().toFile();

        BioCCollection predSet =new Parser().getBioCCollection(samplePreddatafile).readCollection();
        BioCCollection trainingSet=new Parser().getBioCCollection(sampletraindatafile).readCollection();
        
        //Act
        double actual = _sut.CalculateScore(trainingSet, predSet);

        //Assert
        DecimalFormat numFormat = new DecimalFormat("0.000");
        Assert.assertEquals (numFormat.format(expectedScore), numFormat.format(actual));
    }

}