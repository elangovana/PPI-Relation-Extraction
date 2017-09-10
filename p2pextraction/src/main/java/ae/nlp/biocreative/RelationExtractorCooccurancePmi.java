package ae.nlp.biocreative;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.Tokenizer;
import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;


public class RelationExtractorCooccurancePmi implements RelationExtractor {
    private List<Preprocessor> preProcessors;
    private Integer threshold = 2;

    @Override
    public BioCCollection Extract(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {


        try {
            BioCCollection outBiocCollection = new BioCCollection();
            BiocGeneHelper geneHelper = new BiocGeneHelper();
            //Preprocess
           BioCCollection biocCollection = Preprocess(biocCollectionReader.readCollection());

            for (Iterator<BioCDocument> doci = biocCollection.documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();
                HashMap<UnorderedPair, Integer>  genePairCount = new HashMap<UnorderedPair,Integer>();



                for (BioCPassage passage : doc.getPassages()) {
                    List<String> genesInPassage = geneHelper.getNormliasedGenes(passage);

                    for (int i = 0; i < genesInPassage.size(); i++) {

                        if (genesInPassage.size() == 1){
                            UnorderedPair key = new UnorderedPair(genesInPassage.get(0), genesInPassage.get(0));

                            genePairCount.putIfAbsent(key,0);
                            continue;
                        }
                        for (int j = i+1; j < genesInPassage.size(); j++) {
                            String gene1 = genesInPassage.get(i);
                            String gene2 = genesInPassage.get(j);
                            UnorderedPair key = new UnorderedPair(gene1, gene2);

                            genePairCount.putIfAbsent(key,0);
                            //For each sentence check if contains gene pairs
                            for (BioCSentence biocSentence : passage.getSentences() ) {
                                String senence = biocSentence.getText().get();
                                if (senence.contains(gene1) && senence.contains(genesInPassage.get(j))){
                                    genePairCount.replace(key, genePairCount.get(key)+1);

                                };

                            }
                        }



                    }



                }
                for (Map.Entry<UnorderedPair, Integer> entry: genePairCount.entrySet()  ) {
                    //TODO: MOVE genePairCount.size() == 1 outside loop..

                    if ((genePairCount.size() == 1) || (entry.getValue() >= threshold)) {

                        BioCRelation relation = getBioCRelation(entry);


                        doc.addRelation(relation);

                    }

                }


                outBiocCollection.addDocument(doc);


            }


            return outBiocCollection;
        } finally {
            if (biocCollectionReader != null) biocCollectionReader.close();
        }


    }

    private BioCRelation getBioCRelation(Map.Entry<UnorderedPair, Integer> entry) {
        ArrayList<String> genes = entry.getKey().getItems();
        String gene1 = genes.get(0);
        String gene2 = gene1;

        if (genes.size() == 2){

             gene2 = genes.get(1);
        }
        return new BiocP2PRelation().getBioCRelation(gene1, gene2);
    }

    private BioCCollection Preprocess(BioCCollection bioCCollection) throws InterruptedException, XMLStreamException, IOException {

        BioCCollection collection = bioCCollection;

        for (Preprocessor preprocessor: getPreProcessors()
             ) {

           BioCCollection newCollection= preprocessor.Process(collection);
           collection = newCollection;


        }

        return collection;
    }









    public List<Preprocessor> getPreProcessors() {
      if (preProcessors == null) {
          preProcessors = new ArrayList<>();
          preProcessors.add(new PreprocessorNormaliseGeneNameReplacer());
          preProcessors.add(new PreprocessorSentenceExtract());

      }

      return  preProcessors;
    }


}

