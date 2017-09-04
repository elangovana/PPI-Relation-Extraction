package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;


/**
 * Created by aparnaelangovan on 5/08/2017.
 */
public class NerFMeasureScorerTest {
    private NerFMeasureScorer _sut;
    private String _testdatadir;

    @BeforeTest
    void setUp() {
        _sut = new NerFMeasureScorer();
        //Testdata
        _testdatadir = ConfigHelper.getTestDataDirectory();

    }

    @AfterTest
    void tearDown() {

    }

    @DataProvider(name = "calculateScoreTestCases")
    public static Object[][] calculateScoreTestCases() {

        return new Object[][]{{"PMtask_Relations_TrainingSet_noannotation_Gnormplus_out.xml", "PMtask_Relations_TrainingSet.xml", .663}};
    }

    @Test(dataProvider = "calculateScoreTestCases")
    void calculateScore(String iPredictedRelBiocXML, String iTrainingDataBiocXml, double expectedScore) throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {
        String testdatadir = ConfigHelper.getTestDataDirectory();
        File sampletraindatafile = Paths.get(testdatadir, iTrainingDataBiocXml).toFile();
        File samplePreddatafile = Paths.get(testdatadir, iPredictedRelBiocXML).toFile();
        BioCCollection predSet = new Parser().getBioCCollection(samplePreddatafile).readCollection();
        BioCCollection trainingSet = new Parser().getBioCCollection(sampletraindatafile).readCollection();
    
        //Act
        double actual = _sut.Score(trainingSet, predSet);

        //Assert
        DecimalFormat numFormat = new DecimalFormat("0.000");
        Assert.assertEquals(numFormat.format(actual),numFormat.format(expectedScore));
    }

}