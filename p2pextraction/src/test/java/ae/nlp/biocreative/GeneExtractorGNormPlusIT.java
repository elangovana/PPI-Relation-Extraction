package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import ae.nlp.biocreative.helpers.XmlHelper;
import org.testng.annotations.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo;
import static org.xmlmatchers.transform.XmlConverters.the;

/**
 * Created by aparnaelangovan on 27/07/2017.
 */
class GeneExtractorGNormPlusIT {
    private GeneExtractorGNormPlus sut;

    @BeforeTest
    void setUp() {
            sut = new GeneExtractorGNormPlus();
    }

    @AfterTest
    void tearDown() {

    }

    @Test
    void process() throws IOException, XMLStreamException, ParserConfigurationException, SAXException {
        //Arrange
        File inputFile =  Paths.get(ConfigHelper.getTestDataDirectory(), "relationtrainingdata.xml").toAbsolutePath().toFile();
        File expectedFile =  Paths.get(ConfigHelper.getTestDataDirectory(), "relationtrainingdata_gnormplus_out.xml").toAbsolutePath().toFile();
        //Act
        File actual = sut.extract(inputFile);
        //Assert
        assertThat(the(XmlHelper.ParseXml(expectedFile)), isEquivalentTo(the(XmlHelper.ParseXml(actual))));

    }


}