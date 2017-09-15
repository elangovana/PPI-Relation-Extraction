package ae.nlp.biocreative;

import ae.nlp.biocreative.helpers.ConfigHelper;
import ae.nlp.biocreative.helpers.XmlHelper;
import bioc.io.BioCCollectionReader;
import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionWriter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.*;
import static org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo;
import static org.xmlmatchers.transform.XmlConverters.the;

/**
 * Created by aparnaelangovan on 31/08/2017.
 */
public class PreprocessorSentenceExtractTest {
    private PreprocessorSentenceExtract sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new PreprocessorSentenceExtract();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @DataProvider(name = "createsSentencesTestCases")
    public static Object[][] createsSentencesTestCases() {


        return new Object[][]{{"relationPred_relation.xml","relationPred_relationwithSentence.xml"}
                ,{"PMtask_Relations_TrainingSet.xml","relationPred_relationwithSentence.xml"}
    };
    }

    @Test(dataProvider = "createsSentencesTestCases")
    void createsSentences(String biocXml, String expectedBiocXml) throws SAXException, XMLStreamException, ParserConfigurationException, IOException, InterruptedException {

        String testdatadir = ConfigHelper.getTestDataDirectory();
        File ibiocxmlFile = Paths.get(testdatadir, biocXml).toFile();
        File expectedBiocXmlFile = Paths.get(testdatadir, expectedBiocXml).toFile();
        com.pengyifan.bioc.io.BioCCollectionReader bioCCollection = new Parser().getBioCCollection(ibiocxmlFile);
        String sourceexpectedDate = new Parser().getBioCCollection(expectedBiocXmlFile).readCollection().getDate();
        //Act
        BioCCollection actual = sut.Process(bioCCollection.readCollection());


        //Assert
        File actualFile =File.createTempFile("biocSentence", ".xml");
        //Set the date os that file compare works
        actual.setDate(sourceexpectedDate);

        BioCCollectionWriter writer = new BioCCollectionWriter(actualFile);

        writer.writeCollection(actual);
        assertThat(the(XmlHelper.ParseXml(actualFile)), isEquivalentTo(the(XmlHelper.ParseXml(expectedBiocXmlFile))));


    }
}