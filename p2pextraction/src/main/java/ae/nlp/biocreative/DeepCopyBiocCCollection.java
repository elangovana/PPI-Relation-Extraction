package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.io.BioCCollectionReader;
import com.pengyifan.bioc.io.BioCCollectionWriter;
import com.pengyifan.bioc.io.BioCDocumentWriter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aparnaelangovan on 15/09/2017.
 */
public class DeepCopyBiocCCollection {

    public BioCCollection deepCopy(BioCCollection collection) throws  Exception{
        File tmpFile = null;
        BioCCollectionWriter writer = null;
        try {
            tmpFile = File.createTempFile("biocDeepCopy", ".xml");
            writer = new BioCCollectionWriter(tmpFile);
            writer.writeCollection(collection);
            return  new BioCCollectionReader(tmpFile).readCollection();

        }finally {
            if (writer != null){
                writer.close();
            }
            if (tmpFile != null ){
                Files.deleteIfExists(tmpFile.toPath());
            }
        }


    }
}
