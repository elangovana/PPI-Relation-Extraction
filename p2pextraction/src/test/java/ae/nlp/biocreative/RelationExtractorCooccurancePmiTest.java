package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.BioCCollection;
import org.testng.annotations.*;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;


public class RelationExtractorCooccurancePmiTest {

    private RelationExtractorCooccurancePmi sut;
    private String testdatadir;

    @Test
    void extract() throws IOException, XMLStreamException, ParserConfigurationException, SAXException, InterruptedException {
        //Arrange
        String iGeneAnnoatedBiocXml = "relationtrainingdata_gnormplus_out.xml";
        File sampletraindatafile = Paths.get(testdatadir, iGeneAnnoatedBiocXml).toAbsolutePath().toFile();

        //Act
        BioCCollection actual= sut.Extract(new Parser().getBioCCollection(sampletraindatafile).readCollection());

        //Smoke check
        Assert.assertEquals( actual.getDocmentCount(),new Parser().getBioCCollection(sampletraindatafile).readCollection().getDocmentCount());
        //TODO: Check for relation
    }

    @BeforeTest
    void setUp() {
        sut = new RelationExtractorCooccurancePmi();
        //Testdata
        testdatadir = ConfigHelper.getTestDataDirectory();

    }



    @AfterTest
    void tearDown() {

    }

}