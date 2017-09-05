package ae.nlp.biocreative;

import com.pengyifan.bioc.BioCCollection;
import com.pengyifan.bioc.BioCDocument;
import com.pengyifan.bioc.BioCPassage;
import com.pengyifan.bioc.BioCSentence;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class PreprocessorNormaliseGeneNameReplacer implements Preprocessor {


    @Override
    public BioCCollection Process(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {


        try{
            BioCCollection outBiocCollection = new BioCCollection();

            for (Iterator<BioCDocument> doci = biocCollectionReader.readCollection().documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();


                for (BioCPassage passage : doc.getPassages()) {



                }

                outBiocCollection.addDocument(doc);


            }



            return outBiocCollection;
        }
        finally {
            if (biocCollectionReader!= null) biocCollectionReader.close();
        }


    }





}

