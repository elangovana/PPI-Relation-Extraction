package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by aparnaelangovan on 5/08/2017.
 */
class PrecisionScoreTest {
    private PrecisionScore _sut;
    private String _testdatadir;

    @BeforeEach
    void setUp() {
        _sut = new PrecisionScore();
        //Testdata
        _testdatadir = ConfigHelper.getTestDataDirectory();

    }

    @AfterEach
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
        assertEquals(9, actual);
    }

}