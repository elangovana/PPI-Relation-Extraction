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

import static java.lang.Math.round;


/**
 * Created by aparnaelangovan on 5/08/2017.
 */
class PrecisionScoreTest {
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

    @Test

    void calculateScore() throws SAXException, XMLStreamException, ParserConfigurationException, IOException {
        //Arrange
        File sampletraindatafile = Paths.get(_testdatadir, "relationtrainingdata.xml").toAbsolutePath().toFile();
        File samplePreddatafile = Paths.get(_testdatadir, "relationPred_relation.xml").toAbsolutePath().toFile();

        BioCCollection predSet =new Parser().getBioCCollection(samplePreddatafile).readCollection();
        BioCCollection trainingSet=new Parser().getBioCCollection(sampletraindatafile).readCollection();
        
        //Act
        double actual = _sut.CalculateScore(trainingSet, predSet);

        //Assert
        DecimalFormat numFormat = new DecimalFormat("0.000");
        Assert.assertEquals(numFormat.format(.667), numFormat.format(actual));
    }

}