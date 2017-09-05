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
    private PreprocessorSentenceExtract sentenceExtractor;

    @Override
    public BioCCollection Extract(BioCCollectionReader biocCollectionReader) throws XMLStreamException, IOException, InterruptedException {


        try {
            BioCCollection outBiocCollection = new BioCCollection();
            //Preprocess
            BioCCollection biocCollection = getSentenceExtractor().Process(biocCollectionReader);

            HashMap<UnorderedPair, Integer>  genePairCount = new HashMap<UnorderedPair,Integer>();
            for (Iterator<BioCDocument> doci = biocCollection.documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();

                HashSet<String> existingGeneRelationsFromPreviousPassage = new HashSet<>();



                for (BioCPassage passage : doc.getPassages()) {
                    List<String> genesInPassage = getGenes(passage);

                    for (int i = 0; i < genesInPassage.size(); i++) {
                        for (int j = i+1; j < genesInPassage.size(); j++) {
                            String gene1 = genesInPassage.get(i);
                            String gene2 = genesInPassage.get(j);
                            UnorderedPair key = new UnorderedPair(gene1, gene2);

                            genePairCount.putIfAbsent(key,0);
                            for (BioCSentence biocSentence : passage.getSentences() ) {
                                String senence = biocSentence.getText().get();
                                if (senence.contains(gene1) && senence.contains(genesInPassage.get(j))){
                                    genePairCount.replace(key, genePairCount.get(key)+1);

                                };

                            }
                        }


                    }


                    addRelationToDoc(doc, existingGeneRelationsFromPreviousPassage, genesInPassage);
                    //To avoid duplicates
                    existingGeneRelationsFromPreviousPassage.addAll(genesInPassage);

                }
                outBiocCollection.addDocument(doc);


            }


            return outBiocCollection;
        } finally {
            if (biocCollectionReader != null) biocCollectionReader.close();
        }


    }


    private void addRelationToDoc(BioCDocument doc, HashSet<String> geneRelationShipsAlreadyAdded, List<String> genesInPassage) {


        for (int i = 0; i < genesInPassage.size(); i++) {

            for (int j = i + 1; j < genesInPassage.size(); j++) {

                //Ignore if the relationship already exists
                String gene1 = genesInPassage.get(i);
                String gene2 = genesInPassage.get(j);
                if (CheckForDuplicateRelation(geneRelationShipsAlreadyAdded, gene1, gene2)) continue;

                BioCRelation relation = new BiocP2PRelation().getBioCRelation(gene1, gene2);


                doc.addRelation(relation);


            }
        }
    }



    private boolean CheckForDuplicateRelation(HashSet<String> geneList, String gene1, String gene2) {
        return geneList.contains(gene1) && geneList.contains(gene2);
    }


    /**
     * @param passage Biocpassage
     * @return Returns the list of genes from the annotations
     */
    private ArrayList<String> getGenes(BioCPassage passage) {
        HashSet<String> geneSet = new HashSet<String>();


        //Get genes identified
        for (BioCAnnotation annotation : passage.getAnnotations()) {
            //Annotation is a gene
            if (annotation.getInfon("type").get().equals("Gene")) {
                //Get NCBI gene name
                Optional<String> geneName = annotation.getText();
                if (geneName.isPresent()) {
                    geneSet.add(geneName.get());
                }
            }
        }

        return new ArrayList<>(geneSet);
    }

    public PreprocessorSentenceExtract getSentenceExtractor() {
        if (sentenceExtractor == null) sentenceExtractor = new PreprocessorSentenceExtract();

        return sentenceExtractor;
    }

    public void setSentenceExtractor(PreprocessorSentenceExtract sentenceExtractor) {
        this.sentenceExtractor = sentenceExtractor;
    }
}

