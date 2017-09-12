package ae.nlp.biocreative;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by aparnaelangovan on 12/09/2017.
 */
public class UnorderedPairTest {
    @BeforeMethod
    public void setUp() throws Exception {


    }

    @AfterMethod
    public void tearDown() throws Exception {

    }



    @DataProvider(name ="testEqualsTestCases")
    public Object[][] testEqualsTestCases(){

        return new Object[][]{
            {"GENE1", "GENE2", "GENE1", "GENE2", true}
                ,{"GENE1", "GENE2", "GENE2", "GENE1", true}
                , {"GENE1", "GENE1", "GENE1", "GENE1", true}
                , {"GENE1", "GENE1", "GENE2", "GENE1", false}
                , {"GENE1", "GENE2", "GENE2", "GENE2", false}

        };
    }

    @Test(dataProvider="testEqualsTestCases")
    public void testEquals(String pair11, String pair12, String pair21, String pair22, boolean expected) throws Exception {
//    /Arrange
        UnorderedPair sut = new UnorderedPair(pair11, pair12);
        UnorderedPair pairToCompare = new UnorderedPair(pair21,pair22);

        //Act
        boolean actual = sut.equals(pairToCompare);

        // Assert
        Assert.assertEquals(actual, expected);


    }

}