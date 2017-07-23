package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.util.Iterator;

public class RelationExtractor {

    public void Extract(BioCCollectionReader biocCollection) throws XMLStreamException {

        for(Iterator<BioCDocument> doci = biocCollection.readCollection().documentIterator(); doci.hasNext(); ){
            BioCDocument doc= doci.next();

            for(BioCPassage passage :doc.getPassages()){
                System.out.println(passage.toString());

            }
        }



    }

}

