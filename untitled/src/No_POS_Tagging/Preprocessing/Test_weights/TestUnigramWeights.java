package No_POS_Tagging.Preprocessing.Test_weights;

import lingolava.Tuple;
import lingologs.Script;
import lingologs.Texture;

import java.util.*;

/*
 * Key Functionalities:
 * - Testing Unigram Weights: Evaluates the performance of the system using Unigram-based weights on a provided test set.
 * - Learning: Optionally updates the weights during testing if the 'learn' parameter is set to true.
 * - Evaluation Metrics: Computes metrics;
 */

public class TestUnigramWeights {

    private HashMap<Script, HashMap<Script, Integer>> HM;
    private final Texture<Texture<Script>> testSet;

    private final int simi;
    public TestUnigramWeights(HashMap<Script, HashMap<Script, Integer>> HM, Texture<Texture<Script>> testSet,int simi)
    {
        this.HM = HM;
        this.simi = simi;
        this.testSet = testSet;
    }

    public Tuple.Quaple<Integer,Integer,Integer,Integer> getResults(boolean learn)
    {
        return calculate_any_value(testSet,HM,learn);
    }

    private Tuple.Quaple<Integer,Integer,Integer,Integer> calculate_any_value(Texture<Texture<Script>> scripts,
                                             HashMap<Script, HashMap<Script,Integer>> hm,
                                             boolean learn) {
        List<Integer> result = new ArrayList<>();

        for(Texture<Script> L : scripts)
        {
            for (int i = 0; i < L.toList().size(); i++) {
                if (hm.get(L.at(i)) != null && (i + 1) < L.toList().size()) {
                    HashMap<Script, Integer> H = hm.get(L.at(i));
                    boolean t = false;
                    if(H!=null)
                    {
                        List<Script> LR = getMaxValues(H, simi);
                        for(int a = 0; a<LR.size();a++)
                        {
                            if (L.at(i + 1).equals(LR.get(a))) {
                                result.add(1);
                                if(learn)
                                {
                                    int e = H.get(L.at(i+1));
                                    H.put(L.at(i+1),e+1);
                                }
                                t = true;
                                break;
                            }
                        }
                        if(!t)
                        {
                            if(learn)
                            {
                                int e = 0;
                                if(H.get(L.at(i+1))!= null)
                                {
                                    e = H.get(L.at(i+1));
                                }
                                H.put(L.at(i+1),e+1);
                            }

                            result.add(0);
                        }
                        if(learn)
                        {
                            hm.put(L.at(i),H);
                        }
                    }
                    else{
                        result.add(2);
                    }
                }

            }
        }
        int tr = 0; // 1
        int fa = 0; // 0
        int no = 0; // 2
        for (int i = 0; i< result.size();i++)
        {

            if(result.get(i)== 1)
            {
                tr++;
            }
            if(result.get(i)==0)
            {
                fa++;
            }
            if(result.get(i)==2)
            {
                no++;
            }

        }
        return new Tuple.Quaple<>(tr,fa,no,0);
    }

    public List<Script> getMaxValues(HashMap<Script, Integer> hashMap, int n) {
        List<Map.Entry<Script, Integer>> sortedEntries = new ArrayList<>(hashMap.entrySet());

        Collections.sort(sortedEntries, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Script> maxValues = new ArrayList<>();

        for (int i = 0; i < n && i < sortedEntries.size(); i++) {
            maxValues.add(sortedEntries.get(i).getKey());
        }

        return maxValues;
    }



}
