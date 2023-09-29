package No_POS_Tagging.Preprocessing.Test_weights;

import lingolava.Tuple;
import lingologs.Script;
import lingologs.Texture;

import java.util.*;

/**
 * Key Functionalities:
 * - Testing Bigram Weights: Evaluates the performance of the system using Bigram-based weights on a provided test set.
 * - Learning: Optionally updates the weights during testing if the 'learn' parameter is set to true.
 * - Evaluation Metrics: Computes metrics;
 */

public class TestBigramWeights {

    private final HashMap<Script, HashMap<Script, Integer>> HM;
    private final Texture<Texture<Script>> testSet;

    private int tr = 0; // 1
    private int fa = 0; // 0
    private int no = 0; // 2

    private final int simi;
    public TestBigramWeights(HashMap<Script, HashMap<Script, Integer>> HM, Texture<Texture<Script>> testSet,int simi)
    {
        this.HM = HM;
        this.simi = simi;
        this.testSet = makeBigrams(testSet);
    }

    private Texture<Texture<Script>> makeBigrams(Texture<Texture<Script>> L)
    {
        Texture<Texture<Script>> result = new Texture<>();
        for(Texture<Script> LS : L)
        {
            Texture<Script> btw = new Texture<>();
            for (int i = 0; i<LS.toList().size();i++)
            {
                if(i+1<LS.toList().size())
                {
                    btw.add(Script.of(LS.at(i)+" "+LS.at(i+1)));
                }
            }
            result.add(btw);
        }
        return result;
    }

    public Tuple.Quaple<Integer,Integer,Integer,Integer> getResults(boolean learn)
    {
        return calculate_any_value(testSet,HM,simi,learn);
    }

    private Tuple.Quaple<Integer,Integer,Integer,Integer> calculate_any_value(Texture<Texture<Script>> scripts,
                                             HashMap<Script, HashMap<Script, Integer>> hm,
                                             int simi,
                                             boolean learn) {
        List<Integer> result = new ArrayList<>();

        for(Texture<Script> L : scripts)
        {
            for (int i = 0; i < L.toList().size(); i++) {
                if (hm.get(L.at(i)) != null && (i + 1) < L.toList().size()) {
                    HashMap<Script, Integer> H = hm.get(L.at(i));
                    if(H!=null)
                    {
                        List<Script> LR = getMaxValues(H, simi);
                        boolean tr = false;
                        for (Script C : LR) {
                            Script res = L.at(i + 1).split(" ").get(1);
                            if (res.equals(C)) {
                                result.add(1);
                                if (learn) {
                                    int e = H.get(res);
                                    H.put(res, e + 1);
                                }
                                tr = true;
                                break;
                            }
                        }
                        if(!tr)
                        {
                            if(learn)
                            {
                                Script res = L.at(i+1).split(" ").get(1);
                                int e = 0;
                                if(H.get(res)!=null)
                                {
                                    e = H.get(res);
                                }
                                H.put(res,e+1);
                            }
                            result.add(0);
                        }
                        if(learn)
                        {
                            hm.put(L.at(i),H);
                        }

                    }
                    else {
                        result.add(2);
                    }
                }

            }
        }


        for (Integer integer : result) {

            if (integer == 1) {
                tr++;
            }
            if (integer == 0) {
                fa++;
            }
            if (integer == 2) {
                no++;
            }

        }
        return new Tuple.Quaple<>(tr, fa, no,0);
    }

    public List<Script> getMaxValues(HashMap<Script, Integer> hashMap, int n) {
        List<Map.Entry<Script, Integer>> sortedEntries = new ArrayList<>(hashMap.entrySet());

        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        List<Script> maxValues = new ArrayList<>();

        for (int i = 0; i < n && i < sortedEntries.size(); i++) {
            maxValues.add(sortedEntries.get(i).getKey());
        }

        return maxValues;
    }
}
