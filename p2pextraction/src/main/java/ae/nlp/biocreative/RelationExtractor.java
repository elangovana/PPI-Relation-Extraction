package ae.nlp.biocreative;

import GNormPluslib.GNormPlus;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;

public class RelationExtractor {

    public void Extract(BioCCollectionReader biocCollection) throws XMLStreamException, IOException, InterruptedException {

        String[] gnormargs = new String[]{this.getClass().getClassLoader().getResource("input").getPath(), Files.createTempDirectory(null).toAbsolutePath().toString(),this.getClass().getClassLoader().getResource("gnormplus_setup.txt").getPath()};
        GNormPlus.main(gnormargs);

        for(Iterator<BioCDocument> doci = biocCollection.readCollection().documentIterator(); doci.hasNext(); ){
            BioCDocument doc= doci.next();

            for(BioCPassage passage :doc.getPassages()){
                System.out.println(passage.toString());



            }
        }



    }

}

