package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import com.pengyifan.bioc.io.BioCCollectionReader;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;


class ParserTest {
    private Parser sut;
    private static File relationshipDatafile;
    private static final Logger logger =
            Logger.getLogger(ParserTest.class.getName());



    @BeforeAll
    static void  classSetup() throws IOException, ConfigurationException {
        //Download biocreative data file
        relationshipDatafile = Paths.get(ConfigHelper.getTestDataDirectory(), "relationtrainingdata.xml").toAbsolutePath().toFile();

    }


    @BeforeEach
    void setUp() throws IOException {
        sut = new Parser();
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void process() throws IOException, XMLStreamException, ParserConfigurationException, SAXException {
        //Act
        BioCCollectionReader actual = sut.getBioCCollection(relationshipDatafile);
        //Assert
        assertTrue(actual.readCollection().getDocmentCount() > 0);
    }


}