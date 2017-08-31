package ae.nlp.biocreative;

import com.aliasi.sentences.MedlineSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.pengyifan.bioc.BioCSentence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aparnaelangovan on 31/08/2017.
 */
public class LingSentenceExtractor {
    static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
    static final SentenceModel SENTENCE_MODEL  = new MedlineSentenceModel();

    public List<String> GetSentences(String text) {
        ArrayList<String> bioCSentenceList = new ArrayList<String>();
        List<String> tokenList = new ArrayList<String>();
        List<String> whiteList = new ArrayList<String>();
        Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),0,text.length());
        tokenizer.tokenize(tokenList,whiteList);

        System.out.println(tokenList.size() + " TOKENS");
        System.out.println(whiteList.size() + " WHITESPACES");

        String[] tokens = new String[tokenList.size()];
        String[] whites = new String[whiteList.size()];
        tokenList.toArray(tokens);
        whiteList.toArray(whites);
        int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);

        System.out.println(sentenceBoundaries.length
                + " SENTENCE END TOKEN OFFSETS");

        if (sentenceBoundaries.length < 1) {
            System.out.println("No sentence boundaries found.");
            return bioCSentenceList;
        }
        int sentStartTok = 0;
        int sentEndTok = 0;
        for (int i = 0; i < sentenceBoundaries.length; ++i) {
            sentEndTok = sentenceBoundaries[i];
            System.out.println("SENTENCE "+(i+1)+": ");
            StringBuilder sb = new StringBuilder();
            for (int j=sentStartTok; j<=sentEndTok; j++) {
                sb.append(tokens[j]);
                sb.append(whites[j+1]);
            }


            bioCSentenceList.add(sb.toString());
            System.out.println(sb.toString());

            sentStartTok = sentEndTok+1;



        }
        return bioCSentenceList;
    }
}
