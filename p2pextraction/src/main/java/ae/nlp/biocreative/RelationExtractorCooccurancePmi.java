package ae.nlp.biocreative;

import com.pengyifan.bioc.*;
import com.pengyifan.bioc.io.BioCCollectionReader;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.*;


public class RelationExtractorCooccurancePmi implements RelationExtractor {
    private List<Preprocessor> preProcessors;
    public Integer thresholdPairCount;

    public RelationExtractorCooccurancePmi() {
        this(3);
    }

    public RelationExtractorCooccurancePmi(int noOfSentencePerPairThreshold) {
        thresholdPairCount = noOfSentencePerPairThreshold;
    }

    @Override
    public BioCCollection Extract(BioCCollection ibioCCollection) throws XMLStreamException, IOException, InterruptedException {


        try {
            BioCCollection outBiocCollection = new BioCCollection();
            //Preprocess
            BioCCollection biocCollection = Preprocess(ibioCCollection);

            for (Iterator<BioCDocument> doci = biocCollection.documentIterator(); doci.hasNext(); ) {
                BioCDocument doc = doci.next();
                HashMap<UnorderedPair, ArrayList<String>> outgenePairSentences = new HashMap<>();
                HashMap<String, ArrayList<UnorderedPair>> outsentenceToGeneMap = new HashMap<>();

                PopulateGenePairSentences(doc, outgenePairSentences, outsentenceToGeneMap);

//                if (outgenePairSentences.size() == 1){
//                    BioCRelation relation = getBioCRelation(outgenePairSentences.get(outgenePairSentences).);
//                    doc.addRelation(relation);
//                    continue;;
//                }

                for (Map.Entry<UnorderedPair, ArrayList<String>> entry : outgenePairSentences.entrySet()) {

                    BioCRelation relation = null;


                    int numOfSentencesWithGenePairs = entry.getValue().size();
                    boolean isSelfRelation = entry.getKey().getItems().size() == 1;
                    if ((numOfSentencesWithGenePairs >= thresholdPairCount && !isSelfRelation))
                        relation = getBioCRelation(entry.getKey());
                        //Not enough threshold.. lets try rules to see the relation can be added.

                    else {
                        for (Map.Entry<String, ArrayList<UnorderedPair>> sentenceGeneMapEntry : outsentenceToGeneMap.entrySet()) {
                            int numOfgenesPairsInSentence = sentenceGeneMapEntry.getValue().size();
                            if (numOfgenesPairsInSentence == 0 || !sentenceGeneMapEntry.getValue().get(0).equals(entry.getKey()))
                                continue;
                            //Rule 1 - Sentence  contains only one pair and contains the word interact then add it..

                            if (numOfgenesPairsInSentence == 1) {
                                String sentence = sentenceGeneMapEntry.getKey();
                                if (!isSelfRelation && sentence.contains("interact")) {
                                    relation = getBioCRelation(entry.getKey());

                                }

//                                if (isSelfRelation && sentence.contains("muta")) {
//                                    //relation = getBioCRelation(entry.getKey());
//                                }

                            }

                            break;

                        }

                    }
                    if (relation != null) {
                        doc.addRelation(relation);
                    }

                }
                outBiocCollection.addDocument(doc);
            }


            return outBiocCollection;
        } finally {

        }


    }

    private void PopulateGenePairSentences(BioCDocument doc, HashMap<UnorderedPair, ArrayList<String>> outGenePairSentences, HashMap<String, ArrayList<UnorderedPair>> outSentenceToGeneMap) {
        BiocGeneHelper geneHelper = new BiocGeneHelper();

        for (BioCPassage passage : doc.getPassages()) {
            //   if (passage.getInfon("type").get().equals("title")) continue;
            List<String> genesInPassage = geneHelper.getNormliasedGenes(passage);

            for (int i = 0; i < genesInPassage.size(); i++) {


                for (int j = i + 1; j < genesInPassage.size(); j++) {
                    String gene1 = genesInPassage.get(i);
                    String gene2 = genesInPassage.get(j);
                    UnorderedPair key = new UnorderedPair(gene1, gene2);

                    outGenePairSentences.putIfAbsent(key, new ArrayList<>());

                    //For each sentence check if contains gene pairs
                    for (BioCSentence biocSentence : passage.getSentences()) {
                        String senence = biocSentence.getText().get();
                        outSentenceToGeneMap.putIfAbsent(senence, new ArrayList<>());

                        if (senence.contains(gene1) && senence.contains(genesInPassage.get(j))) {

                            outGenePairSentences.get(key).add(senence);
                            outSentenceToGeneMap.get(senence).add(key);

                        }
                        ;

                    }
                }


            }


        }
    }

    private BioCRelation getBioCRelation(UnorderedPair entry) {
        ArrayList<String> genes = entry.getItems();
        String gene1 = genes.get(0);
        String gene2 = gene1;

        if (genes.size() == 2) {

            gene2 = genes.get(1);
        }
        return new BiocP2PRelation().getBioCRelation(gene1, gene2);
    }

    private BioCCollection Preprocess(BioCCollection bioCCollection) throws InterruptedException, XMLStreamException, IOException {

        BioCCollection collection = bioCCollection;

        for (Preprocessor preprocessor : getPreProcessors()
                ) {

            BioCCollection newCollection = preprocessor.Process(collection);
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

        return preProcessors;
    }

    public String toString() {
        return String.format("Co-OccurancePMI with t=%d", thresholdPairCount);
    }
}

