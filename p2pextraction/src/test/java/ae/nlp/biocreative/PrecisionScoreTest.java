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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import static java.lang.Math.round;


/**
 * Created by aparnaelangovan on 5/08/2017.
 */
public class PrecisionScoreTest {
    private PrecisionScore _sut;
    private String _testdatadir;

    @BeforeTest
    void setUp() {
        _sut = new PrecisionScore();
        //Testdata
        _testdatadir = ConfigHelper.getTestDataDirectory();

    }

    @AfterTest
    void tearDown() {

    }

    @DataProvider(name = "calculateScoreTestCases")
    public static Object[][] calculateScoreTestCases() {

        return new Object[][]{{"relationPred_relation.xml", "relationtrainingdata.xml", .889}
                , {"ScoreTestcaseRecall1_Pred.xml", "ScoreTestcaseRecall1_Train.xml", .333}};
    }

    @Test(dataProvider = "calculateScoreTestCases")
    void calculateScore(String iPredictedRelBiocXML, String iTrainingDataBiocXml, double expectedScore) throws SAXException, XMLStreamException, ParserConfigurationException, IOException {
        String testdatadir = ConfigHelper.getTestDataDirectory();
        File sampletraindatafile = Paths.get(testdatadir, iTrainingDataBiocXml).toFile();
        File samplePreddatafile = Paths.get(testdatadir, iPredictedRelBiocXML).toFile();
        BioCCollection predSet = new Parser().getBioCCollection(samplePreddatafile).readCollection();
        BioCCollection trainingSet = new Parser().getBioCCollection(sampletraindatafile).readCollection();

        //Act
        double actual = _sut.CalculateScore(trainingSet, predSet);

        //Assert
        DecimalFormat numFormat = new DecimalFormat("0.000");
        Assert.assertEquals(numFormat.format(expectedScore), numFormat.format(actual));
    }

}