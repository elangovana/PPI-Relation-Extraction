package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;


public class PreprocessorSentenceExtract implements Preprocessor {
    private LingSentenceExtractor sentenceExtractor;

    @Override
    public BioCCollection Process(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {


        try{
            BioCCollection outBiocCollection = new BioCCollection();

            for (Iterator<BioCDocument> doci = biocCollectionReader.readCollection().documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();


                for (BioCPassage passage : doc.getPassages()) {
                  List<String> sentences = getSentenceExtractor().GetSentences(passage.getText().get());
                  FillBiocSentences(passage, sentences);

                }

                outBiocCollection.addDocument(doc);


            }



            return outBiocCollection;
        }
        finally {
            if (biocCollectionReader!= null) biocCollectionReader.close();
        }


    }

    private void FillBiocSentences(BioCPassage passage, List<String> sentences) {
        for (int i = 0,  offset=0; i < sentences.size(); i++) {

            BioCSentence biocSentence=new BioCSentence();
            biocSentence.setText(sentences.get(i));
            passage.addSentence(biocSentence);
            biocSentence.setOffset(offset);

            offset=offset + sentences.get(i).length()+1;
        }
    }


    public LingSentenceExtractor getSentenceExtractor() {
        if ( sentenceExtractor == null) sentenceExtractor = new LingSentenceExtractor();

        return sentenceExtractor;
    }

    public void setSentenceExtractor(LingSentenceExtractor sentenceExtractor) {
        this.sentenceExtractor = sentenceExtractor;
    }
}

