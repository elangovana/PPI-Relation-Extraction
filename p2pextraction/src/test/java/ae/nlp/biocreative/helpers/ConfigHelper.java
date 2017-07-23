package ae.nlp.biocreative.helpers;

/**
 * Created by aparnaelangovan on 23/07/2017.
 */
public class ConfigHelper {

    public static String getTestDataDirectory() {
        return ConfigHelper.class.getClassLoader().getResource("testdata").getPath();
    }
}
