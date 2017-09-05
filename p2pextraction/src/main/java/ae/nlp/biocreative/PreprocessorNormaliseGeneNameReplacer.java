package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class PreprocessorNormaliseGeneNameReplacer implements Preprocessor {

    private static Logger theLogger =
            Logger.getLogger(PreprocessorNormaliseGeneNameReplacer.class.getName());


    @Override
    public BioCCollection Process(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {


        try {
            BioCCollection outBiocCollection = new BioCCollection();

            for (Iterator<BioCDocument> doci = biocCollectionReader.readCollection().documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();


                for (BioCPassage passage : doc.getPassages()) {

                   ReplaceGeneWithNormalisedId(passage);

                }

                outBiocCollection.addDocument(doc);


            }


            return outBiocCollection;
        } finally {
            if (biocCollectionReader != null) biocCollectionReader.close();
        }


    }

    private BioCPassage ReplaceGeneWithNormalisedId(BioCPassage bioCPassage) {
        theLogger.info(String.format("Passage to replace with normalised gene %s", bioCPassage.getText().get()));
        BiocGeneHelper geneHelper = new BiocGeneHelper();
        HashMap<String, String> geneNameToIdMap = geneHelper.GeneNameToGeneidMap(bioCPassage);
        for (Map.Entry<String, String> entry : geneNameToIdMap.entrySet()) {
            String replacedPassage = bioCPassage.getText().get().replace(entry.getKey(), entry.getValue());
            bioCPassage.setText(replacedPassage);

        }
        theLogger.info(String.format("Passage  with normalised gene %s", bioCPassage.getText().get()));

        return bioCPassage;

    }


}

