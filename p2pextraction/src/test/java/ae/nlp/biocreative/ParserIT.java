package ae.nlp.biocreative;

import com.pengyifan.bioc.io.BioCCollectionReader;
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
 * Created by aparnaelangovan on 22/07/2017.
 */
class ParserIT {
    private Parser sut;
    private static File tempFile;
    private static final Logger logger =
            Logger.getLogger(ParserIT.class.getName());
    private static final String trainingdata_url = "http://www.biocreative.org/media/store/files/2017/PMtask_Relations_TrainingSet.xml";



    @BeforeAll
    static void  classSetup() throws IOException {
        tempFile= File.createTempFile("biocreative",".xml");
        FileDownloadHelper.downloadFromUrl(new URL(trainingdata_url), tempFile.getAbsolutePath());

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