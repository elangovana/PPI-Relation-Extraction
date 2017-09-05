package ae.nlp.biocreative;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aparnaelangovan on 5/09/2017.
 */
public class UnorderedPair{
    private final Set<String> set;

    public ArrayList<String> getItems(){
        ArrayList<String> result = new ArrayList();
          result.addAll(set);
          return result;
    }

    public UnorderedPair(String a, String b) {
        set = new HashSet<String>();
        set.add(a);
        set.add(b);
    }

    public boolean equals(Object b) {
       if (!(b instanceof  UnorderedPair)) return false;
       UnorderedPair unorderedPairB = (UnorderedPair) b;

       if (unorderedPairB.set.size() != set.size() ) return  false;

       for (String iteminB : unorderedPairB.set){
          if (! set.contains(iteminB)) return  false;

       }

       return true;
    }

    public int hashCode() {
        return set.hashCode();
    }
}