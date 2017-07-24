package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.io.BioCCollectionReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;


class RelationExtractorIT {

    private RelationExtractor sut;
    private String testdatadir;

    @Test
    void extract() throws IOException, XMLStreamException, ParserConfigurationException, SAXException, InterruptedException {
        //Arrange
        File sampletraindatafile = Paths.get(testdatadir, "relationtrainingdata.xml").toAbsolutePath().toFile();
        BioCCollectionReader bioCCollection = new Parser().getBioCCollection(sampletraindatafile);

        //Act
        sut.Extract(bioCCollection);
    }

    @BeforeEach
    void setUp() {
        sut = new RelationExtractor();
        //Testdata
        testdatadir = ConfigHelper.getTestDataDirectory();

    }



    @AfterEach
    void tearDown() {

    }

}