package ae.nlp.biocreative;

import com.pengyifan.bioc.io.BioCCollectionReader;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by aparnaelangovan on 22/07/2017.
 */
class ParserTest {
    private Parser sut;
    File tempFile;
    private static final Logger logger =
            Logger.getLogger(ParserTest.class.getName());
    private static final String trainingdata_url = "http://www.biocreative.org/media/store/files/2017/PMtask_Relations_TrainingSet.xml";



    @BeforeAll
    static void  classSetup() throws IOException {
        }


    @BeforeEach
    void setUp() throws IOException {
        sut = new Parser();
        tempFile= File.createTempFile("biocreative",".xml");
        downloadFromUrl(new URL(trainingdata_url), tempFile.getAbsolutePath());


    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void process() throws IOException, XMLStreamException, ParserConfigurationException, SAXException {

        BioCCollectionReader actual = sut.process(tempFile.toURI().toURL());
        assertTrue(actual.readCollection().getDocmentCount() > 0);
    }


    void downloadFromUrl(URL url, String localFilename) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URLConnection urlConn = url.openConnection();//connect

            is = urlConn.getInputStream();               //get connection inputstream
            fos = new FileOutputStream(localFilename);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }
}