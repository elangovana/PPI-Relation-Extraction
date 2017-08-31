package ae.nlp.biocreative;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.testng.Assert.*;

/**
 * Created by aparnaelangovan on 31/08/2017.
 */
public class LingSentenceExtractorUnitTest {
    private LingSentenceExtractor sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new LingSentenceExtractor();
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    @DataProvider(name = "testGetSentencesTestCases")
    public static Object[][] testGetSentencesTestCases() throws IOException {

        return new Object[][]{
                //Testcase 1
                {"The tumor suppressor protein p53 tonically suppresses autophagy when it is present in the cytoplasm. This effect is phylogenetically conserved from mammals to nematodes, and human p53 can inhibit autophagy in yeast, as we show here. Bioinformatic investigations of the p53 interactome in relationship to the autophagy-relevant protein network underscored the possible relevance of a direct molecular interaction between p53 and the mammalian ortholog of the essential yeast autophagy protein Atg17, namely RB1-inducible coiled-coil protein 1 (RB1CC1), also called FAK family kinase-interacting protein of 200 KDa (FIP200). Mutational analyses revealed that a single point mutation in p53 (K382R) abolished its capacity to inhibit autophagy upon transfection into p53-deficient human colon cancer or yeast cells. In conditions in which wild-type p53 co-immunoprecipitated with RB1CC1/FIP200, p53 (K382R) failed to do so, underscoring the importance of the physical interaction between these proteins for the control of autophagy. In conclusion, p53 regulates autophagy through a direct molecular interaction with RB1CC1/FIP200, a protein that is essential for the very apical step of autophagy initiation.",
                        new String[]{
                                "The tumor suppressor protein p53 tonically suppresses autophagy when it is present in the cytoplasm. ",
                                "This effect is phylogenetically conserved from mammals to nematodes, and human p53 can inhibit autophagy in yeast, as we show here. ",
                                "Bioinformatic investigations of the p53 interactome in relationship to the autophagy-relevant protein network underscored the possible relevance of a direct molecular interaction between p53 and the mammalian ortholog of the essential yeast autophagy protein Atg17, namely RB1-inducible coiled-coil protein 1 (RB1CC1), also called FAK family kinase-interacting protein of 200 KDa (FIP200). ",
                                "Mutational analyses revealed that a single point mutation in p53 (K382R) abolished its capacity to inhibit autophagy upon transfection into p53-deficient human colon cancer or yeast cells. ",
                                "In conditions in which wild-type p53 co-immunoprecipitated with RB1CC1/FIP200, p53 (K382R) failed to do so, underscoring the importance of the physical interaction between these proteins for the control of autophagy. ",
                                "In conclusion, p53 regulates autophagy through a direct molecular interaction with RB1CC1/FIP200, a protein that is essential for the very apical step of autophagy initiation."
                        }}

        };

    }


    @Test(dataProvider = "testGetSentencesTestCases")
    public void testGetSentences(String text, String[] expectedSentences) throws Exception {


        List<String> sentences = sut.GetSentences(text);
        String[] actualSentences = new String[sentences.size()];
        sentences.toArray(actualSentences);


        //Assert
        Assert.assertEquals(actualSentences,expectedSentences);
    }

}