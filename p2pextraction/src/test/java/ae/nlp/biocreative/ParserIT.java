package ae.nlp.biocreative;

import com.pengyifan.bioc.io.BioCCollectionReader;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.URL;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by  on 22/07/2017.
 */
class ParserIT {
    private Parser sut;
    private static File tempFile;
    private static final Logger logger =
            Logger.getLogger(ParserIT.class.getName());



    @BeforeAll
    static void  classSetup() throws IOException, ConfigurationException {
        //Download biodcreative data file
        tempFile= File.createTempFile("biocreative",".xml");
        String downloadUrl= new XMLConfiguration("config.xml").getString("xmlrelationshipTrainDataUrl");
        FileDownloadHelper.downloadFromUrl(new URL(downloadUrl), tempFile.getAbsolutePath());

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
        BioCCollectionReader actual = sut.process(tempFile.toURI().toURL());
        assertTrue(actual.readCollection().getDocmentCount() > 0);
    }


}