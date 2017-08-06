package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import ae.nlp.biocreative.helpers.XmlHelper;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionReader;
import com.pengyifan.bioc.io.BioCCollectionWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.xml.HasXPath.hasXPath;
import static org.junit.jupiter.api.Assertions.assertEquals;


class RelationExtractorCooccuranceTest {

    private RelationExtractorCooccurance sut;
    private String testdatadir;

    @Test
    void extract() throws IOException, XMLStreamException, ParserConfigurationException, SAXException, InterruptedException {
        //Arrange
        String iGeneAnnoatedBiocXml = "relationtrainingdata_gnormplus_out.xml";
        File sampletraindatafile = Paths.get(testdatadir, iGeneAnnoatedBiocXml).toAbsolutePath().toFile();

        //Act
        BioCCollection actual= sut.Extract(new Parser().getBioCCollection(sampletraindatafile));

        //Smoke check
        assertEquals(new Parser().getBioCCollection(sampletraindatafile).readCollection().getDocmentCount(), actual.getDocmentCount());
       //TODO: Check for relation
    }

    @BeforeEach
    void setUp() {
        sut = new RelationExtractorCooccurance();
        //Testdata
        testdatadir = ConfigHelper.getTestDataDirectory();

    }



    @AfterEach
    void tearDown() {

    }

}